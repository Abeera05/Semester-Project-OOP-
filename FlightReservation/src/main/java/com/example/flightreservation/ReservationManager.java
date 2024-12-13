package com.example.flightreservation;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    private List<Reservation> reservations;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReservationManager() {
        this.reservations = new ArrayList<>();
        loadReservationsFromFile();
    }

    public void makeReservation(Flight flight, List<Seat> seats, List<Passenger> passengers) {
        Reservation reservation = new Reservation(flight, seats, passengers);
        reservations.add(reservation);
        System.out.println("Reservation successful");

        // Write reservation details to file
        writeReservationToFile(reservation);
    }

    public void deleteReservation(String reservationNumber, String flightId) {
        Reservation reservationToDelete = findReservation(reservationNumber, flightId);

        if (reservationToDelete != null) {
            reservations.remove(reservationToDelete);
            System.out.println("Reservation deleted successfully.");
            rewriteReservationsFile();
        } else {
            System.out.println("Reservation not found.");
        }
    }

    private Reservation findReservation(String reservationNumber, String flightId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationNumber().equals(reservationNumber) &&
                    reservation.getFlight().getFlightId().equals(flightId)) {
                return reservation;
            }
        }
        return null;
    }

    private void rewriteReservationsFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AllReservations.txt", false))) {
            for (Reservation reservation : reservations) {
                writer.write(generateReservationDetails(reservation));
            }
        } catch (IOException e) {
            System.err.println("Error rewriting reservations file: " + e.getMessage());
        }
    }

    private String generateReservationDetails(Reservation reservation) {
        StringBuilder details = new StringBuilder();
        details.append("Reservation Number: ").append(reservation.getReservationNumber()).append("\n");

        Flight flight = reservation.getFlight();
        details.append("Flight ID: ").append(flight.getFlightId()).append("\n")
                .append("Source: ").append(flight.getSource()).append("\n")
                .append("Destination: ").append(flight.getDestination()).append("\n")
                .append("Departure: ").append(flight.getDepartureTime()).append("\n")
                .append("Arrival: ").append(flight.getArrivalTime()).append("\n");

        List<Passenger> passengers = reservation.getPassengers();
        details.append("Passengers:\n");
        for (Passenger passenger : passengers) {
            details.append(passenger.getFirstName()).append("\n");
        }

        List<Seat> seats = reservation.getSeats();
        details.append("Selected Seats:\n");
        for (Seat seat : seats) {
            details.append("Seat ID: ").append(seat.getSeatID()).append(" - ")
                    .append(seat.getSeatClass()).append("\n");
        }
        details.append("--- End of Reservation ---\n\n");
        return details.toString();
    }

    private void writeReservationToFile(Reservation reservation) {
        String details = generateReservationDetails(reservation);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AllReservations.txt", true))) {
            writer.write(details);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private void loadReservationsFromFile() {
        File file = new File("AllReservations.txt");
        if (!file.exists()) {
            return; // No file to load from
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Reservation reservation = null;
            List<Seat> seats = new ArrayList<>();
            List<Passenger> passengers = new ArrayList<>();
            Flight flight = null;
            String reservationNumber = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Reservation Number:")) {
                    if (reservation != null) {
                        reservation.setSeats(seats);
                        reservation.setPassengers(passengers);
                        reservations.add(reservation); // Save the previous reservation
                    }
                    reservationNumber = line.split(":")[1].trim();
                    seats.clear();
                    passengers.clear();
                    flight = null; // Reset flight for the new reservation
                    reservation = new Reservation(flight, seats, passengers); // Create a new reservation with flight, seats, and passengers
                    reservation.setReservationNumber(reservationNumber);
                } else if (line.startsWith("Flight ID:")) {
                    flight = new Flight();
                    flight.setFlightId(line.split(":")[1].trim());
                } else if (line.startsWith("Source:")) {
                    if (flight != null) {
                        flight.setSource(line.split(":")[1].trim());
                    }
                } else if (line.startsWith("Destination:")) {
                    if (flight != null) {
                        flight.setDestination(line.split(":")[1].trim());
                    }
                } else if (line.startsWith("Departure:")) {
                    if (flight != null) {
                        flight.setDepartureTime(LocalDate.parse(line.split(":")[1].trim(), dateFormatter));
                    }
                } else if (line.startsWith("Arrival:")) {
                    if (flight != null) {
                        flight.setArrivalTime(LocalDate.parse(line.split(":")[1].trim(), dateFormatter));
                        reservation.setFlight(flight); // Set flight after arrival time
                    }
                } else if (line.startsWith("Passengers:")) {
                    continue; // Passengers data follows
                } else if (!line.isBlank() && line.indexOf(":") == -1) {
                    // Passenger name
                    passengers.add(new Passenger(line.trim()));
                } else if (line.startsWith("Selected Seats:")) {
                    continue; // Seats data follows
                } else if (!line.isBlank()) {
                    // Seat details
                    String[] seatData = line.split(" - ");
                    seats.add(new Seat(seatData[0].trim(), seatData[1].trim()));
                }
            }

            // Add the last reservation
            if (reservation != null) {
                reservation.setSeats(seats);
                reservation.setPassengers(passengers);
                reservations.add(reservation);
            }

        } catch (IOException e) {
            System.err.println("Error loading reservations from file: " + e.getMessage());
        }
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
