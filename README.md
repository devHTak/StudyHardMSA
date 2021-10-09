# StudyHardMSA
- 백기선님 강의를 통해 생성한 StudyHard 를 기반으로 진행한 MSA Project.

#### Service Desc.

<img width="760" alt="스크린샷 2021-10-09 오후 4 53 12" src="https://user-images.githubusercontent.com/42403023/136649565-a8357cbc-c4d4-476b-b3f8-0dc13249ac62.png">

- Eureka service
  - Discovery service
- Gateway service
  - Authentication check by filter
  - Identifies the service with respect to the request URL and sends the request back to the corresponding service.
- User service
- Study service
- Event service
- TagZone service

#### API

|Request service|Response service|HTTP|API Desc|
|---|---|---|---|
|User service|Tag zone service|POST /tag-zone-service/tags|Save tag|
|User service|Tag zone service|GET /tag-zone-service/tags/{name}|Exist tag|
|User service|Tag zone service|GET /tag-zone-service/tags/{name}/find|find tag|
|User service|Tag zone service|DELETE /tag-zone-service/tags/{name}/delete|delete tag|
|User service|Tag zone service|POST /tag-zone-service/zones|Save zone|
|User service|Tag zone service|GET /tag-zone-service/zones|Exist zone|
|User service|Tag zone service|GET /tag-zone-service/zones/find|find zone|
|User service|Tag zone service|DELETE /tag-zone-service/zones/{zoneId}|delete zone|
|Study service|Tag zone service|POST /tag-zone-service/zones|Save zone|
|Study service|Tag zone service|GET /tag-zone-service/zones|Exist zone|
|Study service|Tag zone service|GET /tag-zone-service/zones/find|find zone|
|Study service|Tag zone service|POST /tag-zone-service/tags|Save tag|
|Study service|Tag zone service|GET /tag-zone-service/tags/{name}|Exist tag|
|Study service|Tag zone service|GET /tag-zone-service/tags/{name}/find|find tag|


#### Spec.

- Java 1.8
- Spring Boot
  - Spring Security
- Spring Cloud
  - Eureka(Discovery), Resilience4j(Circuit Breaker), Feign(API), Gateway, Kafka
- JPA
- H2
- JWT


#### TODO

- container servie (ex. Docker, Kubernetes ..)
- monitoring process (ex. Zipkin, Slueth etc ..)
