package com.example.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.models.entities.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer>{
	
	List<Rental> findAll();
	
	Optional<Rental> findById(int id);
	
	

}
