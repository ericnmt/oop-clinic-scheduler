# oop-clinic-scheduler
Eric Johnson, Manasi Movva, Abimael Lozano
## Description
The Clinic Scheduler is a Spring Boot application that manages patients, providers, and appointments through a structured and reliable scheduling system. It supports creating, updating, rescheduling, and canceling appointments while enforcing key business rules such as preventing time conflicts and maintaining valid status transitions.

The project was built to demonstrate object-oriented design and real-world backend development by moving from in-memory data handling to a persistent, database-driven system. It uses a layered architecture with a service layer for business logic and DAO components for database interaction via Spring Boot’s JdbcTemplate. Data is stored in a SQLite database, ensuring persistence across application runs.

## Architectural Design
We settled for a Maven-based Spring Boot application to ensure a clear separation of concerns and layered architecture. This design choice focuses on maintainability and testability by isolating logic into different components, allowing for the strict enforcement of business logic. A key contribution to this design was the refactoring of the core architecture to fully adopt a layered approach + DAO pattern, replacing earlier in-memory implementations with database-backed operations.

### Layered Architecture
The system follows a clean separation of concerns, organized into layers to ensure compatibility between components and preservation of application logic. The components of our system are split into the following schema: Model Layer, Service Layer, Data Access Layer (DAO), and Presentation Layer (via the CLI).
* **Model Layer**: Consists of POJOs such as ```Patient```, ```Provider```, and ```Appointment``` classes. These classes represent the core data entities of the system and are mapped directly to database tables via the Data Access Layer. This model was built off of the initial [**ER Schema**](https://github.com/ericnmt/oop-clinic-scheduler/blob/main/docs/ER_Diagram_Schema.pdf).
* **Service Layer**: Consists of the ```AppointmentManager``` class, which orchestrates operations and ensures the strict application of business logic. Every CRUD operation is passed through this layer for absolute business rule enforcement.
* **Data Access Layer**: We settled with the Data Access Object (DAO) pattern to define and map CRUD operations between POJOs and the database with business logic constraints. This layer is split into two distinct components: Interfaces and Implementation. Interfaces include ```PatientDao```, ```ProviderDao```, and ```AppointmentDao``` to define CRUD operations. These interfaces and their operations are all implemented, containing data handling and specific SQL queries needed to interact with the database.
* **Presentation Layer**: For the primary user interface (UI), we utilized Spring Boot's ```SchedulerApplication``` class. This is the main component that handles input parsing and displays operation outputs, all while invoking the Service Layer for business rule enforcement.

### Data Management & Persistence
Data is managed through a model that utilizes a file-based relational database.
* **Relational Database Integration**: We settled with using the file-based SQLite framework for the relational database. This choice provides persistence across application restarts without the overhead of a dedicated database server. Database entries are stored locally, in the ```clinic.db``` file.
* **Schema Enforcement**: The foundational ER schema was translated into the primary database structure, defined in ```schema.sql```, which establishes the three primary tables consisting of each entity's Primary and Foreign Keys. Note that the ```PRAGMA foreign_keys=ON``` option in the ```application.properties``` enforces database engine to strictly reference foreign keys. This is further represented below, where the existence of appointments also depends on the existence of its associated Providers and Patients, hence the design choice on specifying ```ON DELETE CASCADE``` for Patient and Provider foreign keys in the Appointment table. We utilize DDL to create these structures:
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
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (ProviderID) REFERENCES Provider(ProviderID) ON DELETE CASCADE
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

### DAO Pattern Implementation
* **DAO-Driven Operations**: Replaced all in-memory logic with DAO-based database queries to ensure persistence and scalability.
* **Scheduling Logic Refactor**: Modified scheduleAppointment and rescheduleAppointment to use DAO methods for inserting, updating, and retrieving appointment data.
* **Consistent Data Flow**: Ensured all interactions between application logic and the database are routed through the Data Access Layer.

### Testing & Validation
* **Service Layer Testing**: Verified correct execution of scheduling, rescheduling, and update operations through the service layer.
* **Edge Case Handling**: Tested invalid time ranges, conflicting appointments, and invalid IDs to ensure robust validation.
* **Status Updates**: Confirmed correct handling of appointment state changes such as CANCELLED.

### Data Persistence
Since CRUD operations are reflected on the ```clinic.db``` file, all database changes are stored locally in this file. Therefore, data persists beyond Spring Boot sessions and may be accessed and modified in unique sessions.

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
9. After the application boots, you should now be interacting directly with the user interface of the program, prompted with a list of options.

## VSCode Instructions
1. Copy the clone path in this repository by navigating to ```Code``` > (Select the Copy icon for HTTPS to copy the URL)
2. Open VSCode
3. Navigate to the ```Source Control``` tab
4. Select ```Clone Repository```
5. A box should appear to enter the repository URL, paste the URL in this box and specify the preferred location for the repository on your device
6. Open the repository and select ```Yes, I trust the authors```
7. In the project's directory contents, navigate to ```src\main``` > ```java``` > ```SchedulerApplication.java```
8. Navigate to the ```Run``` tab, select the green run icon to run the application
9. After the application boots, you should now be interacting directly with the user interface of the program, prompted with a list of options.

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
4. Compile, run, and boot the application via Maven
```BASH
./mvnw spring-boot:run
```
5. After the application boots, you should now be interacting directly with the user interface of the program, prompted with a list of options.

## Example Usages & Cases
The Application prints the following menu to the CLI:
```BASH
Clinic Appointment Scheduler
1. Add Patient
2. Add Provider
3. Schedule Appointment
4. View Appointments by Patient
5. View Appointments by Provider
6. View Appointments by Date Range
7. View Appointments by Status
8. Reschedule Appointment
9. Update Appointment Status
10. Update Patient
11. Update Provider
12. Delete Patient
13. Delete Provider
0. Exit
```
### Create Entities
Demonstration of successful instantiation and persistence of entities
1. **Adding valid patients**
```BASH
Choice: 1           // Add Patient
Patient ID: 101
Name: Jane Doe
Date of Birth YYYY-MM-DD: 2000-01-01
Contact Info: jane.doe@example.com
Patient added successfully.

Choice: 1           // Add Patient
Patient ID: 102
Name: Bob Jones
Date of Birth YYYY-MM-DD: 1985-10-22
Contact Info: 555-555-3251
Patient added successfully.
```

Creates the following entries into the database:
```SQL
INSERT INTO "Patient" VALUES (101,'Jane Doe','2000-01-01','jane.doe@example.com');
INSERT INTO "Patient" VALUES (102,'Bob Jones','1985-10-22','555-555-3251');
```

| PatientID | Name | DateOfBirth | ContactInfo |
| -------- | -------- | -------- | -------- | 
| 101 | Jane Doe | 2000-01-01 | jane.doe@example.com |
| 102 | Bob Jones | 1985-10-22 | 555-555-3251 |

2. **Adding valid providers**
```BASH
Choice: 2           // Add Provider
Provider ID: 1
Name: Dr. John Smith
Specialty: Cardiology
Location: Room 101
Provider added successfully.

Choice: 2           // Add Provider
Provider ID: 2
Name: Dr. Gregory Watson
Specialty: Neurology
Location: Room 102
Provider added successfully.
```
Creates the following entries into the database:
```SQL
INSERT INTO "Provider" VALUES (1,'Dr. John Smith','Cardiology','Room 101');
INSERT INTO "Provider" VALUES (2,'Dr. Gregory Watson','Neurology','Room 102');
```
| ProviderID | Name | Specialty | Location |
| --- | --- | --- | --- |
| 1 | Dr. John Smith | Cardiology | Room 101 |
| 2 | Dr. Gregory Watson | Neurology | Room 102 |

### Valid Operations
Demonstration of core scheduling mechanics under normal conditions
1. **Scheduling a valid appointment**
```BASH
Choice: 3           // Schedule Appointment
Patient ID: 101
Provider ID: 2
Reason: Post-operation checkup
Start Date/Time YYYY-MM-DDTHH:MM: 2026-06-10T08:00
End Date/Time YYYY-MM-DDTHH:MM: 2026-06-10T09:00
Appointment scheduled successfully:
------------------------------
NEW APPOINTMENT CREATED
------------------------------
Appointment ID : 1
Patient        : Name: Jane Doe (ID 101), DOB: 2000-01-01, contact info: jane.doe@example.com
Provider       : Name: Dr. Gregory Watson (ID 2), specialty: Neurology, location: Room 102
Start Time     : 2026-06-10T08:00
End Time       : 2026-06-10T09:00
Status         : SCHEDULED
Reason         : Post-operation checkup
------------------------------
```
Creates the following entry into the database:
```SQL
INSERT INTO "Appointment" VALUES (1,'2026-06-10T08:00:00','2026-06-10T09:00:00','SCHEDULED','Post-operation checkup',101,2);
```
| AppointmentID | StartTime | EndTime | Status | Reason | _PatientID_ | _ProviderID_ |
| --- | --- | --- | --- | --- | --- | --- |
1 | 2026-06-10T08:00:00 | 2026-06-10T09:00:00 | SCHEDULED | Post-operation checkup | 101 | 2 |

2. **Rescheduling an appointment**
```BASH
Choice: 8           // Reschedule Appointment
Appointment ID: 1
New Start Date/Time YYYY-MM-DDTHH:MM: 2026-05-31T08:00
New End Date/Time YYYY-MM-DDTHH:MM: 2026-05-31T09:00
Appointment rescheduled successfully.
```
Verifying the appointment has successfully been rescheduled:
```BASH
Choice: 4           // View Appointments by Patient
Patient ID: 101

=== Appointments ===
------------------------------
Appointment ID : 1
Patient        : Jane Doe (ID: 101)
Provider       : Dr. Gregory Watson (Neurology)
Location       : Room 102
Start Time     : 2026-05-31T08:00
End Time       : 2026-05-31T09:00
Status         : SCHEDULED
Reason         : Post-operation checkup
------------------------------
```
3. **Updating Appointment Status**
```BASH
Choice: 9           // Update Appointment Status
Appointment ID: 1
Status SCHEDULED, COMPLETED, or CANCELLED: CANCELLED
Appointment status updated successfully.
```
Verifying the appointment's status has successfully been changed from "SCHEDULED" to "CANCELLED"
```BASH
Choice: 4           // View Appointments by Patient
Patient ID: 101

=== Appointments ===
------------------------------
Appointment ID : 1
Patient        : Jane Doe (ID: 101)
Provider       : Dr. Gregory Watson (Neurology)
Location       : Room 102
Start Time     : 2026-05-31T08:00
End Time       : 2026-05-31T09:00
Status         : CANCELLED
Reason         : Post-operation checkup
------------------------------
```

### Business Rule Violations
Demonstration of Service Layer business logic enforcement.
1. **Attempting to schedule an appointment with invalid entities:**
Here, the Patient with ID '999' does not exist. The service simply rejects the appointment request and re-prompts the user for an operation.
```BASH
Choice: 3           // Schedule Appointment
Patient ID: 999
Provider ID: 1
Reason: Checkup
Start Date/Time YYYY-MM-DDTHH:MM: 2026-05-28T08:00
End Date/Time YYYY-MM-DDTHH:MM: 2026-05-28T09:00
Input error: Cannot schedule: Patient ID 999 does not exist.
```

2. **Attempting to schedule an appointment with an invalid time range:**
In this case, the appointment's start time is after the end time, which is impossible. The service simply rejects the appointment request and re-prompts the user for an operation.
```BASH
Choice: 3           // Schedule Appointment
Patient ID: 101
Provider ID: 2
Reason: Post-operation checkup
Start Date/Time YYYY-MM-DDTHH:MM: 2026-05-31T10:00
End Date/Time YYYY-MM-DDTHH:MM: 2026-05-31T09:00
Input error: Invalid time range: start time must be before end time.
```

3. **Provider time conflict:**
In this case, a provider already has an appointment on May 31, 2026 from 8:00-9:00. We attempt to schedule an appointment on May 31, 2026 from 8:30-9:00 with the same provider. However, since there is booking conflict, the service layer simply rejects the appointment request and re-prompts the user for an operation.
```BASH
Choice: 3           // Schedule Appointment
Patient ID: 102
Provider ID: 2
Reason: Consultation
Start Date/Time YYYY-MM-DDTHH:MM: 2026-05-31T08:30
End Date/Time YYYY-MM-DDTHH:MM: 2026-05-31T09:00
Operation error: Cannot schedule: Provider has a conflicting appointment.
```

4. **Invalid Status Transition:**
In this case, we attempt to change an appointment of the "CANCELLED" status to "COMPLETED," This is logically valid since a CANCELLED appointment cannot occur, and therefore cannot be complete. The service layer simply rejects the appointment request and re-prompts the user for an operation.
```BASH
Choice: 9
Appointment ID: 1
Status SCHEDULED, COMPLETED, or CANCELLED: COMPLETED
Operation error: A CANCELLED appointment cannot become COMPLETED.
```

### Query Functionality
Demonstration of Data Access Layer successfully retrieving filtered sets of data
1. **Query by patient:**
In this case, we query for all of the appointments that Patient with ID '101' has scheduled.
```BASH
Choice: 4           // View Appointments by Patient
Patient ID: 101

=== Appointments ===
------------------------------
Appointment ID : 1
Patient        : Jane Doe (ID: 101)
Provider       : Dr. Gregory Watson (Neurology)
Location       : Room 102
Start Time     : 2026-05-31T08:00
End Time       : 2026-05-31T09:00
Status         : CANCELLED
Reason         : Post-operation checkup
------------------------------
```
2. **Query by Provider:**
In this case, we query for all of the appointments that Provider with ID '1' has scheduled.
```BASH
Choice: 5           // View Appointments by Provider
Provider ID: 1

=== Appointments ===
------------------------------
Appointment ID : 2
Patient        : Bob Jones (ID: 102)
Provider       : Dr. John Smith (Cardiology)
Location       : Room 101
Start Time     : 2026-07-18T10:00
End Time       : 2026-07-18T11:00
Status         : SCHEDULED
Reason         : Consultation
------------------------------
```
3. **Query by date range:**
In this case, we query for all the appointments scheduled in the month of May (2026-05-01 to 2026-05-31).
```BASH
Choice: 6           // View Appointments by Date Range
Start Date YYYY-MM-DD: 2026-05-01
End Date YYYY-MM-DD: 2026-05-31

=== Appointments ===
------------------------------
Appointment ID : 1
Patient        : Jane Doe (ID: 101)
Provider       : Dr. Gregory Watson (Neurology)
Location       : Room 102
Start Time     : 2026-05-31T08:00
End Time       : 2026-05-31T09:00
Status         : CANCELLED
Reason         : Post-operation checkup
------------------------------
```
4. **Query by status:**
In this case, we query for all the appointments of the "SCHEDULED" status.
```BASH
Choice: 7           // View Appointmens by Status
Status SCHEDULED, COMPLETED, or CANCELLED: SCHEDULED

=== Appointments ===
------------------------------
Appointment ID : 2
Patient        : Bob Jones (ID: 102)
Provider       : Dr. John Smith (Cardiology)
Location       : Room 101
Start Time     : 2026-07-18T10:00
End Time       : 2026-07-18T11:00
Status         : SCHEDULED
Reason         : Consultation
------------------------------
```

### Deletion Constraints
Demonstration of proper relational database constraints in regards to deletion of entities. 
In this case, we attempt to delete Patient with ID '102', who has a SCHEDULED appointment. The Service Layer simply rejects the deletion request and re-prompts the user for operations.
1. **Invalid Deletion:**
```BASH
Choice: 12          // Delete Patient
Patient ID: 102
Operation error: Cannot delete patient with active appointments.
```
2. **Valid Deletion:**
In this case, we delete Patient with ID '101,' who has no existing SCHEDULED appointments. Note that since we deleted the Patient, the associated appointments are removed entirely from the system. 
```BASH
Choice: 12          // Delete Patient
Patient ID: 101
Patient deleted successfully.
```
To verify that the appointment was deleted, we may query for the appointments associated with the Provider who previously had an appointment scheduled with Patient with ID '101.'
```BASH
Choice: 5          // View Appointments by Provider
Provider ID: 2     // Dr. Gregory Wattson, previously had a scheduled appointment with Jane Doe, Patient ID '101'
No appointments found.
```

### Edge Cases
