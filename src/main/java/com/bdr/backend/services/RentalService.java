package com.bdr.backend.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bdr.backend.models.dtos.RentalDto;
import com.bdr.backend.models.entities.Rental;

public interface RentalService {
	
	Map<String, List<RentalDto>> getAllRentals();

	Optional<Rental> getRentalById(int id);

	Rental createRental(String name, Integer surface, Integer price, String picture, String description,
			Integer ownerId);
	
	Rental updateRental(int id, String name, Integer surface, Integer price, String picture, String description,
			Integer ownerId);
	
	RentalDto convertToDto(Rental rental);
	
	List<RentalDto> convertListToDto(List<Rental> rentals);
	
	String constructFullUrl(String relativePath);

}
