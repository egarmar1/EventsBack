CREATE TABLE IF NOT EXISTS users (
  id CHAR(36) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  type_id INT NOT NULL,
  created_at date NOT NULL,
  created_by varchar(20) NOT NULL,
  updated_at date DEFAULT NULL,
  updated_by varchar(20) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_type (
  id INT AUTO_INCREMENT NOT NULL,
  type VARCHAR(255) NOT NULL,
  created_at date NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  updated_at DATE DEFAULT NULL,
  updated_by VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO users (id, full_name, email, type_id, created_at, created_by, updated_at, updated_by) VALUES
('4565934b-210c-4b0b-a384-09956802afb4', 'John Doe', 'kikesimp01@gmail.com', 1, NOW(), 'system', NULL, NULL), -- Client user
('dcd81b43-f0cc-4848-b8ae-a636b2c14b61', 'Vendor1', 'zxc@example.com', 2, NOW(), 'system', NULL, NULL), -- Vendor user
(UUID(), 'Bob Johnson', 'bob.johnson@example.com', 1, NOW(), 'system', NULL, NULL), -- Client user
('c31a4735-e357-4c18-99e9-b54da83dd89b', 'Vendor2', 'bob.johnson@example.com', 2, NOW(), 'system', NULL, NULL); -- Vendor user


INSERT INTO user_type (type, created_at, created_by, updated_at, updated_by) VALUES
('client', NOW(), 'system', NULL, NULL),
('vendor', NOW(), 'system', NULL, NULL);