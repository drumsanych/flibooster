package net.yokniga.yoik.services;

import lombok.AllArgsConstructor;
import net.yokniga.yoik.tor.connection.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;

@Component
@AllArgsConstructor
public class HTMLParser {
    private Connection connection;

    public String printHTML(String search) throws IOException {
        URLConnection flibustaURLConnection = connection.getConnection("booksearch?ask=" + search);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(flibustaURLConnection.getInputStream()))) {
            reader.lines().forEach(line -> sb.append(line));
        }
        Document doc = Jsoup.parse(sb.toString());

        Elements links = doc.getElementById("main").select("ul");

        for (Element link : links) {
            if (link.hasClass("pager")) continue;
            for (Element child : link.children()) {
                System.out.println(child.text());
            }
        }
        return null;
    }
}
