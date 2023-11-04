package net.yokniga.yoik.tor.connection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class ConnectionConfig {
    @Bean
    public Proxy getProxy() {
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9050));
    }
}
