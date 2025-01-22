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

## To create jar file
This will take too long because will run all tests
```
mvn clean package
```

To skip the tests
```
mvn clean package -DskipTests
```

## To build docker image
docker build -t spring-boot-docker .

## To run docker compose with forced rebuild
docker-compose up -d --build

## To see the containers
docker compose ls
docker ps
docker logs <container_id>
To show the logs in realtime
docker logs -f <container_id>
docker images

### Kubernetes
To create kubectl deployment
kubectl create deployment spring-course-1-kubernetes --image=caiquecoelho/spring-course-1
To expose the service
kubectl expose deployment spring-course-1-kubernetes --type=LoadBalancer --port=8080
To get the public ip
kubectl get svc
To scale
kubectl scale deployment spring-course-1-kubernetes --replicas=3
To see the number of pods
kubectl get pods -o wide
To delete one replica to test the auto-scaling
kubectl delete pod spring-course-1-kubernetes-<pode_name>
Auto-scaling
kubectl autoscale deployment spring-course-1-kubernetes --cpu-percent=70 --min=3 --max=5
Changing versions without downstream edit the deployment file
kubectl edit deployment spring-course-1-kubernetes
In the file below the repicas number, add a new line called minReadySeconds: 15 and save the file and run
kubectl set image deployment spring-course-1-kubernetes spring-course-1-kubernetes=caiquecoelho/spring-course:1.2.0
See the changes of replicas with kubectl get pods -o wide
To see if the cluster was created
kubectl config current-context
kubectl config view
kubectl get nodes
kubectl version
To see the available clusters
kubectl config get-contexts
To change
kubectl config use-context