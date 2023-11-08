package org.flibooster.tor.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

@Component
public class Connection {
    private final Logger log = LoggerFactory.getLogger(Connection.class);

    @Value("${app.flibusta.url}")
    private String flibustaUrl;

    private Proxy proxy;

    public Connection(@Autowired Proxy proxy) {
        this.proxy = proxy;
    }

    public URLConnection getConnection(String apiUrl) throws IOException {
        if (apiUrl.startsWith("/")) apiUrl = apiUrl.substring(1, apiUrl.length());
        log.info("Getting new connection " + apiUrl);
        URLConnection connection = new URL(flibustaUrl + "/" + apiUrl).openConnection(proxy);
        connection.connect();
        log.info(this.getClass().getName() + ": Connection success!");
        return connection;
    }
}
