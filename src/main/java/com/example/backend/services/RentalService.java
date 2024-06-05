package com.example.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.models.entities.Rental;
import com.example.backend.repositories.RentalRepository;

@Service
public class RentalService {
	
	@Autowired
	private RentalRepository rentalRepository;
	
	public List<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}
	
	public Optional<Rental> getRentalById(int id) {
		return rentalRepository.findById(id);
	}
	
	public Rental createRental(String name, Integer surface, Integer price, String picture, String description, Integer ownerId) {
		Rental newRental = new Rental();
		newRental.setName(name);
		newRental.setSurface(surface);
		newRental.setPrice(price);
		newRental.setPicture(picture);
		newRental.setDescription(description);
		newRental.setOwnerId(ownerId);
		rentalRepository.save(newRental);
		
		return newRental;
	}
	
	public Rental updateRental(int id, String name, Integer surface, Integer price, String picture, String description,
			Integer ownerId) {
		Rental rental = rentalRepository.findById(id).get();
		rental.setName(name);
		rental.setSurface(surface);
		rental.setPrice(price);
		rental.setPicture(picture);
		rental.setDescription(description);
		rental.setOwnerId(ownerId);
		rentalRepository.save(rental);

		return rental;
	}

}
