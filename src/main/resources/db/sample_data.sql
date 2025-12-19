-- Sample Data for Indian Kitchen Food Items API
-- This script inserts diverse food items across different categories, cuisines, and dietary preferences

-- Clear existing data (optional - comment out if you want to keep existing data)
-- TRUNCATE TABLE dish_details;

-- North Indian Main Course Items
INSERT INTO dish_details (
    name, description, price, original_price, discount, 
    cuisine_type, category, spiciness_level, is_vegetarian, is_recommended,
    rating, review_count, in_stock, serving_size, preparation_time, calories,
    ingredients_list, allergens, customizable, tags, brand, meal_type, order_count, image
) VALUES
('Butter Chicken', 'Creamy tomato-based curry with tender chicken pieces', 299, 349, 14, 
 'northIndian', 'main-course', 2, false, true,
 4.8, 342, true, 'Serves 2', '25 mins', 450,
 'chicken,tomato,cream,butter,spices', 'dairy', true, 'popular,chef-special', 'Indian Kitchen', 'lunch', 156,
 'https://images.unsplash.com/photo-1603894584373-5ac82b2ae398'),

('Paneer Tikka Masala', 'Grilled cottage cheese in rich, spicy tomato gravy', 249, 299, 17, 
 'northIndian', 'main-course', 3, true, true,
 4.6, 218, true, 'Serves 2', '30 mins', 380,
 'paneer,tomato,cream,spices', 'dairy', true, 'bestseller', 'Indian Kitchen', 'lunch', 198,
 'https://images.unsplash.com/photo-1631452180519-c014fe946bc7'),

('Dal Makhani', 'Black lentils slow-cooked with butter and cream', 199, 249, 20, 
 'northIndian', 'main-course', 1, true, true,
 4.7, 289, true, 'Serves 2', '45 mins', 320,
 'black lentils,butter,cream,spices', 'dairy', true, 'popular', 'Indian Kitchen', 'dinner', 234,
 'https://images.unsplash.com/photo-1546833999-b9f581a1996d'),

('Chole Bhature', 'Spicy chickpea curry with fluffy fried bread', 179, 199, 10, 
 'northIndian', 'combos', 2, true, false,
 4.5, 167, true, 'Serves 1', '35 mins', 520,
 'chickpeas,onion,tomato,spices,flour', 'gluten', true, 'popular', 'Indian Kitchen', 'breakfast', 145,
 'https://images.unsplash.com/photo-1626132647523-66f5bf380027'),

('Chicken Biryani', 'Aromatic basmati rice with tender chicken and spices', 329, 399, 18, 
 'northIndian', 'rice-biryani', 3, false, true,
 4.9, 456, true, 'Serves 2', '40 mins', 580,
 'basmati rice,chicken,yogurt,spices,saffron', 'dairy', true, 'bestseller,chef-special', 'Indian Kitchen', 'lunch', 389,
 'https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8'),

('Veg Biryani', 'Fragrant rice with mixed vegetables and aromatic spices', 249, 299, 17, 
 'northIndian', 'rice-biryani', 2, true, true,
 4.4, 198, true, 'Serves 2', '35 mins', 420,
 'basmati rice,mixed vegetables,spices,saffron', '', true, 'popular', 'Indian Kitchen', 'lunch', 167,
 'https://images.unsplash.com/photo-1596797038530-2c107229654b');

-- South Indian Items
INSERT INTO dish_details (
    name, description, price, original_price, discount, 
    cuisine_type, category, spiciness_level, is_vegetarian, is_recommended,
    rating, review_count, in_stock, serving_size, preparation_time, calories,
    ingredients_list, allergens, customizable, tags, brand, meal_type, order_count, image
) VALUES
('Masala Dosa', 'Crispy rice crepe filled with spiced potato filling', 149, 179, 17, 
 'southIndian', 'south-indian', 2, true, true,
 4.7, 312, true, 'Serves 1', '20 mins', 280,
 'rice,lentils,potato,spices', '', true, 'bestseller', 'Indian Kitchen', 'breakfast', 278,
 'https://images.unsplash.com/photo-1630383249896-424e482df921'),

('Idli Sambar', 'Steamed rice cakes served with lentil soup', 99, 129, 23, 
 'southIndian', 'south-indian', 1, true, true,
 4.6, 245, true, 'Serves 1', '15 mins', 180,
 'rice,lentils,vegetables,spices', '', false, 'popular', 'Indian Kitchen', 'breakfast', 198,
 'https://images.unsplash.com/photo-1589301760014-d929f3979dbc'),

('Medu Vada', 'Crispy lentil donuts served with chutney', 119, 149, 20, 
 'southIndian', 'south-indian', 2, true, false,
 4.5, 156, true, 'Serves 1', '25 mins', 220,
 'urad dal,curry leaves,spices', '', false, '', 'Indian Kitchen', 'snacks', 123,
 'https://images.unsplash.com/photo-1606491956689-2ea866880c84'),

