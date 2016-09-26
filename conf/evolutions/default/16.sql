# create_invoice_addresses

# --- !Ups
CREATE TABLE invoice_addresses (
	invoice_id         bigint(20),
	company    varchar(255),
	post_code  varchar(255),
	prefecture varchar(255),
	city       varchar(255),
	address1   varchar(255),
	address2   varchar(255),
	address3   varchar(255),
	depertment varchar(255),
	staff      varchar(255),
	staff_email varchar(255),
	phone      varchar(255),
	fax        varchar(255),
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (invoice_id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE invoice_addresses;

