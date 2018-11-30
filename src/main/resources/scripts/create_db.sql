create database sparrow_db CHARACTER SET utf8 COLLATE utf8_bin; -- Create the new database
FLUSH PRIVILEGES;
drop user 'system'@'localhost';
CREATE USER 'system'@'localhost' IDENTIFIED BY 'Kendo.1900'; -- Creates the user
grant all on sparrow_db.* to 'system'@'localhost'; -- Gives all the privileges to the new user on the newly created database