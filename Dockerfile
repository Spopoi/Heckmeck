FROM dew54/heckmeck:base

WORKDIR /Heckmeck

RUN git pull
RUN ./gradlew build

CMD ["./gradlew", "run"]