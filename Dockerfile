#docker build -t jrouter_home:1 .
#docker run --name jrouter_home --rm -it -p 8080:8080 jrouter_home:1
FROM openjdk:11-slim

WORKDIR /opt/jrouter_home
ADD target/jrouter-home.war .

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

ENV JAVA_OPTS="-server -Xmn32M -Xms64M -Xmx128M"
ENV APP_OPTS="--spring.profiles.active=production"
EXPOSE 8080

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar jrouter-home.war --server.port=8080 ${APP_OPTS}
#HEALTHCHECK --interval=10s --timeout=3s --retries=3 CMD curl -f http://localhost:8080/ || exit 1