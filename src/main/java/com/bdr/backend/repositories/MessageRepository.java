package com.bdr.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bdr.backend.models.entities.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer>{

}
