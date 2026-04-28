DROP TABLE IF EXISTS Appointment;
DROP TABLE IF EXISTS Patient;
DROP TABLE IF EXISTS Provider;

-- Patient Table
CREATE TABLE Patient (
    PatientID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name TEXT NOT NULL,
    DateOfBirth TEXT NOT NULL,
    ContactInfo TEXT
);

-- Provider Table
CREATE TABLE Provider (
    ProviderID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name TEXT NOT NULL,
    Specialty TEXT NOT NULL,
    Location TEXT NOT NULL
)

-- Appointment Table
CREATE TABLE Appointment (
    AppointmentID INTEGER PRIMARY KEY AUTOINCREMENT,
    StartTime TEXT NOT NULL,
    EndTime TEXT NOT NULL,
    Status TEXT NOT NULL,
    Reason TEXT NOT NULL,
    PatientID INTEGER NOT NULL,
    ProviderID INTEGER NOT NULL,
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID),
    FOREIGN KEY (ProviderID) REFERENCES Provider(ProviderID)
)