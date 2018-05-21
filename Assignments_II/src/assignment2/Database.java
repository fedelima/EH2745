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
	public static int createSets(String user, String psswd,ArrayList<Sample> learnSet,ArrayList<Sample> testSet) {
		int N = 0; //Number of attributes per sample (number of buses in the system).
		try {	
			//Connect to specified database.
			jdbcString = "jdbc:mysql://localhost:3306/assignment_2?useSSL=false";
			conn = DriverManager.getConnection(jdbcString, user, psswd);
			query = conn.createStatement();
			
			//Get number of buses in the system.		
			ResultSet NBus = query.executeQuery("SELECT count(*) as NBus FROM substations");
			NBus.next(); //place cursor on the first row.
			N =  Integer.parseInt(NBus.getString("NBus"));//Number of buses in the system.
			
			//Convert tables to matrices.
			String[][] measurements = Table2Matrix("measurements");
			String[][] analog_values = Table2Matrix("analog_values");
			
			//Convert matrices to sets.
			Matrix2Set(measurements,learnSet,N); //Create learn set.
			Matrix2Set(analog_values,testSet,N); //Create test set.	
			
			//Close database objects.			
			query.close(); //Close query.
			conn.close(); //Close connection.
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return N;
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
		
		String[][] rdfid = new String[M][N];
		String[][] name = new String[M][N];
		String[][] time = new String[M][N];
		String[][] sub_rdfid = new String[M][N];
		Double[][] attribute = new Double[M][N];		
		
		for (int i=0; i < I; i+=2) {
			rdfid[m][n] = matrix[i][0];					
			name[m][n] = matrix[i][1];;	
			time[m][n] = matrix[i][2];;
			attribute[m][n] = Double.parseDouble(matrix[i][3]);
			sub_rdfid[m][n] = matrix[i][4];
			n++;		
			if (n >= N){
				set.add(new Sample(rdfid[m],name[m],time[m],attribute[m],sub_rdfid[m]));
				m++; //next sample
				n = 0; //reset attribute pointer
			}
		}
	}	
}
