package com.example.flightreservation;

import java.time.LocalDate;
import java.time.Duration;
import java.util.List;

public class Flight {
    private String flightID;
    private static int counter=0;
    private String source;
    private String destination;
    private LocalDate departureTime;
    private LocalDate arrivalTime;
    private int totalSeats;
    private int availableSeats;
    private double price;
    private String status;

    public Flight() {
        this.flightID= String.format("FL%03d",++counter);
    }
    public Flight(String flightid, String fromCity, String toCity, LocalDate departuretime, LocalDate arrivalTime) {
        this.flightID = flightid;
        this.source = fromCity;
        this.destination = toCity;
        this.departureTime = departuretime;
        this.arrivalTime = arrivalTime;
    }

        public void displayFlightInfo() {
        System.out.println("Flight ID: " + flightID);
        System.out.println("Source: " + source + " -> Destination: " + destination);
        System.out.println("Departure: " + departureTime + " | Arrival: " + arrivalTime);
        System.out.println("Available Seats: " + availableSeats);
    }

    public String getFlightId() {
        return flightID;
    }

    public void setFlightId(String flightId) {
        this.flightID = flightId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDate departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDate getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDate arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Duration getFlightDuration() {
        return Duration.between(departureTime, arrivalTime);
    }
}
