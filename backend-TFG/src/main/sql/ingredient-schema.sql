-- Evitar duplicados
DROP SCHEMA IF EXISTS ingredients CASCADE;

-- Esquema de ingredientes
CREATE SCHEMA ingredients;

-- Extensión para crear identificadores UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS ingredients.IngredientType (
    id          uuid            DEFAULT public.uuid_generate_v1(),
    name        VARCHAR(50)     NOT NULL,

    CONSTRAINT PK_IngredientType PRIMARY KEY (id),
    CONSTRAINT UNIQUE_IngredientType_Name   UNIQUE (name)
);


CREATE TABLE IF NOT EXISTS ingredients.Ingredient (
    id                  uuid        DEFAULT public.uuid_generate_v1(),
    name                VARCHAR     NOT NULL,
    creator             uuid        NOT NULL,
    ingredientType      uuid,

    CONSTRAINT PK_Ingredient PRIMARY KEY (id),
    CONSTRAINT UNIQUE_Ingredient_name       UNIQUE (name),
    CONSTRAINT FK_Ingredient_TO_IngredientType
        FOREIGN KEY (ingredientType) REFERENCES ingredients.IngredientType(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT FK_Ingredient_TO_UserTable
        FOREIGN KEY (creator) REFERENCES users.UserTable(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
