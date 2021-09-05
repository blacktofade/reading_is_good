# READING IS GOOD

ReadingIsGood is an online books retail firm which operates only on the Internet. Main 
target of ReadingIsGood is to deliver books from its one centralized warehouse to their 
customers within the same day. <br>

## Tech Stack
- Spring Boot
- Spring Security
- Spring Data
- Spring Validation
- MongoDB
- Lombok
- Swagger
- JWT
- MapStruct
- JUnit
- Mockito

## How to run?

You need to clone project first and you can run docker-compose.yml

**SWAGGER - POSTMAN** <br>
SWAGGER URL:`http://localhost:8080/swagger-ui.html` <br> 
POSTMAN COLLECTION: `${PROJECT_DIR}\Reading Is Good REST API.postman_collection.json`

## How to use?
There are two user type in this project. First one is `ADMIN` and the other one is `CUSTOMER`. When the project runs, the first signUp request sets the admin user. So you can decide the `ADMIN` username, password and email. After that you should use this `ADMIN` account for authorization required processes. Also all other signUp requests create `CUSTOMER` account.<br> <br>

If you want to login as a `CUSTOMER`, you need to create new account with using API `POST - ${HOST}/customer/signup`. Then you can login with new account.

**ADMIN CAN DO**
1) Create New Book
2) Update Book Stock
3) Get Orders
4) Get Orders with Date Interval
5) Cancel Orders

**CUSTOMERS CAN DO**
1) Get Its Own Orders
2) Give Order
3) Cancel Its Own Order
4) Get Monthly Statistics
