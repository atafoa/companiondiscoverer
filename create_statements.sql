CREATE TABLE ANIMAL (
	Animal_ID int NOT NULL AUTO_INCREMENT,
    Description text NOT NULL,
    Age int,
    Name varchar(255) NOT NULL,
    Type varchar(255) NOT NULL,
    Breed varchar(255),
    Size varchar(255),
    Color varchar(255),
    Available bit,
    Posted_Date timestamp DEFAULT CURRENT_TIMESTAMP,
    Sex varchar(50),
    Picture_URL text,
    PRIMARY KEY(Animal_ID)
);
CREATE TABLE COMMON (
    Animal_ID int NOT NULL,
    Type varchar(255) NOT NULL,
    Weight int,
    Coat_Length varchar(255),
    Coat_Texture varchar (255),
    Sterilized bit,
    House_Trained bit,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE VACCINATION (
    Animal_ID int NOT NULL,
    Vaccine bit,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE DOGS (
    Animal_ID int NOT NULL,
    Service_Animal bit,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES COMMON(Animal_ID)
);
CREATE TABLE CATS (
    Animal_ID int NOT NULL,
    DECLAWED bit,
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES COMMON(Animal_ID)
);
CREATE TABLE FISH (
    Animal_ID int NOT NULL,
    Water_Type varchar(255),
    PRIMARY KEY(Animal_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE ACCOUNT (
    Profile_ID int NOT NULL AUTO_INCREMENT,
    First_Name varchar(255) NOT NULL,
    Last_Name varchar(255) NOT NULL,
    Mobile_Number int,
    Email varchar(255) NOT NULL,
    Username varchar(255) NOT NULL,
    Password varchar(255) NOT NULL,
    Join_Date timestamp DEFAULT CURRENT_TIMESTAMP,
    Type varchar(255),
    PRIMARY KEY(Profile_ID)
);
CREATE TABLE ADMIN (
    Profile_ID int NOT NULL,
    SSN int NOT NULL,
    PRIMARY KEY(Profile_ID),
    FOREIGN KEY (Profile_ID) REFERENCES ACCOUNT(Profile_ID)
);
CREATE TABLE PROFILE(
    Profile_ID int NOT NULL,
    PRIMARY KEY(Profile_ID),
    FOREIGN KEY (Profile_ID) REFERENCES ACCOUNT(Profile_ID)
);
CREATE TABLE INQUIRY (
    Profile_ID int NOT NULL,
    Animal_ID int NOT NULL,
    Inquiry_Question varchar(255),
    Inquiry_Answers varchar(255),
    Inquiry_Date timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(Animal_ID, Profile_ID, Inquiry_Date),
    FOREIGN KEY (Profile_ID) REFERENCES ACCOUNT(Profile_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE LIKES (
    Profile_ID int NOT NULL,
    Animal_ID int NOT NULL,
    PRIMARY KEY(Animal_ID,Profile_ID),
    FOREIGN KEY (Profile_ID) REFERENCES PROFILE(Profile_ID),
    FOREIGN KEY(Animal_ID) REFERENCES ANIMAL(Animal_ID)
);
CREATE TABLE ADOPTION (
    Profile_ID int NOT NULL,
    Animal_ID int NOT NULL,
    Adoption_ID int NOT NULL,
    Adoption_Date timestamp DEFAULT CURRENT_TIMESTAMP,
    Adoption_Fee int NOT NULL,
    PRIMARY KEY(Adoption_ID),
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

