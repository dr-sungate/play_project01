# create_contract_detail_segments

# --- !Ups
CREATE TABLE contract_detail_segments (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	contract_detail_id bigint(20) NOT NULL,
	unit       varchar(255),
	unit_value DECIMAL(30,2),
	price      DECIMAL(30,2),
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date	timestamp,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX contract_detail_id_index ON contract_detail_segments(contract_detail_id);

# --- !Downs
DROP TABLE contract_detail_segments;

