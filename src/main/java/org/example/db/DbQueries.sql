create database studentdb;

use studentdb;

CREATE TABLE students (
    studentID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    PRIMARY KEY (studentID)
);

