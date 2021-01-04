//Import all necessary libraries
import java.awt.EventQueue;
import java.util.List;
import javax.swing.*;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//Define Class Frame1
public class Frame1 {
	//Initialises all SWING Components and global variables that need to be reached throughout the class
	private JFrame frame;
	private JTable table;
	private JButton btnAddRecord;
	private JButton btnRemove;
	private JButton btnModify;
	private JButton btnSearch;
	protected Object newRecord;
	private JTextField txtID;
	private JTextField txtName;
	private JTextField txtGender;
	private JTextField txtAddress;
	private JLabel lblEnglishMarks;
	private JTextField txtPostcode;
	private JTextField txtComputerScience;
	private JTextField txtMaths;
	private JTextField txtEnglish;
	private JButton btnHighestCs;
	private JButton btnHighestMaths;
	private JButton btnHighestEnglish;
	private JButton btnLowestCs;
	private JButton btnLowestMaths;
	private JButton btnLowestEnglish;
	private JButton btnCsAverage;
	private JButton btnMathsAverage;
	private JButton btnEnglishAverage;
	private JLabel lblSubjectAverages;
	private JLabel lblHighestAndLowest;
	private DefaultTableModel model;
	private  Object[][] tableData;
	private int tableColumn;
	private List<student> students;
	private JComboBox comboBoxCSGrade;
	private JComboBox comboBoxMathsGrades;
	private JComboBox comboBoxEnglishGrades;
	private JComboBox comboBoxAverageGrades;
	/**
	 * Launch the application.
	 */
	//Main method is the first method to execute. It opens the JFrame.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	//Frame1 calls the initialise method.
	public Frame1() {
		initialise();
	}

	/**
	 * Initialise the contents of the frame.
	 */

	//The initialise method sets the location of the SWING components. There are a total of 45 SWING in the student information system
	public void initialise() {
		//The main window of the application.
		//New Jframe with the title "Student Information System"
		frame = new JFrame("Student Information System");
		frame.addWindowListener(new WindowAdapter() {
			//When the frame is closing:
			@Override
			public void windowClosing(WindowEvent e) {
				//Reset the search parameters to clear the search
				clearSearch();
				//and save the table model to the text file
				save();
				//Displays message showing that the database has been saved.
				JOptionPane.showMessageDialog(null, "Database Saved");
			}
		});
		frame.setBounds(143, -22, 1171, 641);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(139, 26, 711, 389);
		frame.getContentPane().add(scrollPane);
		
		//Initialise JTable. The JTable is used to display the student data.
		table = new JTable(){
			private static final long serialVersionUID = 1L;
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //All cells false, cells cannot be edited by clicking on them and typing.
		       return false;
			}
		};
		scrollPane.setViewportView(table);
		
		//Adds data from text file to JTable.
		importer();
		
		
		//Adds the "Add Record" JButton.
		btnAddRecord = new JButton("Add Record");
		btnAddRecord.setToolTipText("Click to add a new student");
		btnAddRecord.addActionListener(new ActionListener() {
			//When the JButtton is clicked
			public void actionPerformed(ActionEvent e) {
				//Add a student
				studentAdder();
				//And re-grade all the students
				grader();
			}});
		btnAddRecord.setBounds(6, 26, 127, 37);
		frame.getContentPane().add(btnAddRecord);
		
		//Adds the "Remove Record(s)" JButton.
		btnRemove = new JButton("Remove Record(s)");
		btnRemove.setToolTipText("Select a student or students and click here to remove them");
		btnRemove.addActionListener(new ActionListener() {
			//When JButton is clicked
			public void actionPerformed(ActionEvent e) {
				//The selected records are removed.
				remove();
				}
			});
		btnRemove.setBounds(6, 75, 127, 37);
		frame.getContentPane().add(btnRemove);
		
		//Adds the "Modify Record" JButton.
		btnModify = new JButton("Modify Record");
		btnModify.setToolTipText("Select a student to modify them.");
		btnModify.addActionListener(new ActionListener() {
			//When JButton is clicked
			public void actionPerformed(ActionEvent e) {
				//Change the selected students details
				modifier();
				//and re-grade the students.
				grader();
			}
		});
		btnModify.setBounds(6, 127, 127, 37);
		frame.getContentPane().add(btnModify);
		
