package CarRenrt;
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

////////////////////////////////Renter Class (Aggregation) //////////////////////////
class Renter {
private String renterID;
private String name;
private String email;
private List<Car> rentedCars; // Aggregation: A renter can have multiple rented cars
private double totalRentalFee;
private String phoneNumber;
private String address;

public Renter(String renterID, String name, String email, String phoneNumber, String address,double totalRentalFee) {
this.renterID = renterID;
this.name = name;
this.email = email;
this.phoneNumber = phoneNumber;
this.address = address;
this.totalRentalFee= totalRentalFee;
this.setRentedCars(new ArrayList<>()); // Composition: List of cars is part of Renter object
}

public String getPhoneNumber() {
return phoneNumber; // Getter for phoneNumber
}

public void setAddress(String address) { // Setter for address
this.address = address;
}
public String getAddress() { 
return address; // Getter for address
}

// Setters
public void setPhoneNumber(String phoneNumber) {
this.phoneNumber = phoneNumber; // Setter for phoneNumber
}


public void addRentedCar(Car car) {
getRentedCars().add(car);
}

public void removeRentedCar(Car car) {
getRentedCars().remove(car);
}

public String getRenterID() {
return renterID;
}

public String getName() {
return name;
}

public String getEmail() {
return email;
}

public double getTotalRentalFee() {
return totalRentalFee;
}

@Override
public String toString() {
return "Renter ID: " + renterID + ", Name: " + name + ", Total Rental Fee: " + totalRentalFee;
}


public List<Car> getRentedCars() {
return rentedCars;
}


public void setRentedCars(List<Car> rentedCars) {
this.rentedCars = rentedCars;
}
}

////////////////////////////////Regular Renters Class /////////////////////////////
class RegularRenter extends Renter {
public RegularRenter(String renterID, String name, String email, String phoneNumber, String address) {
super(renterID, name, email, phoneNumber, address,34);
}
}

////////////////////////////////Frequent Renters with discount (Polymorphism) /////////////////////////////
class FrequentRenter extends Renter {
private final double discount = 0.20;  // 20% Discount for frequent renters

public FrequentRenter(String renterID, String name, String email, String phoneNumber, String address) {
super(renterID, name, email, phoneNumber, address,23);
}

@Override
public void addRentedCar(Car car) { // Overriding method (Polymorphism)
super.addRentedCar(car);
double discountAmount = car.calculateRent(30) * discount;
}
}

//////////////////////////////Corporate Renters with special rates (Polymorphism) /////////////////////
class CorporateRenter extends Renter {
private final double corporate_rate = 0.65;  // 45% less than regular rate

public CorporateRenter(String renterID, String name, String email, String phoneNumber, String address) {
super(renterID, name, email, phoneNumber, address,45);
}

@Override
public void addRentedCar(Car car) { // Overriding method (Polymorphism)
super.addRentedCar(car);
double specialRateAmount = car.calculateRent(40) * corporate_rate;
}
}
