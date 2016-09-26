# create_oauth_user

# --- !Ups
CREATE TABLE oauth_users (
    guid         binary(16) NOT NULL,
 	application_id bigint(20) NOT NULL,
	agency_id  bigint(20),
	client_id  bigint(20),
	name         varchar(255) NOT NULL,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date timestamp,
    PRIMARY KEY (guid)
) ENGINE=InnoDB;

CREATE INDEX application_id_index ON oauth_users(application_id);
CREATE INDEX agency_id_index ON oauth_users(agency_id);
CREATE INDEX client_id_index ON oauth_users(client_id);


# --- !Downs
DROP TABLE oauth_user;

