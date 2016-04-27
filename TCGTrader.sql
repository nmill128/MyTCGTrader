DROP TABLE IF EXISTS Wants,Trades,CardPhotos,UserPhotos,Users,Cards;

CREATE TABLE Users
(
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR (32) NOT NULL,
    password VARCHAR (256) NOT NULL,
    first_name VARCHAR (32) NOT NULL,
    middle_name VARCHAR (32),
    last_name VARCHAR (32) NOT NULL,
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
    id INT NOT NULL AUTO_INCREMENT,
    cardName VARCHAR(255) NOT NULL,
    cardCondition VARCHAR(255) NOT NULL,
    edition VARCHAR(255) NOT NULL,
    cardValue FLOAT NOT NULL,
    notes VARCHAR(255),
    date_added DATE NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE Wants
(
    id INT NOT NULL,
    cardName VARCHAR(255) NOT NULL,
    cardCondition VARCHAR(255) NOT NULL,
    cardValue FLOAT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);


CREATE TABLE Trades
(
    id INT NOT NULL AUTO_INCREMENT,
    creator_id INT NOT NULL,
    reciever_id INT NOT NULL,
    offer_date DATE NOT NULL,
    parent_offer_id INT NOT NULL,
    approved BOOLEAN NOT NULL,
    completed BOOLEAN NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(parent_offer_id) REFERENCES Trades(id),
    FOREIGN KEY(creator_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY(reciever_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE TradeCards
(
    id INT NOT NULL AUTO_INCREMENT,
    cardID INT NOT NULL,
    tradeID INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(cardID) REFERENCES Cards(id) ON DELETE CASCADE,
    FOREIGN KEY(tradeID) REFERENCES Trades(id) ON DELETE CASCADE
);

CREATE TABLE TradeComments
(
    id INT NOT NULL AUTO_INCREMENT,
    tradeID INT NOT NULL,
    string VARCHAR(255) NOT NULL,
    creator_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(creator_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY(tradeID) references Trades(id) ON DELETE CASCADE
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
