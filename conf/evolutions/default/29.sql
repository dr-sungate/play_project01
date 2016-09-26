# create_contract_detail_billings

# --- !Ups
CREATE TABLE contract_detail_billings (
	contract_detail_id          bigint(20) NOT NULL ,
	activated_date datetime,
	billing_type varchar(100),
	billing_term int(10),
	first_billing_date date,
	last_billing_date  date,
	next_billing_date  date,
	billing_skip       boolean,
	payment_type   varchar(100),
	invoice_term_type  varchar(100),
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (contract_detail_id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE contract_detail_billings;

