# create_contract_billing_destination

# --- !Ups
CREATE TABLE contract_billing_destination (
	contract_id    bigint(20)  NOT NULL,
	billing_destination_id bigint(20) NOT NULL,
	updater    int(10),
	created_date datetime NOT NULL,
	PRIMARY KEY (contract_id, billing_destination_id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE contract_billing_destination;

