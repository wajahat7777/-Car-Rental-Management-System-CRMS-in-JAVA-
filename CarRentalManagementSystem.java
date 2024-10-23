package CarRenrt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.*;
import java.util.Scanner;


/////////////////////////// CarRentalManagementSystem Class (Composition) ////////////////////////////
class CarRentalManagementSystem {

private CarCRUD carCRUD;
private RenterCRUD renterCRUD;
private TransactionCRUD transactionCRUD;

public CarRentalManagementSystem(MySQL dbConnection) {
this.carCRUD = new CarCRUD(dbConnection);
this.renterCRUD = new RenterCRUD(dbConnection);
this.transactionCRUD = new TransactionCRUD(dbConnection);
}

private List<Car> cars; // Composition: List of cars is part of the management system
private List<Renter> renters; // Composition: List of renters is part of the management system

public double calculateRentalCost(Connection con, String carID, int distance) throws SQLException {
    // Query to get the rental fee of the car
    String sql = "SELECT rentalFee FROM Cars WHERE carID = ?";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, carID);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            double rentalFeePerKm = rs.getDouble("rentalFee");
            // Rental cost is fee per km multiplied by the distance driven
            return rentalFeePerKm * distance;
        } else {
            throw new SQLException("Car not found for the given carID: " + carID);
        }
    }
}


public void loadCarsFromDatabase(Connection con) throws SQLException {
    String sql = "SELECT * FROM Cars";
    try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            String carID = rs.getString("carID");
            String brand = rs.getString("brand");
            String model = rs.getString("model");
            int year = rs.getInt("year");
            double rentalFee = rs.getDouble("rentalFee");	
            String plateNumber = rs.getString("plateNumber");
            String carType = rs.getString("carType");  // carType might be null
            
            if (carType == null) {
                throw new IllegalArgumentException("Car type is null for carID: " + carID);
            }

            carType = carType.toLowerCase(); // Safely call toLowerCase now
            Car car;
            switch (carType) {
                case "compactcar":
                    car = new CompactCar(carID, brand, model, year, rentalFee, plateNumber);
                    break;
                case "suv":
                    car = new SUV(carID, brand, model, year, rentalFee, plateNumber);
                    break;
                case "luxurycar":
                    car = new LuxuryCar(carID, brand, model, year, rentalFee, plateNumber);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid car type: " + carType);
            }
            addCar(car); // Adding the car to the internal list
        }
    }
}

public void loadRentersFromDatabase(Connection con) throws SQLException {
    String sql = "SELECT * FROM Renters";
    try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            String renterID = rs.getString("renterID");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phoneNumber = rs.getString("phoneNumber");
            String address = rs.getString("address");
            String renterType = rs.getString("renterType").trim().toLowerCase(); // Normalize the renterType

            Renter renter;
            switch (renterType) {
                case "regularrenter":
                    renter = new RegularRenter(renterID, name, email, phoneNumber, address);
                    break;
                case "frequentrenter":
                    renter = new FrequentRenter(renterID, name, email, phoneNumber, address);
                    break;
                case "corporaterenter":
                    renter = new CorporateRenter(renterID, name, email, phoneNumber, address);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid renter type: " + renterType);
            }
            addRenter(renter); // Adding the renter to the internal list
        }
    }
}

public CarRentalManagementSystem() {
this.cars = new ArrayList<>();
this.renters = new ArrayList<>();
}

//Getters and Setters for cars and renters
public List<Car> getCars() {
return cars;
}

public void setCars(List<Car> cars) {
this.cars = cars;
}

public List<Renter> getRenters() {
return renters;
}

public void setRenters(List<Renter> renters) {
this.renters = renters;
}


//Method to get a specific car by ID
public Car getCarByID(String carID) {
for (Car car : cars) {
if (car.getCarID().equals(carID)) {
return car;
}
}
return null; // Return null if no car is found
}

//Method to get a specific renter by ID
public Renter getRenterByID(String renterID) {
for (Renter renter : renters) {
if (renter.getRenterID().equals(renterID)) {
return renter;
}
}
return null; // Return null if no renter is found
}


