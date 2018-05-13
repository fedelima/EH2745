package assignment1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Color;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

@SuppressWarnings("unused")
public class GUI {

	private JFrame frmAssignmentI;
	private JTextField txtUsername;
	private JPasswordField pwdPassword;
	private JTable tblYbus;
	private JLabel lblNumberOfBuses;
	String eqFile, sshFile;
	private JTextField txtBase;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmAssignmentI.setVisible(true);
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
		frmAssignmentI = new JFrame();
		frmAssignmentI.setResizable(false);
		frmAssignmentI.setTitle("Assignment I - EH2745  CIM-XML to Bus-Branch Ybus Model  V1.2");
		frmAssignmentI.setBounds(0, -50, 720, 437);
		frmAssignmentI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(10, 372, 694, 25);
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = txtUsername.getText();
				@SuppressWarnings("deprecation")
				String psswd = pwdPassword.getText();
				Double SB = Double.parseDouble(txtBase.getText());
				ArrayList<Ybus> ybus_list = assignment1.Assignment_I.execute(eqFile, sshFile, user, psswd, SB); //Get ybus list.
				Integer N = assignment1.Assignment_I.getNumberOfBuses(); //Get number of buses.
				
				DefaultTableModel tableData = new DefaultTableModel();			
				String[] columnNames = {"From Bus","To Bus","R (p.u)","X (p.u)", "Gs (p.u)", "Bs (p.u)", "DevType","Device"};				
				for (int i = 0; i < columnNames.length; i++) {
					tableData.addColumn(columnNames[i]);
				}
				for (Ybus branch : ybus_list) {					
					String[] row = {branch.From, branch.To,
							branch.R.toString(), branch.X.toString(),
							branch.Gch.toString(), branch.Bch.toString(),
							branch.devType, branch.dev};
					tableData.addRow(row);
				}
				lblNumberOfBuses.setText("Number of Buses in the System: " + N.toString());
				tblYbus.setModel(tableData);
			}
		});
		frmAssignmentI.getContentPane().setLayout(null);
		frmAssignmentI.setLocationRelativeTo(null);
		frmAssignmentI.getContentPane().add(btnExecute);
		
		txtUsername = new JTextField();
		txtUsername.setText("root");
		txtUsername.setBounds(70, 71, 86, 20);
		frmAssignmentI.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		pwdPassword = new JPasswordField();
		pwdPassword.setText("xxxx");
		pwdPassword.setBounds(70, 98, 86, 20);
		frmAssignmentI.getContentPane().add(pwdPassword);
		
		JLabel lblEqFile = new JLabel("EQ File:");
		lblEqFile.setBounds(482, 22, 63, 17);
		frmAssignmentI.getContentPane().add(lblEqFile);
		
		JLabel lblSshFile = new JLabel("SSH File:");
		lblSshFile.setBounds(482, 56, 52, 14);
		frmAssignmentI.getContentPane().add(lblSshFile);
		
		JLabel lblDbUser = new JLabel("DB User:");
		lblDbUser.setBounds(10, 74, 52, 14);
		frmAssignmentI.getContentPane().add(lblDbUser);
		
		JLabel lblDbPsswd = new JLabel("DB Psswd:");
		lblDbPsswd.setBounds(10, 101, 63, 14);
		frmAssignmentI.getContentPane().add(lblDbPsswd);
		
		lblNumberOfBuses = new JLabel("Number of Buses in the System:");
		lblNumberOfBuses.setBounds(482, 87, 205, 14);
		frmAssignmentI.getContentPane().add(lblNumberOfBuses);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 143, 694, 218);
		frmAssignmentI.getContentPane().add(scrollPane);
		
		tblYbus = new JTable();
		tblYbus.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"From Bus", "To Bus", "R (p.u.)", "X (p.u)", "Gs (p.u)", "Bs (p.u)", "DevType", "Device"
			}
		));
		scrollPane.setViewportView(tblYbus);
		
		JButton btnEqFile = new JButton("Choose EQ File...");
		btnEqFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(frmAssignmentI);
				
				File file = null;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				    file = fileChooser.getSelectedFile();
				    eqFile = file.getAbsolutePath();
				    System.out.println("Chosen file: " + eqFile);
				} else {
				    System.out.println("Please select a valid file");
				}
			}
		});
		btnEqFile.setBounds(559, 16, 145, 23);
		frmAssignmentI.getContentPane().add(btnEqFile);
		
		JButton btnSshFile = new JButton("Choose SSH File...");
		btnSshFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(frmAssignmentI);
				
				File file = null;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				    file = fileChooser.getSelectedFile();
				    sshFile = file.getAbsolutePath();
				    System.out.println("Chosen file: " + sshFile);
				} else {
				    System.out.println("Please select a valid file");
				}
			}
		});
		btnSshFile.setBounds(559, 50, 145, 23);
		frmAssignmentI.getContentPane().add(btnSshFile);
		
		JLabel lblDatabaseCredentials = new JLabel("Database Credentials:");
		lblDatabaseCredentials.setBounds(10, 46, 146, 14);
		frmAssignmentI.getContentPane().add(lblDatabaseCredentials);
		
	    ImageIcon imageIcon = new ImageIcon("kth_logo.png");
		JLabel lblLogo = new JLabel(imageIcon);
		lblLogo.setBounds(180, 13, 280, 104);
		frmAssignmentI.getContentPane().add(lblLogo);
		
		JButton btnAbout = new JButton("About");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frmAssignmentI, "EH2745 Computer Applications in Power Systems\n"
						+ "Assignment I V1.2\n"
						+ "By: G.Papadopoulos & F. de Lima\n"
						+ "E-mails: gpapa@kth.se, fdl@kth.se");
			}
		});
		btnAbout.setBounds(10, 11, 146, 23);
		frmAssignmentI.getContentPane().add(btnAbout);
		
		txtBase = new JTextField();
		txtBase.setText("1000");
		txtBase.setBounds(663, 112, 41, 20);
		frmAssignmentI.getContentPane().add(txtBase);
		txtBase.setColumns(10);
		
		JLabel lblSystemBasemva = new JLabel("SB (MVA):");
		lblSystemBasemva.setBounds(589, 113, 64, 14);
		frmAssignmentI.getContentPane().add(lblSystemBasemva);
	}
}
