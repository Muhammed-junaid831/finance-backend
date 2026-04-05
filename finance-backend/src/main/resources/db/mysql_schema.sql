-- Finance backend — MySQL schema aligned with JPA entities (Spring Boot + Hibernate).
-- Usage: create database, then run this file, or rely on spring.jpa.hibernate.ddl-auto=update and use only seed data.

CREATE DATABASE IF NOT EXISTS finance_db;
USE finance_db;

-- Application users (roles: VIEWER, ANALYST, ADMIN — match enum RoleType)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    role VARCHAR(32) NOT NULL,
    status VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Financial records (income / expense entries)
CREATE TABLE IF NOT EXISTS records (
    id BIGINT NOT NULL AUTO_INCREMENT,
    amount DOUBLE NOT NULL,
    type VARCHAR(32) NOT NULL,
    category VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    note VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- If you previously used the old `Transaction` entity, you may still have a leftover table named `transaction`.
-- You can drop it manually: DROP TABLE IF EXISTS transaction;
