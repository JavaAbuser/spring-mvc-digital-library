CREATE TABLE IF NOT EXISTS Person(
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     fullName VARCHAR(100) UNIQUE,
                                     yearOfBirth INT NOT NULL CHECK (yearOfBirth > 0)
);

CREATE TABLE IF NOT EXISTS Book(
                                   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                   person_id INT REFERENCES Person(id),
                                   name VARCHAR(100) UNIQUE NOT NULL,
                                   author VARCHAR(50) NOT NULL,
                                   year INT NOT NULL CHECK (year > 0),
                                   CONSTRAINT fk_book_person_id
                                   FOREIGN KEY (person_id) REFERENCES Person(id)
                                   ON DELETE SET NULL
);