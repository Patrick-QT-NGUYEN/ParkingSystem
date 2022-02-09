package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;


public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusHours(1);
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusHours(1);
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusHours(1);
        
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareCarWithFutureInTime(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().minusHours(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
    
    @Test
    public void calculateFareBikeWithFutureInTime(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().minusHours(1);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusMinutes(45); //45 minutes parking should give 3/4 parking fare
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusMinutes(45); //45 minutes parking should give 3/4 parking fare
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanThirtyMinutesParkingTime(){

        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusMinutes(20);

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareBikeWithLessThanThirtyMinutesParkingTime(){

        LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusMinutes(20);

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0 * Fare.BIKE_RATE_PER_HOUR) , ticket.getPrice());
    }
    
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusDays(1);
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithMoreThanADayParkingTime(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusDays(1);
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.BIKE_RATE_PER_HOUR) , ticket.getPrice());
    }

    
    @Test
    public void calculateFareCarRegularUser(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusHours(2);
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setVehicleRegNumber("ABCDE");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (2 * Fare.BIKE_RATE_PER_HOUR * Fare.DISCOUNT) , ticket.getPrice());
    }

    @Test
    public void calculateFareBikeRegularUser(){
    	LocalDateTime inTime = LocalDateTime.now();
        LocalDateTime outTime = LocalDateTime.now().plusHours(2);
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setVehicleRegNumber("FGHIJ");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (2 * Fare.BIKE_RATE_PER_HOUR * Fare.DISCOUNT) , ticket.getPrice());
    } 
    
}
