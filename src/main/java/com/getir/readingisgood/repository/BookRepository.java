package com.getir.readingisgood.repository;

import com.getir.readingisgood.document.Book;
import com.getir.readingisgood.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book,String> {
}
