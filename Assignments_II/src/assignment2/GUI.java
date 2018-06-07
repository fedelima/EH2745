package assignment2;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GUI {

	private JFrame frmSmartPowerSystem;
	private JTextField txtUser;
	private JPasswordField txtPassword;
	private JTextField txtDatabase;
	private JTextField txtHost;
	private JTextField txtLearnSet;
	private JTextField txtTestSet;
	private JTable tblSets;
	
	JLabel lblCentroid1, lblCentroid2, lblCentroid3, lblCentroid4;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmSmartPowerSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public GUI() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frmSmartPowerSystem = new JFrame();
		frmSmartPowerSystem.setTitle("Smart Power System State Classification");
		frmSmartPowerSystem.setBounds(100, 100, 1169, 533);
		frmSmartPowerSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSmartPowerSystem.getContentPane().setLayout(null);
		
		txtUser = new JTextField();
		txtUser.setBounds(93, 93, 86, 20);
		txtUser.setText("root");
		txtUser.setColumns(10);
		frmSmartPowerSystem.getContentPane().add(txtUser);
		
		JLabel label = new JLabel("DB User:");
		label.setBounds(20, 96, 52, 14);
		frmSmartPowerSystem.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("DB Psswd:");
		label_1.setBounds(20, 123, 63, 14);
		frmSmartPowerSystem.getContentPane().add(label_1);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(93, 120, 86, 20);
		txtPassword.setText("xxxx");
		frmSmartPowerSystem.getContentPane().add(txtPassword);
		
		JLabel label_2 = new JLabel("Database Credentials:");
		label_2.setBounds(20, 11, 146, 14);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		frmSmartPowerSystem.getContentPane().add(label_2);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(20, 458, 1110, 25);
		btnExecute.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				//Define variables.
				ArrayList<Sample> learnSet = new ArrayList<Sample>();
				ArrayList<Sample> testSet = new ArrayList<Sample>();
				ArrayList<Sample> centroids = new ArrayList<Sample>();
				
				//Capture values from GUI.
				String host = txtHost.getText();
				String database = txtDatabase.getText();
				String user = txtUser.getText();				
				String psswd = txtPassword.getText();
				String lsname = txtLearnSet.getText();
				String tsname = txtTestSet.getText();
				Assignment_II.execute(learnSet, testSet, centroids, host, database, user, psswd, lsname, tsname); //execute main routine.
				
				//Count clustered samples.
				int K = centroids.size();
				int[] count = new int[K];
				for (Sample sample : learnSet) {
					for (int k=0; k < K; k++) {
						Sample centroid = centroids.get(k);
						if (sample.cluster == centroid.cluster) count[k]++;
					}
				}
				lblCentroid1.setText("Centroid 1: " + count[0] + " samples " + "("+centroids.get(0).GetState()+")");
				lblCentroid2.setText("Centroid 2: " + count[1] + " samples " +"("+centroids.get(1).GetState()+")");
				lblCentroid3.setText("Centroid 3: " + count[2] + " samples " +"("+centroids.get(2).GetState()+")");
				lblCentroid4.setText("Centroid 4: " + count[3] + " samples " +"("+centroids.get(3).GetState()+")");
				
				//Output results in a table.
				DefaultTableModel tableData = new DefaultTableModel();			
				String[] columnNames = {"Dataset","ID","V1","V2","V3","V4","V5","V6","V7","V8","V9","Class"};				
				for (int i = 0; i < columnNames.length; i++) {
					tableData.addColumn(columnNames[i]);
				}
				IncludeSet("Centroids", tableData, centroids);
				IncludeSet("Learn Set", tableData, learnSet);
				IncludeSet("Test Set", tableData, testSet);				
				tblSets.setModel(tableData);
			}
		});
		frmSmartPowerSystem.getContentPane().add(btnExecute);
		
		JLabel lblDatabase = new JLabel("Database:");
		lblDatabase.setBounds(20, 68, 63, 14);
		frmSmartPowerSystem.getContentPane().add(lblDatabase);
		
		txtDatabase = new JTextField();
		txtDatabase.setBounds(93, 65, 86, 20);
		txtDatabase.setText("assignment_2");
		txtDatabase.setColumns(10);
		frmSmartPowerSystem.getContentPane().add(txtDatabase);
		
		JLabel lblHost = new JLabel("Host:");
		lblHost.setBounds(20, 39, 52, 14);
		frmSmartPowerSystem.getContentPane().add(lblHost);
		
		txtHost = new JTextField();
		txtHost.setBounds(93, 36, 86, 20);
		txtHost.setText("localhost");
		txtHost.setColumns(10);
		frmSmartPowerSystem.getContentPane().add(txtHost);
		
		txtLearnSet = new JTextField();
		txtLearnSet.setBounds(954, 36, 99, 20);
		txtLearnSet.setText("measurements");
		txtLearnSet.setColumns(10);
		frmSmartPowerSystem.getContentPane().add(txtLearnSet);
		
		txtTestSet = new JTextField();
		txtTestSet.setBounds(954, 61, 99, 20);
		txtTestSet.setText("analog_values");
		txtTestSet.setColumns(10);
		frmSmartPowerSystem.getContentPane().add(txtTestSet);
		
		JLabel lblLearnSet = new JLabel("Learn Set:");
		lblLearnSet.setBounds(891, 39, 74, 14);
		frmSmartPowerSystem.getContentPane().add(lblLearnSet);
		
		JLabel lblTestSet = new JLabel("Test Set:");
		lblTestSet.setBounds(891, 64, 63, 14);
		frmSmartPowerSystem.getContentPane().add(lblTestSet);
		
		JLabel lblKthRoyalInstitute = new JLabel("KTH Royal Institute of Technology");
		lblKthRoyalInstitute.setBounds(418, 53, 255, 20);
		lblKthRoyalInstitute.setFont(new Font("Tahoma", Font.PLAIN, 16));
		frmSmartPowerSystem.getContentPane().add(lblKthRoyalInstitute);
		
		JLabel lblLearntestSets = new JLabel("Learn/Test Sets:");
		lblLearntestSets.setBounds(891, 11, 146, 14);
		lblLearntestSets.setFont(new Font("Tahoma", Font.PLAIN, 13));
		frmSmartPowerSystem.getContentPane().add(lblLearntestSets);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 168, 1110, 279);
		frmSmartPowerSystem.getContentPane().add(scrollPane);
		
		tblSets = new JTable();
		tblSets.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
					"Dataset","ID","V1","V2","V3","V4","V5","V6","V7","V8","V9","Class"
			}
		));
		scrollPane.setViewportView(tblSets);
		
		JLabel lblAssignmentIi = new JLabel("Machine Learning in Power Systems");
		lblAssignmentIi.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAssignmentIi.setBounds(379, 19, 331, 25);
		frmSmartPowerSystem.getContentPane().add(lblAssignmentIi);
		
		JLabel lblByGpapakthse = new JLabel("e-mails: gpapa@kth.se & fdl@kth.se");
		lblByGpapakthse.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblByGpapakthse.setBounds(447, 123, 170, 25);
		frmSmartPowerSystem.getContentPane().add(lblByGpapakthse);
		
		JLabel lblEhAssignment = new JLabel("EH2745 - Assignment II");
		lblEhAssignment.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEhAssignment.setBounds(459, 79, 150, 20);
		frmSmartPowerSystem.getContentPane().add(lblEhAssignment);
		
		JLabel lblAuthorsGPapadopoulos = new JLabel("Authors: G. Papadopoulos & F. de Lima");
		lblAuthorsGPapadopoulos.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblAuthorsGPapadopoulos.setBounds(437, 102, 192, 25);
		frmSmartPowerSystem.getContentPane().add(lblAuthorsGPapadopoulos);
		
		JLabel label_3 = new JLabel("___________________");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_3.setBounds(18, 12, 133, 19);
		frmSmartPowerSystem.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("______________");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_4.setBounds(890, 13, 133, 19);
		frmSmartPowerSystem.getContentPane().add(label_4);
		
		lblCentroid1 = new JLabel("Centroid 1: ## samples (??)");
		lblCentroid1.setBounds(894, 90, 236, 14);
		frmSmartPowerSystem.getContentPane().add(lblCentroid1);
		
		lblCentroid2 = new JLabel("Centroid 2: ## samples (??)");
		lblCentroid2.setBounds(894, 107, 236, 14);
		frmSmartPowerSystem.getContentPane().add(lblCentroid2);
		
		lblCentroid3 = new JLabel("Centroid 3: ## samples (??)");
		lblCentroid3.setBounds(894, 123, 236, 14);
		frmSmartPowerSystem.getContentPane().add(lblCentroid3);
		
		lblCentroid4 = new JLabel("Centroid 4: ## samples (??)");
		lblCentroid4.setBounds(894, 141, 236, 14);
		frmSmartPowerSystem.getContentPane().add(lblCentroid4);
	}
	
	//*** INCLUDE SET TO SHOW IN GUI ***
	private static void IncludeSet(String setName, DefaultTableModel tableData, ArrayList<Sample> set) {
		for (Sample sample : set) {				
			String[] row = new String[tableData.getColumnCount()];
			double magnitude, angle;
			int n=2;
			row[0] = setName;
			row[1] = Integer.toString(sample.id);					
			for (int i=0; i < sample.attribute.length-1; i+=2) {
				magnitude = Math.round(sample.attribute[i]*100.0)/100.0;
				angle = Math.round(sample.attribute[i+1]*100.0)/100.0;						
				row[n] = Double.toString(magnitude) + " /_" + Double.toString(angle);
				n++;
			}
			row[tableData.getColumnCount()-1] = sample.GetState();
			tableData.addRow(row);
		}
	}
}