public void addCar(Car car) {
//Check for duplicate Car ID
for (Car existingCar : cars) {
if (existingCar.getCarID().equals(car.getCarID())) {
System.out.println("Error: Car with ID " + car.getCarID() + " already exists.");
return;
}
}
cars.add(car);
System.out.println("Car added successfully!");
}

public void displayAvailableCars() {
for (Car car : cars) {
if (!car.isRented()) {
System.out.println(car);
}
}
}

public void removeCar(String carID) {
Car carToRemove = getCarByID(carID); // Using the new getter method
if (carToRemove != null) {
if (!carToRemove.isRented()) {
cars.remove(carToRemove);
System.out.println("Car removed successfully!");
} else {
System.out.println("Error: Cannot remove car with ID " + carID + " as it is currently rented.");
}
} else {
System.out.println("Error: Car with ID " + carID + " not found.");
}
}

public void addRenter(Renter renter) {
//Check for duplicate Renter ID
for (Renter existingRenter : renters) {
if (existingRenter.getRenterID().equals(renter.getRenterID())) {
System.out.println("Error: Renter with ID " + renter.getRenterID() + " already exists.");
return;
}
}
renters.add(renter);
System.out.println("Renter added successfully!");
}

public void displayRenters() {
for (Renter renter : renters) {
System.out.println(renter);
}
}

public String generateTransactionID() {
    // Generate a unique transaction ID using UUID
    return UUID.randomUUID().toString();
}

public void removeRenter(String renterID) {
Renter renterToRemove = getRenterByID(renterID); // Using the new getter method
if (renterToRemove != null) {
if (renterToRemove.getRentedCars().isEmpty()) {
renters.remove(renterToRemove);
System.out.println("Renter removed successfully!");
} else {
System.out.println("Error: Cannot remove renter with ID " + renterID + " as they have active rentals.");
}
} else {
System.out.println("Error: Renter with ID " + renterID + " not found.");
}
}

public void removeCarFromDatabase(Connection con, String carID) throws SQLException {
String sql = "DELETE FROM Cars WHERE carID = ?";
try (PreparedStatement stmt = con.prepareStatement(sql)) {
stmt.setString(1, carID);
stmt.executeUpdate();
System.out.println("Car removed from database successfully!");
}
}

public Transaction rentCar(Connection con, String carID, String renterID, int distance) throws SQLException {
    // Step 1: Check if the car is available
    String availabilityCheckQuery = "SELECT * FROM Cars WHERE carID = ? AND isAvailable = 1"; // Assuming there's an isAvailable column
    try (PreparedStatement checkStmt = con.prepareStatement(availabilityCheckQuery)) {
        checkStmt.setString(1, carID);
        ResultSet rs = checkStmt.executeQuery();
        
        if (!rs.next()) {
            System.out.println("Car is not available for rent.");
            return null; // Return null if car is unavailable
        }
    }
    
    // Step 2: Calculate rental cost (example: cost per km * distance)
    double rentalCost = calculateRentalCost(con, carID, distance);
    
    // Assuming there are fixed insurance and damage costs for simplicity
    double insuranceCost = 50.0;  // Example fixed cost
    double damageCost = 0.0;  // Initially, no damage
    
    // Step 3: Generate a unique transaction ID
    String transactionID = UUID.randomUUID().toString();  // Use UUID for generating a unique transaction ID

    // Step 4: Insert transaction into the Transactions table
    String transactionInsertQuery = "INSERT INTO Transactions (transactionID, carID, renterID, rentalCost, insuranceCost, damageCost) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement transactionStmt = con.prepareStatement(transactionInsertQuery)) {
        transactionStmt.setString(1, transactionID);
        transactionStmt.setString(2, carID);
        transactionStmt.setString(3, renterID);
        transactionStmt.setDouble(4, rentalCost);
        transactionStmt.setDouble(5, insuranceCost);
        transactionStmt.setDouble(6, damageCost);
        transactionStmt.executeUpdate();
    }
    
    // Step 5: Update the car's availability
    String updateCarAvailabilityQuery = "UPDATE Cars SET isAvailable = 0 WHERE carID = ?";
    try (PreparedStatement updateStmt = con.prepareStatement(updateCarAvailabilityQuery)) {
        updateStmt.setString(1, carID);
        updateStmt.executeUpdate();
    }
    
    // Step 6: Return the Transaction object
    Transaction transaction = new Transaction(transactionID, carID, renterID, rentalCost, insuranceCost, damageCost);
    System.out.println("Car rented successfully! Transaction recorded.");
    return transaction;  // Return the Transaction object
}


