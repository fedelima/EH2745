package assignment2;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
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
	
	String csvFile = "";

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
		frmSmartPowerSystem.setBounds(100, 100, 790, 513);
		frmSmartPowerSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSmartPowerSystem.getContentPane().setLayout(null);
		
		txtUser = new JTextField();
		txtUser.setBounds(93, 89, 86, 20);
		txtUser.setText("root");
		txtUser.setColumns(10);
		frmSmartPowerSystem.getContentPane().add(txtUser);
		
		JLabel label = new JLabel("DB User:");
		label.setBounds(20, 92, 52, 14);
		frmSmartPowerSystem.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("DB Psswd:");
		label_1.setBounds(20, 117, 63, 14);
		frmSmartPowerSystem.getContentPane().add(label_1);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(93, 114, 86, 20);
		txtPassword.setText("xxxx");
		frmSmartPowerSystem.getContentPane().add(txtPassword);
		
		JLabel label_2 = new JLabel("Database Credentials:");
		label_2.setBounds(20, 11, 146, 14);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		frmSmartPowerSystem.getContentPane().add(label_2);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(20, 438, 734, 25);
		btnExecute.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				//Define variables.
				ArrayList<Sample> learnSet = new ArrayList<Sample>();
				ArrayList<Sample> testSet = new ArrayList<Sample>();
				
				//Capture values from GUI.
				String host = txtHost.getText();
				String database = txtDatabase.getText();
				String user = txtUser.getText();				
				String psswd = txtPassword.getText();
				String lsname = txtLearnSet.getText();
				String tsname = txtTestSet.getText();
				
				if (!csvFile.equals("")) {
					Assignment_II.execute(learnSet, testSet, host, database, user, psswd, lsname, tsname, csvFile); //Execute main routine.
				}else {
					JOptionPane.showMessageDialog(frmSmartPowerSystem,
						    "Please select a valid centroids file before proceeding !",
						    "No centroids file selected",
						    JOptionPane.WARNING_MESSAGE);
				}
				
				//Output results in a table.
				DefaultTableModel tableData = new DefaultTableModel();			
				String[] columnNames = {"Set","Time","V1","V2","V3","V4", "V5", "V6", "V7","V8","V9","Class"};				
				for (int i = 0; i < columnNames.length; i++) {
					tableData.addColumn(columnNames[i]);
				}
				for (Sample sample : learnSet) {					
					String[] row = new String[columnNames.length];
					row[0] = "Learn";
					row[1] = sample.time;
					for (int i=0; i < sample.attribute.length; i++) {
						row[i+2] = sample.attribute[i].toString(); 
					}
					row[columnNames.length-1] = sample.GetState();
					tableData.addRow(row);
				}
				for (Sample sample : testSet) {					
					String[] row = new String[columnNames.length];
					row[0] = "Test";
					row[1] = sample.time;
					for (int i=0; i < sample.attribute.length; i++) {
						row[i+2] = sample.attribute[i].toString(); 
					}
					row[columnNames.length-1] = sample.GetState();
					tableData.addRow(row);
				}
				tblSets.setModel(tableData);
			}
		});
		frmSmartPowerSystem.getContentPane().add(btnExecute);
		
		JLabel lblDatabase = new JLabel("Database:");
		lblDatabase.setBounds(20, 64, 63, 14);
		frmSmartPowerSystem.getContentPane().add(lblDatabase);
		
		txtDatabase = new JTextField();
		txtDatabase.setBounds(93, 61, 86, 20);
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
		txtLearnSet.setBounds(655, 41, 99, 20);
		txtLearnSet.setText("measurements");
		txtLearnSet.setColumns(10);
		frmSmartPowerSystem.getContentPane().add(txtLearnSet);
		
		txtTestSet = new JTextField();
		txtTestSet.setBounds(655, 74, 99, 20);
		txtTestSet.setText("analog_values");
		txtTestSet.setColumns(10);
		frmSmartPowerSystem.getContentPane().add(txtTestSet);
		
		JLabel lblLearnSet = new JLabel("Learn Set:");
		lblLearnSet.setBounds(592, 44, 74, 14);
		frmSmartPowerSystem.getContentPane().add(lblLearnSet);
		
		JLabel lblTestSet = new JLabel("Test Set:");
		lblTestSet.setBounds(592, 77, 63, 14);
		frmSmartPowerSystem.getContentPane().add(lblTestSet);
		
		JLabel lblKthRoyalInstitute = new JLabel("KTH Royal Institute of Technology");
		lblKthRoyalInstitute.setBounds(246, 45, 255, 20);
		lblKthRoyalInstitute.setFont(new Font("Tahoma", Font.PLAIN, 16));
		frmSmartPowerSystem.getContentPane().add(lblKthRoyalInstitute);
		
		JLabel lblLearntestSets = new JLabel("Learn/Test Sets:");
		lblLearntestSets.setBounds(592, 12, 146, 14);
		lblLearntestSets.setFont(new Font("Tahoma", Font.PLAIN, 13));
		frmSmartPowerSystem.getContentPane().add(lblLearntestSets);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 152, 734, 275);
		frmSmartPowerSystem.getContentPane().add(scrollPane);
		
		tblSets = new JTable();
		tblSets.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
					"Set","Time","V1","V2","V3","V4", "V5", "V6", "V7","V8","V9","Class"
			}
		));
		scrollPane.setViewportView(tblSets);
		
		JLabel lblAssignmentIi = new JLabel("Machine Learning in Power Systems");
		lblAssignmentIi.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAssignmentIi.setBounds(223, 11, 331, 25);
		frmSmartPowerSystem.getContentPane().add(lblAssignmentIi);
		
		JLabel lblByGpapakthse = new JLabel("e-mails: gpapa@kth.se & fdl@kth.se");
		lblByGpapakthse.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblByGpapakthse.setBounds(286, 117, 170, 25);
		frmSmartPowerSystem.getContentPane().add(lblByGpapakthse);
		
		JButton btnCentroidsFile = new JButton("Select centroids file...");
		btnCentroidsFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				
				File file = null;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				    file = fileChooser.getSelectedFile();
				    csvFile = file.getAbsolutePath();
				    System.out.println("Chosen file: " + csvFile);
				} else {
				    System.out.println("Please select a valid file");
				}
			}
		});
		btnCentroidsFile.setBounds(592, 113, 162, 23);
		frmSmartPowerSystem.getContentPane().add(btnCentroidsFile);
		
		JLabel lblEhAssignment = new JLabel("EH2745 - Assignment II");
		lblEhAssignment.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEhAssignment.setBounds(286, 73, 150, 20);
		frmSmartPowerSystem.getContentPane().add(lblEhAssignment);
		
		JLabel lblAuthorsGPapadopoulos = new JLabel("Authors: G. Papadopoulos & F. de Lima");
		lblAuthorsGPapadopoulos.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblAuthorsGPapadopoulos.setBounds(272, 96, 192, 25);
		frmSmartPowerSystem.getContentPane().add(lblAuthorsGPapadopoulos);
		
		JLabel label_3 = new JLabel("___________________");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_3.setBounds(18, 12, 133, 19);
		frmSmartPowerSystem.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("______________");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label_4.setBounds(589, 12, 133, 19);
		frmSmartPowerSystem.getContentPane().add(label_4);
	}
}
