CREATE TABLE customer (
	id serial PRIMARY KEY,
	customer_ref text,
	customer_name text,
	address_line_1 text,
	address_line_2 text,
	town text,
	county text,
	country text,
	postcode text,
    last_updated timestamp DEFAULT now() NOT NULL
);

insert into customer(customer_ref, customer_name, address_line_1, address_line_2, town, 
county, country, postcode) values('customer_ref_test', 'customer_name_test', 'address_line_1_test', 
'address_line_2_test', 'town_test', 'county_test', 'country_test', 'postcode_test');
