package org.flibooster.services;

import lombok.AllArgsConstructor;
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
import java.util.Map;

@Component
@AllArgsConstructor
public class HTMLParser {
    private Connection connection;

    public String printHTML(String search) throws IOException {
        long startTime = System.nanoTime();
// Выполнение кода или вызов метода, время выполнения которого нужно измерить



        URLConnection flibustaURLConnection = connection.getConnection("booksearch?ask=" + search);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(flibustaURLConnection.getInputStream()))) {
            reader.lines().forEach(line -> sb.append(line));
        }
        Document doc = Jsoup.parse(sb.toString());

        Elements links = doc.getElementById("main").select("ul");

        Map<String, String> results = new HashMap<>();
        for (Element link : links) {
            if (link.hasClass("pager")) continue;
            for (Element child : link.children()) {
                String attr = child.select("a").attr("href");
                if (attr.contains("/b/")) {
                    results.put(child.text(), attr);
                }
            }
        }



        if (results.size() > 0) {
            long endTime = System.nanoTime();
            for (Map.Entry<String, String> entry : results.entrySet()) {
                System.out.println(entry.getKey());
            }
            System.out.println("**********");

            double duration = (endTime - startTime) / 1000000000.0; // Перевод из наносекунд в секунды
            System.out.println(duration + " секунд");
        } else {

        }

        return null;
    }
}
