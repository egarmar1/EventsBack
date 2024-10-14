CREATE TABLE IF NOT EXISTS suscription (
  id INT NOT NULL AUTO_INCREMENT,
  vendor_id VARCHAR(255) NOT NULL,  -- El ID del proveedor, relacionado con Keycloak
  client_id VARCHAR(255) NOT NULL  -- El ID del cliente, relacionado con Keycloak
);