package com.getir.readingisgood.document;

import com.getir.readingisgood.enums.RoleType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Data
@Builder
public class Role {

    @Id
    private String id;

    private RoleType roleType;
}
