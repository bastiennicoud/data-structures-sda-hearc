-- users table
-- -----------------
CREATE TABLE users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL
);

-- Index on users names
CREATE INDEX users_names ON users (first_name, last_name);

-- Search table for full text search on the indexed datas
CREATE VIRTUAL TABLE full_text_search_index
    USING FTS5(
                  type, -- The human readable type (Ulilisateur, Cours...)
                  title, -- The title of the resource (user name, course name)
                  description, -- Some supplementary search friendly information
                  resource_table_name UNINDEXED,
                  resource_id UNINDEXED
);

SELECT *
FROM full_text_search_index
WHERE title MATCH 'test'
ORDER BY rank;

INSERT INTO full_text_search_index
VALUES (
        'Utilisateur',
        'Bastien Nicoud',
        'Status de lutilisateur',
        'users',
        '4'
        );