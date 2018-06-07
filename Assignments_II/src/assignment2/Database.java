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
			jdbcString = "jdbc:mysql://" + host + ":3306/" + database + "?useSSL=false&serverTimezone=UTC";
			conn = DriverManager.getConnection(jdbcString, user, psswd);
			query = conn.createStatement();
			
			//Get number of attributes per sample.		
			ResultSet NBus = query.executeQuery("SELECT count(*) as NBus FROM substations");
			NBus.next(); //place cursor on the first row.
			int N =  2*Integer.parseInt(NBus.getString("NBus")); //Number of attributes per sample.
			
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
			jdbcString = "jdbc:mysql://" + host + ":3306/" + database + "?useSSL=false&serverTimezone=UTC";
			conn = DriverManager.getConnection(jdbcString, user, psswd);
			query = conn.createStatement();
			
			//Clear table if it already exists.
			String deleteTable = "DROP TABLE IF EXISTS " + tableName;
			boolean ResultSet1 = query.execute(deleteTable);
			
			// Create table if it doesn't already exist.			
			String createTable = "CREATE TABLE IF NOT EXISTS " + tableName+ "(" 
		            + "id VARCHAR(50),"  
		            + "v1 DECIMAL(10,4),o1 DECIMAL(10,4)," 
		            + "v2 DECIMAL(10,4),o2 DECIMAL(10,4),"
		            + "v3 DECIMAL(10,4),o3 DECIMAL(10,4),"
		            + "v4 DECIMAL(10,4),o4 DECIMAL(10,4),"
		            + "v5 DECIMAL(10,4),o5 DECIMAL(10,4),"
		            + "v6 DECIMAL(10,4),o6 DECIMAL(10,4),"
		            + "v7 DECIMAL(10,4),o7 DECIMAL(10,4),"
		            + "v8 DECIMAL(10,4),o8 DECIMAL(10,4),"
		            + "v9 DECIMAL(10,4),o9 DECIMAL(10,4),"
		            + "class VARCHAR(50))"; 
			boolean ResultSet2 = query.execute(createTable);
			
			// Insert records into table.
			for (Sample sample : set) {
				String insertTable = "INSERT INTO " + tableName + " VALUES('" 
						+ sample.id + "'," 
						+ sample.attribute[0] + "," + sample.attribute[1] + ","
						+ sample.attribute[2] + "," + sample.attribute[3] + ","
						+ sample.attribute[4] + "," + sample.attribute[5] + ","
						+ sample.attribute[6] + "," + sample.attribute[7] + ","
						+ sample.attribute[8] + "," + sample.attribute[9] + ","
						+ sample.attribute[10] + "," + sample.attribute[11] + ","
						+ sample.attribute[12] + "," + sample.attribute[13] + ","
						+ sample.attribute[14] + "," + sample.attribute[15] + ","
						+ sample.attribute[16] + "," + sample.attribute[17] + ",'"
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
		int M = I/N; //Number of samples. 
		
		int[] id = new int[M];
		double[][] attribute = new double[M][N];
		
		for (int i=0; i < I; i++) {
			id[m] = Integer.parseInt(matrix[i][2]);
			attribute[m][n] = Double.parseDouble(matrix[i][3]);
			n++;		
			if (n >= N){
				set.add(new Sample(id[m], attribute[m], 0));
				m++; //next sample
				n = 0; //reset attribute pointer
			}
		}
	}
}
