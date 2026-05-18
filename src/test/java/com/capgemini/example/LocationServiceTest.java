package com.capgemini.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
 
import com.capgemini.example.entity.Flight;
import com.capgemini.example.entity.Location;
import com.capgemini.example.exception.AlreadyExistsException;
import com.capgemini.example.exception.IdNotFoundException;
import com.capgemini.example.repository.LocationRepository;
import com.capgemini.example.service.LocationServiceImplementation;

@ExtendWith(MockitoExtension.class)
  @SpringBootTest
   public class LocationServiceTest {
		@Mock
	    private LocationRepository locationRepository;
	 
	    @InjectMocks
	    private LocationServiceImplementation locationServiceImplementation;
	 
	    @Test
	    void testAddLocation_ShouldReturnSavedLocation() throws AlreadyExistsException {
	       
	    	 List<Flight> flights = new ArrayList<Flight>();
	    	flights.add(new Flight(1, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30));
			flights.add(new Flight(2, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30));
	 
	        Location locationToSave = new Location(1, "bangalore", "bhg", "india", "kemp",flights);
	        Location savedLocation = new Location(1, "bangalore", "bhg", "india", "kemp",flights);
	        when(locationRepository.save(locationToSave)).thenReturn(savedLocation);
	 
	        
	        Location result = locationServiceImplementation.addLocation(locationToSave);
	 
	       
	        assertEquals(savedLocation, result);
	    }
	    @Test
	    void testGetLocation_ShouldReturnAllLocations() {
	       
	    	 List<Flight> flights = new ArrayList<Flight>();
	     	flights.add(new Flight(1, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30));
			flights.add(new Flight(2, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30));
	 
	        List<Location> myLocations = new ArrayList<>();
	        myLocations.add(new Location(1, "bangalore", "bhg", "india", "kemp",flights));
	        myLocations.add(new Location(1, "bangalore", "epip", "india", "hjd",flights));
	        when(locationRepository.findAll()).thenReturn(myLocations);
	 
	        
	        List<Location> result =locationServiceImplementation.getLocation();
	 
	        
	        assertEquals(myLocations, result);
	    }
	 
	    @Test
	    void testGetLocation_WhenNoLocationsExist_ShouldReturnEmptyList() {
	        
	        when(locationRepository.findAll()).thenReturn(new ArrayList<>());      
	 
	      
	        List<Location> result =locationServiceImplementation.getLocation();
	 
	        
	        assertNotNull(result);
	        assertTrue(result.isEmpty());
	    }
	    @Test
	    void testUpdateLocationById_WhenLocationExists_ShouldReturnUpdatedLocation() throws IdNotFoundException {
	        
	        int locationId = 1;
	        List<Flight> flights = new ArrayList<Flight>();
	     	flights.add(new Flight(1, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30));
			flights.add(new Flight(2, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30));
	 
	        Location existingLocation = new Location(1, "bangalore", "bhg", "india", "kemp",flights);
	        Location updatedLocation = new Location(1, "Bangalore", "bhg", "india", "kemp",flights);
	 
	        when(locationRepository.existsById(locationId)).thenReturn(true);
	        when(locationRepository.findById(locationId)).thenReturn(Optional.of(existingLocation));
	        when(locationRepository.save(existingLocation)).thenReturn(updatedLocation);
	 
	        
	        Location result = locationServiceImplementation.updateLocationById(locationId, existingLocation);
	 
	        
	        assertEquals(updatedLocation, result);
	    }
	 
	    @Test
	    void testUpdateLocationById_WhenLocationDoesNotExist_ShouldThrowIdNotFoundException() {
	        
	        int locationId = 1;
	        Location locationToUpdate = new Location(/* location to update details */);
	 
	        when(locationRepository.existsById(locationId)).thenReturn(false);
	 
	        
	        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> {
	        	locationServiceImplementation.updateLocationById(locationId, locationToUpdate);
	        });
	 
	        
	        assertEquals("no id found to update", exception.getMessage());
	    }
	    @Test
	    void testDeleteLocationById_WhenLocationExists_ShouldDeleteSuccessfully() throws IdNotFoundException {
	        
	        int locationId = 1;
	        when(locationRepository.existsById(locationId)).thenReturn(true);
	 
	        
	        String result =locationServiceImplementation.deleteLocationById(locationId);
	 
	        
	        verify(locationRepository,times(1)).deleteById(locationId);
	        assertEquals("location deleted successfully", result);
	    }
	 
	    @Test
	    void testDeleteLocationById_WhenLocationDoesNotExist_ShouldThrowIdNotFoundException() {
	       
	        int locationId = 1;
	        when(locationRepository.existsById(locationId)).thenReturn(false);
	 
	        
	        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> {
	        	locationServiceImplementation.deleteLocationById(locationId);
	        });
	 
	       
	        assertEquals("USER_ID_NOT_FOUND_INFO", exception.getMessage());
	        verify(locationRepository, never()).deleteById(anyInt());
	    }
	}

