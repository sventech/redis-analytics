FROM andreptb/oracle-java:8
# based on debian:jessie

# Leiningen
RUN curl -s https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > \
            /usr/local/bin/lein && \
            chmod 0755 /usr/local/bin/lein
ENV LEIN_ROOT 1
RUN lein

# Redis Analytics app
RUN mkdir -p /redis-app
WORKDIR /redis-app
COPY project.clj /redis-app/
RUN lein deps
COPY . /redis-app
#RUN mv "$(lein uberjar | sed -n 's/^Created \(.*redis*\.jar\)/\1/p')" redis-analytics.jar
RUN lein uberjar
COPY ./target/redis-analytics.jar /redis-app/
CMD ["java", "-jar", "redis-analytics.jar"]
