# create_default_products

# --- !Ups
CREATE TABLE default_products (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	agency_id  bigint(20) NOT NULL,
	product_name varchar(255),
	product_type varchar(255),
	price      DECIMAL(30,2),
	memo       text,
	is_disabled     boolean,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX agency_id_index ON default_products(agency_id);
CREATE INDEX is_disabled_index ON default_products(is_disabled);

# --- !Downs
DROP TABLE default_products;

