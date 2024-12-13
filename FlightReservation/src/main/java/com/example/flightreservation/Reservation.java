package com.example.flightreservation;

import java.util.List;

public class Reservation {
    private static int counter = 1;
    private String reservationNumber;
    private Flight flight;
    private List<Seat> seats;
    private List<Passenger> passengers;             // The selected flight

    // Constructor for creating a reservation
    public Reservation(Flight flight, List<Seat> seats, List<Passenger> passengers) {
        this.reservationNumber = String.format("RN%03d",counter++);
        this.flight = flight;
        this.passengers = passengers;
        this.seats = seats;
    }

    // Getters for the details
    public String getReservationNumber() {
        return reservationNumber;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    @Override
    public String toString() {
        return "Reservation Number: " + reservationNumber + "\n" +
                "Passengers: " + passengers + "\n" +
                "Seats: " + seats + "\n" +
                "Flight: " + flight;
    }
}

