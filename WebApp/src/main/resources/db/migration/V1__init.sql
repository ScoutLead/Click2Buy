CREATE TABLE "users" (
  "id" serial NOT NULL,
  "phone" int NOT NULL UNIQUE,
  "surname" varchar(50) NOT NULL,
  "name" varchar(50) NOT NULL,
  "father_name" varchar(50) NOT NULL,
  "password" varchar(50) NOT NULL,
  "city_address" varchar(50) NOT NULL,
  "street_address" varchar(50) NOT NULL,
  "role_id" int NOT NULL,
  "birthday" DATE,
  "creation_date" TIMESTAMP NOT NULL,
  CONSTRAINT users_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "roles" (
  "id" serial NOT NULL,
  "name" varchar NOT NULL UNIQUE,
  CONSTRAINT roles_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "products" (
  "id" serial NOT NULL,
  "creation_date" TIMESTAMP NOT NULL,
  "name" varchar(50) NOT NULL UNIQUE,
  "maker" int NOT NULL,
  "maker_country" int NOT NULL,
  "rating" float4 NOT NULL,
  "count" int NOT NULL,
  "count_of_selled" int NOT NULL,
  "price" money NOT NULL,
  "currency" varchar(5) NOT NULL,
  CONSTRAINT products_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "categories" (
  "id" int NOT NULL,
  "name" varchar(25) NOT NULL UNIQUE,
  "parent" int,
  CONSTRAINT categories_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "languages" (
  "id" serial NOT NULL,
  "name" varchar(5) NOT NULL,
  CONSTRAINT languages_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "product_translatings" (
  "product_id" int NOT NULL,
  "lang_id" int NOT NULL,
  "description" TEXT NOT NULL,
  "characteristics" json NOT NULL,
  CONSTRAINT product_translatings_pk PRIMARY KEY ("product_id","lang_id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "products_in_categories" (
  "product_id" int NOT NULL,
  "category_id" int NOT NULL,
  CONSTRAINT products_in_categories_pk PRIMARY KEY ("product_id","category_id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "wishes" (
  "user_id" int NOT NULL,
  "product_id" int NOT NULL,
  CONSTRAINT wishes_pk PRIMARY KEY ("user_id","product_id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "reviews" (
  "user_id" int NOT NULL,
  "product_id" int NOT NULL,
  "advantages" TEXT,
  "disadvantages" TEXT,
  "description" TEXT NOT NULL,
  "date" TIMESTAMP NOT NULL,
  "count_of_likes" int NOT NULL,
  "count_of_dislikes" int NOT NULL,
  "product_rating" int NOT NULL,
  "is_recommended" bit NOT NULL,
  "is_published" bit NOT NULL,
  CONSTRAINT reviews_pk PRIMARY KEY ("user_id","product_id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "questions" (
  "id" serial NOT NULL,
  "user_id" int NOT NULL,
  "product_id" int NOT NULL,
  "question" TEXT NOT NULL,
  "count_of_likes" int NOT NULL,
  "count_of_dislikes" int NOT NULL,
  "date" TIMESTAMP NOT NULL,
  "is_published" bit NOT NULL,
  CONSTRAINT questions_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "answers" (
  "id" serial NOT NULL,
  "user_id" int NOT NULL,
  "question_id" int NOT NULL,
  "answer" TEXT NOT NULL,
  "date" TIMESTAMP NOT NULL,
  CONSTRAINT answers_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "product_photos" (
  "id" serial NOT NULL,
  "path" varchar(50) NOT NULL,
  "product_id" int NOT NULL,
  "isMain" bit NOT NULL,
  CONSTRAINT product_photos_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "characteristic_tags" (
  "id" serial NOT NULL,
  "name" varchar(50) NOT NULL,
  CONSTRAINT characteristic_tags_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "headers" (
  "id" serial NOT NULL,
  "name" varchar(50) NOT NULL,
  CONSTRAINT headers_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "characteristics_headers" (
  "charecteristic_id" int NOT NULL,
  "header_id" int NOT NULL,
  CONSTRAINT characteristics_headers_pk PRIMARY KEY ("charecteristic_id","header_id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "product_characteristics" (
  "product_id" int NOT NULL,
  "characteristic_id" int NOT NULL,
  "lang_id" int NOT NULL,
  "value" TEXT NOT NULL,
  CONSTRAINT product_characteristics_pk PRIMARY KEY ("product_id","characteristic_id","lang_id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "action_translatings" (
  "name" varchar(50) NOT NULL,
  "description" TEXT NOT NULL,
  "action_id" int NOT NULL,
  "lang_id" int NOT NULL,
  CONSTRAINT action_translatings_pk PRIMARY KEY ("action_id","lang_id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "actions" (
  "id" serial NOT NULL,
  "start" TIMESTAMP NOT NULL,
  "end" TIMESTAMP NOT NULL,
  "image_path" varchar(50) NOT NULL,
  CONSTRAINT actions_pk PRIMARY KEY ("id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "products_in_action" (
  "product_id" int NOT NULL,
  "action_id" int NOT NULL,
  "sale" int2 NOT NULL,
  CONSTRAINT products_in_action_pk PRIMARY KEY ("product_id","action_id")
) WITH (
OIDS=FALSE
);



CREATE TABLE "presents" (
  "product_id" int NOT NULL,
  "action_id" int NOT NULL,
  CONSTRAINT presents_pk PRIMARY KEY ("product_id","action_id")
) WITH (
OIDS=FALSE
);



ALTER TABLE "users" ADD CONSTRAINT "users_fk0" FOREIGN KEY ("role_id") REFERENCES "roles"("id");



ALTER TABLE "categories" ADD CONSTRAINT "categories_fk0" FOREIGN KEY ("parent") REFERENCES "categories"("id");


ALTER TABLE "product_translatings" ADD CONSTRAINT "product_translatings_fk0" FOREIGN KEY ("product_id") REFERENCES "products"("id");
ALTER TABLE "product_translatings" ADD CONSTRAINT "product_translatings_fk1" FOREIGN KEY ("lang_id") REFERENCES "languages"("id");

ALTER TABLE "products_in_categories" ADD CONSTRAINT "products_in_categories_fk0" FOREIGN KEY ("product_id") REFERENCES "products"("id");
ALTER TABLE "products_in_categories" ADD CONSTRAINT "products_in_categories_fk1" FOREIGN KEY ("category_id") REFERENCES "categories"("id");

ALTER TABLE "wishes" ADD CONSTRAINT "wishes_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("id");
ALTER TABLE "wishes" ADD CONSTRAINT "wishes_fk1" FOREIGN KEY ("product_id") REFERENCES "products"("id");

ALTER TABLE "reviews" ADD CONSTRAINT "reviews_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("id");
ALTER TABLE "reviews" ADD CONSTRAINT "reviews_fk1" FOREIGN KEY ("product_id") REFERENCES "products"("id");

ALTER TABLE "questions" ADD CONSTRAINT "questions_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("id");
ALTER TABLE "questions" ADD CONSTRAINT "questions_fk1" FOREIGN KEY ("product_id") REFERENCES "products"("id");

ALTER TABLE "answers" ADD CONSTRAINT "answers_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("id");
ALTER TABLE "answers" ADD CONSTRAINT "answers_fk1" FOREIGN KEY ("question_id") REFERENCES "questions"("id");

ALTER TABLE "product_photos" ADD CONSTRAINT "product_photos_fk0" FOREIGN KEY ("product_id") REFERENCES "products"("id");



ALTER TABLE "characteristics_headers" ADD CONSTRAINT "characteristics_headers_fk0" FOREIGN KEY ("charecteristic_id") REFERENCES "characteristic_tags"("id");
ALTER TABLE "characteristics_headers" ADD CONSTRAINT "characteristics_headers_fk1" FOREIGN KEY ("header_id") REFERENCES "headers"("id");

ALTER TABLE "product_characteristics" ADD CONSTRAINT "product_characteristics_fk0" FOREIGN KEY ("product_id") REFERENCES "products"("id");
ALTER TABLE "product_characteristics" ADD CONSTRAINT "product_characteristics_fk1" FOREIGN KEY ("characteristic_id") REFERENCES "characteristic_tags"("id");
ALTER TABLE "product_characteristics" ADD CONSTRAINT "product_characteristics_fk2" FOREIGN KEY ("lang_id") REFERENCES "languages"("id");

ALTER TABLE "action_translatings" ADD CONSTRAINT "action_translatings_fk0" FOREIGN KEY ("action_id") REFERENCES "actions"("id");
ALTER TABLE "action_translatings" ADD CONSTRAINT "action_translatings_fk1" FOREIGN KEY ("lang_id") REFERENCES "languages"("id");


ALTER TABLE "products_in_action" ADD CONSTRAINT "products_in_action_fk0" FOREIGN KEY ("product_id") REFERENCES "products"("id");
ALTER TABLE "products_in_action" ADD CONSTRAINT "products_in_action_fk1" FOREIGN KEY ("action_id") REFERENCES "actions"("id");

ALTER TABLE "presents" ADD CONSTRAINT "presents_fk0" FOREIGN KEY ("product_id") REFERENCES "products"("id");
ALTER TABLE "presents" ADD CONSTRAINT "presents_fk1" FOREIGN KEY ("action_id") REFERENCES "actions"("id");

INSERT INTO roles VALUES (1, 'admin')