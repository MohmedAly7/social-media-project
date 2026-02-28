# Social Media Application

A desktop-based social media application built using Java, JavaFX, and MySQL. It implements core object-oriented programming concepts, custom data structures, and database persistence. 

## Features Included

This project implements the following core features:
1. **User Authentication:** Registration and secure login using hashed passwords.
2. **Profile Management:** View and edit user profiles, bios, and profile pictures.
3. **Posting Updates:** Create and store text or image posts via custom data structure management.
4. **News Feed:** View a unified feed of friends' posts using database pagination.
5. **Likes & Comments:** Interact with posts by liking them or leaving comments.
6. **Friend System:** Search for users and establish friend connections to populate your feed.

## Prerequisites and Requirements

To run or build this application locally, your system must have the following installed:

1. **Java Development Kit (JDK) 21:** The project is strictly compiled for Java 21. Ensure your `JAVA_HOME` variable is correctly pointing to a JDK 21 installation (e.g., OpenJDK 21 or Oracle JDK 21).
2. **Maven:** Required to build the project, run tests, and manage dependencies (JavaFX 21, JUnit 5, and MySQL Connector).
3. **MySQL Server (8.0+):** A local or remote MySQL database is required to store application data.

## Database Setup

Before running the application, you must initialize the database:
1. Ensure your MySQL server is running.
2. Create a database named `social_media_app` (or your preferred name).
3. Execute the SQL script located at `src/main/resources/db/schema.sql` to generate the necessary tables and relationships.
4. Update the database connection credentials in your environment or configuration setup to match your local MySQL instance.

## How to Build and Run

### Running via Maven (Recommended)
You can compile and launch the application directly from your terminal using Maven:
```bash
mvn clean compile javafx:run
```

### Building the Executable JAR
To package the application and all its dependencies into an executable "Fat JAR":
```bash
mvn clean package
```
This will generate `social-media-app-1.0-SNAPSHOT.jar` inside the `target/` directory.

### Running the JAR File
Since the application uses JavaFX 21, assure you run the JAR using Java 21 to avoid `QuantumRenderer` incompatibility errors on Windows:
```bash
java -jar target/social-media-app-1.0-SNAPSHOT.jar
```

## Testing
The application features comprehensive unit-testing using **JUnit 5**. To execute the test suite, run:
```bash
mvn test
```
