CREATE TABLE IF NOT EXISTS users(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    login VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS friends(
    user_id INTEGER REFERENCES users(id),
    friend_id INTEGER REFERENCES users(id),
    status VARCHAR(100),
    CONSTRAINT friends_PK PRIMARY KEY (user_id, friend_id)
    );

CREATE TABLE IF NOT EXISTS genres(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS ratings(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS films(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    rating_id INTEGER REFERENCES ratings(id)
);

CREATE TABLE IF NOT EXISTS films_genres(
    genre_id INTEGER REFERENCES genres(id),
    film_id INTEGER REFERENCES films(id),
    CONSTRAINT films_genres_PK PRIMARY KEY (genre_id, film_id)
    );


CREATE TABLE IF NOT EXISTS likes(
    like_id INTEGER REFERENCES users(id),
    film_id INTEGER REFERENCES films(id),
    CONSTRAINT films_likes_PK PRIMARY KEY (like_id, film_id)
);