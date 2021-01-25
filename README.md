# Listagem de diretórios no Spring Boot

*Directory Listing on Spring Boot*

Esse projeto é pensado em fazer uma listagem de diretórios usando o Spring Boot, função similar à realizada pelo modulo **ngx_http_autoindex_module do NGINX**, similar também a configuração **Options -Indexes** no Apache HTTP Server ("httpd") e **listings=true** no Apache Tomcat®.

Essa abordagem de usar o Spring Boot permite controlar programaticamente a listagem de diretórios e usar toda estrutura do Spring para criar regras de negócio.

A pasta que o servidor observa é a `public` que está no mesmo nivel que a aplicação.

---

## Projeto no [Repl.it](https://repl.it)

[GuilhermeRodri8/directory-listing-on-spring-boot](https://directory-listing-on-spring-boot.guilhermerodri8.repl.co)

---

## Gerando o pacote JAR e executando o projeto

````shell
# Comandos executados na raiz do projeto

# Gerando pacote jar (Use a flag `-DskipTests` para pular os testes)
./mvnw clean package

# Executando pacote jar
java -jar target/serveindex-0.0.1-SNAPSHOT.jar

# Executando pacote jar em uma porta
java -jar target/serveindex-0.0.1-SNAPSHOT.jar --server.port=8010

# Executando projeto sem criar pacote jar
./mvnw spring-boot:run

# Executando projeto sem criar pacote jar em uma porta
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8010
````

---

## Feature Request

- Deploy sites estáticos
- Template Apache HTTP Server ("httpd")
- Template com icones [http://www.apache.org/icons/](http://www.apache.org/icons/)
- Template Apache Tomcat®
- Template Personalizado
- Criar template de captura de exceções no thymeleaf
