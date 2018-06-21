CREATE TABLE public.regions_novaposhta
(
  name VARCHAR(255) NOT NULL,
  reference VARCHAR(255),
  areas_center VARCHAR(255),
  updated_date_time TIMESTAMP,
  id INT PRIMARY KEY NOT NULL
);
CREATE UNIQUE INDEX regions_novaposhta_name_uindex ON public.regions_novaposhta (name);
CREATE UNIQUE INDEX regions_novaposhta_id_uindex ON public.regions_novaposhta (id);

CREATE TABLE public.settlements_novaposhta
(
  name VARCHAR(255) NOT NULL,
  reference VARCHAR(255),
  areas_center VARCHAR(255),
  updated_date_time TIMESTAMP,
  region_reference VARCHAR(255),
  region_id INT,
  type VARCHAR(255),
  warehouse BOOLEAN NOT NULL DEFAULT FALSE,
  id INT PRIMARY KEY NOT NULL
);
CREATE UNIQUE INDEX settlements_novaposhta_name_uindex ON public.settlements_novaposhta (reference);
CREATE UNIQUE INDEX settlements_novaposhta_id_uindex ON public.settlements_novaposhta (id);

CREATE TABLE public.warehouse_novaposhta
(
  id INT PRIMARY KEY NOT NULL,
  site_key INT NOT NULL,
  name VARCHAR(255),
  updated_date_time TIMESTAMP,
  city_reference VARCHAR(255),
  settlement_id INT
);
CREATE UNIQUE INDEX warehouse_novaposhta_siteKey_uindex ON public.warehouse_novaposhta (site_key);
CREATE UNIQUE INDEX warehouse_novaposhta_id_uindex ON public.warehouse_novaposhta (id);