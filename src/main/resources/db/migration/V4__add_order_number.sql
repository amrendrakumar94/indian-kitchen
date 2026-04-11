-- Add order_number column to orders table
ALTER TABLE orders ADD COLUMN order_number VARCHAR(50) NOT NULL AFTER order_id;
