CREATE TABLE IF NOT EXISTS user (
  id CHAR(36) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  type_id INT NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  updated_at DATE DEFAULT NULL,
  updated_by VARCHAR(20) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO user (id, full_name, email, type_id, created_by, updated_at, updated_by) VALUES
('ea9493bf-496e-4c32-9e61-d673c92812cb', 'John Doe', 'john.doe@example.com', 1, 'system', NULL, NULL),
('f3c8e051-a170-4c50-8471-0543643c6899', 'Jane Smith', 'jane.smith@example.com', 2, 'admin', NULL, NULL),
(UUID(), 'Bob Johnson', 'bob.johnson@example.com', 1, 'system', NULL, NULL);