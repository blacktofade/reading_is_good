package com.getir.readingisgood;

import com.getir.readingisgood.document.Role;
import com.getir.readingisgood.enums.OrderType;
import com.getir.readingisgood.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ReadingIsGoodApplication implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;


    public static void main(String[] args) {
        SpringApplication.run(ReadingIsGoodApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = Role.builder().roleType(RoleType.ROLE_ADMIN).build();
        Role roleCustomer = Role.builder().roleType(RoleType.ROLE_CUSTOMER).build();

        mongoTemplate.getCollection("roles").drop();
        mongoTemplate.getCollection("users").drop();
        mongoTemplate.getCollection("orders").drop();
        mongoTemplate.getCollection("books").drop();
        mongoTemplate.save(roleAdmin, "roles");
        mongoTemplate.save(roleCustomer, "roles");
    }

}
