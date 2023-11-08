package org.flibooster.repository;

import org.flibooster.model.Book;
import org.flibooster.tor.connection.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class FlibustaRepository {
    private final Logger log = LoggerFactory.getLogger(FlibustaRepository.class);
    private Connection connection;
    @Value("${file.storage}")
    private String fileStorage;

    public FlibustaRepository(@Autowired Connection connection) {
        this.connection = connection;
    }

    public List<Book> searchBooks(String search) throws IOException {

        URLConnection flibustaURLConnection = connection.getConnection("booksearch?ask=" + search);
        log.info("Parsing search response...");
        String htmlResponse = getHTMLResponse(flibustaURLConnection);

        Map<String, String[]> parsedRows = getParsedRows(htmlResponse);
        List<Book> bookList = collectLinks(parsedRows);

        return bookList;
    }

    private String getHTMLResponse(URLConnection flibustaURLConnection) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(flibustaURLConnection.getInputStream()))) {
            reader.lines().forEach(line -> sb.append(line));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info(flibustaURLConnection + " parsed");
        return sb.toString();
    }
    private Map<String, String[]> getParsedRows(String htmlResponse) {
        Document doc = Jsoup.parse(htmlResponse);
        Elements links = doc.getElementById("main").select("ul");
        Map<String, String[]> parsedRows = new HashMap<>();
        for (Element link : links) {
            if (link.hasClass("pager")) continue;
            for (Element child : link.children()) {
                String attr = child.select("a").attr("href");
                if (attr.contains("/b/")) {
                    String[] authorAndTitle = new String[2];
                    child.childNodes().stream()
                            .filter(ch -> ch instanceof Element)
                            .forEach(el -> {
                                if (((Element) el).attr("href").contains("/a/")) {
                                    authorAndTitle[0] = ((Element) el).text();
                                }
                                if (((Element) el).attr("href").contains("/b/")) {
                                    authorAndTitle[1] = ((Element) el).text();
                                }
                            });
                    parsedRows.put(attr, authorAndTitle);
                }
            }
        }
        return parsedRows;
    }

    private List<Book> collectLinks(Map<String, String[]> parsedRows) throws IOException {
        List<Book> bookList = parsedRows.entrySet().stream()
                .parallel()
                .map(entry -> {
                    log.info(entry.getKey() + " getting book model");
                    return getBookModelFromLink(entry.getKey(), entry.getValue());
                })
                .filter(book -> book.hasAnyLink())
                .collect(Collectors.toList());
        return bookList;
    }

    private Book getBookModelFromLink(String href, String[] authorAndTitle) {
        URLConnection bookLinkConnection = null;
        try {
            bookLinkConnection = connection.getConnection(href);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("Collecting files links to book dto: " + authorAndTitle[1]);
        String htmlResponse = getHTMLResponse(bookLinkConnection);
        Document doc = Jsoup.parse(htmlResponse);
        Book book = new Book();
        AtomicReference<String> pdf = new AtomicReference<>();
        AtomicReference<String> fb2 = new AtomicReference<>();
        AtomicReference<String> epub = new AtomicReference<>();
        AtomicReference<String> mobi = new AtomicReference<>();
        doc.select("a").stream()
                .filter(element -> element.attr("href").contains(href))
                .forEach(el -> {
                    String text = el.text();
                    if (text.contains("pdf")) pdf.set(el.attr("href"));
                    if (text.contains("fb2")) fb2.set(el.attr("href"));
                    if (text.contains("epub")) epub.set(el.attr("href"));
                    if (text.contains("mobi")) mobi.set(el.attr("href"));
                });
        if (authorAndTitle[0] != null) {
            book.setAuthor(authorAndTitle[0]);
        } else {
            book.setAuthor("");
        }
        book.setTitle(authorAndTitle[1]);
        book.setPdf(pdf.get());
        book.setFb2(fb2.get());
        book.setEpub(epub.get());
        book.setMobi(mobi.get());
        return book;
    }

    public File getFileByUrl(String url) throws IOException {
        URLConnection bookLinkConnection = connection.getConnection(url);
        String fileName = bookLinkConnection.getHeaderField("Content-Disposition");
        if (fileName != null && !fileName.isEmpty()) {
            fileName = fileName.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
        } else {
            String path = bookLinkConnection.getURL().getPath();
            fileName = path.substring(path.lastIndexOf("/") + 1);
        }
        File outputFile = new File(fileStorage, fileName);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile); InputStream inputStream = bookLinkConnection.getInputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        System.out.println("Файл успешно сохранен: " + outputFile.getAbsolutePath());
        return outputFile;
    }

}
