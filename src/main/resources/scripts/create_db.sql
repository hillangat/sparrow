DROP database IF EXISTS sparrow_db;
create database IF NOT EXISTS sparrow_db;
FLUSH PRIVILEGES;
drop user 'system'@'localhost';
CREATE USER 'system'@'localhost' IDENTIFIED BY 'Kendo.1900';
grant all on sparrow_db.* to 'system'@'localhost';