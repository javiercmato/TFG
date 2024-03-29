-- Evitar duplicados
DROP SCHEMA IF EXISTS social CASCADE;

-- Esquema social
CREATE SCHEMA social;

-- Extensión para crear identificadores UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS social.Comment (
    id                  uuid            DEFAULT "public".uuid_generate_v1(),
    author_id           uuid,
    recipe_id           uuid,
    creationDate        TIMESTAMP       NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    text                TEXT            NOT NULL,
    isBannedByAdmin     bool            NOT NULL        DEFAULT false,

    CONSTRAINT PK_Comment PRIMARY KEY (id),
    CONSTRAINT FK_Comment_TO_User FOREIGN KEY (author_id) REFERENCES users.usertable(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT FK_Comment_TO_Recipe FOREIGN KEY (recipe_id) REFERENCES recipes.recipe(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS social.Rating (
    author_id           uuid,
    recipe_id           uuid,
    rating              INT            NOT NULL,

    CONSTRAINT PK_Rating PRIMARY KEY (author_id, recipe_id),
    CONSTRAINT FK_Rating_TO_User FOREIGN KEY (author_id) REFERENCES users.usertable(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_Rating_TO_Recipe FOREIGN KEY (recipe_id) REFERENCES recipes.recipe(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS social.Follow (
    -- Following sigue al usurio Followed
    following_id            uuid,           -- Usuario que empieza a seguir
    followed_id             uuid,           -- Usuario al que se ha seguido,
    followDate              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_Follow PRIMARY KEY (following_id, followed_id),
    CONSTRAINT FK_Following_TO_User FOREIGN KEY (following_id) REFERENCES users.usertable(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_Followed_TO_User FOREIGN KEY (following_id) REFERENCES users.usertable(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS social.Notification (
    id                  uuid        DEFAULT "public".uuid_generate_v1(),
    isRead              bool        NOT NULL    DEFAULT false,
    createdAt           TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    title               varchar     NOT NULL,
    message             varchar     NOT NULL,
    target_id           uuid        NOT NULL,

    CONSTRAINT PK_Notification PRIMARY KEY (id),
    CONSTRAINT FK_Notification_TO_User FOREIGN KEY (target_id) REFERENCES users.usertable(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
