PRAGMA encoding = 'UTF-8';

-- users table
-- -----------------
CREATE TABLE users
(
    user_id    INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL
);

-- Index on users names
CREATE INDEX users_names ON users (first_name, last_name);

-- User insertion
INSERT INTO users (first_name, last_name)
VALUES ('Tutu', 'Toto');

-- VIRTUAL TABLE FOR FULL TEXT SEARCH
-- ----------------------------------

-- Search table for full text search on the indexed datas
CREATE VIRTUAL TABLE edulearn_full_text_search
    USING FTS5
(
    type,        -- The human readable type (Ulilisateur, Cours...)
    title,       -- The title of the resource (user name, course name)
    description, -- Some supplementary search friendly information
    resource_table_name
    UNINDEXED,   -- Resource table name
    resource_id
    UNINDEXED,   -- The id of the ressource to retrieve it
    prefix=
    1,
    prefix=
    2
);

-- Drop the virtual table
DROP TABLE IF EXISTS edulearn_full_text_search;

-- Inserting on the search virtual table
INSERT INTO edulearn_full_text_search
VALUES ('Utilisateur',
        'Jean Phil-ai-mon-pantalon',
        'Découvre des cours de java et python.',
        'users',
        '1');
/*
SELECT highlight(edulearn_full_text_search, 0, '<b>', '</b>') AS type,
       highlight(edulearn_full_text_search, 1, '<b>', '</b>') AS title,
       highlight(edulearn_full_text_search, 2, '<b>', '</b>'),
       resource_table_name,
       resource_id
FROM edulearn_full_text_search
WHERE edulearn_full_text_search MATCH '"Maxime" *'
ORDER BY bm25(edulearn_full_text_search, 5, 10, 5);*/


DELETE
FROM edulearn_full_text_search
WHERE title = 'Bryan Gomes';