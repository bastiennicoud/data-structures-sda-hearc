-- users table
-- -----------------
CREATE TABLE users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL
);

-- Index on users names
CREATE INDEX users_names ON users (first_name, last_name);