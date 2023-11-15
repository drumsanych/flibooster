FROM alpine:latest

RUN apk add openjdk17


RUN apk add tor
RUN apk add --no-cache supervisor

COPY ./target/flibooster-0.0.1-SNAPSHOT.jar /opt/app.jar
COPY ./supervisord.conf /etc/supervisord.conf

RUN mkdir -p /opt/data/flibooster
RUN echo "SocksPort 9050" >> /etc/tor/torrc
RUN echo "RunAsDaemon 0" >> /etc/tor/torrc

EXPOSE 8090
EXPOSE 9050

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]