package com.getir.readingisgood.repository;

import com.getir.readingisgood.enums.RoleType;
import com.getir.readingisgood.document.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByRoleType(RoleType roleType);
}
