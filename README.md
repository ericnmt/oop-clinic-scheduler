# oop-clinic-scheduler
Eric Johnson, Manasi Movva, Abimael Lozano
## Description
The Clinic Scheduler is a Spring Boot application that manages patients, providers, and appointments through a structured and reliable scheduling system. It supports creating, updating, rescheduling, and canceling appointments while enforcing key business rules such as preventing time conflicts and maintaining valid status transitions.

The project was built to demonstrate object-oriented design and real-world backend development by moving from in-memory data handling to a persistent, database-driven system. It uses a layered architecture with a service layer for business logic and DAO components for database interaction via Spring Boot’s JdbcTemplate. Data is stored in a SQLite database, ensuring persistence across application runs.

## Architectural Design
We settled for a Maven-based Spring Boot application to ensure a clear separation of concerns and layered architecture. This design choice focuses on maintainability and testability by isolating logic into different components, allowing for the strict enforcement of business logic. A key contribution to this design was the refactoring of the core architecture to fully adopt a layered approach + DAO pattern, replacing earlier in-memory implementations with database-backed operations.

### Layered Architecture
The system follows a clean separation of concerns, organized into layers to ensure compatibility between components and preservation of application logic. The components of our system is split into the following schema: Model Layer, Service Layer, Data Access Layer (DAO), and Presentation Layer (via the CLI).
* **Model Layer**: Consists of POJOs such as ```Patient```, ```Provider```, and ```Appointment``` classes. These classes represent the core data entities of the system and are mapped directly to database tables via the Data Access Layer. This model was built off of the initial [**ER Schema**](https://github.com/ericnmt/oop-clinic-scheduler/blob/main/docs/ER_Diagram_Schema.pdf).
* **Service Layer**: Consists of the ```AppointmentManager``` class, which orchestrates operations and ensures the strict application of business logic. Every CRUD opertion is passed through this layer for absolute business rule enforcement.
* **Data Access Layer**: We settled with the Data Access Object (DAO) pattern to define and map CRUD operations between POJOs and the database with business logic constraints. This layer is split into two distinct components: Interfaces and Implementation. Interfaces include ```PatientDao```, ```ProviderDao```, and ```AppointmentDao``` to define CRUD operations. These interfaces and their operations are all implemented, containing data handling and specific SQL queries needed to interact with the database.
* **Presentation Layer**: For the primary user interface (UI), we utilized Spring Boot's ```SchedulerApplication``` class. This is the main component that handles input parsing and displays operation outputs, all while invoking the Service Layer for business rule enforcement.

### Data Management & Persistence
Data is managed through a model that utilizes a file-based relational database.
* **Relational Database Integration**: We settled with using the file-based SQLite framework for the relational database. This choice provides persistence across application restarts without the overhead of a dedicated database server.
* **Schema Enforcement**: The foundational ER schema was translated into the primary database structure, defined in ```schema.sql```, which establishes the three primary tables consisting of each entity's Primary and Foreign Keys. We utilize DDL to create these structures:
```SQL
--- schema.sql
--- Patient Table
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

### Update & Deletion Constraints
* **Update Operations**: Implemented update functionality for Patient and Provider entities with proper validation checks before committing changes.
* **Safe Deletion Logic**: Enforced constraints preventing deletion of patients or providers with active appointments.
* **Referential Integrity**: Integrated database checks to avoid orphaned records and maintain consistent relationships between entities.

### Database Setup Instructions
* **DAO-Driven Operations**: Replaced all in-memory logic with DAO-based database queries to ensure persistence and scalability.
* **Scheduling Logic Refactor**: Modified scheduleAppointment and rescheduleAppointment to use DAO methods for inserting, updating, and retrieving appointment data.
* **Consistent Data Flow**: Ensured all interactions between application logic and the database are routed through the Data Access Layer.

### Testing & Validation
* **Service Layer Testing**: Verified correct execution of scheduling, rescheduling, and update operations through the service layer.
* **Edge Case Handling**: Tested invalid time ranges, conflicting appointments, and invalid IDs to ensure robust validation.
* **Status Updates**: Confirmed correct handling of appointment state changes such as CANCELLED.

## How To Run
With the use of the Spring Boot framework, setup and running the Clinic Scheduler application is easy and can be done by following the steps below.
## IntelliJ Instructions
1. Copy the clone path in this repository by navigating to ```Code``` > (Select the Copy icon for HTTPS to copy the URL)
2. Open IntelliJ
3. Navigate to ```File``` > ```New Project from Version Control```
4. Paste the copied URL into the ```URL``` box and specify the preferred location for the repository on your device
5. Select ```Clone```
6. Open the project
7. Within the project's directory contents, navigate to ```src``` > ```main``` > ```com.clinic.scheduler``` > ```SchedulerApplication```
8. Select the green run icon to run the program
9. After the application boots, you should now be interacting directly witht the user interface of the program, prompted with a list of options.

## VSCode Instructions
1. Copy the clone path in this repository by navigating to ```Code``` > (Select the Copy icon for HTTPS to copy the URL)
2. Open VSCode
3. Navigate to the ```Source Control``` tab
4. Select ```Clone Repository```
5. A box should appear to enter the repository URL, paste the URL in this box and specify the preferred location for the repository on your device
6. Open the repository and select ```Yes, I trust the authors```
7. In the project's directory conents, navigate to ```src\main``` > ```java``` > ```SchedulerApplication.java```
8. Navigate to the ```Run``` tab, select the green run icon to run the application
9. After the application boots, you should now be interacting directly witht the user interface of the program, prompted with a list of options.

## Command Line Instructions
1. Navigate to the preferred location on your device to clone the repository
2. Clone the repository
```BASH
git clone https://github.com/ericnmt/oop-clinic-scheduler.git
```
3. Navigate to the newly cloned repository
```BASH
cd oop-clinic-scheduler
```
3. Compile, run, and boot the application via Maven
```BASH
./mvnw spring-boot:run
```
4. After the application boots, you should now be interacting directly witht the user interface of the program, prompted with a list of options.

## Example Usages & Cases
