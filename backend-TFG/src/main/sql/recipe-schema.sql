-- Evitar duplicados
DROP SCHEMA IF EXISTS recipes CASCADE;

-- Esquema de recetas
CREATE SCHEMA recipes;

-- Extensión para crear identificadores UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS recipes.Category (
    id          uuid            DEFAULT public.uuid_generate_v1(),
    name        VARCHAR(50)     NOT NULL,

    CONSTRAINT PK_Category PRIMARY KEY (id),
    CONSTRAINT UNIQUE_Category_Name UNIQUE (name)
);


CREATE TABLE IF NOT EXISTS recipes.Recipe (
    id                  uuid            DEFAULT public.uuid_generate_v1(),
    name                VARCHAR(100)    NOT NULL,
    description         TEXT,
    creationDate        TIMESTAMP       NOT NULL        DEFAULT CURRENT_TIMESTAMP,
    duration            BIGINT          NOT NULL,
    diners              INT,
    author              uuid,
    isBannedByAdmin     bool            NOT NULL        DEFAULT false,

    CONSTRAINT PK_Recipe PRIMARY KEY (id),
    CONSTRAINT FK_Recipe_TO_User
        FOREIGN KEY (author) REFERENCES users.usertable(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS recipes.RecipePicture (
    recipe              uuid,
    placement           INT,                                    -- Ordenación de la foto (order es palabra reservada)
    pictureData         bytea           NOT NULL,

    CONSTRAINT PK_RecipePicture PRIMARY KEY (recipe, placement),
    CONSTRAINT FK_RecipePicture_TO_Recipe
        FOREIGN KEY (recipe) REFERENCES recipes.Recipe(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS recipes.RecipeStep (
    recipe              uuid,
    step                INT,
    text                TEXT            NOT NULL,

    CONSTRAINT PK_RecipeStep PRIMARY KEY (recipe, step),
    CONSTRAINT FK_RecipeStep_TO_Recipe
        FOREIGN KEY (recipe) REFERENCES recipes.Recipe(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS recipes.RecipeIngredient (
    recipe              uuid,
    ingredient          uuid,
    quantity            VARCHAR,
    measureUnit         VARCHAR         NOT NULL,

    CONSTRAINT PK_RecipeIngredient PRIMARY KEY (recipe, ingredient),
    CONSTRAINT FK_RecipeIngredient_TO_Recipe
        FOREIGN KEY (recipe) REFERENCES recipes.Recipe(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_RecipeIngredient_TO_Ingredient
        FOREIGN KEY (ingredient) REFERENCES ingredients.ingredient(id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS recipes.PrivateListRecipe (
    privateList         uuid,
    recipe              uuid,
    quantity            VARCHAR,
    measureUnit         VARCHAR         NOT NULL,

    CONSTRAINT PK_PrivateListRecipe PRIMARY KEY (privateList, recipe),
    CONSTRAINT FK_PrivateListRecipe_TO_PrivateList
        FOREIGN KEY (privateList) REFERENCES users.usertable(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT FK_PrivateListRecipe_TO_Recipe
        FOREIGN KEY (recipe) REFERENCES recipes.Recipe(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);