('Hyderabadi Biryani', 'Authentic Hyderabadi style chicken biryani', 349, 449, 22, 
 'southIndian', 'rice-biryani', 4, false, true,
 4.9, 523, true, 'Serves 2', '50 mins', 620,
 'basmati rice,chicken,yogurt,spices,saffron,mint', 'dairy', true, 'chef-special,bestseller', 'Indian Kitchen', 'dinner', 412,
 'https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8');

-- Starters
INSERT INTO dish_details (
    name, description, price, original_price, discount, 
    cuisine_type, category, spiciness_level, is_vegetarian, is_recommended,
    rating, review_count, in_stock, serving_size, preparation_time, calories,
    ingredients_list, allergens, customizable, tags, brand, meal_type, order_count, image
) VALUES
('Paneer Tikka', 'Grilled cottage cheese marinated in spices', 229, 279, 18, 
 'northIndian', 'starters', 2, true, true,
 4.6, 234, true, 'Serves 2', '20 mins', 280,
 'paneer,yogurt,spices,bell peppers', 'dairy', true, 'popular', 'Indian Kitchen', 'snacks', 189,
 'https://images.unsplash.com/photo-1599487488170-d11ec9c172f0'),

('Chicken Tikka', 'Tender chicken pieces marinated and grilled', 259, 299, 13, 
 'northIndian', 'starters', 3, false, true,
 4.7, 289, true, 'Serves 2', '25 mins', 320,
 'chicken,yogurt,spices', 'dairy', true, 'bestseller', 'Indian Kitchen', 'snacks', 245,
 'https://images.unsplash.com/photo-1610057099443-fde8c4d50f91'),

('Samosa (2 pcs)', 'Crispy pastry filled with spiced potatoes', 49, 59, 17, 
 'northIndian', 'starters', 2, true, false,
 4.4, 412, true, 'Serves 1', '10 mins', 150,
 'potato,peas,flour,spices', 'gluten', false, 'popular', 'Indian Kitchen', 'snacks', 567,
 'https://images.unsplash.com/photo-1601050690597-df0568f70950'),

('Veg Spring Rolls', 'Crispy rolls filled with vegetables', 129, 159, 19, 
 'chinese', 'starters', 1, true, false,
 4.3, 178, true, 'Serves 2', '15 mins', 200,
 'cabbage,carrot,flour,spices', 'gluten', false, '', 'Indian Kitchen', 'snacks', 134,
 'https://images.unsplash.com/photo-1534422298391-e4f8c172dddb');

-- Breads
INSERT INTO dish_details (
    name, description, price, original_price, discount, 
    cuisine_type, category, spiciness_level, is_vegetarian, is_recommended,
    rating, review_count, in_stock, serving_size, preparation_time, calories,
    ingredients_list, allergens, customizable, tags, brand, meal_type, order_count, image
) VALUES
('Butter Naan', 'Soft leavened bread brushed with butter', 49, 59, 17, 
 'northIndian', 'breads', 0, true, true,
 4.5, 456, true, '2 pieces', '10 mins', 180,
 'flour,yogurt,butter', 'gluten,dairy', false, 'popular', 'Indian Kitchen', 'lunch', 678,
 'https://images.unsplash.com/photo-1601050690597-df0568f70950'),

('Garlic Naan', 'Naan topped with garlic and coriander', 59, 69, 14, 
 'northIndian', 'breads', 0, true, true,
 4.6, 389, true, '2 pieces', '12 mins', 200,
 'flour,yogurt,garlic,butter', 'gluten,dairy', false, 'bestseller', 'Indian Kitchen', 'lunch', 534,
 'https://images.unsplash.com/photo-1601050690597-df0568f70950'),

('Tandoori Roti', 'Whole wheat flatbread cooked in tandoor', 29, 39, 26, 
 'northIndian', 'breads', 0, true, false,
 4.4, 298, true, '2 pieces', '8 mins', 120,
 'whole wheat flour', 'gluten', false, '', 'Indian Kitchen', 'lunch', 445,
 'https://images.unsplash.com/photo-1601050690597-df0568f70950'),

('Laccha Paratha', 'Layered whole wheat flatbread', 69, 89, 22, 
 'northIndian', 'breads', 0, true, false,
 4.5, 234, true, '2 pieces', '15 mins', 240,
 'whole wheat flour,ghee', 'gluten,dairy', false, 'popular', 'Indian Kitchen', 'lunch', 312,
 'https://images.unsplash.com/photo-1601050690597-df0568f70950');

-- Desserts
INSERT INTO dish_details (
    name, description, price, original_price, discount, 
    cuisine_type, category, spiciness_level, is_vegetarian, is_recommended,
    rating, review_count, in_stock, serving_size, preparation_time, calories,
    ingredients_list, allergens, customizable, tags, brand, meal_type, order_count, image
) VALUES
('Gulab Jamun (2 pcs)', 'Soft milk dumplings in sugar syrup', 79, 99, 20, 
 'northIndian', 'desserts', 0, true, true,
 4.7, 456, true, 'Serves 1', '5 mins', 280,
 'milk solids,flour,sugar,cardamom', 'dairy,gluten', false, 'popular', 'Indian Kitchen', 'dinner', 389,
 'https://images.unsplash.com/photo-1585638693728-0ac5a8d2b1e5'),

