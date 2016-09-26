# create_contract_details

# --- !Ups
CREATE TABLE contract_details (
	id          bigint(20) NOT NULL AUTO_INCREMENT,
	contract_id bigint(20) NOT NULL,
	default_product_id bigint(20),
	app_product_id varchar(255),
	product_name varchar(255),
	product_type varchar(255),
	account_name varchar(255),
	details      text,
	registed_date datetime,
	close_date   date,
	close_scheduled_date date,
	close_reason text,
	status       varchar(100) NOT NULL,
	memo         text,
	is_disabled  boolean,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX contract_id_index ON contract_details(contract_id);
CREATE INDEX status_index ON contract_details(status);
CREATE INDEX is_disabled_index ON contract_details(is_disabled);

# --- !Downs
DROP TABLE contract_details;

