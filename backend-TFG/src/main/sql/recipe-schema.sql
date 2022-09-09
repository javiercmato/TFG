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
    category_id         uuid,
    totalVotes          BIGINT          NOT NULL        DEFAULT 0,
    averageRating       FLOAT           NOT NULL        DEFAULT 0,
    version             INT,

    CONSTRAINT PK_Recipe PRIMARY KEY (id),
    CONSTRAINT FK_Recipe_TO_User
        FOREIGN KEY (author) REFERENCES users.usertable(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT FK_Recipe_TO_Category
        FOREIGN KEY (category_id) REFERENCES recipes.category(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS recipes.RecipePicture (
    recipe_id           uuid,
    pictureOrder        INT,                                    -- Ordenación de la foto (order es palabra reservada)
    pictureData         bytea           NOT NULL,

    CONSTRAINT PK_RecipePicture PRIMARY KEY (recipe_id, pictureOrder),
    CONSTRAINT FK_RecipePicture_TO_Recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes.Recipe(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS recipes.RecipeStep (
    recipe_id           uuid,
    step                INT,
    text                TEXT            NOT NULL,

    CONSTRAINT PK_RecipeStep PRIMARY KEY (recipe_id, step),
    CONSTRAINT FK_RecipeStep_TO_Recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes.Recipe(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS recipes.RecipeIngredient (
    recipe_id           uuid,
    ingredient_id       uuid,
    quantity            VARCHAR,
    measureUnit         VARCHAR         NOT NULL,

    CONSTRAINT PK_RecipeIngredient PRIMARY KEY (recipe_id, ingredient_id),
    CONSTRAINT FK_RecipeIngredient_TO_Recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes.Recipe(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_RecipeIngredient_TO_Ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredients.ingredient(id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS recipes.PrivateListRecipe (
    privateList_id         uuid,
    recipe_id           uuid,
    insertionDate       TIMESTAMP       NOT NULL        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_PrivateListRecipe PRIMARY KEY (privateList_id, recipe_id),
    CONSTRAINT FK_PrivateListRecipe_TO_PrivateList
        FOREIGN KEY (privateList_id) REFERENCES users.privatelist(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT FK_PrivateListRecipe_TO_Recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes.Recipe(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);
