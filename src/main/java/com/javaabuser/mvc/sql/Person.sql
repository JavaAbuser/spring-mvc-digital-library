CREATE TABLE IF NOT EXISTS Person(
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     fullName VARCHAR(100) UNIQUE,
                                     yearOfBirth INT NOT NULL CHECK (yearOfBirth > 0)
    );