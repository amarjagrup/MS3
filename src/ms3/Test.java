package ms3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/*This class parses a CSV file, insert valid records into an SQLite database. 
 * All other records are placed into another CSV called new.csv. A log file is also create to show how many records were read,
 * valid and invalid. All valid records are stored in an array list. The array list is of type Test and it takes 10 parameters,
 * which corresponds to the 10 columns in the csv file. Each parameter in Test has it's own accesor method. One assumption I made
 * was that the after every run the table will be dropped. One instruction was that the same file should be re-runnable and instead
 * of having a very large database with too many duplicates, the table will be dropped. Since the instructions did not mention
 * anything about duplicates I assumed that there will not be any duplicates. I made column C which corresponds to one's email 
 * the primary key.Every valid record should have a unique e-mail address.
 */
public class Test {
	static int  rec; // keeps count of all records.
	static int success ; // keeps count of all successful records.
	static int fail ; // keeps count of all failed records.
	String A,B,C,D,E,F,G,H,I,J;
	static boolean flag = false;// used to see if record should be added to array list 
	static ArrayList<Test> arr = new ArrayList<Test>(); // arraylist of all valid records

	/*
	 * The main method runs four methods in succession. The main method will drop the table first
	 * then it will create a new table, then main will execute the parse method followed by the log method.
	 */
	public static void main(String args[]) throws SQLException, IOException {
		dropTable();
		createTable();
		parse();
		log();
	}

	/*
	 * This constructor initializes the 10 variables A to J.
	 */
	Test( String a,String b,String c,String d,String e,String f,String g,String h,String i,String j){
		A=a;
		B=b;
		C=c;
		D=d;
		E=e;
		F=f;
		G=g;
		H=h;
		I=i;
		J=j;
	}
	String getA () {
		return A;
	}
	String getB () {
		return B;
	}
	String getC () {
		return C;
	}
	String getD () {
		return D;
	}
	String getE () {
		return E;
	}
	String getF () {
		return F;
	}
	String getG () {
		return G;
	}
	String getH () {
		return H;
	}
	String getI () {
		return I;
	}
	String getJ () {
		return J;
	}

	/*
	 * This method writes the stats of the records parse from the csv file to a log file called myFile.log
	 */
	public static void log() throws IOException {

		// create a new file with specified file name
		FileWriter fw = new FileWriter("myFile.log");
		fw.append("Total number of records recieved " + rec + "\n");
		fw.append("Total number of records failed " + fail + "\n");
		fw.append("Total number of successful records read " + success);

		fw.flush();
		fw.close();

	}

	/*
	 * This method establishes a connection to the database.
	 */
	public static Connection connection(){
		Connection conn = null;

		try {
			String url = "jdbc:sqlite:test2.db";
			conn = DriverManager.getConnection(url);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;

	}

	/*
	 * This method creates the records tables
	 */
	public static void createTable() {
		// SQLite connection string
		String url = "jdbc:sqlite:test2.db";

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS Records (\n" + "	A TEXT,\n" 
				+ "	B TEXT,\n" 
				+ "	C TEXT Primary key,\n"
				+ "	D TEXT,\n" 
				+ "	E TEXT,\n" 
				+ "	F TEXT,\n" 
				+ "	G TEXT,\n" 
				+ "	H TEXT,\n" 
				+ "	I TEXT,\n"
				+ "	J TEXT" + ");";

		try (Connection conn = DriverManager.getConnection(url);
				Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * This method drops the Records table
	 */
	public static void dropTable() {
		String sql = "DROP TABLE IF EXISTS Records;";

		try (
				Connection conn = connection(); 
				Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}




	/*
	 * This method parses the csv file and stores valid records in the arr array list and writes
	 * the invalid records to the new.csv.  Then parse will call the insertDB() method will insert 
	 * all records into the Records table.
	 */
	public static  void parse() throws SQLException, IOException {
		FileWriter writer = new FileWriter("new.csv");

		String line = "";

		BufferedReader br = new BufferedReader(new FileReader("ms3Interview.csv"));  
		br.readLine(); //skip first line containing the column headers
		while ((line = br.readLine()) != null)    
		{  

			rec++;
			String[] rec = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");   // splits the line based on the regex. 

			//go through the line and if there is an empty value write that value to the 
			//csv file and break out the loop. If the line is valid add it to the arr array list.
			for(int i=0;i<rec.length;i++)
			{
				if(rec[i] == "")
				{					
					writer.append(line);
					writer.append(",");

					writer.append("\n");
					fail++;
					flag = true;
					break;
				}

			}
             
			if(!flag) {
				arr.add(new Test(rec[0],rec[1],rec[2],rec[3],rec[4],rec[5],rec[6],rec[7],rec[8],rec[9]));
				success++;


			}
			flag = false;  
		}   


		writer.flush();
		writer.close();
		insertDB();
	}  

	/*
	 *  This method inserts valid records into the database. 
	 */
	public static  void insertDB() throws SQLException {
		Connection con = connection();
		con.setAutoCommit(false);

		String sql = "INSERT INTO Records (A,B,C,D,E,F,G,H,I,J) VALUES (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement statement = con.prepareStatement(sql);

		//insert each record in array list into database
		for(int i=0; i< arr.size();i++) {

			statement.setString(1, arr.get(i).getA());
			statement.setString(2, arr.get(i).getB());
			statement.setString(3, arr.get(i).getC());
			statement.setString(4, arr.get(i).getD());
			statement.setString(5, arr.get(i).getE());
			statement.setString(6, arr.get(i).getF());
			statement.setString(7, arr.get(i).getG());
			statement.setString(8, arr.get(i).getH());
			statement.setString(9, arr.get(i).getI());
			statement.setString(10, arr.get(i).getJ());


			statement.executeUpdate();
		}
		System.out.println("Records added to database");

		con.commit();

	}
}



