package com.capgemini.example;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
 
import com.capgemini.example.entity.Flight;
import com.capgemini.example.entity.Location;
import com.capgemini.example.entity.User;
import com.capgemini.example.exception.FlightNotFoundException;
import com.capgemini.example.exception.IdNotFoundException;
import com.capgemini.example.repository.FlightRepository;
import com.capgemini.example.repository.LocationRepository;
import com.capgemini.example.repository.UserRepository;
import com.capgemini.example.service.FlightServiceImplementation;
 
 
@SpringBootTest
public class FlightServiceTest {
 
	@Mock
	private FlightRepository flightRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	LocationRepository locationRepository;

	@InjectMocks
	private FlightServiceImplementation flightServiceImplementation;
	 @BeforeEach
	    void setUp() {
	        MockitoAnnotations.initMocks(this);
	    }
	@Test
	public void  getAllFlightTest()
	{
		  List<Flight> flights = new ArrayList<Flight>();
		  Location location = new Location(1, "bangalore", "bhg", "india", "kemp");
		  flights.add(new Flight(1, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14), LocalTime.of(20, 30,35)), 30, location));
		  flights.add(new Flight(2, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14), LocalTime.of(20, 30,35)), 30, location));
		  when(flightRepository.findAll()).thenReturn(flights);
		  assertEquals(2, flightServiceImplementation.getFlights().size());
	}
	@Test
	public void addFligths() {
		 int userId = 3;
	        User adminUser = new User(userId,"admin"); // Assuming admin user
	        Location location = new Location(1, "bangalore", "bhg", "india", "kemp");
	        Flight flightToAdd = new Flight(1, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30, location);

 
	        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
	        when(locationRepository.existsById(location.getLocationId())).thenReturn(true);
	        when(locationRepository.findById(location.getLocationId())).thenReturn(Optional.of(location));
	        when(flightRepository.save(any(Flight.class))).thenReturn(flightToAdd);
 
	       
	        Flight addedFlight =flightServiceImplementation.addFlights(userId,flightToAdd);
	       
	        assertEquals(flightToAdd, addedFlight);
	    }
	    @Test      
	    public void testUpdateFlight() throws IdNotFoundException, FlightNotFoundException {
	        
	        int flightId = 1;
	        Location location = new Location(1, "bangalore", "bhg", "india", "kemp");
	        Flight existingFlight = new Flight(flightId, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30, location);
	        Flight updatedFlight = new Flight(flightId, "Chennai", "Bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30, location);
 
	        when(flightRepository.existsById(flightId)).thenReturn(true);
	        when(flightRepository.findById(flightId)).thenReturn(Optional.of(existingFlight));
	        when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);
 
	        
	        Flight result = flightServiceImplementation.updateFlight(flightId, updatedFlight);
 
	       
	        assertEquals(updatedFlight, result);
	    }
        
	 @Test
	    void testDeleteFlightById_WhenFlightExists_ShouldDeleteSuccessfully() throws IdNotFoundException, FlightNotFoundException {
	        
	        int flightId = 1;
	        when(flightRepository.existsById(flightId)).thenReturn(true);
 
	       
	        String result = flightServiceImplementation.deleteFlightById(flightId);
	       
	        verify(flightRepository,times(1)).deleteById(flightId);
	        assertEquals("flight deleted successfully",result);
	    }
 
	    @Test
	    void testDeleteFlightById_WhenFlightDoesNotExist_ShouldThrowIdNotFoundException() {
	        
	        int flightId = 60;
	        when(flightRepository.existsById(flightId)).thenReturn(false);
 
	        
	        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> {
	        	flightServiceImplementation.deleteFlightById(flightId);
	        });
 
	        
	        verify(flightRepository,never()).deleteById(anyInt());
	        assertEquals("USER_ID_NOT_FOUND_INFO", exception.getMessage());
	    }

	 
	    void testGetFlightsByLocationId_WhenFlightsExist_ShouldReturnFlightList() throws IdNotFoundException {
	        
	        int locationId = 1;
	        List<Flight> myFlights = new ArrayList<>();
	        Location location = new Location(1, "bangalore", "bhg", "india", "kemp");
	        myFlights.add(new Flight(1, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30, location));
	        when(flightRepository.getAllFlightsByLocationId(locationId)).thenReturn(myFlights);
 
	        
	        List<Flight> result = flightServiceImplementation.getFlightsByLocationId(locationId);
 
	      
	        assertEquals(myFlights, result);
	    }
 
	    @Test
	    void testGetFlightsByLocationId_WhenNoFlightsExist_ShouldThrowIdNotFoundException() {
	        
	        int locationId = 1;
	        when(flightRepository.getAllFlightsByLocationId(locationId)).thenReturn(new ArrayList<>());
 
	        
	        assertThrows(IdNotFoundException.class, () -> {
	        	flightServiceImplementation.getFlightsByLocationId(locationId);
	        });
	    }
	    @Test
	    public void testFindFlightsByFlightId() throws IdNotFoundException {
	        
	        int flightId =1;
	        Location location = new Location(1, "bangalore", "bhg", "india", "kemp");
	        Flight expectedFlight = new Flight(flightId, "chennai", "bangalore", "bg01", "emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30, location);
	        
	        when(flightRepository.findFlightsByFlightId(flightId)).thenReturn(expectedFlight);
 
	      
	        Flight actualFlight =flightServiceImplementation.findFlightsByFlightId(flightId);
 
	     
	        verify(flightRepository,times(1)).findFlightsByFlightId(flightId);
 
	       
	        assertNotNull(actualFlight,"The returned flight should not be null");
 
	       
	        assertEquals(actualFlight,expectedFlight);
	    }

	    @Test
	    void testFindFlightsByLocations() throws FlightNotFoundException {
	       
	        String departureLocation = "Chennai";
	        String arrivalLocation = "Bangalore";
	        List<Flight> expectedFlights = new ArrayList<Flight>();
	        Location location = new Location(1, "Bangalore", "bhg", "india", "kemp");
	        expectedFlights.add(new Flight(1, "Chennai", "Bangalore", "bg01", "Emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30, location));
	       
	        when(flightRepository.findFlightsByDepartureLocationAndArrivalLocation(departureLocation, arrivalLocation))
	                .thenReturn(expectedFlights);
 
	       
	        List<Flight> actualFlights =flightServiceImplementation.findFlightsByLocations(departureLocation, arrivalLocation);
 
	     
	        verify(flightRepository, times(1)).findFlightsByDepartureLocationAndArrivalLocation(departureLocation, arrivalLocation);
 
	        
	        assertFalse(actualFlights.isEmpty(), "The list of flights should not be empty");
 
	        
	        assertEquals(expectedFlights, actualFlights, "The returned list of flights should match the expected list");
	    }
	    @Test
	    void testFindFlightsByLocationsThrowsException() {
	       
	    	String departureLocation = "Chennai";
	        String arrivalLocation = "Bangalore";
	        List<Flight> expectedFlights = new ArrayList<Flight>();
	        Location location = new Location(1, "Bangalore", "bhg", "india", "kemp");
	        expectedFlights.add(new Flight(1, "Chennai", "Bangalore", "bg01", "Emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30, location));
	        
	        when(flightRepository.findFlightsByDepartureLocationAndArrivalLocation(departureLocation, arrivalLocation))
	                .thenReturn(Arrays.asList());
 
	      
	        assertThrows(FlightNotFoundException.class,
	                () -> flightServiceImplementation.findFlightsByLocations(departureLocation, arrivalLocation),
	                "FlightNotFoundException should be thrown when no flights are found");
	    }
	    @Test
	    void testFindFlightsByDate() throws FlightNotFoundException {
	        
	        LocalDateTime departureTime = LocalDateTime.now();
	        List<Flight> expectedFlights = Arrays.asList(new Flight(/* flight details go here */));
 
	       
	        when(flightRepository.findFlightsByDate(departureTime))
	                .thenReturn(expectedFlights);
 
	       
	        List<Flight> actualFlights = flightServiceImplementation.findFlightsByDate(departureTime);
 
	       
	        verify(flightRepository, times(1)).findFlightsByDate(departureTime);
 
	        
	        assertFalse(actualFlights.isEmpty(), "The list of flights should not be empty");
 
	        
	        assertEquals(expectedFlights, actualFlights, "The returned list of flights should match the expected list");
	    }
 
	    @Test
	    void testFindFlightsByDateThrowsException() {
	       
	        LocalDateTime departureTime = LocalDateTime.now();
 
	
	        when(flightRepository.findFlightsByDate(departureTime))
	                .thenReturn(Arrays.asList());
 
	        assertThrows(FlightNotFoundException.class,
	                () -> flightServiceImplementation.findFlightsByDate(departureTime),
	                "FlightNotFoundException should be thrown when no flights are found at the given time");
	    }
	    @Test
	    void testFindFlightsByLocationsAndDate() throws FlightNotFoundException {
	       
	        String departureLocation = "Bangalore";
	        String arrivalLocation = "Chennai";
	        LocalDateTime departureTime = LocalDateTime.now();
	        List<Flight> expectedFlights = new ArrayList<Flight>();
	        Location location = new Location(1, "Bangalore", "bhg", "india", "kemp");
	        expectedFlights.add(new Flight(1, "Chennai", "Bangalore", "bg01", "Emirates", 40, 3000, LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(12, 45, 40)), LocalDateTime.of(LocalDate.of(2023, 12, 14),LocalTime.of(20, 30,35)), 30, location));

 
	        when(flightRepository.findFlightsByLocationsAndDate(departureLocation, arrivalLocation, departureTime))
	                .thenReturn(expectedFlights);
 
	     
	        List<Flight> actualFlights = flightServiceImplementation.findFlightsByLocationsAndDate(departureLocation, arrivalLocation, departureTime);
 
	       
	        verify(flightRepository, times(1)).findFlightsByLocationsAndDate(departureLocation, arrivalLocation, departureTime);
 
	        
	        assertFalse(actualFlights.isEmpty(), "The list of flights should not be empty");
 
	        assertEquals(expectedFlights, actualFlights, "The returned list of flights should match the expected list");
	    }
 
	    @Test
	    void testFindFlightsByLocationsAndDateThrowsException() {
	       
	        String departureLocation = "Bangalore";
	        String arrivalLocation = "Chennai";
	        LocalDateTime departureTime = LocalDateTime.now();
 
	        
	        when(flightRepository.findFlightsByLocationsAndDate(departureLocation, arrivalLocation, departureTime))
	                .thenReturn(Arrays.asList());
 
	     
	        assertThrows(FlightNotFoundException.class,
	                () ->  flightServiceImplementation.findFlightsByLocationsAndDate(departureLocation, arrivalLocation, departureTime),
	                "FlightNotFoundException should be thrown when no flights are found for the given locations and time");
	    }
 
}