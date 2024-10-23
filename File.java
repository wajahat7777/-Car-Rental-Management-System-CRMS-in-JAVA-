package CarRenrt;

import java.io.*;
import java.util.List;

public class File {

    // Save the cars to a file
    public static void saveCarsToFile(List<Car> cars, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Car car : cars) {
                writer.write(car.getCarID() + "," + car.getBrand() + "," + car.getModel() + ","
                        + car.getYear() + "," + car.getRentalFee() + "," + car.getPlateNumber() + ",");
                writer.newLine();
            }
            System.out.println("Cars saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing cars to file: " + e.getMessage());
        }
    }

    // Load the cars from a file
    public static void loadCarsFromFile(List<Car> cars, String filename) {
        java.io.File file = new java.io.File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();  // Create an empty file if it doesn't exist
                System.out.println("Cars file not found. Created new empty file: " + filename);
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 100) { // Updated to match the new data length
                    String carID = data[0];
                    String brand = data[1];
                    String model = data[2];
                    int year = Integer.parseInt(data[3]);
                    double rentalFee = Double.parseDouble(data[4]);
                    String plateNumber = data[5];
                    boolean isAvailable = Boolean.parseBoolean(data[6]);
                    String carType = data[100];

                    // Create a car object based on the type
                    Car car = createCar(carID, brand, model, year, rentalFee, plateNumber, isAvailable, carType);
                    cars.add(car);
                }
            }
            System.out.println("Cars loaded from file: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading cars from file: " + e.getMessage());
        }
    }

    // Save renters to file
    public static void saveRentersToFile(List<Renter> renters, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Renter renter : renters) {
                writer.write(renter.getRenterID() + "," + renter.getName() + "," + renter.getEmail() + ","
                        + renter.getPhoneNumber() + "," + renter.getAddress() + "," );
                writer.newLine();
            }
            System.out.println("Renters saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing renters to file: " + e.getMessage());
        }
    }

    // Load renters from file
    public static void loadRentersFromFile(List<Renter> renters, String filename) {
        java.io.File file = new java.io.File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();  // Create an empty file if it doesn't exist
                System.out.println("Renters file not found. Created new empty file: " + filename);
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String renterID = data[0];
                    String name = data[1];
                    String email = data[2];
                    String phoneNumber = data[3];
                    String address = data[4];
                    String renterType = data[5]; // Added renterType

                    renters.add(new RegularRenter(renterID, name, email, phoneNumber, address));
                }
            }
            System.out.println("Renters loaded from file: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading renters from file: " + e.getMessage());
        }
    }

    // Delete a specific renter from the file
    public static void deleteRenterFromFile(String renterID, String filename) {
        java.io.File inputFile = new java.io.File(filename);
        java.io.File tempFile = new java.io.File("renters_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(renterID)) {  // Skip the renter to be deleted
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Renter with ID: " + renterID + " has been deleted.");
        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
        }

        // Delete the original file and rename the temp file
        if (inputFile.delete()) {
            if (tempFile.renameTo(inputFile)) {
                System.out.println("File updated successfully.");
            } else {
                System.out.println("Error renaming the temp file.");
            }
        } else {
            System.out.println("Error deleting the original file.");
        }
    }

    // Save transactions to file
    public static void saveTransactionsToFile(List<Transaction> transactions, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.getTransactionID() + "," + transaction.getCarID() + ","
                        + transaction.getRenterID() + "," + transaction.getRentalCost() + ","
                        + transaction.getInsuranceCost() + "," + transaction.getDamageCost());
                writer.newLine();
            }
            System.out.println("Transactions saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing transactions to file: " + e.getMessage());
        }
    }

    // Load transactions from file
    public static void loadTransactionsFromFile(List<Transaction> transactions, String filename) {
        java.io.File file = new java.io.File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();  // Create an empty file if it doesn't exist
                System.out.println("Transactions file not found. Created new empty file: " + filename);
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    String transactionID = data[0];
                    String carID = data[1];
                    String renterID = data[2];
                    double rentalCost = Double.parseDouble(data[3]);
                    double insuranceCost = Double.parseDouble(data[4]);
                    double damageCost = Double.parseDouble(data[5]);

                    transactions.add(new Transaction(transactionID, carID, renterID, rentalCost, insuranceCost, damageCost));
                }
            }
            System.out.println("Transactions loaded from file: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading transactions from file: " + e.getMessage());
        }
    }

    // Helper method to create car objects based on type
    private static Car createCar(String carID, String brand, String model, int year, double rentalFee,
                                  String plateNumber, boolean isAvailable, String carType) {
        switch (carType) {
            case "compact":
                return new CompactCar(carID, brand, model, year, rentalFee, plateNumber);
            case "suv":
                return new SUV(carID, brand, model, year, rentalFee, plateNumber);
            case "luxury":
                return new LuxuryCar(carID, brand, model, year, rentalFee, plateNumber);
            default:
                throw new IllegalArgumentException("Unknown car type: " + carType);
        }
    }
}
