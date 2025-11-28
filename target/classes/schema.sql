CREATE TABLE IF NOT EXISTS genres (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS groups (
                                      id SERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      genre_id INT REFERENCES genres(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS songs (
                                     id SERIAL PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
                                     group_id INT REFERENCES groups(id) ON DELETE CASCADE,
                                     duration_seconds INT
);

CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(255) NOT NULL,
                                     role VARCHAR(20) NOT NULL
);