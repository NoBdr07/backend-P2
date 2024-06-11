package com.bdr.backend.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.bdr.backend.models.dtos.RentalDto;
import com.bdr.backend.models.entities.Rental;
import com.bdr.backend.repositories.RentalRepository;
import com.bdr.backend.utils.DateUtils;

@Service
public class RentalService {
	
	// Get the api host and port from the application.properties file
	@Value("${api.host}")
    private String apiHost;

    @Value("${server.port}")
    private int apiPort;
	
	@Autowired
	private RentalRepository rentalRepository;
	
	public Map<String, List<RentalDto>> getAllRentals() {
		List<Rental> rentals = rentalRepository.findAll();
	    List<RentalDto> rentalDtos = convertListToDto(rentals);
	    Map<String, List<RentalDto>> response = new HashMap<>();
	    response.put("rentals", rentalDtos);
	    return response;
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
		newRental.setCreatedAt(DateUtils.formatToMySQLDateTime(new Date()));
		
		rentalRepository.save(newRental);
		
		return newRental;
	}
	
	public Rental updateRental(int rentalId, String name, Integer surface, Integer price, String filePath, String description, Integer owner_id) {
	    Optional<Rental> optionalRental = getRentalById(rentalId);
	    if (!optionalRental.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Rental not found");
	    }
	    Rental rental = optionalRental.get();
	    if (name != null) {
	        rental.setName(name);
	    }
	    if (surface != null) {
	        rental.setSurface(surface);
	    }
	    if (price != null) {
	        rental.setPrice(price);
	    }
	    if (filePath != null) {
	        rental.setPicture(filePath);
	    }
	    if (description != null) {
	        rental.setDescription(description);
	    }
	    if (owner_id != null) {
	        rental.setOwnerId(owner_id);
	    }
	    
	    rental.setUpdatedAt(DateUtils.formatToMySQLDateTime(new Date()));
	    
	    return rentalRepository.save(rental);
	}
	
	public RentalDto convertToDto(Rental rental) {
	    RentalDto rentalDto = new RentalDto();
	    rentalDto.setId(rental.getRentalId());
	    rentalDto.setName(rental.getName());
	    rentalDto.setSurface(rental.getSurface());
	    rentalDto.setPrice(rental.getPrice());
	    rentalDto.setPicture(rental.getPicture());
	    rentalDto.setDescription(rental.getDescription());
	    rentalDto.setOwner_id(rental.getOwnerId());
	    rentalDto.setCreatedAt(rental.getCreatedAt());
	    rentalDto.setUpdatedAt(rental.getUpdatedAt());
	    return rentalDto;
	}
	
	public List<RentalDto> convertListToDto(List<Rental> rentals) {
		return rentals.stream().map(rental -> convertToDto(rental)).toList();
	}
	
	// Method to construct the full url when the picture need to be displayed in the front-end
	 public String constructFullUrl(String relativePath) {
	        return UriComponentsBuilder.newInstance()
	        		.scheme("http")
	                .host(apiHost)
	                .port(apiPort)
	                .path(relativePath)
	                .toUriString();
	    }

}
