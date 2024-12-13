package com.example.flightreservation;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FlightDatabase {
    private List<Flight> flights;

    // Constructor
    public FlightDatabase() {
        flights = new ArrayList<>();
    }

    // Save flights to a file
    public void saveFlights(String fileName) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Flight flight : flights) {
                writer.write(flight.getFlightId() + "," + flight.getDestination() + "," + flight.getSource() + ","
                        + flight.getDepartureTime().format(dateFormatter) + "," + flight.getArrivalTime().format(dateFormatter) );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add a flight
    public void addFlight(Flight flight) {
        flights.add(flight);
        saveFlights("flights.txt");
    }

    // Delete a flight by ID
    public boolean deleteFlight(String flightID) {
        boolean removed = flights.removeIf(flight -> flight.getFlightId().equals(flightID));
        if (removed) {
            saveFlights("flights.txt");
        }
        return removed;
    }
    // Get all flights
    public List<Flight> getFlights() {
        return flights;
    }

    public void loadFlightsFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String[] parts = line.split(",");
                    Flight flight = new Flight();
                    flight.setFlightId(parts[0]);
                    flight.setSource(parts[1]);
                    flight.setDestination(parts[2]);

                    flight.setDepartureTime(LocalDate.parse(parts[3], dateFormatter));
                    flight.setArrivalTime(LocalDate.parse(parts[4], dateFormatter));

                    flights.add(flight);
                    System.out.println("Loaded flight: " + flight.getFlightId());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading flights: " + e.getMessage());
        }
    }



    public List<Flight> searchFlights(String source, String destination, LocalDate departureDate, LocalDate arrivalDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formattedDepartureDate = departureDate.format(formatter);
        String formattedArrivalDate = arrivalDate.format(formatter);

        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            String flightDepartureDate = flight.getDepartureTime().format(formatter);
            String flightArrivalDate = flight.getArrivalTime().format(formatter);

            if (flight.getSource().equalsIgnoreCase(source)
                    && flight.getDestination().equalsIgnoreCase(destination)
                    && flightDepartureDate.equals(formattedDepartureDate)
                    && flightArrivalDate.equals(formattedArrivalDate)) {
                result.add(flight);
            }
        }

        return result;
    }
}

