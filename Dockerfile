FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY Main.java Servidor.java ./
COPY model ./model
COPY web ./web
RUN javac model/Pessoa.java model/Funcionario.java Servidor.java Main.java
EXPOSE 8080
CMD ["java", "Main"]
