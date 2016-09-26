# create_billing_addresses

# --- !Ups
CREATE TABLE billing_addresses (
	billing_destination_id  bigint(20) NOT NULL,
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
	PRIMARY KEY (billing_destination_id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE billing_addresses;

