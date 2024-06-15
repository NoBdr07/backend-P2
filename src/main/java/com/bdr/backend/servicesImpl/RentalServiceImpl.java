package com.bdr.backend.servicesImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.bdr.backend.models.dtos.RentalDto;
import com.bdr.backend.models.entities.Rental;
import com.bdr.backend.repositories.RentalRepository;
import com.bdr.backend.services.RentalService;
import com.bdr.backend.utils.DateUtils;

@Service
public class RentalServiceImpl implements RentalService {
	
	/** Get the api host from the application.properties file */
	@Value("${api.host}")
    private String apiHost;

	/** Get the server port on which the API will run from the application.properties file */
    @Value("${server.port}")
    private int apiPort;
	
	@Autowired
	private RentalRepository rentalRepository;
	
	/**
	 * Get all rentals
	 * @return a map containing a list of rental DTO objects
	 */
	public Map<String, List<RentalDto>> getAllRentals() {
		List<Rental> rentals = rentalRepository.findAll();
	    List<RentalDto> rentalDtos = convertListToDto(rentals);
	    Map<String, List<RentalDto>> response = new HashMap<>();
	    response.put("rentals", rentalDtos);
	    return response;
	}
	
	/**
	 * Get a rental by its id
	 * 
	 * @param id The rental id
	 * @return an optional rental object
	 */
	public Optional<Rental> getRentalById(int id) {
		return rentalRepository.findById(id);
	}
	
	/**
	 * Create a new rental
	 * 
	 * @param name Name of the rental
	 * @param surface Surface of the house
	 * @param price Price per night
	 * @param picture Relative url of the picture like /uploads/example.jpg
	 * @param description Description of the house
	 * @param ownerId Owner Id of the house
	 * @return an optional rental object
	 */
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
	
	/**
	 * Update a rental
	 * 
	 * @param rentalId Id of the rental in db
	 * @param name Name of the rental
	 * @param surface Surface of the house
	 * @param price Price per night
	 * @param filePath Url of the picture file
	 * @param description Description of the house
	 * @param ownerId Owner Id the db
	 * @return an optional rental object
	 */
	public Rental updateRental(int rentalId, String name, Integer surface, Integer price, String filePath, String description, Integer ownerId) {
	    Rental rental = getRentalById(rentalId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Rental not found"));

	    updateIfNotNull(name, rental::setName);
	    updateIfNotNull(surface, rental::setSurface);
	    updateIfNotNull(price, rental::setPrice);
	    updateIfNotNull(filePath, rental::setPicture);
	    updateIfNotNull(description, rental::setDescription);
	    updateIfNotNull(ownerId, rental::setOwnerId);

	    rental.setUpdatedAt(DateUtils.formatToMySQLDateTime(new Date()));
	    
	    return rentalRepository.save(rental);
	}

	/**
	 * Check if the value is not null and update the rental object.
	 * To simplify the update process
	 */
	private <T> void updateIfNotNull(T value, Consumer<T> setter) {
	    if (value != null) {
	        setter.accept(value);
	    }
	}	
	
	/**
	 * Convert a rental object to a rental DTO object
	 * 
	 * @param rental The rental object
	 * @return a rental DTO object
	 */
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
	
	/**
	 * Convert a list of rental objects to a list of rental DTO objects
	 * 
	 * @param rentals The list of rental objects
	 * @return a list of rental DTO objects
	 */
	public List<RentalDto> convertListToDto(List<Rental> rentals) {
		return rentals.stream().map(this::convertToDto).toList();
	}
	
	/**	
	 * Construct the full URL for the picture
	 * @param relativePath The relative path to the picture that come from the database
	 *        with this format : /uploads/picture.jpg
	 * @return the full URL
	 */
	 public String constructFullUrl(String relativePath) {
	        return UriComponentsBuilder.newInstance()
	        		.scheme("http")
	                .host(apiHost)
	                .port(apiPort)
	                .path(relativePath)
	                .toUriString();
	    }

}
