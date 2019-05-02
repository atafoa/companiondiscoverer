CREATE TABLE ANIMAL (
    Animal_ID int NOT NULL AUTO_INCREMENT,
    Description text NOT NULL,
    Age int,
    Name varchar(50) NOT NULL,
    Type varchar(50) NOT NULL,
    Breed varchar(50),
    Size varchar(50),
    Color varchar(50) NOT NULL,
    Available bit DEFAULT 1,
    Posted_Date timestamp DEFAULT CURRENT_TIMESTAMP,
    Sex char(1),
    Picture_URL text,
    PRIMARY KEY(Animal_ID)
);
CREATE TABLE COMMON (
    Animal_ID int NOT NULL,
    Type varchar(50) NOT NULL,
    Weight int,
    Coat_Length varchar(50),
    Coat_Texture varchar(50),
    Sterilized bit,
    House_Trained bit,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE VACCINATION (
    Animal_ID int NOT NULL,
    Vaccine varchar(100) NOT NULL,
    PRIMARY KEY(Animal_ID, Vaccine),
    FOREIGN KEY(Animal_ID) REFERENCES COMMON(Animal_ID)
);
CREATE TABLE CATS (
    Animal_ID int NOT NULL,
    Declawed bit,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES COMMON(Animal_ID)
);
CREATE TABLE DOGS (
    Animal_ID int NOT NULL,
    Service_Animal bit,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES COMMON(Animal_ID)
);
CREATE TABLE FISH (
    Animal_ID int NOT NULL,
    Water_Type varchar(10) NOT NULL,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE ACCOUNT (
    Profile_ID int NOT NULL AUTO_INCREMENT,
    First_Name varchar(100) NOT NULL,
    Last_Name varchar(100) NOT NULL,
    Mobile_Number varchar(12),
    Email varchar(100) NOT NULL,
    Username varchar(12) NOT NULL,
    Password varchar(64) NOT NULL,
    Join_Date timestamp DEFAULT CURRENT_TIMESTAMP,
    Type varchar(100) NOT NULL,
    UNIQUE(Email),
    UNIQUE(Username),
    PRIMARY KEY(Profile_ID)
);
CREATE TABLE ADMIN (
    Profile_ID int NOT NULL,
    SSN int NOT NULL,
    UNIQUE(SSN),
    PRIMARY KEY(Profile_ID),
    FOREIGN KEY(Profile_ID) REFERENCES ACCOUNT(Profile_ID)
);
CREATE TABLE PROFILE (
    Profile_ID int NOT NULL,
    PRIMARY KEY(Profile_ID),
    FOREIGN KEY(Profile_ID) REFERENCES ACCOUNT(Profile_ID)
);
CREATE TABLE INQUIRY (
    Animal_ID int NOT NULL,
    Profile_ID int NOT NULL,
    Inquiry_Date timestamp DEFAULT CURRENT_TIMESTAMP,
    Inquiry_Question text NOT NULL,
    Inquiry_Answer text,
    PRIMARY KEY(Animal_ID, Profile_ID, Inquiry_Date),
    FOREIGN KEY(Profile_ID) REFERENCES PROFILE(Profile_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE LIKES (
    Profile_ID int NOT NULL,
    Animal_ID int NOT NULL,
    PRIMARY KEY(Animal_ID, Profile_ID),
    FOREIGN KEY(Profile_ID) REFERENCES PROFILE(Profile_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE ADOPTION (
    Animal_ID int NOT NULL,
    Adoption_Fee int NOT NULL,
    Profile_ID int,
    Adoption_Date timestamp,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY (Profile_ID) REFERENCES PROFILE(Profile_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE DONATION (
    Profile_ID int NOT NULL,
    Animal_ID int NOT NULL,
    Donation_Date timestamp DEFAULT CURRENT_TIMESTAMP,
    Donation_Amount int NOT NULL,
    PRIMARY KEY(Animal_ID, Profile_ID, Donation_Date),
    FOREIGN KEY (Profile_ID) REFERENCES PROFILE(Profile_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);