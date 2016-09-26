# create_payment_histories

# --- !Ups
CREATE TABLE payment_histories (
	id          bigint(20) NOT NULL AUTO_INCREMENT,
	client_id   bigint(20) NOT NULL,
	contract_detail_id bigint(20),
	app_product_id varchar(255),
	product_name   varchar(255),
	price       DECIMAL(30,2),
	tax_rate    DECIMAL(10,2),
	taxed_price DECIMAL(30,2),
	payment_date datetime,
	payment_issue_type varchar(100),
	payment_status varchar(100),
	accounting_from datetime,
	accounting_to datetime,
	details     text,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE payment_histories;

