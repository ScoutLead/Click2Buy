CREATE TABLE maker
(
  id   INTEGER      NOT NULL
    CONSTRAINT maker_pkey
    PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX maker_id_uindex
  ON maker (id);

CREATE UNIQUE INDEX maker_name_uindex
  ON maker (name);