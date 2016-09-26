# create_contract_detail_prices

# --- !Ups
CREATE TABLE contract_detail_prices (
	contract_detail_id          bigint(20) NOT NULL ,
	unit_price  DECIMAL(30,2),
	quantity    int(10),
	discount    DECIMAL(30,2),
	discount_rate DECIMAL(10,2),
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (contract_detail_id)
) ENGINE=InnoDB;

# --- !Downs
DROP TABLE contract_detail_prices;

