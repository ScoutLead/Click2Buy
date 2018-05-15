DROP TABLE IF EXISTS products_in_categories;
ALTER TABLE public.products ADD category_id INTEGER NULL;
ALTER TABLE public.products
  ADD CONSTRAINT products_categories_id_fk
FOREIGN KEY (category_id) REFERENCES categories (id);

