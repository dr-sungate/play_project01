# create_contract_detail_segment_logs

# --- !Ups
CREATE TABLE contract_detail_segment_logs (
	id         bigint(20) NOT NULL AUTO_INCREMENT,
	client_id  bigint(20) NOT NULL,
	contract_detail_id bigint(20) NOT NULL,
	unit_log   DECIMAL(30,2),
	accounting_from datetime,
	accounting_to   datetime,
	created_date  datetime NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE INDEX client_id_index ON contract_detail_segment_logs(client_id);
CREATE INDEX contract_detail_id_index ON contract_detail_segment_logs(contract_detail_id);

# --- !Downs
DROP TABLE contract_detail_segment_logs;

