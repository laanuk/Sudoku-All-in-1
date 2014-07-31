package Sudoku;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;


@SuppressWarnings("serial")
public class Game extends JPanel implements TableModelListener {
	
	final static int WIDTH = 500;
	final static int HEIGHT = 500;
	static SudokuTable table;
	boolean enteringpuzzle = false;
	boolean puzzledisplayed = false;
	boolean blankpuzzle = false;
	String digits[] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
	static long dumbLinux = System.currentTimeMillis();
	static long solvestart;
	static boolean finalize = false;
	static DefaultTableCellRenderer centerRenderer;
	static int shuffled = 0;
	static int numswitched = 0;
	JButton button1;
	
	public int gensudoku[][] = new int[9][9];
	public int storedpuzzlestate[][] = new int[9][9];
	public int basepuzzle[][] = new int [][] {
		  { 2, 4, 8, 3, 9, 5, 7, 1, 6},
		  { 5, 7, 1, 6, 2, 8, 3, 4, 9},
		  { 9, 3, 6, 7, 4, 1, 5, 8, 2},
		  { 6, 8, 2, 5, 3, 9, 1, 7, 4},
		  { 3, 5, 9, 1, 7, 4, 6, 2, 8},
		  { 7, 1, 4, 8, 6, 2, 9, 5, 3},
		  { 8, 6, 3, 4, 1, 7, 2, 9, 5},
		  { 1, 9, 5, 2, 8, 6, 4, 3, 7},
		  { 4, 2, 7, 9, 5, 3, 8, 6, 1},
	};

	public String formattedsudoku[][] = new String[9][9];
	int num;


	public Game() {
		JFrame frame = new JFrame("Sudoku by Kunaal Naik");
		table = new SudokuTable(new SudokuTableModel());
	
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JScrollPane tableContainer = new JScrollPane(table);
        container.add(tableContainer, BorderLayout.CENTER);
        frame.getContentPane().add(container);
        
        JToolBar toolBar = new JToolBar("toolbar");
        container.add(toolBar, BorderLayout.SOUTH);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        
        DefaultCellEditor singleclick = new DefaultCellEditor(new JTextField());
        singleclick.setClickCountToStart(1);

        //set the editor as default on every column
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.setDefaultEditor(table.getColumnClass(i), singleclick);
        } 
        
        //JButton button1 = new JButton("ENTER YOUR OWN PUZZLE");
        
        button1 = new JButton( new AbstractAction("ENTER YOUR OWN PUZZLE") {
            @Override
            public void actionPerformed( ActionEvent e ) {
   				puzzledisplayed = false;
            	if (((JButton) e.getSource()).getText().equals("Done")) {
            		finalizepuzzle();
            		for(int r = 0; r < 9; r++)
                		for (int c = 0; c < 9; c++) {
                			if (table.getValueAt(r, c) == null) {
                				gensudoku[r][c] = 0;
                				storedpuzzlestate[r][c] = 0;
                			}
                			else {
                				gensudoku[r][c] =  Integer.valueOf((String) table.getValueAt(r, c));
                				storedpuzzlestate[r][c] = Integer.valueOf((String) table.getValueAt(r, c));
                			}
                		}
            		blankpuzzle = true;
            	}
            	else ((SudokuTableModel) table.getModel()).emptygrid();
            	enteringpuzzle = !enteringpuzzle;
                if (enteringpuzzle) ((JButton) e.getSource()).setText("Done");
                else ((JButton) e.getSource()).setText("ENTER YOUR OWN PUZZLE");
            }
        });
        
        toolBar.add(button1);
        
        JButton button2 = new JButton( new AbstractAction("GENERATE SUDOKU") {
            @Override
            public void actionPerformed( ActionEvent e ) {
   				puzzledisplayed = false;
   				blankpuzzle = true;
            	gengrid();
            }
        });
        toolBar.add(button2);
        
        JButton button3 = new JButton( new AbstractAction("SOLVE") {
            @Override
            public void actionPerformed( ActionEvent e ) {
            	if(blankpuzzle == true) {
            	recursivesolve();
            	
            	for(int r = 0; r < 9; r++)
        			for (int c = 0; c < 9; c++) {
        				formattedsudoku[r][c] = String.valueOf(gensudoku[r][c]);
        				table.setValueAt(formattedsudoku[r][c], r, c);
        			}
            	blankpuzzle = false;
            	}
            }
        });
        toolBar.add(button3);
        
