CREATE USER payments WITH PASSWORD 'payments';
CREATE DATABASE payments WITH OWNER payments;

CREATE USER audit WITH PASSWORD 'audit';
CREATE DATABASE audit WITH OWNER audit;


ALTER SYSTEM SET max_connections = 100;

SELECT pg_reload_conf();
