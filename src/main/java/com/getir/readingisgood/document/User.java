package com.getir.readingisgood.document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Document(collection= "users")
@Data
@NoArgsConstructor
public class User extends AuditDocument {
    @Id
    private String id;

    @NotBlank(message = "Username can not be empty!")
    private String username;

    @NotBlank(message = "Email can not be empty!")
    private String email;

    @NotBlank(message = "Password can not be empty!")
    private String password;

    @DBRef
    private Role role;

    public User(String username, String password,String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
