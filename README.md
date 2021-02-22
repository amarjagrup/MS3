# MS3
Overview: 
The goal of this program is to parse an CSV file, insert valid records into an SQLite database. All other records are placed
into another CSV called new.csv. A log file is also created to show how many records were read,valid and invalid. 

Installation:
Download the source code as a ZIP file, extract the folder from the ZIP file, finally import the extracted folder into an IDE. For
example in eclipse if you go to file then import and then go to general you can import the folder you want.

Approach: 
*My program will read each record one by one to determine if the record is valid or not. All valid records are stored in an array list.
*One assumption I made was that the after every run the table will be dropped. I assumed that there will not be any duplicates. The same file should be re-runnable and instead of *having a very large database with too many duplicates, the table will be dropped.