        JButton button4 = new JButton( new AbstractAction("RESET") {
            @Override
            public void actionPerformed( ActionEvent e ) {
            	((SudokuTableModel)table.getModel()).reset();
            	for(int r = 0; r < 9; r++)
        			for (int c = 0; c < 9; c++) {
        				gensudoku[r][c] = storedpuzzlestate[r][c];
        			}
            	if(blankpuzzle == false) blankpuzzle = true;
            }
        });
        toolBar.add(button4);
        
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    for (int i = 0; i <9; i++){
	    	table.getColumnModel().getColumn(i).setPreferredWidth(WIDTH/9);
	    	table.setRowHeight(i, HEIGHT/10);
	    	centerRenderer = new DefaultTableCellRenderer();
	    	centerRenderer.setHorizontalAlignment( JLabel.CENTER );
	    	table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
	    }
		Container c = frame.getContentPane();
		Dimension d = new Dimension(WIDTH, HEIGHT);
		c.setPreferredSize(d); 
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		table.getModel().addTableModelListener(this);
	}

	public static void main(String[] args) throws InterruptedException {
		@SuppressWarnings("unused")
		final Game game = new Game();
		
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
	// 3 types of events - delete, insert, update
		if (e.getType() ==  TableModelEvent.UPDATE && ((double) System.currentTimeMillis() - dumbLinux > 50)) {
			boolean isValid = false;
			int row = e.getFirstRow();
	        int column = e.getColumn();
	        TableModel model = (TableModel)e.getSource();
	        String data = (String) model.getValueAt(row, column);
	        for (String s : digits) {
	        	if(data!= null && data.equals(s)) isValid = true;	
	        }
	        dumbLinux = System.currentTimeMillis(); 
	        if (isValid == false) table.setValueAt(null, row, column);
	        
	        if (isValid){
	        	for (int i  = 0; i < 9; i++) {
	        		if((String) table.getValueAt(i, column) != null) {
	        			if ((((String) table.getValueAt(i, column)).equals(data)) && i!= row) table.setValueAt(null, row, column);
	        		}
	        		if((String) table.getValueAt(row, i) != null) {
	        			if ((((String) table.getValueAt(row, i)).equals(data)) && i != column) table.setValueAt(null, row, column);
	        		}
	        	}
	        	int r = 3 * (row/3);
	        	int c = 3 * (column/3);

	        	// check box
	        	for (int boxr = r; boxr < r + 3; boxr++) {
	        		for (int boxc = c; boxc < c + 3; boxc++) {
	        			if((String) table.getValueAt(boxr, boxc) != null) {
	        				if ((((String) table.getValueAt(boxr, boxc)).equals(data)) && !(boxr == row && boxc == column)) {
	        					table.setValueAt(null, row, column);	
	        				}
	        			}
	        		}
	        	}
	        }
		}
	}
	
	public void finalizepuzzle() {
		finalize = true;
	}
	
	public void gengrid() {
		((SudokuTableModel) table.getModel()).emptygrid();
		((SudokuTableModel) table.getModel()).reset();
		//create a filled board
		shuffleboard();
		shuffled = 0;
		remove();
		
		for(int r = 0; r < 9; r++)
			for (int c = 0; c < 9; c++) {
				if (gensudoku[r][c] == 0) {
					formattedsudoku[r][c] = null;
					table.setValueAt(formattedsudoku[r][c], r, c);
				}
				else {
				formattedsudoku[r][c] = String.valueOf(gensudoku[r][c]);
				table.setValueAt(formattedsudoku[r][c], r, c);
				}
			}
		finalizepuzzle();
		puzzledisplayed = true;
	}
	
	public void shuffleboard() {
		//randomly perform transformations here
		int picktrans = (int) (Math.random() * 3);
		
		switch (picktrans) {
		case 0: switchcols(); 
		break;
		case 1: switchrows();
		break;
		case 2: switchnums();
		break;
		case 3: switchinnerbandsrow();
		break;
		case 4: switchinnerbandscol();
		break;
		}
		shuffled++;
		if (shuffled < 9001) shuffleboard();
	}
	
	public void remove() {
		//create puzzle through completed grid
		
		for (int r = 0; r < 9; r++)	
			for (int c = 0; c < 9; c++) {
				int rem = (int)(Math.random() + .7); //chance to remove
				if (rem == 1) gensudoku[r][c] = 0; 
				else gensudoku[r][c] = basepuzzle[r][c];
				storedpuzzlestate[r][c] = gensudoku[r][c];
			}
	}
	
	public void switchcols() {
	for (int r = 0; r < 9; r++)	
		for (int c = 0; c < 4; c++) {
			int temp = basepuzzle[r][c];
			basepuzzle [r][c] = basepuzzle [r][8-c];
			basepuzzle [r][8-c] = temp;
		}
	}
	
	public void switchrows() {
	for (int c = 0; c < 9; c++)	
		for (int r = 0; r < 4; r++) {
			int temp = basepuzzle[r][c];
			basepuzzle [r][c] = basepuzzle [8-r][c];
			basepuzzle [8-r][c] = temp;
		}
	}
	
	public void switchinnerbandsrow() {
		for (int c = 0; c < 9; c++)	{
				int temp = basepuzzle[3][c];
				basepuzzle [3][c] = basepuzzle [5][c];
				basepuzzle [5][c] = temp;
		}
	}
	
	public void switchinnerbandscol() {
		for (int r = 0; r < 9; r++)	{
				int temp = basepuzzle[r][3];
				basepuzzle [r][3] = basepuzzle [r][5];
				basepuzzle [r][5] = temp;
		}
	}
		
	public void switchnums() {
		int num1 = (int) (Math.random() * 9 + 1);
		int num2 = (int) (Math.random() * 9 + 1);
		if (num1 == num2) switchnums();
		for (int r = 0; r < 9; r++)	
			for (int c = 0; c < 9; c++) {
				if (basepuzzle[r][c] == num1) basepuzzle[r][c] = -2;
				if (basepuzzle[r][c] == num2) basepuzzle[r][c] = -1;
				if (basepuzzle[r][c] == -2) basepuzzle[r][c] = num2;
				if (basepuzzle[r][c] == -1) basepuzzle[r][c] = num1;
			}
		numswitched++;
		if (!(numswitched > 4)) switchnums();
		else numswitched = 0;	
	}
	
	public int pickvalue(int r, int c, int v) {
		v++;
		if (v > 9) return 0; // if no value then backtrack
		
		//check for conflicts
		for(int i = 0; i < 9; i++) {
			if (v == gensudoku [r][i] && i!= c) return pickvalue(r, c, v); //check row conflicts
			if (v == gensudoku [i][c] && i!=r) return pickvalue(r, c, v); //check col conflicts
		}
    	int row = 3 * (r/3);
    	int col = 3 * (c/3);
    	for (int boxr = row; boxr < row + 3; boxr++) {
    		for (int boxc = col; boxc < col + 3; boxc++) {
    			if (v == gensudoku [boxr][boxc] && !(boxc == c && boxr == r)) return pickvalue(r, c, v); //check box conflicts
    		}
    	}
    	boolean onlyone = false;
    	//SPEED UP ALGORITHM RULES
    	if (v != 9) { // check for when v can only have one value
    		//if(pickvalue(r,c,v) == 0) table.
    	}
    	
    	
    	
		return v; // if number is valid then try it out
	}
	
	public void recursivesolve() {
		int r = 0; int c = 0; boolean forward = true;
		solvestart = System.currentTimeMillis();
		do {
	    // solve code
			if(r == -1) { //user enters invalid puzzle display message and exit
				button1.setText("Invalid Puzzle!");
				break;
			}
			if(c == 0 && r == 9) break;
			if(!(table.isCellEditable(r,c))) {
				if (forward) { //solving forward
					if(c == 8) {
						r++; c = 0; // if at end of col go to next row first cell
					}
					else c++; // go to next cell in row
				}
				else { //backtracking
					if(c == 0) {
						c = 8; r--; // if at beginning of col go to previous col last cell
						}
					else c--; // go to previous cell in row
				}
			}
			else {
				gensudoku[r][c] = pickvalue(r,c, gensudoku[r][c]);	
				if(gensudoku [r][c] == 0) { //if no value then backtrack
					forward = false;
					if(c == 0) {
						c = 8; r--; // if at beginning of col go to previous col last cell
					}
					else c--; // go to previous cell in row	
				}
				else {
					forward = true;
					if(c == 8) {
						r++; c = 0; // if at end of col go to next row first cell
					}
					else c++; // go to next cell in row
				}
			}
		} while(true);
		System.out.println("Solved in " + (double) (System.currentTimeMillis() - solvestart) + " milliseconds Ayole");
	}
}
