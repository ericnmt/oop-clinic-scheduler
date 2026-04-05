# Checkpoint 1.5

## What Was Implemented
All business rules established in Checkpoint 1 (refer to ../docs) were implemented into the core logic class, AppointmentManager. Major changed include the use of exceptions in the case where a method fails (i.e. updateAppointmentStatus fails in the case of an invalid time range). Minor changes include the shift from 'Appointment' objects to 'appointmentId' integers in parameters of methods to simplify searching and preparation for backend development. In addition, Patient and Provider objects are stored in a Map to optimize search times, while Appointment objects are still stored in an array list.

## Business Rules Enforced
The system strictly enforces rules using Java Exceptions, rather than failing (and possibly crashing):
