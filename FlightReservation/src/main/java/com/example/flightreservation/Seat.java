package com.example.flightreservation;
enum SeatClass {
    BUSINESS,
    PREMIUM_ECONOMY,
    ECONOMY
}
public class Seat {

    private final String seatID;
    private final SeatClass seatClass;
    private boolean isBooked;
    private boolean isSelected;
    private int price;

    public Seat(String seatID, SeatClass seatClass) {
        this.seatID = seatID;
        this.seatClass = seatClass;
        this.isBooked = false;
        this.isSelected = false;
    }
    public Seat(String seatId, String seatClass) {
        this.seatID = seatId;
        this.seatClass = SeatClass.valueOf(seatClass);
    }

    public String getSeatID() {
        return seatID;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatID='" + seatID + '\'' +
                ", seatClass=" + seatClass +
                ", price=" + price +
                '}';
    }
}
