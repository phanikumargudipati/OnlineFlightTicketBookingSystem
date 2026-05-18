package com.capgemini.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.example.entity.Location;
import com.capgemini.example.exception.AlreadyExistsException;
import com.capgemini.example.exception.IdNotFoundException;
import com.capgemini.example.repository.LocationRepository;

@Service
public class LocationServiceImplementation implements LocationService{

	@Autowired
	LocationRepository locationRepository;
	
	public Location addLocation(Location location) throws AlreadyExistsException{
		
		Optional<Location> optional=	locationRepository.findByAirportName(location.getAirportName());
		   if(optional.isPresent())
			   throw new AlreadyExistsException("Already location is present");	
		return locationRepository.save(location);
	}
	
	public List<Location> getLocation(){
		return locationRepository.findAll();
	}

	@Override
	public Location updateLocationById(int locationId, Location location) throws IdNotFoundException {
		Location updateLocation=null;
		
		if(locationRepository.existsById(locationId))
		{
			updateLocation=locationRepository.findById(locationId).get();
			
			location.setLocationId(locationId);
		
			return locationRepository.save(location);
		}
		else
		{
			throw new IdNotFoundException("no id found to update");
		}
	
	}
	
	public String deleteLocationById(int locationId)throws IdNotFoundException {
		String msg;
		if(locationRepository.existsById(locationId))
		{
			locationRepository.deleteById(locationId);
			msg="location deleted successfully";
		}
		else {
			throw new IdNotFoundException("USER_ID_NOT_FOUND_INFO");
		}
		return msg;
	}

	
}
