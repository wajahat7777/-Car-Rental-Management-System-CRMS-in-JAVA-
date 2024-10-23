# -Car-Rental-Management-System-CRMS-in-JAVA-
 Car Rental Management System (CRMS) by integrating it with three different storage systems: File-based storage, Oracle database, and SQL database. 
 (CRMS)Assignment:
Assignment Requirements:
1. Multi-Storage Data Persistence Integration:
 Your CRMS must support File-based storage, Oracle, and SQL databases for data storage and
retrieval.
 The system should display a menu that allows users to choose one of the storage methods (File,
Oracle, or SQL) at the time of execution. The selected method should then be used for all
subsequent operations (e.g., adding cars, retrieving data).
 Ensure that changes made to the data are reflected correctly based on the storage method chosen
during runtime.
2. Fix Issues from Assignment 1:
Several common issues were identified in Assignment 1 that must be addressed in this assignment:
 Input Validation:
o Ensure that no two cars or renters are added with the same Car ID or Renter ID.
o Validate all inputs to prevent incorrect or malformed data entry (e.g., invalid data types,
missing fields).
o Properly structure and implement additional attributes related to insurance for cars,
ensuring they follow the correct data structure.
o Correct the use of static and final variables where applicable, ensuring they are used
appropriately.
o Ensure proper understanding and implementation of Object-Oriented Programming
(OOP) principles, including encapsulation, abstraction, inheritance, and polymorphism.

 Deletion Logic:
o Prevent the deletion of a renter if they have an active rental. Renters should only be
deletable after all rented cars have been returned.
o Ensure that deleting a car is only possible if the car is not currently rented.

3. Data Management Requirements:
 File-based storage:
o Implement efficient techniques to store and retrieve data in a structured manner.

o The system should use a file format such as .txt or another format of your choice, but the
data must be organized in a way that is easily retrievable and readable in file.

 Oracle Database:
o Establish a connection to an Oracle database and perform CRUD (Create, Read,
Update, Delete) operations for Cars, Renters, and Transactions.

 SQL Database:
o Establish a connection to a SQL database and perform the same CRUD operations as
with the Oracle database for Cars, Renters, and Transactions.

4. CRUD Operations:
 Cars:
o Implement functionality to add, remove, update, and display car details. Ensure that all
operations are changed across the File, Oracle, and SQL databases based on the chosen
storage method.

 Renters:
o Implement functionality to add, remove, update, and display renter details. Ensure that all
operations are changed across all three storage options (File, Oracle, and SQL).

 Transactions:
o Log and display rental transactions, including rental cost, insurance details, and damage
costs. These transaction records should be maintained across all storage methods.

5. Error Handling:
 Implement proper error handling for issues such as database connectivity failures or invalid
operations (e.g., trying to delete a rented car or renter with active rentals).
 Ensure smooth handling of CRUD operations across all storage options, including error handling
during read/write operations to the File, Oracle, and SQL databases.
Assignment Deliverables:
 Java Code: A fully implemented Java program that supports data persistence across File, Oracle,
and SQL databases, adhering to OOP principles and following the Google Java Style Guide.
 Database Schema: Provide the database schema for both Oracle and SQL databases, including
tables, fields, and relationships for Cars, Renters, and Transactions.
