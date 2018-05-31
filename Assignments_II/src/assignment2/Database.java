package assignment2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {	
	// *** VARIABLE DEFINITION ***
	static String jdbcString = null;
	static Connection conn = null;
	static Statement query = null;
	
	//*** BUILD LEARNING AND TEST SET FROM SQL DATA ***
	public static void buildSet(String host, String database,String user, String psswd, ArrayList<Sample> set, String tableName) {
		try {	
			//Connect to specified database.
			jdbcString = "jdbc:mysql://" + host + ":3306/" + database + "?useSSL=false";
			conn = DriverManager.getConnection(jdbcString, user, psswd);
			query = conn.createStatement();
			
			//Get number of buses in the system.		
			ResultSet NBus = query.executeQuery("SELECT count(*) as NBus FROM substations");
			NBus.next(); //place cursor on the first row.
			int N =  Integer.parseInt(NBus.getString("NBus"));//Number of buses in the system.
			
			//Convert tables to matrices.
			String[][] matrix = Table2Matrix(tableName);
			
			//Convert matrices to sets.
			Matrix2Set(matrix,set,N); //Create learn set.
			
			//Close database objects.			
			query.close(); //Close query.
			conn.close(); //Close connection.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	//*** BUILD RESULT SET INTO DATABASE ***
	@SuppressWarnings("unused")
	public static void populateSet(String host, String database,String user, String psswd, ArrayList<Sample> set, String tableName) {
		try {								
			//Connect to specified database.
			jdbcString = "jdbc:mysql://" + host + ":3306/" + database + "?useSSL=false";
			conn = DriverManager.getConnection(jdbcString, user, psswd);
			query = conn.createStatement();
			
			//Clear table if it already exists.
			String deleteTable = "DROP TABLE IF EXISTS " + tableName;
			boolean ResultSet1 = query.execute(deleteTable);
			
			// Create table if it doesn't already exist.			
			String createTable = "CREATE TABLE IF NOT EXISTS " + tableName+ "(" 
		            + "Time VARCHAR(50),"  
		            + "V1 DECIMAL(10,4)," 
		            + "V2 DECIMAL(10,4),"
		            + "V3 DECIMAL(10,4),"
		            + "V4 DECIMAL(10,4),"
		            + "V5 DECIMAL(10,4),"
		            + "V6 DECIMAL(10,4),"
		            + "V7 DECIMAL(10,4),"
		            + "V8 DECIMAL(10,4),"
		            + "V9 DECIMAL(10,4),"
		            + "Class VARCHAR(50))"; 
			boolean ResultSet2 = query.execute(createTable);
			
			// Insert records into table.
			for (Sample sample : set) {
				String insertTable = "INSERT INTO " + tableName + " VALUES('" 
						+ sample.time + "'," 
						+ sample.attribute[0] + ","
						+ sample.attribute[1] + ","
						+ sample.attribute[2] + ","
						+ sample.attribute[3] + ","
						+ sample.attribute[4] + ","
						+ sample.attribute[5] + ","
						+ sample.attribute[6] + ","
						+ sample.attribute[7] + ","
						+ sample.attribute[8] + ",'"
						+ sample.GetState() + "');";
				int RowCount = query.executeUpdate(insertTable);
			}
			
			//Close database objects.			
			query.close(); //Close query.
			conn.close(); //Close connection.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	//*** CREATE MATRIX FROM TABLE ***
	private static String[][] Table2Matrix(String tableName) {
		int i = 0;
		String[][] matrix = null;
		try {
			ResultSet Table = query.executeQuery("SELECT * FROM " + tableName + " ORDER BY time");
			Table.last();
			int R = Table.getRow();
			ResultSetMetaData metadata = Table.getMetaData();
			int C = metadata.getColumnCount();
			Table.beforeFirst();
			matrix = new String[R][C];
			while(Table.next()) {
				for (int j=0; j < C; j++) {
					matrix[i][j] = Table.getString(j+1);
				}
				i++;
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return matrix;
	}
	
	//*** CREATE SET FROM MATRIX ***
	private static void Matrix2Set(String[][] matrix, ArrayList<Sample> set, int N) {
		int m = 0; //Initialize sample counter.
		int n = 0; //Initialize attribute counter.
		int I = matrix.length; //Number of rows.
		int M = I/(2*N); //Number of samples. 
		
		String[] time = new String[M];
		Double[][] attribute = new Double[M][N];		
		
		for (int i=0; i < I; i+=2) {
			time[m] = matrix[i][2];;
			attribute[m][n] = Double.parseDouble(matrix[i][3]);
			n++;		
			if (n >= N){
				set.add(new Sample(time[m], attribute[m]));
				m++; //next sample
				n = 0; //reset attribute pointer
			}
		}
	}
}
