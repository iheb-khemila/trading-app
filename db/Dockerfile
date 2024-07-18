FROM mysql:8.3

ENV MYSQL_DATABASE=swtp
ENV MYSQL_ALLOW_EMPTY_PASSWORD=true

COPY ./conf/* /etc/mysql/conf.d
COPY ./migrations/* /docker-entrypoint-initdb.d/