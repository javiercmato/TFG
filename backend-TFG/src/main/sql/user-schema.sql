-- Evitar duplicados
DROP SCHEMA IF EXISTS users CASCADE;

-- Extensión para crear identificadores UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Esquema del usuario
CREATE SCHEMA users;


-- User es una palabra reservada.
CREATE TABLE IF NOT EXISTS users.UserTable (
    id              uuid            DEFAULT uuid_generate_v1(),
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
    id                  uuid            DEFAULT uuid_generate_v1(),
    title               varchar(50)     NOT NULL,
    description         varchar(100),
    creatorID           uuid,

    CONSTRAINT PK_PrivateList PRIMARY KEY (id),
    CONSTRAINT FK_PrivateList_TO_IUser
        FOREIGN KEY (creatorID) REFERENCES users.UserTable(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);



-- ******************** DATOS POR DEFECTO ********************
-- Contraseña del administrador: secretPassword
INSERT INTO users.UserTable (nickname, password, name, surname, email, role) VALUES ('admin', '$2a$10$T.xjZnNTCmZzMMTQbUfac.k3wNsHe4mhVhxEH0kLMn8TqE21vKbTK', 'Administrador', 'Sistema', 'admin@admin.es', 'ADMIN');
