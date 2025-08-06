package com.example.cashedi.repositories;

import com.example.cashedi.entites.Formule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IFormuleRepository extends MongoRepository<Formule, String> {
}
