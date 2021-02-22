# MS3
Overview: 
The goal of this program is to parse an CSV file, insert valid records into an SQLite database. All other records are placed
into another CSV called new.csv. A log file is also create to show how many records were read,valid and invalid. 

Installation:
Download the source code as a ZIP file, extract the folder from the ZIP file, finally import the extracted folder into an IDE. 

Approach: All valid records are stored in an array list.One assumption I made was that the after every run the table will be dropped. One instruction was that the same file should be re-runnable and instead of having a very large database with too many duplicates, the table will be dropped. Since the instructions did not mention anything about duplicates I assumed that there will not be any duplicates. I made column C which corresponds to one's email the primary key.Every valid record should have a unique e-mail address.
