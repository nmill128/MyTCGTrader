DROP TABLE IF EXISTS Wants,Trades,CardPhotos,UserPhotos,Users,Cards;

CREATE TABLE Users
(
    id INT NOT NULL AUTO_INCREMENT,
    cards INT NOT NULL,
    wants INT NOT NULL,
    username VARCHAR (32) NOT NULL,
    password VARCHAR (256) NOT NULL,
    first_name VARCHAR (32) NOT NULL,
    middle_name VARCHAR (32),
    last_name VARCHAR (32) NOT NULL,
    height INT, 
    weight INT, 
    address1 VARCHAR (255) NOT NULL,
    address2 VARCHAR (255),
    city VARCHAR (255) NOT NULL,
    usState VARCHAR (32) NOT NULL,
    zipcode INT NOT NULL,
    security_question INT NOT NULL,
    security_answer VARCHAR (255) NOT NULL,
    good_trades INT NOT NULL,
    bad_trades INT NOT NULL,
    email VARCHAR(64) NOT NULL,      
    PRIMARY KEY (id)
);

CREATE TABLE Cards
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    cardName VARCHAR(255) NOT NULL,
    cardCondition VARCHAR(255) NOT NULL,
    edition VARCHAR(255) NOT NULL,
    cardValue INT NOT NULL,
    notes VARCHAR(255),
    binder INT NOT NULL,
    date_added VARCHAR(255) NOT NULL
);

CREATE TABLE Wants
(
    id INT PRIMARY KEY NOT NULL,
    user_id INT NOT NULL
);


CREATE TABLE Trades
(
	id INT NOT NULL AUTO_INCREMENT,
	creator_id INT NOT NULL,
	reciever_id INT NOT NULL,
	ccards_id VARCHAR(255) NOT NULL,
	rcards_id VARCHAR(255) NOT NULL,
    offer_date VARCHAR(255) NOT NULL,
    parent_offer VARCHAR(255),
	approved BOOLEAN NOT NULL,
    PRIMARY KEY(id)
);

/* The Photo table contains attributes of interest of a card's photo. */
CREATE TABLE CardPhotos
(
       id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT ,
       extension ENUM('jpeg', 'jpg', 'png') NOT NULL,
       card_id INT,
       FOREIGN KEY (card_id) REFERENCES Cards(id) ON DELETE CASCADE
);

CREATE TABLE UserPhotos
(
       id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
       extension ENUM('jpeg', 'jpg', 'png') NOT NULL,
       user_id INT,
       FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);
