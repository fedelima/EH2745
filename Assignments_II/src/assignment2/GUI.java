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
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GUI {

	private JFrame frame;
	private JTextField txtUser;
	private JPasswordField txtPassword;
	private JTextField txtDatabase;
	private JTextField txtHost;
	private JTextField txtLearnSet;
	private JTextField txtTestSet;
	private JTable tblSets;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 718, 513);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtUser = new JTextField();
		txtUser.setBounds(80, 89, 86, 20);
		txtUser.setText("root");
		txtUser.setColumns(10);
		frame.getContentPane().add(txtUser);
		
		JLabel label = new JLabel("DB User:");
		label.setBounds(20, 92, 52, 14);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("DB Psswd:");
		label_1.setBounds(20, 117, 63, 14);
		frame.getContentPane().add(label_1);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(80, 114, 86, 20);
		txtPassword.setText("xxxx");
		frame.getContentPane().add(txtPassword);
		
		JLabel label_2 = new JLabel("Database Credentials:");
		label_2.setBounds(20, 11, 146, 14);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		frame.getContentPane().add(label_2);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(20, 438, 672, 25);
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
				Assignment_II.execute(learnSet, testSet, host, database, user, psswd, lsname, tsname); //Execute main routine.
				
				//Output results in a table.
				DefaultTableModel tableData = new DefaultTableModel();			
				String[] columnNames = {"Set","Time","V1","V2","V3","V4", "V5", "V6", "V7","V8","V9","Class"};				
				for (int i = 0; i < columnNames.length; i++) {
					tableData.addColumn(columnNames[i]);
				}
				for (Sample sample : learnSet) {					
					String[] row = new String[columnNames.length];
					row[0] = "Learn";
					row[1] = sample.time[0];
					for (int i=0; i < sample.attribute.length; i++) {
						row[i+2] = sample.attribute[i].toString(); 
					}
					row[columnNames.length-1] = Integer.toString(sample.state);
					tableData.addRow(row);
				}
				for (Sample sample : testSet) {					
					String[] row = new String[columnNames.length];
					row[0] = "Test";
					row[1] = sample.time[0];
					for (int i=0; i < sample.attribute.length; i++) {
						row[i+2] = sample.attribute[i].toString(); 
					}
					row[columnNames.length-1] = Integer.toString(sample.state);
					tableData.addRow(row);
				}
				tblSets.setModel(tableData);
			}
		});
		frame.getContentPane().add(btnExecute);
		
		JLabel lblDatabase = new JLabel("Database:");
		lblDatabase.setBounds(20, 64, 52, 14);
		frame.getContentPane().add(lblDatabase);
		
		txtDatabase = new JTextField();
		txtDatabase.setBounds(80, 61, 86, 20);
		txtDatabase.setText("assignment_2");
		txtDatabase.setColumns(10);
		frame.getContentPane().add(txtDatabase);
		
		JLabel lblHost = new JLabel("Host:");
		lblHost.setBounds(20, 39, 52, 14);
		frame.getContentPane().add(lblHost);
		
		txtHost = new JTextField();
		txtHost.setBounds(80, 36, 86, 20);
		txtHost.setText("localhost");
		txtHost.setColumns(10);
		frame.getContentPane().add(txtHost);
		
		txtLearnSet = new JTextField();
		txtLearnSet.setBounds(593, 80, 99, 20);
		txtLearnSet.setText("measurements");
		txtLearnSet.setColumns(10);
		frame.getContentPane().add(txtLearnSet);
		
		txtTestSet = new JTextField();
		txtTestSet.setBounds(593, 105, 99, 20);
		txtTestSet.setText("analog_values");
		txtTestSet.setColumns(10);
		frame.getContentPane().add(txtTestSet);
		
		JLabel lblLearnSet = new JLabel("Learn Set:");
		lblLearnSet.setBounds(530, 83, 74, 14);
		frame.getContentPane().add(lblLearnSet);
		
		JLabel lblTestSet = new JLabel("Test Set:");
		lblTestSet.setBounds(530, 108, 63, 14);
		frame.getContentPane().add(lblTestSet);
		
		JLabel lblKthRoyalInstitute = new JLabel("KTH Royal Institute of Technology");
		lblKthRoyalInstitute.setBounds(220, 78, 255, 20);
		lblKthRoyalInstitute.setFont(new Font("Tahoma", Font.PLAIN, 16));
		frame.getContentPane().add(lblKthRoyalInstitute);
		
		JLabel lblLearntestSets = new JLabel("Learn/Test Sets:");
		lblLearntestSets.setBounds(546, 62, 146, 14);
		lblLearntestSets.setFont(new Font("Tahoma", Font.PLAIN, 13));
		frame.getContentPane().add(lblLearntestSets);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 152, 672, 275);
		frame.getContentPane().add(scrollPane);
		
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
		
		JLabel lblAssignmentIi = new JLabel("Assignment II - Machine Learning");
		lblAssignmentIi.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAssignmentIi.setBounds(197, 39, 315, 25);
		frame.getContentPane().add(lblAssignmentIi);
		
		JLabel lblByGpapakthse = new JLabel("By: gpapa@kth.se & fdl@kth.se");
		lblByGpapakthse.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblByGpapakthse.setBounds(268, 106, 146, 25);
		frame.getContentPane().add(lblByGpapakthse);
	}
}
