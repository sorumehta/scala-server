CREATE TABLE users
(
    user_id  INTEGER PRIMARY KEY,
    email    TEXT NOT NULL UNIQUE,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    bio      TEXT,
    image    TEXT
);
