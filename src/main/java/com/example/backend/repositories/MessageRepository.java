package com.example.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.models.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{

}
