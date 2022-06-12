-- Evitar duplicados
DROP SCHEMA IF EXISTS users CASCADE;

-- Esquema del usuario
CREATE SCHEMA users;

-- Extensión para crear identificadores UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" schema users;

-- User es una palabra reservada.
-- Aprovechando que User es un interfaz, se le pone el prefijo I
CREATE TABLE IF NOT EXISTS users.UserTable (
    id              uuid            DEFAULT users.uuid_generate_v1(),
    nickname        VARCHAR(30)     NOT NULL,
    password        VARCHAR         NOT NULL,
    name            VARCHAR(50)     NOT NULL,
    surname         VARCHAR(50),
    email           VARCHAR(100)    NOT NULL,
    avatar          bytea,
    registerDate    TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    role            VARCHAR         NOT NULL,
    isBannedByAdmin bool            NOT NULL    DEFAULT false,

    CONSTRAINT PK_IUser PRIMARY KEY (id),
    CONSTRAINT UNIQUE_IUser_nickname    UNIQUE (nickname),
    CONSTRAINT UNIQUE_IUser_email       UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS users.PrivateList (
    id                  uuid,
    title               varchar(50),
    description         varchar(100),
    creatorID           uuid,

    CONSTRAINT PK_PrivateList PRIMARY KEY (id),
    CONSTRAINT FK_PrivateList_TO_IUser
        FOREIGN KEY (creatorID) REFERENCES users.UserTable(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
