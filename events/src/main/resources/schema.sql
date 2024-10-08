CREATE TABLE IF NOT EXISTS event (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  description TEXT,
  price DECIMAL(10, 2) NOT NULL,
  vendor_id VARCHAR(255) NOT NULL,  -- El ID del proveedor, relacionado con Keycloak
  max_num_bookings INT,  -- Máximo de reservas permitidas para este evento
  current_num_bookings INT DEFAULT 0,  -- Número de reservas actuales confirmadas
  availability ENUM('AVAILABLE', 'NOT_AVAILABLE') DEFAULT 'AVAILABLE',
  created_at date NOT NULL,
  created_by varchar(20) NOT NULL,
  updated_at date DEFAULT NULL,
  updated_by varchar(20) DEFAULT NULL
);