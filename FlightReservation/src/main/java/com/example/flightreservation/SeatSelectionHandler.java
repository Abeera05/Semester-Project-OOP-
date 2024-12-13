package com.example.flightreservation;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionHandler {
    private List<Seat> selectedSeats = new ArrayList<>();

    public void seatSelection(Seat seat) {
        if (seat.isBooked()) {
            System.out.println("Seat already booked!");
            return;
        }
        if (seat.isSelected()) {
            seat.setSelected(false);
            selectedSeats.remove(seat);
        } else {
            seat.setSelected(true);
            selectedSeats.add(seat);
        }
    }

    public List<Seat> getSelectedSeats() {
        return selectedSeats;
    }

}

