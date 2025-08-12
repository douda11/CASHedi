package com.example.cashedi.repositories;

import com.example.cashedi.entites.Projet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetRepository extends MongoRepository<Projet, String> {
}
