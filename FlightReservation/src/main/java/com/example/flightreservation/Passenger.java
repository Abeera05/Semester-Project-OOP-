package com.example.flightreservation;

public class Passenger {
    private String firstName;
    private String email;
    private String phoneNumber;
    private String passportNumber;
    private String nationality;
    private String address;

    public Passenger(String firstName, String email, String phoneNumber, String passportNumber, String nationality, String address) {
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
        this.nationality = nationality;
        this.address = address;
    }
    public Passenger(String firstName) {
        this.firstName = firstName;}

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public String getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return "Name: " + firstName + ", Email: " + email + ", Phone: " + phoneNumber +
                ", Passport: " + passportNumber + ", Nationality: " + nationality + ", Address: " + address;
    }
}
