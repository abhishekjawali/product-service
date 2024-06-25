## Reference app to showcase code transformation using Amazon Q Developer

### Requirements
 - Java 8
 - Maven
 - Postgres

### Setup
 - Update the Postgres database details in application.properties. 

```
mvn clean install
java -jar target/product-service.jar
```

### Testing
```
curl localhost:8080/products
```

