# create_invoice_details

# --- !Ups
CREATE TABLE invoice_details (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	invoice_id bigint(20) NOT NULL ,
	unit_price DECIMAL(30,2),
	quantity   int(10),
	product_name varchar(255),
	product_type varchar(255),
	account_name varchar(255),
	details      text,
	bill_term_from date,
	bill_term_to date,
	memo         text,
	is_disabled boolean,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX invoice_id_index ON invoice_details(invoice_id);
CREATE INDEX is_disabled_index ON invoice_details(is_disabled);

# --- !Downs
DROP TABLE invoice_details;

