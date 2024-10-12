CREATE TABLE IF NOT EXISTS suscription (
  id INT NOT NULL AUTO_INCREMENT,
  vendor_id VARCHAR(255) NOT NULL,  -- El ID del proveedor, relacionado con Keycloak
  user_id VARCHAR(255) NOT NULL,  -- El ID del usuario, relacionado con Keycloak
  created_at date NOT NULL,
  created_by varchar(20) NOT NULL,
  updated_at date DEFAULT NULL,
  updated_by varchar(20) DEFAULT NULL
);