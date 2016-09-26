# create_default_products

# --- !Ups
CREATE TABLE default_product_segments (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	default_product_id bigint(20)NOT NULL,
	unit       varchar(255),
	unit_value DECIMAL(30,2),
	price      DECIMAL(30,2),
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX default_product_id_index ON default_product_segments(default_product_id);


# --- !Downs
DROP TABLE default_product_segments;

