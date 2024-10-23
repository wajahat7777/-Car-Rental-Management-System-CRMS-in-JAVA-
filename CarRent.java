package CarRenrt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CarRent {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        CarRentalManagementSystem crms = new CarRentalManagementSystem();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        int storageChoice = 0;
        boolean validChoice = false;

        // Choose storage method (File, MySQL, or Oracle) with validation
        while (!validChoice) {
            System.out.println("Select storage method:");
            System.out.println("1. File-based storage");
            System.out.println("2. MySQL-based storage");
            System.out.println("3. Oracle-based storage");
            System.out.print("Enter your choice: ");
            try {
                storageChoice = scanner.nextInt();
                if (storageChoice >= 1 && storageChoice <= 3) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        scanner.nextLine(); // Consume newline

        boolean useSQL = false; // Default to file storage
        Connection con = null; // Initialize connection as null for file-based storage
        String dbType = ""; // Track the type of database (MySQL or Oracle)

        if (storageChoice == 2) {
            // Establish MySQL database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarRentt", "root", "manizahra");
            useSQL = true;
            dbType = "MySQL";
        } else if (storageChoice == 3) {
            // Establish Oracle database connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orclpdb", "system", "system");
            useSQL = true;
            dbType = "Oracle";
        } else {
            // Load initial data from files
            crms.loadCarsFromFile();
            crms.loadRentersFromFile();
        }

        if (useSQL) {
            // Load initial data from the selected database
            if (dbType.equals("MySQL")) {
                crms.loadCarsFromDatabase(con);
                crms.loadRentersFromDatabase(con);
            } else if (dbType.equals("Oracle")) {
                crms.loadCarsFromOracle(con);
                crms.loadRentersFromOracle(con);
            }
        }

        while (!exit) {
            System.out.println("\n\n//////////////************Car Rental Management System************/////////////");
            System.out.println("                               1. Display Available Cars");
            System.out.println("                               2. Display Renters");
            System.out.println("                               3. Rent a Car");
            System.out.println("                               4. Add a Car");
            System.out.println("                               5. Add a Renter");
            System.out.println("                               6. Remove a Car");
            System.out.println("                               7. Remove a Renter");
            System.out.println("                               8. Display All Transactions");
            System.out.println("                               9. Exit");
            System.out.print("                                Choose an option: \n\n");

            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Consume invalid input
                continue; // Skip to next iteration
            }
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    crms.displayAvailableCars();
                    break;
                case 2:
                    crms.displayRenters();
                    break;
                case 3:
                    System.out.print("Enter Car ID to Rent: ");
                    String carID = scanner.nextLine();
                    System.out.print("Enter Renter ID: ");
                    String renterID = scanner.nextLine();

                    int distance = 0;
                    boolean validDistance = false;
                    while (!validDistance) {
                        System.out.print("Enter Distance to Travel: ");
                        try {
                            distance = scanner.nextInt();
                            if (distance > 0) {
                                validDistance = true;
                            } else {
                                System.out.println("Please enter a positive integer for distance.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid integer.");
                            scanner.nextLine(); // Clear invalid input
                        }
                    }

                    // Rent car based on storage choice
                    Transaction transaction = (useSQL)
                            ? (dbType.equals("Oracle")
                            ? crms.rentCarOracle(con, carID, renterID, distance)
                            : crms.rentCar(con, carID, renterID, distance))
                            : crms.rentCarFromFile(carID, renterID, distance);

                    if (transaction != null) {
                        System.out.println("Transaction Details:");
                        System.out.println("Transaction ID: " + transaction.getTransactionID());
                        System.out.println("Renter ID: " + transaction.getRenterID());
                        System.out.println("Car ID: " + transaction.getCarID());
                        System.out.println("Rental Cost: $" + transaction.getRentalCost());
                        System.out.println("Insurance Cost: $" + transaction.getInsuranceCost());
                        System.out.println("Damage Cost: $" + transaction.getDamageCost());
                    } else {
                        System.out.println("Rental failed. Car may not be available.");
                    }
                    break;

                case 4:
                    // Add Car with validation
                    int carType = 0;
                    boolean validCarType = false;
                    while (!validCarType) {
                        System.out.print("Enter Car Type (1. Compact, 2. SUV, 3. Luxury): ");
                        try {
                            carType = scanner.nextInt();
                            if (carType >= 1 && carType <= 3) {
                                validCarType = true;
                            } else {
                                System.out.println("Invalid input. Please enter 1, 2, or 3.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                            scanner.nextLine(); // Clear invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    // Gather car details
                    System.out.print("Enter Car ID: ");
                    String newCarID = scanner.nextLine();
                    System.out.print("Enter Brand: ");
                    String brand = scanner.nextLine();
                    System.out.print("Enter Model: ");
                    String model = scanner.nextLine();

                    int year = 0;
                    boolean validYear = false;
                    while (!validYear) {
                        System.out.print("Enter Year: ");
                        try {
                            year = scanner.nextInt();
                            if (year > 1900 && year <= 2024) {
                                validYear = true;
                            } else {
                                System.out.println("Invalid year. Please enter a realistic value.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid year.");
                            scanner.nextLine(); // Clear invalid input
                        }
                    }

                    double rentalFee = 0.0;
                    boolean validFee = false;
                    while (!validFee) {
                        System.out.print("Enter Rental Fee: ");
                        try {
                            rentalFee = scanner.nextDouble();
                            if (rentalFee > 0) {
                                validFee = true;
                            } else {
                                System.out.println("Invalid fee. Please enter a positive value.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                            scanner.nextLine(); // Clear invalid input
                        }
                    }
                    scanner.nextLine();  // Consume newline

                    System.out.print("Enter Plate Number: ");
                    String plateNumber = scanner.nextLine();

                    // Create and add car based on car type
                    Car newCar;
                    if (carType == 1) {
                        newCar = new CompactCar(newCarID, brand, model, year, rentalFee, plateNumber);
                    } else if (carType == 2) {
                        newCar = new SUV(newCarID, brand, model, year, rentalFee, plateNumber);
                    } else {
                        newCar = new LuxuryCar(newCarID, brand, model, year, rentalFee, plateNumber);
                    }

                    // Add car based on storage choice
                    if (useSQL) {
                        if (dbType.equals("Oracle")) {
                            crms.addCarToOracle(con, newCar);
                        } else {
                            crms.addCarToDatabase(con, newCar);
                        }
                    } else {
                        crms.addCarToFile(newCar);
                    }

                    System.out.println("Car added successfully!");
                    break;

                case 5:
                    // Add Renter with validation
                    int renterType = 0;
                    boolean validRenterType = false;
                    while (!validRenterType) {
                        System.out.print("Enter Renter Type (1. Regular, 2. Frequent, 3. Corporate): ");
                        try {
                            renterType = scanner.nextInt();
                            if (renterType >= 1 && renterType <= 3) {
                                validRenterType = true;
                            } else {
                                System.out.println("Invalid input. Please enter 1, 2, or 3.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                            scanner.nextLine(); // Clear invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    // Gather renter details
                    System.out.print("Enter Renter ID: ");
                    String newRenterID = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String phoneNumber = scanner.nextLine();
                    System.out.print("Enter Address: ");
                    String address = scanner.nextLine();

                    Renter newRenter;
                    if (renterType == 1) {
                        newRenter = new RegularRenter(newRenterID, name, email, phoneNumber, address);
                    } else if (renterType == 2) {
                        newRenter = new FrequentRenter(newRenterID, name, email, phoneNumber, address);
                    } else {
                        newRenter = new CorporateRenter(newRenterID, name, email, phoneNumber, address);
                    }

                    // Add renter based on storage choice
                    if (useSQL) {
                        if (dbType.equals("Oracle")) {
                            crms.addRenterToOracle(con, newRenter);
                        } else {
                            crms.addRenterToDatabase(con, newRenter);
                        }
                    } else {
                        crms.addRenterToFile(newRenter);
                    }

                    System.out.println("Renter added successfully!");
                    break;

                case 6:
                    System.out.print("Enter Car ID to Remove: ");
                    String removeCarID = scanner.nextLine();

                    // Remove car based on storage choice
                    if (useSQL) {
                        if (dbType.equals("Oracle")) {
                            crms.removeCarFromOracle(con, removeCarID);
                        } else {
                            crms.removeCarFromDatabase(con, removeCarID);
                        }
                    } else {
                        crms.removeCarFromFile(removeCarID);
                    }

                    System.out.println("Car removed successfully!");
                    break;

                case 7:
                    System.out.print("Enter Renter ID to Remove: ");
                    String removeRenterID = scanner.nextLine();

                    // Remove renter based on storage choice
                    if (useSQL) {
                        if (dbType.equals("Oracle")) {
                            crms.removeRenterFromOracle(con, removeRenterID);
                        } else {
                            crms.removeRenterFromDatabase(con, removeRenterID);
                        }
                    } else {
                        crms.removeRenterFromFile(removeRenterID);
                    }

                    System.out.println("Renter removed successfully!");
                    break;

                case 8:
                    if (useSQL) {
                        if (dbType.equals("Oracle")) {
                            crms.displayAllTransactionsOracle(con);
                        } else {
                            crms.displayAllTransactions(con);
                        }
                    } else {
                        crms.displayTransactionsFromFile();
                    }
                    break;

                case 9:
                    exit = true;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        // Close the database connection if SQL was used
        if (useSQL && con != null) {
            con.close();
        }
        scanner.close();
    }
}
