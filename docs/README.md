## Description of changes made between Steps 1-5
### A. Entity Relation Diagram and Relational Schema Drafts
Created the system's ER diagram to map out relational schema to be used in Part **B**. These can be found **[here](https://github.com/ericnmt/oop-clinic-scheduler/blob/main/docs/ER_Diagram_Schema.pdf)**

### B. Spring Boot Implementation
Converted into a Maven-based Spring Boot application using the following configuration:
* Maven, Java, Spring Boot 4.0.6
* Group: com.clinic
* Artifact: scheduler
* Package name: com.clinic.scheduler
* Jar, Propperties, 17
* Dependencies: JDBC API for DAO database interaction.
Added the following SQLite dependency to the ```<dependencies>``` section in ```pom.xml```
```xml
<dependency>
			<groupId>org.xerial</groupId>
			<artifactId>sqlite-jdbc</artifactId>
			<version>3.45.1.0</version>
</dependency>
```
Adding this dependency ensured that SQLite is defined as the system's relational database framework, and aligns with the requirements of Spring Boot.
This also change involved refactoring the file structure by migrating packages into ```main.java.com.clinic.scheduler``` with the following structure:
```bash
├── SchedulerApplication.java
├── dao
│   ├── AppointmentDao.java
│   ├── AppointmentDaoImpl.java
│   ├── PatientDao.java
│   ├── PatientDaoImpl.java
│   ├── ProviderDao.java
│   └── ProviderDaoImpl.java
├── model
│   ├── Appointment.java
│   ├── AppointmentStatus.java
│   ├── Patient.java
│   └── Provider.java
└── service
    └── AppointmentManager.java
```

### C. Relational Database Schema Implementation & Configuration
The following properties were added to the ```application.properties``` file to configure database connection and ensure schema initialization on startup
```
# SQLite connection URL
spring.datasource.url=JDBC:sqlite:clinic.db

# SQLite JDBC driver
spring.datasource.driver-class-name=org.sqlite.JDBC

# ALWAYS run schema.sql on startup
spring.sql.init.mode=always
```
As a result, the ```schema.sql``` file was added to define the Data Definition Language (DDL) for the database. This involved creating the three primary tables for ```Patient```, ```Povider```, and ```Appointment``` objects, and establishing their appropriate keys.

### D. Data Access Object (DAO) Pattern implementation
The ```Patient```, ```Provider```, and ```Appointment``` DAO interfaces were added to define their CRUD operations, along with the implementation of those interfaces. The implementation of these **interfaces involves the following parts for each class**:
* Injection of the JDBC Template into each implementation class to isolate database queries, allowing encapsulation.
* (ONLY AppointmentImpl) Injection of ```PatientDao``` and ```ProviderDao``` DAOs to query those object's information.
* Definition of a constructors for each implementation to formally inject templates/DAOs.
* Definition of the RowMapper function to map database result sets directly to POJOs (Java Object instances).
* Formal definitions of each interface's abstract method, including their database SQL queries.

As a result, this step fulfills keeping SQL usage behind the Spring Boot framework via the DAO pattern.

