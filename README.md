# user-auth-api

User Authenticate API

## Clean and Build

Execute command to clean and build project

```bash
./gradlew clean build
```

## Run
Next, to execute app in port 8080 with active profile local execute command

```bash
./gradlew bootRun -Dspring.profiles.active=local
```

### Curls

Endpoint Sing-Up
```bash
curl --location --request POST 'http://localhost:8080/api/v1/users/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "David Navarro",
    "email": "david.navastud@gmail.co",
    "password": "a2asfGfdfdf4",
    "phones": [
        {
            "number": 987654321,
            "citycode": 7,
            "contrycode": "25"
        }
    ]
}
'
```
Endpoint Login
```bash
curl --location --request GET 'http://localhost:8080/api/v1/users/login' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYXZpZC5uYXZhc3R1ZEBnbWFpbC5jbyIsImlhdCI6MTcwMTY4OTg0NSwiZXhwIjoxNzAxNjg5ODQ1fQ.ubXW7eOrEPQS57ulGASt8THHQVy4K5iwSqWE1pM5CiyKkU1AA_gskUtqcF1OjXKdaEYoKU_UesW-8GNcgwj6RQ'
```
Actuator
```bash
curl --location --request GET 'http://localhost:8080/api/v1/actuator'
```

Actuator HeathCheck
```bash
curl --location --request GET 'http://localhost:8080/api/v1/actuator/health'
```
## Open API

To review the Open API documentation, you should go to the URL

http://localhost:8080/api/v1/swagger-ui.html

## Diagrams

* Component
![component_diagram.png](uml%2Fcomponent_diagram.png)

* Sequence Login
![login_sequence_diagram.png](uml%2Flogin_sequence_diagram.png)

* Sequence Sign-Up
![sign_up_sequence_diagram.png](uml%2Fsign_up_sequence_diagram.png)