public void addCarToDatabase(Connection con, Car car) throws SQLException {
    String sql = "INSERT INTO Cars (carID, brand, model, year, rentalFee, plateNumber, carType) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, car.getCarID());
        stmt.setString(2, car.getBrand());
        stmt.setString(3, car.getModel());
        stmt.setInt(4, car.getYear());
        stmt.setDouble(5, car.getRentalFee());
        stmt.setString(6, car.getPlateNumber());
        stmt.setString(7, car.getClass().getSimpleName().toLowerCase()); // carType is set based on class name
        stmt.executeUpdate();
        System.out.println("Car added to the database successfully!");
    }
}


public void removeRenterFromDatabase(Connection con, String renterID) throws SQLException {
String sql = "DELETE FROM Renters WHERE renterID = ?";
try (PreparedStatement stmt = con.prepareStatement(sql)) {
stmt.setString(1, renterID);
stmt.executeUpdate();
System.out.println("Renter removed from database successfully!");
}
}

public void addRenterToDatabase(Connection con, Renter renter) throws SQLException {
    String sql = "INSERT INTO Renters (renterID, name, email, phoneNumber, address, renterType) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, renter.getRenterID());
        stmt.setString(2, renter.getName());
        stmt.setString(3, renter.getEmail());
        stmt.setString(4, renter.getPhoneNumber());
        stmt.setString(5, renter.getAddress());
        stmt.setString(6, renter.getClass().getSimpleName()); // renterType is set based on class name
        stmt.executeUpdate();
        System.out.println("Renter added to database successfully!");
    }
}



public void rentCar(String carID, String renterID, int distance) {
Car selectedCar = getCarByID(carID); // Using the new getter method
Renter selectedRenter = getRenterByID(renterID); // Using the new getter method

if (selectedCar != null && selectedRenter != null) {
selectedRenter.addRentedCar(selectedCar);
cars.remove(selectedCar); // Remove the rented car from the available list
System.out.println("Car rented successfully!");
} else {
System.out.println("Error: Unable to rent car.");
}
}
//Logs the transaction in the database
public void logTransaction(Connection con, Transaction transaction) throws SQLException {
 String sql = "INSERT INTO Transactions (transactionID, carID, renterID, rentalCost, insuranceCost, damageCost) VALUES (?, ?, ?, ?, ?, ?)";
 PreparedStatement pstmt = con.prepareStatement(sql);
 pstmt.setString(1, transaction.getTransactionID());
 pstmt.setString(2, transaction.getCarID());
 pstmt.setString(3, transaction.getRenterID());
 pstmt.setDouble(4, transaction.getRentalCost());
 pstmt.setDouble(5, transaction.getInsuranceCost());
 pstmt.setDouble(6, transaction.getDamageCost());
 pstmt.executeUpdate();
}

//Displays all transactions from the database
public void displayAllTransactions(Connection con) throws SQLException {
 String sql = "SELECT * FROM Transactions";
 Statement stmt = con.createStatement();
 ResultSet rs = stmt.executeQuery(sql);

 System.out.println("All Transactions:");
 while (rs.next()) {
     System.out.println("Transaction ID: " + rs.getString("transactionID"));
     System.out.println("Car ID: " + rs.getString("carID"));
     System.out.println("Renter ID: " + rs.getString("renterID"));
     System.out.println("Rental Cost: $" + rs.getDouble("rentalCost"));
     System.out.println("Insurance Cost: $" + rs.getDouble("insuranceCost"));
     System.out.println("Damage Cost: $" + rs.getDouble("damageCost"));
     System.out.println("------------------------------------------------");
 }
}






