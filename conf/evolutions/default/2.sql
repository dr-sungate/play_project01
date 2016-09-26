# create_account

# --- !Ups
INSERT INTO accounts (id, login_id, password, name, role,is_disabled, created_date) VALUES(1, 'admin', '$2a$10$NFvU2nGb0fPK6.R/mNji5.tavHU/HGuoOoLuZIaykCAtqGUfrA.fi', '管理者', 'Administrator', 0, now());

# --- !Downs

