-- Dummy data for local development.
-- Run after tables exist (either Hibernate ddl-auto=update or mysql_schema.sql).
-- If rows already exist, truncate or adjust emails to avoid duplicate key errors.
--
-- Login (HTTP Basic): username = email below, password = password
-- (BCrypt hash below matches the literal string "password" for Spring BCryptPasswordEncoder).

USE finance_db;

INSERT INTO users (name, email, password, role, status) VALUES
    ('Admin User', 'admin@finance.local', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN', 'active'),
    ('Analyst User', 'analyst@finance.local', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ANALYST', 'active'),
    ('Viewer User', 'viewer@finance.local', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'VIEWER', 'active');

INSERT INTO records (amount, type, category, date, note) VALUES
    (5000.00, 'INCOME', 'Salary', '2026-03-01', 'March salary'),
    (1200.50, 'EXPENSE', 'Rent', '2026-03-02', 'Monthly rent'),
    (85.20, 'EXPENSE', 'Groceries', '2026-03-05', 'Weekly shop'),
    (350.00, 'INCOME', 'Freelance', '2026-03-10', 'Side project'),
    (45.00, 'EXPENSE', 'Transport', '2026-03-12', 'Fuel'),
    (200.00, 'EXPENSE', 'Utilities', '2026-03-15', 'Electricity'),
    (120.75, 'EXPENSE', 'Groceries', '2026-03-18', ''),
    (800.00, 'INCOME', 'Freelance', '2026-04-01', 'April invoice'),
    (95.00, 'EXPENSE', 'Entertainment', '2026-04-02', 'Concert tickets'),
    (60.00, 'EXPENSE', 'Groceries', '2026-04-03', 'Quick run');
