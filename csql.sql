CREATE DATABASE IF NOT EXISTS contact_book;

USE contact_book;

CREATE TABLE IF NOT EXISTS contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255)
);
select * from contacts;
