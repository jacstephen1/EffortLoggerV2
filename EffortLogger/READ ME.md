EffortLoggerV2
CSE360 Final Project EffortLogger V2

Company code is currently set to 012345

[IMPORTANT] SQL DATABASE NOTES [IMPORTANT]
Users will need to setup a mysql database for local storage
Port: 3306
Name: effortlogger_db
Username: root
Password: ELDatabasePassword1!

SQL Database Instructions 
MySQL Database Setup Video -- https://youtu.be/OM4aZJW_Ojs (Credit to Amit Thinks)
1a. Watch the above video to setup SQL
1b. MAKE SURE YOUR DATABASE PASSWORD IS SET TO ELDatabasePassword1!
2. Access your local instance
3. Click on the schemas tab and create a new schema called effortlogger_db
4. Double click on effortlogger_db to select the database, the text should become bold
5. Click the drop down menu under effortlogger_db and right click on tables and click import "Table Data Import Wizard"
6. Import effortlogger_db_users.csv file
7. Right click on the users table and click "alter tables..."
8. Next to user_id checkmark the following boxes: PK, NN, AI
9. Click apply in the bottom right
11a. To check if its working you can go to the query table in type: SELECT * FROM users
11b. Then execute the query which should return the users table

NOTE: The users table no longer contains an admin, an admin account can be created through the sign up page