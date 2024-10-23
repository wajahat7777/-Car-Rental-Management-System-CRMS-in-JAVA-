package CarRenrt;


/////////////////////////// Abstraction ///////////////////////////

abstract class Car {
    // Private fields (Encapsulation)
    private String carID;
    private String brand;
    private String model;
    private int year;
    private boolean rentalStatus;
    private double rentalFee;
    private String plateNumber;

    // Constructor
    public Car(String carID, String brand, String model, int year, double rentalFee, String plateNumber) {
        this.carID = carID;	
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.rentalFee = rentalFee;
        this.plateNumber = plateNumber;
        this.rentalStatus = false; 
    }
    
    
  
    //////////////// Encapsulation //////////////////////////
    public void setCarID(String ID) {
        carID = ID;
    }
    
    public String getCarID() {
        return carID;
    }


    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public String getPlateNumber() {
        return plateNumber;
    }
    public void setRentalFee(double rentalFee) {
        if (rentalFee >= 0) {
            this.rentalFee = rentalFee;
        } else {                                                       //->>change
            System.out.println("Rental fee cannot be negative.");
        }
    }


    public boolean isValidRentalFee(double rentalFee) {
        return rentalFee >= 0;
    }
    public boolean isRented() {
        return rentalStatus;
    }

    // Abstract method for subclasses to implement (Polymorphism)
    abstract double calculateRent(int d);

    public double getBaseRent() {
        return rentalFee;
    } 

    public String toString() {
        return "Car ID: " + carID + ", Brand: " + brand + ", Model: " + model + ", Year: " + year;
    }


}

///////////////////////////Compact Car Class (Polymorphism) /////////////////////////////
class CompactCar extends Car {
	    public CompactCar(String carID, String brand, String model, int year, double rentalFee, String plateNumber) {
	        super(carID, brand, model, year, rentalFee, plateNumber);
	    }


    // Overridden method (Polymorphism)
    public double calculateRent(int distance) {
        return getBaseRent() + distance; // Specific calculation for compact cars
    }

    public String getFeatures() {
        return "Basic Features, Suitable for Short Distance Travel";
    }

    public boolean getInsuranceStatus() {
        return false;
    }
}

/////////////////////////// SUV Class (Polymorphism) ///////////////////////////
class SUV extends Car {
    private final double insurance_per = getBaseRent() * 1 / 100;

    public SUV(String carID, String brand, String model, int year, double rentalFee, String plateNumber) {
        super(carID, brand, model, year, rentalFee, plateNumber);
    }

    @Override
    public double calculateRent(int distance) {
        double baseCost = getBaseRent() + distance;
        return baseCost; 
    }

    public double getDamageCost(boolean insurable) {
        double DamageCost = 1 / 100 * getBaseRent();
        return DamageCost; 
    }

    public String getFeatures() {
        return "Spacious, Suitable for family trips";
    }

    public boolean getInsuranceStatus() {
        return true;
    }
}

/////////////////////////// LuxuryCar Class (Polymorphism) ///////////////////////////
class LuxuryCar extends Car {
    private static final double luxuryTax = 0.20;   //->change static to final 
    private final double insuranceCostPercentage = getBaseRent() * 1 / 100;

    public LuxuryCar(String carID, String brand, String model, int year, double rentalFee, String plateNumber) {
        super(carID, brand, model, year, rentalFee, plateNumber);
    }

    @Override
    public double calculateRent(int distance) {
        double baseCost = getBaseRent() + distance;  
        double totalCost = baseCost + luxuryTax;  
        return totalCost;
    }

    public double getDamageCost(boolean insurable) {
        double DamageCost = 1 / 100 * getBaseRent();
        return DamageCost; 
    }

    public boolean getInsuranceStatus() {
        return true;
    }

    public String getFeatures() {
        return "High-end, Suitable for Special Occasions";
    }
}