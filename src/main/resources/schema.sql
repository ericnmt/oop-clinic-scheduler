-- Patient Table
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