-- Add Sample Artworks to AURA Gallery
-- Run this in MySQL Workbench or MySQL CLI after creating your artist accounts

-- First, add a test artist user (you'll need to create one via signup)
-- OR use this if you want to add directly:

-- !!! IMPORTANT: Replace artist_id values with actual user IDs from your database !!!
-- To find user IDs: SELECT id, name, email, role FROM users;

-- For now, these assume artist_id = 1 (first user created)
-- Adjust the artist_id number based on your actual users

INSERT IGNORE INTO artworks (title, artist_id, category, price, medium, description, sold, created_at) VALUES
('Crimson Reverie', 1, 'Abstract', 4800.0, 'Oil on Canvas', 'A passionate exploration of colour and form.', 0, NOW()),
('The Golden Hour', 2, 'Landscape', 3200.0, 'Watercolour', 'Dusk over the Amalfi coast.', 0, NOW()),
('Inner Silence', 3, 'Portrait', 5600.0, 'Charcoal & Pastel', 'A meditation on solitude.', 0, NOW()),
('Fractured Light', 3, 'Abstract', 7200.0, 'Acrylic on Panel', 'Light refracted through geometric forms.', 1, NOW()),
('Terra Memoria', 1, 'Landscape', 2800.0, 'Mixed Media', 'Memory of the Amazonian highlands.', 0, NOW()),
('Algorithm Dreams', 2, 'Digital', 1800.0, 'Digital Print', 'Where code becomes canvas.', 0, NOW()),
('The Watcher', 1, 'Portrait', 9500.0, 'Oil on Linen', 'A gaze that spans centuries.', 0, NOW()),
('Void Form #3', 2, 'Sculpture', 12000.0, 'Bronze Cast', 'Negative space as primary medium.', 0, NOW()),
('Whispered Geometry', 1, 'Abstract', 3400.0, 'Gouache on Paper', 'Subtle forms emerging from silence.', 0, NOW()),
('Aurora Borealis', 3, 'Landscape', 5200.0, 'Acrylic', 'The dance of northern lights.', 0, NOW()),
('Reflection', 1, 'Portrait', 4100.0, 'Charcoal', 'Identity in transition.', 0, NOW()),
('Genesis Code', 3, 'Digital', 2600.0, 'NFT/Digital', 'The moment before creation.', 0, NOW());

-- Verify the data was inserted
SELECT COUNT(*) as total_artworks FROM artworks;
SELECT id, title, artist_id, category, price, sold FROM artworks ORDER BY created_at DESC LIMIT 5;
