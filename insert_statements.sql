INSERT INTO ANIMAL (Description, Age, Name, Type, Breed, Size, Color, Available, Posted_Date, Sex)
VALUES ('Meet Tammy. Tammy was very timid when she first arrived with us however she has really become comfortable in her foster home and is very sweet. She is completely house trained, does well in a crate and loves to cuddle under the covers. She would love a home of her own where she will be loved. She would do find with other small dogs. She tries to bully bigger dogs. She would probably be best with older (10+)',5,'Tammy','Dog','Chihuahua','Small','Black',1,TO_DATE('4/27/2019','MM/DD/YYYY'),'F');

INSERT INTO COMMON(Animal_ID,Type, Weight, Coat_Length, Sterilized, House_Trained)
VALUES(1,'Dog',5,'Short',1,1);

INSERT INTO DOG(Animal_ID,Service_Animal)
VALUES(1,0);

INSERT INTO ANIMAL(Description, Age, Name, Type, Breed, Size, Color, Available, Posted_Date, Sex,Picture_URL)
VALUES ('Meet Todd. Todd is about a year and a half and is an Old English Bulldog. He is sweet and playful and great with kids and adults. He loves to play fetch and then get loved on. He would do best as an only dog or with a submissive female. He is crate and house trained. He was treated for heartworms and has been fully vetted.',1,'Todd','Dog','American Bulldog','Large','Brown',1,TO_DATE('4/29/2019','MM/DD/YYYY'),'M','https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/44347084/1/?bust=1553926457&width=560');

INSERT INTO COMMON(Animal_ID,Type, Weight, Coat_Length, Coat_Texture, Sterilized, House_Trained)
VALUES(2,'Dog',25,'Short','Matte',1,1);

INSERT INTO DOG(Animal_ID,Service_Animal)
VALUES(2,1);

INSERT INTO ANIMAL(Description, Age, Name, Type, Breed, Size, Color, Available, Posted_Date, Sex,Picture_URL)
VALUES ('Meet Jaxx, a 4 year old Parvo survivors. She was surrendered to us along with her mate Harlem when they both came down with Parvo. They have been treated and are back to complete health. She is a little shy but very sweet.',5,'Jaxx','Dog','Dachshund Chihuahua Mix','Small','Brown',1,TO_DATE('4/15/2019','MM/DD/YYYY'),'F','https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/44203335/1/?bust=1552317894&width=560');

INSERT INTO COMMON(Animal_ID,Type, Weight, Coat_Length, Coat_Texture, Sterilized, House_Trained)
VALUES(3,'Dog',12,'Short','Matte',1,1);

INSERT INTO DOG(Animal_ID,Service_Animal)
VALUES(3,0);

INSERT INTO ANIMAL(Description, Age, Name, Type, Breed, Size, Color, Available, Posted_Date, Sex,Picture_URL)
VALUES('Four month old neutered male. Purrs and plays!',2,'Toothless','Cat','Domestic Short Hair','Black',1,TO_DATE('4/21/2019','MM/DD/YYYY'),'M','https://dl5zpyw5k3jeb.cloudfront.net/photos/pets/44558614/1/?bust=1556385378&width=560');

INSERT INSERT COMMON(Animal_ID,Type, Weight, Coat_Length, Coat_Texture, Sterilized, House_Trained)
VALUES










INSERT INTO ACCOUNT ( First_Name, Last_Name, Mobile_Number, Email, Username, Password, Type)
VALUES ('Atafo', 'Abure', 231,'Test mail','xx1','xxx','Admin');

INSERT INTO ADMIN ( Profile_ID, SSN)
VALUES (1,2341);