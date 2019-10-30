
-- Default Data for tests
INSERT INTO login(email, username, password, deleted)
VALUES('admin@xx.xx', 'admin', 'admin', FALSE);

INSERT INTO login(email, username, password, deleted)
VALUES('user@xx.xx', 'user', 'user', FALSE);

INSERT INTO login(email, username, password, deleted)
VALUES('deleted@xx.xx', 'deleted', 'deleted', TRUE);
