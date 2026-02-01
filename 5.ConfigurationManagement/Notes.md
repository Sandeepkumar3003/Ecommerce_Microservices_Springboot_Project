✅ Current Architecture



Services:



Hotel Service



User Service



Rating Service



New Component:



Eureka Service (Service Registry)



🎯 Purpose of Adding Eureka



Enables service discovery



Eliminates hardcoded service URLs



Supports load balancing and scalability



Improves fault tolerance



🏗️ Eureka Server Setup



Dependency



<dependency>

    <groupId>org.springframework.cloud</groupId>

    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>

</dependency>





Main Class



@EnableEurekaServer

@SpringBootApplication

public class EurekaServerApplication {

    public static void main(String\[] args) {

        SpringApplication.run(EurekaServerApplication.class, args);

    }

}





Configuration (application.yml)



server:

  port: 8761



spring:

  application:

    name: eureka-server



eureka:

  client:

    register-with-eureka: false

    fetch-registry: false



🔌 Eureka Client Setup (Hotel, User, Rating Services)



Dependency



<dependency>

    <groupId>org.springframework.cloud</groupId>

    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>

</dependency>





Configuration (application.yml)



spring:

  application:

    name: hotel-service   # change per service



eureka:

  client:

    service-url:

      defaultZone: http://localhost:8761/eureka/





Optional (Auto-enabled in newer versions)



@EnableEurekaClient



🔄 Service Registration Flow



Eureka Server starts on port 8761



Hotel, User, Rating services start



Each service:



Registers with Eureka



Sends heartbeats



Fetches registry



Services can now call each other using service names instead of URLs



🔗 Inter-Service Communication Example



Instead of:



http://localhost:8082/hotels/1





Use:



http://HOTEL-SERVICE/hotels/1





(With @LoadBalanced RestTemplate or WebClient)



🧩 Benefits Achieved



Dynamic service discovery



No hardcoded endpoints



Built-in load balancing



Easier scaling and deployment



📌 Final Architecture

User Service  ----\\

Hotel Service ----->  Eureka Server (Registry)

Rating Service ----/

