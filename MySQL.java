	package CarRenrt;
	import java.sql.*;
	import java.util.ArrayList;
	import java.util.List;
	public class MySQL {
	    private Connection connection;
	
	    public MySQL(String dbURL, String user, String password) throws SQLException {
	        this.connection = DriverManager.getConnection(dbURL, user, password);
	    }
	
	    public Connection getConnection() {
	        return connection;
	    }
	}
	
	
	
	class CarCRUD {
	    private MySQL dbConnection;
	
	    public CarCRUD(MySQL dbConnection) {
	        this.dbConnection = dbConnection;
	    }
	
	    public void addCar(Car car) throws SQLException {
	        String sql = "INSERT INTO Cars (carID, brand, model, year, rentalFee, plateNumber) VALUES (?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
	            stmt.setString(1, car.getCarID());
	            stmt.setString(2, car.getBrand());
	            stmt.setString(3, car.getModel());
	            stmt.setInt(4, car.getYear());
	            stmt.setDouble(5, car.getRentalFee());
	            stmt.setString(6, car.getPlateNumber());
	            stmt.executeUpdate();
	        }
	    }
	
	    public void removeCar(String carID) throws SQLException {
	        String sql = "DELETE FROM Cars WHERE carID = ?";
	        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
	            stmt.setString(1, carID);
	            stmt.executeUpdate();
	        }
	    }
	
	    public void updateCar(Car car) throws SQLException {
	        String sql = "UPDATE Cars SET brand = ?, model = ?, year = ?, rentalFee = ?, plateNumber = ? WHERE carID = ?";
	        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
	            stmt.setString(1, car.getBrand());
	            stmt.setString(2, car.getModel());
	            stmt.setInt(3, car.getYear());
	            stmt.setDouble(4, car.getRentalFee());
	            stmt.setString(5, car.getPlateNumber());
	            stmt.setString(6, car.getCarID());
	            stmt.executeUpdate();
	        }
	    }
	
	    public List<Car> displayCars() throws SQLException {
	        List<Car> cars = new ArrayList<>();
	        String sql = "SELECT * FROM Cars";
	        try (Statement stmt = dbConnection.getConnection().createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            while (rs.next()) {
	                String carID = rs.getString("carID");
	                String brand = rs.getString("brand");
	                String model = rs.getString("model");
	                int year = rs.getInt("year");
	                double rentalFee = rs.getDouble("rentalFee");
	                String plateNumber = rs.getString("plateNumber");
	                cars.add(new CompactCar(carID, brand, model, year, rentalFee, plateNumber)); // Adjust the type accordingly
	            }
	        }
	        return cars;
	    }
	}
	
	class RenterCRUD {
	    private MySQL dbConnection;
	
	    public RenterCRUD(MySQL dbConnection) {
	        this.dbConnection = dbConnection;
	    }
	
	    public void addRenter(Renter renter) throws SQLException {
	        String sql = "INSERT INTO Renters (renterID, name, email, phoneNumber, address) VALUES (?, ?, ?, ?, ?)";
	        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
	            stmt.setString(1, renter.getRenterID());
	            stmt.setString(2, renter.getName());
	            stmt.setString(3, renter.getEmail());
	            stmt.setString(4, renter.getPhoneNumber());
	            stmt.setString(5, renter.getAddress());
	            stmt.executeUpdate();
	        }
	    }
	
	    public void removeRenter(String renterID) throws SQLException {
	        String sql = "DELETE FROM Renters WHERE renterID = ?";
	        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
	            stmt.setString(1, renterID);
	            stmt.executeUpdate();
	        }
	    }
	
	    public void updateRenter(Renter renter) throws SQLException {
	        String sql = "UPDATE Renters SET name = ?, email = ?, phoneNumber = ?, address = ? WHERE renterID = ?";
	        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
	            stmt.setString(1, renter.getName());
	            stmt.setString(2, renter.getEmail());
	            stmt.setString(3, renter.getPhoneNumber());
	            stmt.setString(4, renter.getAddress());
	            stmt.setString(5, renter.getRenterID());
	            stmt.executeUpdate();
	        }
	    }
	
	    public List<Renter> displayRenters() throws SQLException {
	        List<Renter> renters = new ArrayList<>();
	        String sql = "SELECT * FROM Renters";
	        try (Statement stmt = dbConnection.getConnection().createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            while (rs.next()) {
	                String renterID = rs.getString("renterID");
	                String name = rs.getString("name");
	                String email = rs.getString("email");
	                String phoneNumber = rs.getString("phoneNumber");
	                String address = rs.getString("address");
	                renters.add(new RegularRenter(renterID, name, email, phoneNumber, address)); // Adjust the type accordingly
	            }
	        }
	        return renters;
	    }
	}
	
	
	
	class TransactionCRUD {
	    private MySQL dbConnection;	
	
	    public TransactionCRUD(MySQL dbConnection) {
	        this.dbConnection = dbConnection;
	    }
	
	    public void logTransaction(Transaction transaction) throws SQLException {
	        String sql = "INSERT INTO Transactions (transactionID, carID, renterID, rentalCost, insuranceCost, damageCost) VALUES (?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {
	            stmt.setString(1, transaction.getTransactionID());
	            stmt.setString(2, transaction.getCarID());
	            stmt.setString(3, transaction.getRenterID());
	            stmt.setDouble(4, transaction.getRentalCost());
	            stmt.setDouble(5, transaction.getInsuranceCost());
	            stmt.setDouble(6, transaction.getDamageCost());
	            stmt.executeUpdate();
	        }
	    }
	
	    public List<Transaction> displayTransactions() throws SQLException {
	        List<Transaction> transactions = new ArrayList<>();
	        String sql = "SELECT * FROM Transactions";
	        try (Statement stmt = dbConnection.getConnection().createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            while (rs.next()) {
	                String transactionID = rs.getString("transactionID");
	                String carID = rs.getString("carID");
	                String renterID = rs.getString("renterID");
	                double rentalCost = rs.getDouble("rentalCost");
	                double insuranceCost = rs.getDouble("insuranceCost");
	                double damageCost = rs.getDouble("damageCost");
	                transactions.add(new Transaction(transactionID, carID, renterID, rentalCost, insuranceCost, damageCost)); // Adjust constructor accordingly
	            }
	        }
	        return transactions;
	    }
	}
	
