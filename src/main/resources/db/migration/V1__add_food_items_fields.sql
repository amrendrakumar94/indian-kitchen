-- Migration script to add new columns to dish_details table for Food Items API

-- Add new columns to dish_details table
ALTER TABLE dish_details
ADD COLUMN original_price INT NULL COMMENT 'Original price before discount',
ADD COLUMN discount INT NULL COMMENT 'Discount percentage',
ADD COLUMN rating DECIMAL(3,2) NULL DEFAULT 0.0 COMMENT 'Average rating (0-5)',
ADD COLUMN review_count INT NULL DEFAULT 0 COMMENT 'Number of reviews',
ADD COLUMN in_stock BOOLEAN NULL DEFAULT TRUE COMMENT 'Availability status',
ADD COLUMN serving_size VARCHAR(100) NULL COMMENT 'Serving size description (e.g., Serves 2)',
ADD COLUMN preparation_time VARCHAR(50) NULL COMMENT 'Estimated preparation time',
ADD COLUMN calories INT NULL COMMENT 'Calorie count',
ADD COLUMN allergens VARCHAR(500) NULL COMMENT 'Comma-separated allergen list',
ADD COLUMN customizable BOOLEAN NULL DEFAULT FALSE COMMENT 'Whether item can be customized',
ADD COLUMN tags VARCHAR(500) NULL COMMENT 'Comma-separated tags (popular, bestseller, chef-special)',
ADD COLUMN brand VARCHAR(100) NULL DEFAULT 'Indian Kitchen' COMMENT 'Brand/restaurant name',
ADD COLUMN meal_type VARCHAR(50) NULL COMMENT 'Meal type (breakfast, lunch, dinner, snacks)',
ADD COLUMN category VARCHAR(50) NULL COMMENT 'Food category',
ADD COLUMN order_count INT NULL DEFAULT 0 COMMENT 'For popularity sorting',
ADD COLUMN created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp';

-- Create indexes for frequently queried columns
CREATE INDEX idx_dish_category ON dish_details(category);
CREATE INDEX idx_dish_cuisine ON dish_details(cuisine_type);
CREATE INDEX idx_dish_price ON dish_details(price);
CREATE INDEX idx_dish_rating ON dish_details(rating);
CREATE INDEX idx_dish_meal_type ON dish_details(meal_type);
CREATE INDEX idx_dish_order_count ON dish_details(order_count);
CREATE INDEX idx_dish_in_stock ON dish_details(in_stock);

-- Create food_images table for multiple images support
CREATE TABLE IF NOT EXISTS food_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    food_item_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (food_item_id) REFERENCES dish_details(id) ON DELETE CASCADE,
    INDEX idx_food_item_id (food_item_id),
    INDEX idx_is_primary (is_primary)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Multiple images for food items';

-- Create food_addons table for add-ons
CREATE TABLE IF NOT EXISTS food_addons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    food_item_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    price INT NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (food_item_id) REFERENCES dish_details(id) ON DELETE CASCADE,
    INDEX idx_food_item_id (food_item_id),
    INDEX idx_available (available)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Add-ons for food items';

-- Update existing records with default values
UPDATE dish_details 
SET 
    in_stock = TRUE WHERE in_stock IS NULL,
    customizable = FALSE WHERE customizable IS NULL,
    rating = 0.0 WHERE rating IS NULL,
    review_count = 0 WHERE review_count IS NULL,
    order_count = 0 WHERE order_count IS NULL,
    brand = 'Indian Kitchen' WHERE brand IS NULL;

-- Set category based on cuisine_type for existing records (example mapping)
UPDATE dish_details 
SET category = CASE 
    WHEN cuisine_type = 'northIndian' THEN 'north-indian'
    WHEN cuisine_type = 'southIndian' THEN 'south-indian'
    WHEN cuisine_type = 'chinese' THEN 'chinese'
    ELSE 'main-course'
END
WHERE category IS NULL;

-- Set meal_type based on existing data (example logic)
UPDATE dish_details 
SET meal_type = 'lunch'
WHERE meal_type IS NULL;

COMMIT;
