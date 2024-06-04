package com.example.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.models.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer>{

}
