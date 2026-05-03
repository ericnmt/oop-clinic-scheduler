# oop-clinic-scheduler
Eric Johnson, Manasi Movva, Abimael Lozano
## Description 

## Architectural Design
We settled for a Maven-based Spring Boot application to ensure a clear separation of concerns and layered architecture. This design choice focuses on maintainability and testability by isolating logic into different components, allowing for the strict enforcement of business logic.

### Layered Architecture
The system follows a clean separation of concerns, organized into layers to ensure compatibility between components and preservation of application logic. The components of our system is split into the following schema: Model Layer, Service Layer, Data Access Layer (DAO), and Presentation Layer (via the CLI).
* **Model Layer**: Consists of POJOs such as ```Patient```, ```Provider```, and ```Appointment``` classes. These classes represent the core data entities of the system and are mapped directly to database tables via the Data Access Layer. This model was built off of the initial [**ER Schema**](https://github.com/ericnmt/oop-clinic-scheduler/blob/8ffdeaa9ecf08fae8f112b247505bd33bd3a0f9b/docs/ER_Diagram_Schema.pdf).
* **Service Layer**: Consists of the ```AppointmentManager``` class, which orchestrates operations and ensures the strict application of business logic. Every CRUD opertion is passed through this layer for absolute business rule enforcement.
* **Data Access Layer**: We settled with the Data Access Object (DAO) pattern to define and map CRUD operations between POJOs and the database with business logic constraints. This layer is split into two distinct components: Interfaces and Implementation. Interfaces include ```PatientDao```, ```ProviderDao```, and ```AppointmentDao``` to define CRUD operations. These interfaces and their operations are all implemented, containing data handling and specific SQL queries needed to interact with the database.
* **Presentation Layer**: For the primary user interface (UI), we utilized Spring Boot's ```SchedulerApplication``` class. This is the main component that handles input parsing and displays operation outputs, all while invoking the Service Layer for business rule enforcement.

### Data Management & Persistence
Data is managed through a model that utilizes a file-based relational database.
* **Relational Database Integration**: We settled with using the file-based SQLite framework for the relational database. This choice provides persistence across application restarts without the overhead of a dedicated database server.
* **Schema Enforcement**: The foundational ER schema was translated into the primary database structure, defined in ```schema.sql```, which establishes the three primary tables consisting of each entity's Primary and Foreign Keys. We utilize DDL to create these structures:
```SQL
CREATE TABLE IF NOT EXISTS Patient (
    PatientID INTEGER PRIMARY KEY,
    Name TEXT NOT NULL,
    DateOfBirth TEXT NOT NULL,
    ContactInfo TEXT NOT NULL
);

-- Provider Table
CREATE TABLE IF NOT EXISTS Provider (
    ProviderID INTEGER PRIMARY KEY,
    Name TEXT NOT NULL,
    Specialty TEXT NOT NULL,
    Location TEXT NOT NULL
);

-- Appointment Table
CREATE TABLE IF NOT EXISTS Appointment (
    AppointmentID INTEGER PRIMARY KEY AUTOINCREMENT,
    StartTime TEXT NOT NULL,
    EndTime TEXT NOT NULL,
    Status TEXT NOT NULL,
    Reason TEXT NOT NULL,
    PatientID INTEGER NOT NULL,
    ProviderID INTEGER NOT NULL,
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID),
    FOREIGN KEY (ProviderID) REFERENCES Provider(ProviderID)
);
```
* **Spring Boot JDBC Template**: The system uses Spring Boot's ```JdbcTemplate``` to bridge interaction between Java objects and SQL data management. This allows for the abstraction of low-level JDBC API, simplifying database interactions while keeping SQL logic hidden behind DAO interfaces.

### Centralized Business Logic
The architecture ensures integrity and robustness via validation through the Service Layer.
* **Exception Validation**: Java exceptions are defined within the Service Layer to reject invalid operations.
* **Conflict Prevention**: The system design includes specific logic to prevent the overlap of appointments for providers. The Service Layer queries existing records and performs necessary checks before confirming new or rescheduled appointments.
* **Status Transition Constraints**: The system enforces business logic for appointment statuses via enumerations. A "CANCELLED" appointment is architecturally unable to transition to "COMPLETED"

## Database Setup Instructions

## How To Run

## Example Usages & Cases
