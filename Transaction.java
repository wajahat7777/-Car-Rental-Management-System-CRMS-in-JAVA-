package CarRenrt;
import java.util.UUID;

public class Transaction {
    private String transactionID;
    private String carID;
    private String renterID;
    private double rentalCost;
    private double insuranceCost;
    private double damageCost;

    // Constructor
    public Transaction(String transactionID, String carID, String renterID, double rentalCost, double insuranceCost, double damageCost) {
        this.transactionID = transactionID;
        this.carID = carID;
        this.renterID = renterID;
        this.rentalCost = rentalCost;
        this.insuranceCost = insuranceCost;
        this.damageCost = damageCost;
    }

    public String generateTransactionID() {
        // Use UUID to generate a unique identifier for the transaction
        return UUID.randomUUID().toString();
    }


    // Getters
    public String getTransactionID() {
        return transactionID;
    }

    public String getCarID() {
        return carID;
    }

    public String getRenterID() {
        return renterID;
    }

    public double getRentalCost() {
        return rentalCost;
    }

    public double getInsuranceCost() {
        return insuranceCost;
    }

    public double getDamageCost() {
        return damageCost;
    }

    // Setters
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public void setRenterID(String renterID) {
        this.renterID = renterID;
    }

    public void setRentalCost(double rentalCost) {
        this.rentalCost = rentalCost;
    }

    public void setInsuranceCost(double insuranceCost) {
        this.insuranceCost = insuranceCost;
    }

    public void setDamageCost(double damageCost) {
        this.damageCost = damageCost;
    }
}

