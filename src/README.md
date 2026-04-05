# Checkpoint 1.5
## Eric Johnson, Abimael Lozano, Manasi Movva

## What Was Implemented
All business rules established in Checkpoint 1 (refer to ../docs) were implemented into the core logic class, AppointmentManager. Major changed include the use of exceptions in the case where a method fails (i.e. updateAppointmentStatus fails in the case of an invalid time range). Minor changes include the shift from 'Appointment' objects to 'appointmentId' integers in parameters of methods to simplify searching and preparation for backend development. In addition, Patient and Provider objects are stored in a Map to optimize search times, while Appointment objects are still stored in an array list.

** *

## Business Rules Enforced
The system strictly enforces rules using Java Exceptions, rather than failing (and possibly crashing):

* **Entity Existence:** The system verifies that patientId and providerId (key identifier for Patient and Provider objects) actually exist before allowing scheduling.
* **Time Validity:** Appointments are rejected if the start time is strictly on or after the end time, or if the appointment is being scheduled in the past.
* **NO Provider Overlaps:** The system loops through a provider's existing schedule to ensure new or rescheduled appointments do not overlap with existing commitments.
* **State Transitions:** Status changes follow strict logic (i.e. A CANCELLED appointment cannot become COMPLETED).

## What is Not Implemented Yet
As per Checkpoint 1.5 requirements, the following features are not yet implemented:
* **Database Integration:** There is no database implementation (SQLite), data will reset once the program terminates.
* **Frontend:** There is no graphical interface developed yet. Interaction currently only handled via the console.

## How Run the Test
We decided on going with a driver-based approach. All test are contained within the 'Main.java' file.

TO run the tests:
1. Compile all Java files in the 'src' directory
2. Run the 'Main.java' class

The test driver will output to the console, demonstrating:
1. The successful addition of entities to the manager.
2. The scheduling of a valid appointment.
3. The successful update of an appointment's status.
4. Intentional failing scheduling attempts.
5. The successful retrieval of appointments filtered by patientId.
