ALTER TABLE public.product_photos DROP ismain;
ALTER TABLE public.product_photos ADD is_main BOOLEAN NULL;