('Rasmalai (2 pcs)', 'Cottage cheese dumplings in sweetened milk', 99, 129, 23, 
 'northIndian', 'desserts', 0, true, true,
 4.8, 345, true, 'Serves 1', '5 mins', 320,
 'paneer,milk,sugar,saffron,cardamom', 'dairy', false, 'chef-special', 'Indian Kitchen', 'dinner', 298,
 'https://images.unsplash.com/photo-1631452180519-c014fe946bc7'),

('Kulfi', 'Traditional Indian ice cream', 69, 89, 22, 
 'northIndian', 'desserts', 0, true, false,
 4.6, 267, true, 'Serves 1', '5 mins', 180,
 'milk,sugar,cardamom,pistachios', 'dairy,nuts', false, '', 'Indian Kitchen', 'dinner', 234,
 'https://images.unsplash.com/photo-1563805042-7684c019e1cb');

-- Beverages
INSERT INTO dish_details (
    name, description, price, original_price, discount, 
    cuisine_type, category, spiciness_level, is_vegetarian, is_recommended,
    rating, review_count, in_stock, serving_size, preparation_time, calories,
    ingredients_list, allergens, customizable, tags, brand, meal_type, order_count, image
) VALUES
('Mango Lassi', 'Refreshing yogurt drink with mango', 89, 109, 18, 
 'northIndian', 'beverages', 0, true, true,
 4.7, 389, true, '300ml', '5 mins', 180,
 'yogurt,mango,sugar', 'dairy', false, 'popular', 'Indian Kitchen', 'lunch', 456,
 'https://images.unsplash.com/photo-1623065422902-30a2d299bbe4'),

('Sweet Lassi', 'Traditional sweet yogurt drink', 69, 89, 22, 
 'northIndian', 'beverages', 0, true, true,
 4.5, 298, true, '300ml', '5 mins', 150,
 'yogurt,sugar,cardamom', 'dairy', false, '', 'Indian Kitchen', 'lunch', 345,
 'https://images.unsplash.com/photo-1623065422902-30a2d299bbe4'),

('Masala Chai', 'Spiced Indian tea with milk', 39, 49, 20, 
 'northIndian', 'beverages', 0, true, false,
 4.6, 567, true, '200ml', '5 mins', 80,
 'tea,milk,sugar,spices', 'dairy', false, 'popular', 'Indian Kitchen', 'breakfast', 789,
 'https://images.unsplash.com/photo-1597318112874-f0f7e6a1e8c6'),

('Fresh Lime Soda', 'Refreshing lime drink with soda', 49, 59, 17, 
 'northIndian', 'beverages', 0, true, false,
 4.4, 234, true, '300ml', '5 mins', 60,
 'lime,soda,sugar,salt', '', false, '', 'Indian Kitchen', 'lunch', 298,
 'https://images.unsplash.com/photo-1556679343-c7306c1976bc');

-- Chinese Items
INSERT INTO dish_details (
    name, description, price, original_price, discount, 
    cuisine_type, category, spiciness_level, is_vegetarian, is_recommended,
    rating, review_count, in_stock, serving_size, preparation_time, calories,
    ingredients_list, allergens, customizable, tags, brand, meal_type, order_count, image
) VALUES
('Veg Hakka Noodles', 'Stir-fried noodles with vegetables', 179, 219, 18, 
 'chinese', 'main-course', 2, true, true,
 4.5, 289, true, 'Serves 2', '20 mins', 380,
 'noodles,vegetables,soy sauce,spices', 'gluten,soy', true, 'popular', 'Indian Kitchen', 'dinner', 267,
 'https://images.unsplash.com/photo-1585032226651-759b368d7246'),

('Chicken Fried Rice', 'Fried rice with chicken and vegetables', 219, 269, 19, 
 'chinese', 'rice-biryani', 2, false, true,
 4.6, 234, true, 'Serves 2', '25 mins', 450,
 'rice,chicken,vegetables,soy sauce,spices', 'soy', true, 'bestseller', 'Indian Kitchen', 'dinner', 298,
 'https://images.unsplash.com/photo-1603133872878-684f208fb84b'),

('Veg Manchurian', 'Vegetable balls in spicy Indo-Chinese sauce', 199, 239, 17, 
 'chinese', 'starters', 3, true, true,
 4.5, 198, true, 'Serves 2', '25 mins', 320,
 'vegetables,flour,soy sauce,spices', 'gluten,soy', true, 'popular', 'Indian Kitchen', 'snacks', 234,
 'https://images.unsplash.com/photo-1626804475297-41608ea09aeb');

COMMIT;

-- Display summary
SELECT 
    category,
    COUNT(*) as item_count,
    AVG(price) as avg_price,
    SUM(CASE WHEN is_vegetarian = true THEN 1 ELSE 0 END) as veg_items
FROM dish_details
GROUP BY category
ORDER BY category;