//******************************************************????????????????????????????????????????////////////////////////

// Loads cars from a file
public void loadCarsFromFile() {
    File.loadCarsFromFile(cars, "Cars.txt");
}

// Loads renters from a file
public void loadRentersFromFile() {
    File.loadRentersFromFile(renters, "Renters.txt");
}

    // Adds a car to the cars file
    public void addCarToFile(Car car) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cars.txt", true))) {
            writer.write(car.getCarID() + "," + car.getBrand() + "," + car.getModel() + "," + car.getYear() + "," + car.getRentalFee() + "," + car.getPlateNumber() + "," + car.getClass().getSimpleName().toLowerCase());
            writer.newLine();
            System.out.println("Car added to file successfully!");
        } catch (IOException e) {
            System.out.println("Error while adding car to file: " + e.getMessage());
        }
    }

    // Removes a car from the cars file
    public void removeCarFromFile(String carID) {
        java.io.File inputFile = new java.io.File("Cars.txt");
        java.io.File tempFile = new java.io.File("Cars_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            boolean found = false;
            while ((currentLine = reader.readLine()) != null) {
                String[] carData = currentLine.split(",");
                if (carData[0].equals(carID)) {
                    found = true;  // Skip this car (i.e., remove it)
                    continue;
                }
                writer.write(currentLine);
                writer.newLine();
            }
            if (found) {
                inputFile.delete();
                tempFile.renameTo(inputFile);
                System.out.println("Car removed from file successfully!");
            } else {
                System.out.println("Car not found in file.");
            }
        } catch (IOException e) {
            System.out.println("Error while removing car from file: " + e.getMessage());
        }
    }

    // Adds a renter to the renters file
    public void addRenterToFile(Renter renter) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("renters.txt", true))) {
            writer.write(renter.getRenterID() + "," + renter.getName() + "," + renter.getEmail() + "," + renter.getPhoneNumber() + "," + renter.getAddress() + "," + renter.getClass().getSimpleName().toLowerCase());
            writer.newLine();
            System.out.println("Renter added to file successfully!");
        } catch (IOException e) {
            System.out.println("Error while adding renter to file: " + e.getMessage());
        }
    }

    // Removes a renter from the renters file
    public void removeRenterFromFile(String renterID) {
        java.io.File inputFile = new java.io.File("Renters.txt");
        java.io.File tempFile = new java.io.File("renters_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            boolean found = false;
            while ((currentLine = reader.readLine()) != null) {
                String[] renterData = currentLine.split(",");
                if (renterData[0].equals(renterID)) {
                    found = true;  // Skip this renter (i.e., remove it)
                    continue;
                }
                writer.write(currentLine);
                writer.newLine();
            }
            if (found) {
                inputFile.delete();
                tempFile.renameTo(inputFile);
                System.out.println("Renter removed from file successfully!");
            } else {
                System.out.println("Renter not found in file.");
            }
        } catch (IOException e) {
            System.out.println("Error while removing renter from file: " + e.getMessage());
        }
    }


    // Displays transactions from a file
    public void displayTransactionsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Transactions.txt"))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] transactionData = currentLine.split(",");
                System.out.println("Transaction ID: " + transactionData[0]);
                System.out.println("Car ID: " + transactionData[1]);
                System.out.println("Renter ID: " + transactionData[2]);
                System.out.println("Rental Cost: $" + transactionData[3]);
                System.out.println("Insurance Cost: $" + transactionData[4]);
                System.out.println("Damage Cost: $" + transactionData[5]);
                System.out.println("------------------------------------------------");
            }
            System.out.println("Transactions displayed from file successfully!");
        } catch (IOException e) {
            System.out.println("Error while displaying transactions from file: " + e.getMessage());
        }
    }

    public Transaction rentCarFromFile(String carID, String renterID, int distance) {
        double rentalCost = calculateRentalCost(distance);
        double insuranceCost = calculateInsuranceCost(distance);
        double damageCost = calculateDamageCost(distance);
        
        String transactionID = UUID.randomUUID().toString(); // Move this outside the try block
        Transaction transaction = null; // Declare the Transaction object

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Transactions.txt", true))) {
            writer.write(transactionID + "," + carID + "," + renterID + "," + rentalCost + "," + insuranceCost + "," + damageCost);
            writer.newLine();
            System.out.println("Transaction logged in file successfully!");

            // Create the Transaction object after logging
            transaction = new Transaction(transactionID, carID, renterID, rentalCost, insuranceCost, damageCost);
        } catch (IOException e) {
            System.out.println("Error while logging transaction to file: " + e.getMessage());
        }

        // Ensure the Transaction object is returned
        return transaction;
    }

    // Helper methods to calculate the costs
    private double calculateRentalCost(int distance) {
        // Assuming rental cost is $0.5 per km
        return distance * 0.5;
    }

    private double calculateInsuranceCost(int distance) {
        // Assuming insurance cost is $0.1 per km
        return distance * 0.1;
    }

    private double calculateDamageCost(int distance) {
        // Assuming damage cost is $0.05 per km
        return distance * 0.05;
    }



