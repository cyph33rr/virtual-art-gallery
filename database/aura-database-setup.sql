-- ╔══════════════════════════════════════════════════════════════╗
-- ║           AURA GALLERY — MySQL Setup Script                 ║
-- ║  Run this in MySQL Workbench or your MySQL CLI              ║
-- ╚══════════════════════════════════════════════════════════════╝

-- 1. Create the database
CREATE DATABASE IF NOT EXISTS aura_gallery
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
    

USE aura_gallery;


-- 2. Create a dedicated user (safer than using root)
--    Replace 'yourpassword' with a strong password
CREATE USER 'aura_user'@'localhost' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON aura_gallery.* TO 'aura_user'@'localhost';
FLUSH PRIVILEGES;

-- 3. Tables are auto-created by Hibernate (spring.jpa.hibernate.ddl-auto=update)
--    But you can pre-create them manually here if preferred:

CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)        NOT NULL,
    email       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255)        NOT NULL,
    role        ENUM('ARTIST','COLLECTOR') NOT NULL
);

CREATE TABLE IF NOT EXISTS artworks (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255)        NOT NULL,
    artist_id   BIGINT              NOT NULL,
    category    ENUM('Abstract','Landscape','Portrait','Sculpture','Digital') NOT NULL,
    price       DOUBLE              NOT NULL,
    medium      VARCHAR(255),
    description TEXT,
    image_path  VARCHAR(512),
    sold        TINYINT(1) DEFAULT 0 NOT NULL,
    created_at  DATETIME            NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    buyer_id     BIGINT  NOT NULL,
    total_amount DOUBLE  NOT NULL,
    ordered_at   DATETIME NOT NULL,
    FOREIGN KEY (buyer_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS order_artworks (
    order_id   BIGINT NOT NULL,
    artwork_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, artwork_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (artwork_id) REFERENCES artworks(id)
);

-- 4. (Optional) Seed some sample data
INSERT IGNORE INTO users (name, email, password, role) VALUES
    ('Elena Voss', 'elena@aura.art', '$2a$10$example_bcrypt_hash_here', 'ARTIST'),
    ('Marcus Thein', 'marcus@aura.art', '$2a$10$example_bcrypt_hash_here', 'ARTIST');

-- To generate a real bcrypt hash, use the app's signup endpoint.
-- Sample artwork insert (after users exist):
-- INSERT INTO artworks (title, artist_id, category, price, medium, description, sold, created_at)
-- VALUES ('Crimson Reverie', 1, 'Abstract', 4800, 'Oil on Canvas', 'A passionate piece.', 0, NOW());

-- 5. Verify tables created
SHOW TABLES;
