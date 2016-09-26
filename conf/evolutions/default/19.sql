# create_oauth_client

# --- !Ups
CREATE TABLE oauth_clients (
    oauth_client_id   binary(16) NOT NULL ,
    oauth_user_guid     binary(16) NOT NULL ,
    client_secret     varchar(255),
    redirect_uri      varchar(255),
    grant_type        varchar(255),
    expires_in        int NOT NULL,
	updater    int(10),
	created_date datetime NOT NULL,
	updated_date timestamp,
    PRIMARY KEY (oauth_client_id)
) ENGINE=InnoDB;

CREATE INDEX oauth_user_guid_index ON oauth_clients(oauth_user_guid);

# --- !Downs
DROP TABLE oauth_client;