/////////////////////////////////////



    // Load cars from Oracle database
    public void loadCarsFromOracle(Connection con) throws SQLException {
        String sql = "SELECT * FROM Cars";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String carID = rs.getString("carID");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                double rentalFee = rs.getDouble("rentalFee");
                String plateNumber = rs.getString("plateNumber");
                String carType = rs.getString("carType").toLowerCase();
                Car car;

                switch (carType) {
                    case "compactcar":
                        car = new CompactCar(carID, brand, model, year, rentalFee, plateNumber);
                        break;
                    case "suv":
                        car = new SUV(carID, brand, model, year, rentalFee, plateNumber);
                        break;
                    case "luxurycar":
                        car = new LuxuryCar(carID, brand, model, year, rentalFee, plateNumber);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid car type: " + carType);
                }
                addCar(car);  // Add the car to the internal list
            }
        }
    }

    // Load renters from Oracle database
    public void loadRentersFromOracle(Connection con) throws SQLException {
        String sql = "SELECT * FROM Renters";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String renterID = rs.getString("renterID");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                String address = rs.getString("address");
                String renterType = rs.getString("renterType").toLowerCase();

                Renter renter;
                switch (renterType) {
                    case "regularrenter":
                        renter = new RegularRenter(renterID, name, email, phoneNumber, address);
                        break;
                    case "frequentrenter":
                        renter = new FrequentRenter(renterID, name, email, phoneNumber, address);
                        break;
                    case "corporaterenter":
                        renter = new CorporateRenter(renterID, name, email, phoneNumber, address);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid renter type: " + renterType);
                }
                addRenter(renter);  // Add renter to the list
            }
        }
    }

    // Add a car to the Oracle database
    public void addCarToOracle(Connection con, Car car) throws SQLException {
        String sql = "INSERT INTO Cars (carID, brand, model, year, rentalFee, plateNumber, carType) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, car.getCarID());
            stmt.setString(2, car.getBrand());
            stmt.setString(3, car.getModel());
            stmt.setInt(4, car.getYear());
            stmt.setDouble(5, car.getRentalFee());
            stmt.setString(6, car.getPlateNumber());
            stmt.setString(7, car.getClass().getSimpleName().toLowerCase());
            stmt.executeUpdate();
            System.out.println("Car added to Oracle database successfully!");
        }
    }

    // Remove a car from the Oracle database
    public void removeCarFromOracle(Connection con, String carID) throws SQLException {
        String sql = "DELETE FROM Cars WHERE carID = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, carID);
            stmt.executeUpdate();
            System.out.println("Car removed from Oracle database successfully!");
        }
    }

    // Add a renter to the Oracle database
    public void addRenterToOracle(Connection con, Renter renter) throws SQLException {
        String sql = "INSERT INTO Renters (renterID, name, email, phoneNumber, address, renterType) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, renter.getRenterID());
            stmt.setString(2, renter.getName());
            stmt.setString(3, renter.getEmail());
            stmt.setString(4, renter.getPhoneNumber());
            stmt.setString(5, renter.getAddress());
            stmt.setString(6, renter.getClass().getSimpleName().toLowerCase());
            stmt.executeUpdate();
            System.out.println("Renter added to Oracle database successfully!");
        }
    }

    // Remove a renter from the Oracle database
    public void removeRenterFromOracle(Connection con, String renterID) throws SQLException {
        String sql = "DELETE FROM Renters WHERE renterID = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, renterID);
            stmt.executeUpdate();
            System.out.println("Renter removed from Oracle database successfully!");
        }
    }

    // Rent a car using Oracle database
    public Transaction rentCarOracle(Connection con, String carID, String renterID, int distance) throws SQLException {
        // Check car availability
        String availabilityCheckQuery = "SELECT * FROM Cars WHERE carID = ? AND isAvailable = 1";
        try (PreparedStatement checkStmt = con.prepareStatement(availabilityCheckQuery)) {
            checkStmt.setString(1, carID);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Car is not available for rent.");
                return null;
            }
        }

        // Calculate rental cost and perform the transaction
        double rentalCost = calculateRentalCost(con, carID, distance);
        String transactionID = UUID.randomUUID().toString();

        String transactionInsertQuery = "INSERT INTO Transactions (transactionID, carID, renterID, rentalCost, insuranceCost, damageCost) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement transactionStmt = con.prepareStatement(transactionInsertQuery)) {
            transactionStmt.setString(1, transactionID);
            transactionStmt.setString(2, carID);
            transactionStmt.setString(3, renterID);
            transactionStmt.setDouble(4, rentalCost);
            transactionStmt.setDouble(5, 50.0); // Insurance cost
            transactionStmt.setDouble(6, 0.0); // Damage cost
            transactionStmt.executeUpdate();
        }

        // Update car availability
        String updateCarAvailabilityQuery = "UPDATE Cars SET isAvailable = 0 WHERE carID = ?";
        try (PreparedStatement updateStmt = con.prepareStatement(updateCarAvailabilityQuery)) {
            updateStmt.setString(1, carID);
            updateStmt.executeUpdate();
        }

        System.out.println("Car rented successfully in Oracle! Transaction recorded.");
        return new Transaction(transactionID, carID, renterID, rentalCost, 50.0, 0.0);
    }

    // Log a transaction in Oracle
    public void logTransactionToOracle(Connection con, Transaction transaction) throws SQLException {
        String sql = "INSERT INTO Transactions (transactionID, carID, renterID, rentalCost, insuranceCost, damageCost) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getTransactionID());
            pstmt.setString(2, transaction.getCarID());
            pstmt.setString(3, transaction.getRenterID());
            pstmt.setDouble(4, transaction.getRentalCost());
            pstmt.setDouble(5, transaction.getInsuranceCost());
            pstmt.setDouble(6, transaction.getDamageCost());
            pstmt.executeUpdate();
            System.out.println("Transaction logged in Oracle successfully!");
        }
    }

    // Display all transactions from Oracle database
    public void displayAllTransactionsOracle(Connection con) throws SQLException {
        String sql = "SELECT * FROM Transactions";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Transaction ID: " + rs.getString("transactionID"));
                System.out.println("Car ID: " + rs.getString("carID"));
                System.out.println("Renter ID: " + rs.getString("renterID"));
                System.out.println("Rental Cost: $" + rs.getDouble("rentalCost"));
                System.out.println("Insurance Cost: $" + rs.getDouble("insuranceCost"));
                System.out.println("Damage Cost: $" + rs.getDouble("damageCost"));
                System.out.println("---------------------------------------");
            }
        }
    }

   
}

