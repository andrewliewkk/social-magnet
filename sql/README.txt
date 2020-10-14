## Deploying Social Magnet SQL DB

1) Import deploy.sql through phpmyadmmin

Things to note about the Database
a) The magnet database contains 14 Tables in total, and some of it has been pre-populated with data
b) Crop and Rank table has been pre-populated with the data given in the csv files. 
c) Member table has two users:
	i)  UserID: apple | Password: apple123 | Xp: 3000 | Gold: 3000
	ii) UserID: kenny | Password: kenny123 | Xp: 0	  | Gold: 50
	
	*apple is of rank Journeyman, whereas kenny is a Novice

d) Plot table has been pre-populated with the number of plots based on the rank of the two users above
e) Inventory Table has pre-populated crops (seeds) for (UserID: apple) which can be used for planting


*** deploy-test.sql is used for our junit testing
