package assignment2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
	//*** BUILD LEARNING AND TEST SET FROM SQL DATA ***
	public static int createSets(String user, String psswd,ArrayList<Sample> learnSet,ArrayList<Sample> testSet) {
		int N = 0; //Number of attributes per sample (number of buses in the system).
		try {
			//Connect to database.			
			String jdbcString = "jdbc:mysql://localhost:3306/assignment_2?useSSL=false";
			Connection conn = DriverManager.getConnection(jdbcString, user, psswd);
			Statement query = conn.createStatement();
			
			//Initialize variables.		
			ResultSet NBus = query.executeQuery("SELECT count(*) as NBus FROM substations");
			NBus.next(); //place on the first row
			N =  Integer.parseInt(NBus.getString("NBus"));//Number of buses in the system.
			int i = 0; //Initialize counter i.
			int j = 0; //Initialize counter j.
			String[] rdfid = new String[N];
			String[] name = new String[N];
			String[] time = new String[N];
			String[] sub_rdfid = new String[N];
			Double[] attribute = new Double[N];

			//Create learning set.
			ResultSet measurementsTable = query.executeQuery("SELECT * FROM measurements ORDER BY time");
			while(measurementsTable.next()) {				
				if (i < 2*N-1){					
					if (i%2 == 0){
						rdfid[j] = measurementsTable.getString("rdfid");
						name[j] = measurementsTable.getString("name");	
						time[j] = measurementsTable.getString("time");
						attribute[j] = Double.parseDouble(measurementsTable.getString("value"));
						sub_rdfid[j] = measurementsTable.getString("sub_rdfid");
						j++;
					}
					i++;
				}else {
					learnSet.add(new Sample(rdfid,name,time,attribute,sub_rdfid));
					i=0;
					j=0;
				}
			}
			
			//Create test set.
			ResultSet analogValuesTable = query.executeQuery("SELECT * FROM analog_values ORDER BY time");
			while(analogValuesTable.next()) {				
				if (i < 2*N-1){					
					if (i%2 == 0){
						rdfid[j] = analogValuesTable.getString("rdfid");
						name[j] = analogValuesTable.getString("name");	
						time[j] = analogValuesTable.getString("time");
						attribute[j] = Double.parseDouble(analogValuesTable.getString("value"));
						sub_rdfid[j] = analogValuesTable.getString("sub_rdfid");
						j++;
					}
					i++;
				}else {
					testSet.add(new Sample(rdfid,name,time,attribute,sub_rdfid));
					i=0;
					j=0;
				}
			}
			
			//Close database objects.			
			query.close(); //Close query.
			conn.close(); //Close connection.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return N;
	}
}
