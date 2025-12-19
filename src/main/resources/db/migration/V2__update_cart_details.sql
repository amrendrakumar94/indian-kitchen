-- Migration script to update cart_details table for Shopping Cart API

-- Rename count column to quantity
ALTER TABLE cart_details CHANGE COLUMN `count` `quantity` INT NOT NULL DEFAULT 1;

-- Add price column to store price at time of adding to cart
ALTER TABLE cart_details 
ADD COLUMN price DECIMAL(10, 2) NOT NULL DEFAULT 0.00 AFTER quantity;

-- Add timestamps
ALTER TABLE cart_details 
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER price,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

-- Add unique constraint to prevent duplicate entries
ALTER TABLE cart_details 
ADD UNIQUE KEY unique_user_dish (user_id, dish_id);

-- Add indexes for performance
CREATE INDEX idx_cart_user_id ON cart_details(user_id);
CREATE INDEX idx_cart_dish_id ON cart_details(dish_id);

-- Update existing records with default price (fetch from dish_details)
UPDATE cart_details c
INNER JOIN dish_details d ON c.dish_id = d.id
SET c.price = d.price
WHERE c.price = 0.00;

COMMIT;
