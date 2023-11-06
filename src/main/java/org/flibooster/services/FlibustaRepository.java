package org.flibooster.services;

import lombok.AllArgsConstructor;
import org.flibooster.model.Book;
import org.flibooster.tor.connection.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class FlibustaRepository {
    private Connection connection;

    public List<Book> searchBooks(String search) throws IOException {

        URLConnection flibustaURLConnection = connection.getConnection("booksearch?ask=" + search);
        String htmlResponse = getHTMLResponse(flibustaURLConnection);

        Map<String, String> parsedRows = getParsedRows(htmlResponse);
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
        return sb.toString();
    }
    private Map<String, String> getParsedRows(String htmlResponse) {
        Document doc = Jsoup.parse(htmlResponse);
        Elements links = doc.getElementById("main").select("ul");
        Map<String, String> parsedRows = new HashMap<>();
        for (Element link : links) {
            if (link.hasClass("pager")) continue;
            for (Element child : link.children()) {
                String attr = child.select("a").attr("href");
                if (attr.contains("/b/")) {
                    parsedRows.put(child.text(), attr);
                }
            }
        }
        return parsedRows;
    }

    private List<Book> collectLinks(Map<String, String> parsedRows) throws IOException {
        for (Map.Entry<String, String> entry : parsedRows.entrySet()) {
            Book book = getBookModelFromLink(entry.getValue());
        }
        return null;
    }

    private Book getBookModelFromLink(String href) throws IOException {
        URLConnection bookLinkConnection = connection.getConnection(href);

        String htmlResponse = getHTMLResponse(bookLinkConnection);
        Document doc = Jsoup.parse(htmlResponse);
//        Book book = new Book()
        List<Element> collect = doc.select("a").stream()
                .filter(element -> element.attr("href").contains(href))
                .collect(Collectors.toList());
        return null;
    }
}
