### Running migration in the terminal skipping the tests
```
mvn clean package spring-boot:run -DskipTests
```

Running the application should be fine to run migrations, but we can do this in the therminal or we can use the flyway-maven-plugin
```
mvn flyway:migrate
```

## Running tests
mvn clean test
allure serve

## Open API Docs
http://localhost:8081/v3/api-docs

## Open Swagger UI
http://localhost:8081/swagger-ui/index.html

# Troubleshooting

Test Container test cases are failing due to "Could not find a valid Docker environment"
## To run SwaggerIntegarionTests in MAC, run the following command
```
sudo ln -s $HOME/.docker/run/docker.sock /var/run/docker.sock
```