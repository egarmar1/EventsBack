CREATE TABLE IF NOT EXISTS booking (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id VARCHAR(255) NOT NULL,       -- ID of the user making the booking
  event_id BIGINT NOT NULL,          -- ID of the related service/event
  book_date DATETIME NOT NULL,         -- Date and time of the booking
  state ENUM('PENDING', 'ACCEPTED', 'ATTENDED') NOT NULL,  -- Assuming the State enum has these values
  created_at DATETIME NOT NULL,        -- BaseEntity field: created timestamp
  created_by VARCHAR(20) NOT NULL,     -- BaseEntity field: user who created the record
  updated_at DATETIME DEFAULT NULL,    -- BaseEntity field: updated timestamp
  updated_by VARCHAR(20) DEFAULT NULL, -- BaseEntity field: user who last updated the record
  PRIMARY KEY (id)
);
