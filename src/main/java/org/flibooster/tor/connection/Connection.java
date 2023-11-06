package org.flibooster.tor.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

@Component
public class Connection {

    @Value("${app.flibusta.url}")
    private String flibustaUrl;

    @Autowired
    private Proxy proxy;

    public URLConnection getConnection(URL url) throws IOException {
        return url.openConnection(proxy);
    }

    public URLConnection getConnection() throws IOException {
        return new URL(flibustaUrl).openConnection(proxy);
    }

    public URLConnection getConnection(String apiUrl) throws IOException {
        return new URL(flibustaUrl + "/" + apiUrl).openConnection(proxy);
    }
}