		//JButton to perform a combined search upon the ID, Name, Gender, Address, Postcode, Marks and Grades.
		btnSearch = new JButton("Search");
		btnSearch.setToolTipText("Enter one or multiple search criteria into the textfields and click here to search the table");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Call the multiSearch method.
				multiSearch();
			}
		});
		btnSearch.setBounds(882, 428, 227, 46);
		frame.getContentPane().add(btnSearch);
		
		//Ads the "Clear Search" JButton
		JButton btnClearSearch = new JButton("Clear Search");
		btnClearSearch.setToolTipText("Clear all search fields and reset database\n");
		btnClearSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//Clears search fields and performs an empty search to reset search function.
			clearSearch();
			}
		});
		btnClearSearch.setBounds(882, 490, 227, 50);
		frame.getContentPane().add(btnClearSearch);
		
		//Labels and the search boxes
		JLabel lblIdNumber = new JLabel("ID Number:");
		lblIdNumber.setBounds(16, 430, 72, 16);
		frame.getContentPane().add(lblIdNumber);
		
		txtID = new JTextField();
		txtID.setToolTipText("Search by ID Number");
		txtID.setBounds(96, 427, 169, 26);
		frame.getContentPane().add(txtID);
		txtID.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(42, 460, 40, 16);
		frame.getContentPane().add(lblName);
		
		txtName = new JTextField();
		txtName.setToolTipText("Search by Names");
		txtName.setBounds(96, 455, 169, 26);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);
		
		JLabel lblGender = new JLabel("Gender:");
		lblGender.setBounds(40, 490, 48, 16);
		frame.getContentPane().add(lblGender);
		
		txtGender = new JTextField();
		txtGender.setToolTipText("Search for either Males for Females");
		txtGender.setBounds(96, 485, 169, 26);
		frame.getContentPane().add(txtGender);
		txtGender.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setBounds(35, 520, 55, 16);
		frame.getContentPane().add(lblAddress);
		
		txtAddress = new JTextField();
		txtAddress.setToolTipText("Search by Address");
		txtAddress.setBounds(96, 514, 169, 26);
		frame.getContentPane().add(txtAddress);
		txtAddress.setColumns(10);
		
		
		
		JLabel lblPostcode = new JLabel("Postcode:");
		lblPostcode.setBounds(301, 432, 61, 16);
		frame.getContentPane().add(lblPostcode);
		
		txtPostcode = new JTextField();
		txtPostcode.setToolTipText("Search by Postcode");
		txtPostcode.setBounds(369, 427, 169, 26);
		frame.getContentPane().add(txtPostcode);
		txtPostcode.setColumns(10);
		
		JLabel lblComputerScienceMarks = new JLabel("CS Marks:");
		lblComputerScienceMarks.setBounds(300, 460, 62, 16);
		frame.getContentPane().add(lblComputerScienceMarks);
		
		txtComputerScience = new JTextField();
		txtComputerScience.setToolTipText("To search inbetween two marks enter a number followed by a \"-\" and then another number (e.g 34.4-70)");
		txtComputerScience.setBounds(369, 455, 169, 26);
		frame.getContentPane().add(txtComputerScience);
		txtComputerScience.setColumns(10);
		
		JLabel lblMathsMarks = new JLabel("Maths Marks:");
		lblMathsMarks.setBounds(278, 491, 84, 16);
		frame.getContentPane().add(lblMathsMarks);
		
		lblEnglishMarks = new JLabel("English Marks:");
		lblEnglishMarks.setBounds(270, 519, 92, 16);
		frame.getContentPane().add(lblEnglishMarks);
		
		
		
		
		
		txtMaths = new JTextField();
		txtMaths.setToolTipText("To seach inbetween two marks enter a number followed by a \"-\" and then another number (e.g 34.4-70)");
		txtMaths.setBounds(369, 486, 169, 26);
		frame.getContentPane().add(txtMaths);
		txtMaths.setColumns(10);
		
		txtEnglish = new JTextField();
		txtEnglish.setToolTipText("To seach inbetween two marks enter a number followed by a \"-\" and then another number (e.g 34.4-70)");
		txtEnglish.setBounds(369, 514, 169, 26);
		frame.getContentPane().add(txtEnglish);
		txtEnglish.setColumns(10);
		
		
		//Performs an calculation averaging the 3 marks of a selected student.
		JButton btnStudentAverage = new JButton("Student Average");
		btnStudentAverage.setToolTipText("Select a student and click here to view the average of their 3 marks");
		btnStudentAverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				studentAverage();
			}
		});
		btnStudentAverage.setBounds(872, 375, 246, 29);
		frame.getContentPane().add(btnStudentAverage);
		
		btnHighestCs = new JButton("Highest CS");
		btnHighestCs.setToolTipText("Display student with highest mark in Computer Science");
		btnHighestCs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highest(5);
			}
		});
		btnHighestCs.setBounds(872, 32, 117, 37);
		frame.getContentPane().add(btnHighestCs);
		
		btnHighestMaths = new JButton("Highest Maths");
		btnHighestMaths.setToolTipText("Display student with highest mark in Maths");
		btnHighestMaths.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highest(6);
			}
		});
		btnHighestMaths.setBounds(872, 73, 117, 37);
		frame.getContentPane().add(btnHighestMaths);
		
		btnHighestEnglish = new JButton("Highest English");
		btnHighestEnglish.setToolTipText("Display student with highest mark in English\n");
		btnHighestEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highest(7);
			}
		});
		btnHighestEnglish.setBounds(872, 117, 117, 37);
		frame.getContentPane().add(btnHighestEnglish);
		
		btnLowestCs = new JButton("Lowest CS");
		btnLowestCs.setToolTipText("Display student with lowest mark in Computer Science\n");
		btnLowestCs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lowest(5);
			}
		});
		btnLowestCs.setBounds(1001, 32, 117, 37);
		frame.getContentPane().add(btnLowestCs);
		
		btnLowestMaths = new JButton("Lowest Maths");
		btnLowestMaths.setToolTipText("Display student with lowest mark in Maths");
		btnLowestMaths.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lowest(6);
			}
		});
		btnLowestMaths.setBounds(1001, 73, 117, 37);
		frame.getContentPane().add(btnLowestMaths);
		
		btnLowestEnglish = new JButton("Lowest English");
		btnLowestEnglish.setToolTipText("Display student with lowest mark in English");
		btnLowestEnglish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lowest(7);
			}
		});
		btnLowestEnglish.setBounds(1001, 117, 117, 37);
		frame.getContentPane().add(btnLowestEnglish);
		
		
		//Calculates the average Computer Science mark.
		btnCsAverage = new JButton("CS Average");
		btnCsAverage.setToolTipText("View the overall average Computer Science mark");
		btnCsAverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AverageCalc2(5);
			}
		});
		btnCsAverage.setBounds(875, 221, 246, 29);
		frame.getContentPane().add(btnCsAverage);
		
		//Calculates the average Math mark.
		btnMathsAverage = new JButton("Maths Average");
		btnMathsAverage.setToolTipText("View the overall average Maths mark");
		btnMathsAverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AverageCalc2(6);
			}
		});
		btnMathsAverage.setBounds(872, 262, 249, 29);
		frame.getContentPane().add(btnMathsAverage);
		
		//Calculates the average English mark.
		btnEnglishAverage = new JButton("English Average");
		btnEnglishAverage.setToolTipText("View the overall average English mark");
		btnEnglishAverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AverageCalc2(7);
			}
		});
		btnEnglishAverage.setBounds(875, 303, 246, 29);
		frame.getContentPane().add(btnEnglishAverage);
		
		JLabel lblSelectAStudent = new JLabel("Select a Student and Click Student Average");
		lblSelectAStudent.setBounds(862, 344, 279, 29);
		frame.getContentPane().add(lblSelectAStudent);
		
		lblSubjectAverages = new JLabel("Subject Averages");
		lblSubjectAverages.setBounds(941, 193, 127, 16);
		frame.getContentPane().add(lblSubjectAverages);
		
		lblHighestAndLowest = new JLabel("Highest and Lowest Grades");
		lblHighestAndLowest.setBounds(910, 6, 181, 16);
		frame.getContentPane().add(lblHighestAndLowest);
		
		JLabel lblCsGrades = new JLabel("CS Grades:");
		lblCsGrades.setBounds(580, 430, 77, 16);
		frame.getContentPane().add(lblCsGrades);
		
		JLabel lblMathsGrades = new JLabel("Maths Grades:");
		lblMathsGrades.setBounds(558, 460, 92, 16);
		frame.getContentPane().add(lblMathsGrades);
		
		JLabel lblEnglishGrades = new JLabel("English Grades:");
		lblEnglishGrades.setBounds(551, 488, 103, 16);
		frame.getContentPane().add(lblEnglishGrades);
		
		JLabel lblAverageGrades = new JLabel("Average Grades:");
		lblAverageGrades.setBounds(546, 520, 103, 16);
		frame.getContentPane().add(lblAverageGrades);
		
		
		
		//Combo boxes for grade searches
		comboBoxEnglishGrades = new JComboBox();
		comboBoxEnglishGrades.setToolTipText("Displays students by English grades\n");
		comboBoxMathsGrades = new JComboBox();
		comboBoxMathsGrades.setToolTipText("Displays students by Maths grades\n");
		comboBoxCSGrade  = new JComboBox();
		comboBoxCSGrade.setToolTipText("Displays Computer Science grades\n");
		comboBoxCSGrade.setModel(new DefaultComboBoxModel(new String[] {"-", "Distinction", "Merit", "Pass", "Fail"}));
		comboBoxCSGrade.setBounds(653, 427, 197, 27);
		frame.getContentPane().add(comboBoxCSGrade);
		
		
		comboBoxMathsGrades.setModel(new DefaultComboBoxModel(new String[] {"-", "Distinction", "Merit", "Pass", "Fail"}));
		comboBoxMathsGrades.setBounds(653, 456, 197, 27);
		frame.getContentPane().add(comboBoxMathsGrades);
		comboBoxEnglishGrades.setModel(new DefaultComboBoxModel(new String[] {"-", "Distinction", "Merit", "Pass", "Fail"}));
		comboBoxEnglishGrades.setBounds(653, 486, 197, 27);
		frame.getContentPane().add(comboBoxEnglishGrades);
		
		comboBoxAverageGrades = new JComboBox();
		comboBoxAverageGrades.setModel(new DefaultComboBoxModel(new String[] {"-", "Distinction", "Merit", "Pass", "Fail"}));
		comboBoxAverageGrades.setToolTipText("Displays students by their average grades.");
		comboBoxAverageGrades.setBounds(653, 516, 197, 27);
		frame.getContentPane().add(comboBoxAverageGrades);
		

	}
	
	//Converts JTable into an array
	public Object[][] getTableArray () {
	    DefaultTableModel model = (DefaultTableModel) table.getModel();
	    //Get the number of rows and columns
	    int nRow = model.getRowCount();
	    int nCol = model.getColumnCount();
	    //Create new array
	    Object[][] tableData = new Object[nRow][nCol];
	    //For each cells write data to array
	    for (int i = 0 ; i < nRow ; i++)
	        for (int j = 0 ; j < nCol ; j++)
	            tableData[i][j] = model.getValueAt(i,j);
	    		return tableData;
	}
	
	//Calculates average overall mark for a subject
	public void AverageCalc2(int nCol){
		//Get tablemodel
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		//Get table data in array form
		Object tableData[][] = getTableArray();
		//declare two floats, sum and average. set them to 0
		float sum = 0;
		float average = 0;
		//Add up all the marks in the column
		for(int i = 0; i< model.getRowCount();i++){
			sum += (Float) tableData[i][nCol];
		}
			//Divide sum by number of rows to get average
			average = sum / model.getRowCount();
		//Display the correct message depending on which button was pressed.
		if (nCol == 5){
			JOptionPane.showMessageDialog(frame, "The average overall Computer Science Mark is " + average);
		}
		if (nCol == 6){
			JOptionPane.showMessageDialog(frame, "The average overall Maths Mark is " + average);
		}
		if (nCol == 7){
			JOptionPane.showMessageDialog(frame, "The average overall English Mark is " + average);
		}
		
	}
	
	//Saves the Table model to a text file
	public void save(){
		//Try to catch empty JTables
		try{
			//Declare a file location
			File file = new File(System.getProperty("user.dir") + "/bin/DataBases/data.txt");
			//If the file does not exist then create the file
			if(!file.exists()){
				file.createNewFile();
			}
			//Declare a FileWriter and BufferedWriter to write in file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Write each cell to a text file separated by "   "
			for(int i = 0; i < table.getRowCount(); i++){
				for(int j = 0; j < 8; j++){
					if (table.getModel().getValueAt(i, j) == ""){
						bw.write("");
					}else{
						bw.write(table.getModel().getValueAt(i, j) + "   ");
					}
				}
				//At the end of each line write a line break
				bw.write("\n");
			}
			bw.close();
			fw.close();
			
		}catch(Exception ex){
			//ex.printStackTrace();
			}
	}
	
	//Calculates the average of the 3 marks for a particular student
	public void studentAverage(){
		//If a student is selected
		if(table.getSelectedRow() != -1){
			//Try to catch empty JTables
			try{
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				//Get the selected row
				int studentRowIndex = table.getSelectedRow();
				//Set variables as the selected rows marks
				float CS = (Float) model.getValueAt(studentRowIndex, 5);
				float math = (Float) model.getValueAt(studentRowIndex, 6);
				float eng = (Float) model.getValueAt(studentRowIndex, 7);
				
				float StudentAverage = 0;
	            
	            //add up all the marks
				float sum = CS + math + eng;
				//and divide them by 3 to get the average
				StudentAverage = sum / 3 ;
	            //Display the information in a dialog box
	            JOptionPane.showMessageDialog(frame, "Student " + Integer.toString( (Byte)table.getModel().getValueAt(table.getSelectedRow(), 0))+ ": ("+ (String) table.getModel().getValueAt(table.getSelectedRow(), 1) + ") scored an average of " +StudentAverage + " marks.");
			}catch(Exception e){
			}
		}else{
			//Display message saying that no student is selectd
			JOptionPane.showMessageDialog(null, "Please select a record to see the student's average mark across 3 subjects!");
		}
	}
	//Class based student information gather
		/*if(table.getSelectedRow() != -1){
			try{
				String filePath = System.getProperty("user.dir") + "/bin/DataBases/data.txt";
				File file = new File(filePath);
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				BufferedReader in = new BufferedReader(new FileReader(file));
				Scanner sc = new Scanner(file);
				List<student> students= new ArrayList<student>();
				int count =0;
				while(sc.hasNextLine()){
					String line = sc.nextLine();
					String[] details = line.split("   ");
					int id =  Integer.parseInt(details[0]);
					String name = details[1];
					String gender = details[2];
					String address = details[3];
					String postcode = details[4];
					float CS = Float.valueOf(details[5]);
					float math = Float.valueOf(details[6]);
					float eng = Float.valueOf(details[7]);
					count = count + 1;
					student p = new student(id, name, gender, address, postcode, CS, math ,eng);
					students.add(p);
		            }
		            
		            float StudentAverage = 0;
		            float sum = 0;
		            for(int i= table.getSelectedRow();i< table.getSelectedRow()+1;i++){
		            	float CS = students.get(i).getCS();
		            	float math = students.get(i).getMath();
		            	float eng = students.get(i).getEng();
		            	sum = CS + math + eng;
		            	StudentAverage = sum / 3 ;
		            }
		            JOptionPane.showMessageDialog(frame, "Student " + Integer.toString( (Byte)table.getModel().getValueAt(table.getSelectedRow(), 0))+ ": ("+ (String) table.getModel().getValueAt(table.getSelectedRow(), 1) + ") scored an average of " +StudentAverage + " marks.");
			}catch (Exception e) {
				e.printStackTrace();
			}}else{
				JOptionPane.showMessageDialog(frame, "Select a student to see their average marks.");
	
			}
			}*/
	
	//Sets the ID number column's values equal to their row numbers
	public void setIdNumbers(){
        DefaultTableModel model = (DefaultTableModel)table.getModel();
		for (int i = 0 ; i < table.getRowCount(); i++)
			model.setValueAt(i+1, i, 0);
	}
	


	
	//The grader method adds grades to the tables in the 9th, 10th and 11th column. The grades are based on the marks received in the relevant exams.
	public void grader(){
			//Surrounded in a try and catch to handle any errors that may occur.
			try{
				//Creates local variable for the table data. It is a 2d object array containing each cells' data as each element. 
				Object tableData[][] = getTableArray();

				//Three arrays are created and a place for them in the memory is allocated. They can contain a maximum of 20 elements.
				Float[] CSMark = new Float[20];
	            Float[] mathMark = new Float[20];
	            Float[] engMark = new Float[20];
	            Float[] averageMark = new Float[20];
	            
	            //This for loop assigns the computer science marks from the 2d array to the CSMark array
	            for(int i = 0 ; i <table.getRowCount(); i++ ){
	         	   CSMark[i]=  (Float) tableData[i][5];
	            }
	            //This for loop assigns the math marks from the 2d array to the mathMark array
	            for(int i = 0 ; i <table.getRowCount(); i++ ){
	         	  
	            	mathMark[i]= (Float)  tableData[i][6];
	            }
	            //This for loop assigns the English marks from the 2d array to the CSMark array
	            for(int i = 0 ; i <table.getRowCount(); i++ ){
	         	   
	            	engMark[i]=  (Float) tableData[i][7];
	            }
	            
	            for(int i = 0 ; i <table.getRowCount(); i++ ){
		         	   
	            	averageMark[i]=  (float) ((((Float) tableData[i][5]) + ((Float)  tableData[i][6]) + ((Float) tableData[i][7]))/3);
	            }
				DefaultTableModel model = (DefaultTableModel) table.getModel();

	            //For every row
	            for(int i = 0 ; i <table.getRowCount(); i++ ){
	            	//If the particular value is less than 50 print Fail in that rows 9th column
	         	   if (CSMark[i]<50){
	            		model.setValueAt("Fail", i, 8);
	            	}
	         	   //If the particular value is less than 50 print Pass in that rows 9th column
	            	if ((CSMark[i]>=50)&&(CSMark[i]<70)){
	            		model.setValueAt("Pass", i, 8);
	            	}
	            	//If the particular value is less than 50 print Merit in that rows 9th column
	            	if ((CSMark[i]>=70)&&(CSMark[i]<90)){
	            		model.setValueAt("Merit", i, 8);
	            	}
	            	//If the particular value is less than 50 print Distinction in that rows 9th column
	            	if (CSMark[i]>=90){
	            		model.setValueAt("Distinction", i, 8);
	            	}
	            }
	            //The same technique is applied for the Math and English grades
	            for(int i = 0 ; i <table.getRowCount(); i++ ){
	         	   if (mathMark[i]<50){
	            		model.setValueAt("Fail", i, 9);
	            	}
	            	if ((mathMark[i]>=50)&&(mathMark[i]<70)){
	            		model.setValueAt("Pass", i, 9);
	            	}
	            	if ((mathMark[i]>=70)&&(mathMark[i]<90)){
	            		model.setValueAt("Merit", i, 9);
	            	}
	            	if (mathMark[i]>=90){
	            		model.setValueAt("Distinction", i, 9);
	            	}
	            }
	            
	            for(int i = 0 ; i <table.getRowCount(); i++ ){
	         	   if (engMark[i]<50){
	            		model.setValueAt("Fail", i, 10);
	            	}
	            	if ((engMark[i]>=50)&&(engMark[i]<70)){
	            		model.setValueAt("Pass", i, 10);
	            	}
	            	if ((engMark[i]>=70)&&(engMark[i]<90)){
	            		model.setValueAt("Merit", i, 10);
	            	}
	            	if (engMark[i]>=90){
	            		model.setValueAt("Distinction", i, 10);
	            	}
	            }
	            
	            for(int i = 0 ; i <table.getRowCount(); i++ ){
	            	if (averageMark[i]<50){
	            		model.setValueAt("Fail", i, 11);
	            	}
	            	if ((averageMark[i]>=50)&&(averageMark[i]<70)){
	            		model.setValueAt("Pass", i, 11);
		            }
		            if ((averageMark[i]>=70)&&(averageMark[i]<90)){
		         		model.setValueAt("Merit", i, 11);
		           	}
		           	if (averageMark[i]>=90){
		           		model.setValueAt("Distinction", i, 11);
		           	}
	            }
	        /*If the try encounters any problems executing the code above it will handle the error with a message box 
	         * instead. The main error that can occur here is trying to cast non-numeric values as floats, however prior
	         * validation stopped this from happening. The only way this can occur is if the user edits the text file 
	         * and tries to open the program.
	         */  
			}catch(Exception e){
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Can not add grades. There is no data in the table.");
			}	
	}
			
	/*The "highest" method displays the student with the highest amount of marks in a particular subject. It requires 
	 * one parameter which will serve as the index number of the column that will be analysed.
	 */
	public void highest(int nCol){
		//Resets table to default
		clearSearch();
		//Creates table model
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		//min is set equal to the first rows value
		Float max = (Float) model.getValueAt(0, nCol); //tableData[0][nCol];
		for (int i = 1; i < table.getRowCount(); i++) {
			//IF the current row value is smaller than the variable min THEN 
			if ((Float) model.getValueAt(i, nCol) > max) {
				//Assign current row value to min
		    	max = (Float)model.getValueAt(i, nCol);
		    }
		}
		//Shows every record with the value of min as nCol.
		//Table Row Sorter is used on table
		TableRowSorter<DefaultTableModel> tableSorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(tableSorter);
		//If the user clicks the CS button
		if (nCol == 5){
			//Print CS related message
			JOptionPane.showMessageDialog(null, "The highest scoring Computer Science Students are:");
		}
		//If the user clicks the Math button
		if (nCol == 6){
			//Print Math related message
			JOptionPane.showMessageDialog(null, "The highest coring Maths Students are:");
		}
		//If the user clicks the English button
		if (nCol == 7){
			//Print English related message
			JOptionPane.showMessageDialog(null, "The highest scoring English Students are:");
		}
		//Searches for all values equal to min in the relevant column
		tableSorter.setRowFilter(RowFilter.regexFilter("(?i)\\b" + max.toString() + "\\b", nCol));
	}
	
	/* The "lowest" method displays the student with the lowest amount of marks in a particular subject. It requires 
	 * one parameter which will serve as the index number of the column that will be analysed. It uses the same program
	 * flow as the highest method, but uses "<" to take the minimum amount of marks.
	 */
	public void lowest(int nCol){
		//Resets table to default
		clearSearch();
		//Creates table model
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		//min is set equal to the first rows value
		Float min = (Float) model.getValueAt(0, nCol); //tableData[0][nCol];
		for (int i = 1; i < table.getRowCount(); i++) {
			//IF the current row value is smaller than the variable min THEN 
			if ((Float) model.getValueAt(i, nCol) < min) {
				//Assign current row value to min
		    	min = (Float)model.getValueAt(i, nCol);
		    }
		}
		//Shows every record with the value of min as nCol.
		//Table Row Sorter is used on table
		TableRowSorter<DefaultTableModel> tableSorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(tableSorter);
		//If the user clicks the CS button
		if (nCol == 5){
			//Print CS related message
			JOptionPane.showMessageDialog(null, "The lowest scoring Computer Science Students are:");
		}
		//If the user clicks the Math button
		if (nCol == 6){
			//Print Math related message
			JOptionPane.showMessageDialog(null, "The lowest coring Maths Students are:");
		}
		//If the user clicks the English button
		if (nCol == 7){
			//Print English related message
			JOptionPane.showMessageDialog(null, "The lowest scoring English Students are:");
		}
		//Searches for all values equal to min in the relevant column
		tableSorter.setRowFilter(RowFilter.regexFilter("(?i)\\b" + min.toString() + "\\b", nCol));

	}
	
	
	/*
	 * Edits an existing record
	 */
	public void modifier(){
		//If no record is selected display error message.
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		try {
		
		if (table.getSelectedRow() == -1){
			JOptionPane.showMessageDialog(null, "Please select a student to modify!");
			
		}else{
			
				//Input boxes to store data. The correct data types are used to validate the input. 
				

				byte ID =  (byte) (table.getSelectedRow() +1); 
	            String[] Genders = {"Male", "Female"};
	                
	            NullPointerException noName = new NullPointerException() ;
	            String name = JOptionPane.showInputDialog("Name:");
	            if (name.equals("")){
	            	
	                JOptionPane.showMessageDialog(frame, "Invalid Input");
	                throw noName;
	            }
	            String gender = (String) JOptionPane.showInputDialog(null, "Gender:", "Selection", JOptionPane.DEFAULT_OPTION, null, Genders, "0");
	            String address = JOptionPane.showInputDialog("Address:");
	            if (address.equals("")){
	            	
	            	JOptionPane.showMessageDialog(frame, "Invalid Input");
	            	throw noName;
	            }
	          	String postcode = JOptionPane.showInputDialog("Postcode:");
	         	if (postcode.equals("")){
	              	
	              	JOptionPane.showMessageDialog(frame, "Invalid Input");
	             	throw noName;
	          	}
	           	float CS = Float.parseFloat(JOptionPane.showInputDialog("Computer Science Marks:"));
	               // markRangeValidation(CS);
	        	if (CS<0 || CS>100){
	              	
	               	JOptionPane.showMessageDialog(frame, "Marks must be decimal numbers between 0 and 100");
	               	throw noName;
	        	}
	          	float math =Float.parseFloat(JOptionPane.showInputDialog("Maths Marks:"));
	             //   markRangeValidation(math);
	          	if (math<0 || math>100){
	          		
	             	JOptionPane.showMessageDialog(frame, "Marks must be decimal numbers between 0 and 100");
	             	throw noName;
	         	}
	         	float eng = Float.parseFloat(JOptionPane.showInputDialog("English Marks:"));
	         	//   markRangeValidation(eng);
	          	if ( eng < 0 || eng > 100){
	          		
	             	JOptionPane.showMessageDialog(frame, "Marks must be decimal numbers between 0 and 100");
	             	throw noName;
	          	}
	             
				//Array of strings containing data.
				Object[] arrayRecord = {ID, name, gender, address, postcode, CS, math, eng};
				 
				//	if ( Integer.valueOf((String) model.getValueAt(table.getRowCount()-1, 0)) == table.getRowCount()){
				//Add new string to JTable in place of selected record.
				 for (int i =0; i< 8; i++){
					 model.setValueAt(arrayRecord[i], table.getSelectedRow(), i);
				 }
			}}
			//If the data inputed is not the correct data type then an error message is displayed. This validates the input.
			catch(Exception ex){
				JOptionPane.showMessageDialog(frame, "invalid");
				//ex.printStackTrace();
			}
		
	}
	
	/*This method adds new students to the table. It ensures that no more than 20 students are in the table before adding
	 *  a new one. It also validates the inputs making sure they are inputed using the correct data type.
	 */
	public void studentAdder(){
		//Creates model variable
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		//If there are less than 20 students add new student
		if (model.getRowCount() < 20){
			//Try catches incorrect inputs. If user tries to enter a word as a mark the error is handled.
			try {
				//An array that will contain the user input
				Object[] dataR = new Object[8];
				//ID is equal to the row number
		        byte ID =  (byte)(table.getRowCount()+1); 
		        //User options for an input combo box
		        String[] Genders = {"Male", "Female"};
		        //dataR ID equal to the row number
		        dataR[0] = ID;
		        //A dialog boxes are used to get the users' input. If they are blank an error message occurs
		        dataR[1] = JOptionPane.showInputDialog("Name:");
		        if (dataR[1].equals("")){
		        	 NullPointerException noName = new NullPointerException() ;
		        	 JOptionPane.showMessageDialog(frame, "The textfield was left blank");
		        	 throw noName;
		        }
		        //The user is given the option to select male or female for a combo box
		        dataR[2] = JOptionPane.showInputDialog(null, "Gender:", "Selection", JOptionPane.DEFAULT_OPTION, null, Genders, "0");
		        
		        dataR[3] = JOptionPane.showInputDialog("Address:");
		        if (dataR[3].equals("")){
		        	 NullPointerException noName = new NullPointerException() ;
		        	 JOptionPane.showMessageDialog(frame, "The textfield was left blank");
		        	 throw noName;
		        }
		        dataR[4] = JOptionPane.showInputDialog("Postcode:");
		        if (dataR[4].equals("")){
		        	 NullPointerException noName = new NullPointerException() ;
		        	 JOptionPane.showMessageDialog(frame, "The textfield was left blank");
		        	 throw noName;
		        }
		        //The input in the marks are cast as float. If a non-numeric value is enter the try and catch will \
		        //handle the error with an error message
		        float CS = Float.parseFloat(JOptionPane.showInputDialog("Computer Science Marks:"));
		       
		        //Validation to ensure the marks entered are between 0 and 100
		        if (CS<0 || CS>100){
		        	NullPointerException noName = new NullPointerException() ;
		        	 JOptionPane.showMessageDialog(frame, "Marks must be decimal numbers between 0 and 100");
		        	 throw noName;
		        }
		        float math =Float.parseFloat(JOptionPane.showInputDialog("Maths Marks:"));
		     
		        if (math<0 || math>100){
		        	NullPointerException noName = new NullPointerException() ;
		        	 JOptionPane.showMessageDialog(frame, "Marks must be decimal numbers between 0 and 100");
		        	 throw noName;
		        }
		        float eng = Float.parseFloat(JOptionPane.showInputDialog("English Marks:"));
		    
		        if ( eng < 0 || eng > 100){
		        	 NullPointerException noName = new NullPointerException() ;
		        	 JOptionPane.showMessageDialog(frame, "Marks must be decimal numbers between 0 and 100");
		        	 throw noName;
		        	 
		        }
		        //Adds marks to dataR
		        dataR[5] = CS;
		        dataR[6] =  math;
		        dataR[7] = eng;
		        
		
				//If the last cell in the first column equals the number of total rows:
		        if (model.getValueAt(table.getRowCount()-1, 0).equals((byte)(table.getRowCount())) ){
					//Then add the array to the JTable and
		        	model.addRow(dataR);	
					//Update the text file
					save();
				//Otherwise(the last cell in the first column does not equal the number of total rows)
				}else{
					//For loop from 0 to number of numbers. S as a counter.
					for(byte s = 0;  s < table.getRowCount() ; s++){
						//If value cell s,0 is not equal to the counter plus 1(to account for indexes)
						//The value is of a student that has been removed from the system beforehand. In this case,
						if (!( (Byte)(model.getValueAt(s, 0)) == (s+1) )){
							//set new ID number equal to row number
							dataR[0] = s+1; 
							//An integer variable to use for the insert row method(since s is a byte)
							int rowInteger = s;
							//Inserts row in the correct place with the user input
							model.insertRow(rowInteger, dataR);	
							//Updates text file
							save(); 
							//Exit loop since the loops task has now been completed
							break;}
							}
					}
			//Catches errors. If incorrect data type is entered error message occurs. This validates the user input
		      	}catch(Exception ex){
		      		JOptionPane.showMessageDialog(frame, "Please enter all text fields correctly");
		      		//ex.printStackTrace();
		      	}
		//If there are 20 students in the database show an error message 
		}else{
			JOptionPane.showMessageDialog(frame, "A maximum of 20 Students are allowed in the Database.");
		}
	}

	public void importer(){
		//Import Database
				//Create variable file that holds the text file location.
				String filePath = System.getProperty("user.dir") + "/bin/DataBases/data.txt";
				File file = new File(filePath);
				
		//reads text file
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			//Buffered Reader used to read text file.
			BufferedReader br = new BufferedReader(new FileReader(file));
	           
			//The array columnsName is set to hold the headings of the 11 columns in the JTable.
			String[] columnsName = "ID Number, Name, Gender, Address, Postcode, CS Marks, Maths Marks, English Marks, CS Grade, Math Grade, English Grade, Average Grade".split(", ");
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			model.setColumnIdentifiers(columnsName);
	            
/*
			//Gets each line of the text file and stores it as the array tableLine[]
			//.lines() method is not available in java 1.7
			Object[] tableLines = br.lines().toArray();
			//If tableLines[] contains more than 20 student records print an error message informing the user that only the first 20 student have been imported.
			if (tableLines.length > 20){
				JOptionPane.showMessageDialog(frame, "There are " + tableLines.length + " Students. A maximum of 20 are allowed.\n The first 20 have been imported.");
			}
*/
			String[] tableLines = new String[20];
			String textFileLine = br.readLine();
			int lineNumber = 0;
			while( (textFileLine != null) ){
				if(textFileLine.contains("eof")){
					break;
				}
				
				if (lineNumber > 19){
					JOptionPane.showMessageDialog(null, "The data.txt file contains more than 20 students.\nOnly the first 20 have been added to the database");
					break;

				}
				tableLines[lineNumber] = textFileLine;
				
				lineNumber =  lineNumber + 1;
				textFileLine = br.readLine();
				
					
				
			}
			//A for loop to look at each line of of the text file.
			for(int i = 0; i < lineNumber; i++)
	            {
	            	//Extract data from lines
					//A new variable line is created. It uses the type string. It is equal to the current line.
	                String line = tableLines[i].toString().trim();
	                //The variable line is split up by the "   " into individual elements of the array dataRow.
	                String[] dataRow = line.split("   ");
	                //A object cellData is initialised. It will hold the values of dataRow but in their correct data types
	                Object[] cellData = new Object[8];
	                
	                //If the current line contains the string "eof" break the for loop (stops importing data)
	                if (line.contains("eof")){
	             		
	              		break;
	               	}
	               
	                //Students ID number is equal to the number of their row
	                //byte ID = (byte)  (table.getRowCount()+1);
	                byte ID = Byte.parseByte(dataRow[0]);
	                cellData[0] = ID;
	                //Object cellData is now a string from dataRow[1] (Name)
	                cellData[1] = dataRow[1];
	                //Object cellData is now a string from dataRow[2] (Gender)
	                cellData[2] = dataRow[2];
	                //Object cellData is now a string from dataRow[3] (Address)
	                cellData[3] = dataRow[3];
	                //Object cellData is now a string from dataRow[4] (Post code)
	                cellData[4] = dataRow[4];
	                //Marks are set equal to their respective float variables
	                float CS = 0;
	                float math = 0;
	                float eng = 0;
	                if (Float.parseFloat(dataRow[5]) >= 0 && Float.parseFloat(dataRow[5]) <= 100 && Float.parseFloat(dataRow[6]) >= 0 && Float.parseFloat(dataRow[6]) <= 100 && Float.parseFloat(dataRow[7]) >= 0 && Float.parseFloat(dataRow[7]) <= 100){
	                	CS = Float.parseFloat(dataRow[5]);
	                	math = Float.parseFloat(dataRow[6]);
	                	eng = Float.parseFloat(dataRow[7]);
	                }else{
	                	grader();
	                	IOException outOfRange = new IOException() ;
		             	JOptionPane.showMessageDialog(frame, "Marks in the textfile must be decimal numbers between 0 and 100. PLease check the text file.");
		             	throw outOfRange;
	                }
	                
	                
	                
	                //Object cellData[5,6,7] is now a string float 
	                cellData[5] = CS;
	                cellData[6] = math;
	                cellData[7] = eng;
	                
	                //If i is bigger than 19 (20 is the maximum number of students), then break loop(stop importing)
	                if (i > 19){
	                	break;
	                }
	                
	                //Add the array cellData to the table model. (Adds the row from the text file to the table)
	                model.addRow(cellData);
	            }
	            
				//Close Buffered Reader
				br.close();
				//Adds the grades Distinction, Merit, Pass, or Fail to the table in all three column. See grader() for more info.
				grader();
	        //Catches any errors so program does not crash. Prints error in consoles, displays error message, and loads blank JTable.
	        } catch (Exception ex) {
	        	//ex.printStackTrace();
	        	table= null;
	        	JOptionPane.showMessageDialog(null, "There was an error loading the text file. Ensure you have enter the marks correctly!");

	        }
	}
	
	public void multiSearch(){
		//Variables that equal their search box values. The values are taken as strings and then handled later.
		
		String queryID = txtID.getText();
		String queryName= txtName.getText();
		String queryGender = txtGender.getText();
		String queryAddress= txtAddress.getText();
		String queryPostcode= txtPostcode.getText();
		String queryComputerScience= txtComputerScience.getText();
		String queryMaths= txtMaths.getText();
		String queryEnglish= txtEnglish.getText();
		//model is imported to create a Row sorter 
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        //Row sorter used for search to call filters on.
		TableRowSorter<DefaultTableModel> tableSorter = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(tableSorter);
		//An Array list of searches is created, each Regex Filter searches the relevant column. The searches are then combined using an AND filter
		ArrayList<RowFilter<Object, Object>> listOfAndFilters = new ArrayList<RowFilter<Object,Object>>(12);
		ArrayList<RowFilter<Object, Object>> listOfOrFilters = new ArrayList<RowFilter<Object,Object>>(12);
		ArrayList<RowFilter<Object, Object>> listOfCombinationFilters = new ArrayList<RowFilter<Object,Object>>(12);
		

		//Filters
		/*The regular expression filter returns all rows that have at least one cell that contains the first argument attached to the method
		This makes it perfect for searching tables. The second argument of of the regex filter states in which column 
		index the search should be performed in.*/
		//In the first regex filter is associated with the the ID number text box. It searches the for the entered value in the first (ID) column.
		//If the text in the ID text box is contained in any of the cells in the first column, those rows will be returned.
		/*"\\b" Creates a bound for the search. The bound stops the search from returning rows that contain the search value with additional characters
		 * For example if male is searched it would return rows that contain the term female because the word male is 
		 * part of the word female. If a bound is put in front of male only male when be returned
		 * */
		//The (?i) indicates that the search will be case insensitive
		//try and catch the catches incorrect regex inputs
		try{
			listOfAndFilters.add(RowFilter.regexFilter("\\b" +queryID+"\\b", 0));
			listOfAndFilters.add(RowFilter.regexFilter("(?i)\\b" +queryName, 1));
			//This regex filters the gender. Word boundaries are set up to filter for only the word. If they are not the search "male" would return feMALE.
			listOfAndFilters.add(RowFilter.regexFilter("(?i)\\b"+queryGender+"\\b"  , 2));
			listOfAndFilters.add(RowFilter.regexFilter("(?i)" +queryAddress , 3));
			listOfAndFilters.add(RowFilter.regexFilter("(?i)" +queryPostcode, 4));
			//If the computer science text field contains the character "-"/
			String[] parts = new String[2];
			if (queryComputerScience.contains("-")){
				//The string is split into two parts through the - and stored as an array.
				parts = queryComputerScience.split("-");
				
				try{
					//number1 is the first part of the array. It is parse as a floating point number
					Float number1 = Float.parseFloat(parts[0]);
					//number2 is the second part of the array. It is parse as a floating point number
					Float number2 = Float.parseFloat(parts[1]);
					/*If number1 is smaller than number2 then all the rows with the CS Mark above number1 
					 * and below number2 are displayed in the Jtable. In addition number1 and number2 are 
					 * displayed.
					 */
					if (number1 < number2){
						/*Two number filters are added one that returns values above the first digit 
						 * and the second returns values under the second digit.
						 * 
						 * The comparison types AFTER and BEFORE are not inclusive and therefore 
						 * have to be added to a different filter list. This is because no value is
						 * Equal to itself AND larger OR smaller than itself. If the filters were in 
						 * the same list no values would be returned.
						 */
						listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.AFTER, number1 , 5));
						listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.BEFORE, number2  , 5));
						listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number1  , 5));
						listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number2  , 5));
					}else{
						/*If number1 is larger then the row are returned that have a value in that column
						 * less than number1 and larger than number2. The inclusive values are also added.
						 */
						listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.AFTER, number2  , 5));
						listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.BEFORE, number1 , 5));
						listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number1  , 5));
						listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number2 , 5));
					}
				//Catches incorrect inputs (anything other than "number"-"number") and displays an error message
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "To search between two values enter a number followed by a '-' followed by another number. E.g 4-40");
				}
				
			// If there is no "-" character then 
			}else{
				//If the CS mark search bar is not empty
				if (!queryComputerScience.equals("")){
					//Declare markCS as integer and assign value of the CS text box
					float markCS = Float.parseFloat(queryComputerScience);
					//Add a number filter for exactly that number
					listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, markCS, 5));
				}
			}
			//The same logic is applied to the math search bar. Since they use the same lists the values returned are combined together
			if (queryMaths.contains("-")){
				parts = queryMaths.split("-");
				try{
						Float number1 = Float.parseFloat(parts[0]);
						Float number2 = Float.parseFloat(parts[1]);
						if (number1 < number2){
							
							listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.AFTER, number1 , 6));
							listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.BEFORE, number2  , 6));
							listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number1  , 6));
							listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number2  , 6));
						}else{
							listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.AFTER, number2  , 6));
							listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.BEFORE, number1 , 6));
							listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number1  , 6));
							listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number2 , 6));
						}
					
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, "To search between two values enter a number followed by a '-' followed by another number. E.g 4-40");
					}
			}else{
				//If the Math mark search bar is not empty
				if (!queryMaths.equals("")){
					//Declare markMath as integer and assign value of the Math text box
					float markMath = Float.parseFloat(queryMaths);
					//Add a number filter for exactly that number on the Math column
					listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, markMath, 6));
				}
			}
			//The same logic is applied to the english search bar. Since they use the same lists the values returned are combined together
			if (queryEnglish.contains("-")){
				parts = queryEnglish.split("-");
				try{
					float number1 = Float.parseFloat(parts[0]);
					float number2 = Float.parseFloat(parts[1]);
					if (number1 < number2){
						
						listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.AFTER, number1 , 7));
						listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.BEFORE, number2  , 7));
						listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number1  , 7));
						listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number2  , 7));
					}else{
						listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.AFTER, number2  , 7));
						listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.BEFORE, number1 , 7));
						listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number1  , 7));
						listOfOrFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, number2 , 7));
					}
					//catches incorrect values when searching in between two numbers
					}catch(Exception e){
						JOptionPane.showMessageDialog(null, "To search between two values enter a number followed by a '-' followed by another number. E.g 4-40");
						//e.printStackTrace();
					}
			}else{
				//If the English mark search bar is not empty
				if (!queryEnglish.equals("")){
					//Declare markEng as integer and assign value of the Math text box
					float markEng = Float.parseFloat(queryEnglish);
					listOfAndFilters.add(RowFilter.numberFilter(ComparisonType.EQUAL, markEng, 7));
				}
			}
			//Get values from combo boxes and search if value is not "-"
			String CSGrade = (String) comboBoxCSGrade.getSelectedItem();
			if (CSGrade.equals("-")){
				
			}else{
				listOfAndFilters.add(RowFilter.regexFilter(CSGrade, 8));
	
			}
			
			String mathsGrades = (String) comboBoxMathsGrades.getSelectedItem();
			if (mathsGrades.equals("-")){
				
			}else{
				listOfAndFilters.add(RowFilter.regexFilter(mathsGrades, 9));
	
			}
			
			String englishGrades = (String) comboBoxEnglishGrades.getSelectedItem();
			if (englishGrades.equals("-")){
				
			}else{
				listOfAndFilters.add(RowFilter.regexFilter(englishGrades, 10));
	
			}
			
			String averageGrades = (String) comboBoxAverageGrades.getSelectedItem();
			
			if (averageGrades.equals("-")){
				
			}else{
				listOfAndFilters.add(RowFilter.regexFilter(averageGrades, 11));
			}
			
			/*
			 * An and filter is created. The filter applies the logical function AND to all the filters and returns the rows 
			 * that intersection the constrains. Essentially applying all filters in listOfFilters at once.
			 */
			RowFilter<Object, Object> andFilter;
			RowFilter<Object, Object> orFilter;
			RowFilter<DefaultTableModel, Object> comboFilter;
			
			andFilter = RowFilter.andFilter(listOfAndFilters);
			orFilter = RowFilter.orFilter(listOfOrFilters);
			
			listOfCombinationFilters.add(andFilter);
			listOfCombinationFilters.add(orFilter);
			
			comboFilter = RowFilter.orFilter(listOfCombinationFilters);
			//andFilter.add((RowFilter.regexFilter(averageGrades, 11)));
			tableSorter.setRowFilter(comboFilter);
		}catch(Exception nonNumeric){
			JOptionPane.showMessageDialog(null, "Please enter valid characters into the search bars");
			//nonNumeric.printStackTrace();
		}
	}
	
	//This method is removes students' records from the table.
		public void remove(){
			//Validation for when there is no record selected. If no record is selected an error message is displayed.
			//-1 is the index of the row that is out of bounds
			if (table.getSelectedRow() == -1){
				JOptionPane.showMessageDialog(null, "Please select a record to remove!");
			}else{
				//Get the table's model
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				 //Array of selected row indexes
				int[] rows = table.getSelectedRows();
				
				for(int i=0;i<rows.length;i++){
					 //Removes selected row(s).
					//row[i]-i will always return the same number. The program must account for a row being lost after an iteration 
				     model.removeRow(rows[i]-i);
				}
			}
		}
		//Returns all search boxes to empty and performs an empty search to reset the JTable
		public void clearSearch(){
			txtID.setText("");
			txtName.setText("");
			txtGender.setText("");
			txtAddress.setText("");
			txtPostcode.setText("");
			txtComputerScience.setText("");
			txtMaths.setText("");
			txtEnglish.setText("");
			comboBoxCSGrade.setSelectedIndex(0);
			comboBoxMathsGrades.setSelectedIndex(0);
			comboBoxEnglishGrades.setSelectedIndex(0);
			comboBoxAverageGrades.setSelectedIndex(0);
			 DefaultTableModel model = (DefaultTableModel)table.getModel();
				//Runs an empty search to restore the table model to its default state.
			 	TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(model);
				table.setRowSorter(tr);
				RowFilter<Object, Object> clear = RowFilter.regexFilter("");
				tr.setRowFilter(clear);
		}
}
