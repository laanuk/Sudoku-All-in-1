package Sudoku;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class SudokuTable extends JTable {
	int rowc;
	int colc;
	
	public SudokuTable(SudokuTableModel sudokuTableModel) {
		super(sudokuTableModel);
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
		Component c = super.prepareRenderer(renderer, row, col);
		JComponent jc = (JComponent)c;

		// color code
		//if (Game.table.isCellSelected(row, column))
			//c.setBackground(Color.RED);
		
		if (row == 2 || row == 5) rowc = 3;
		else rowc = 0;
		if (col == 2 || col == 5) colc = 3;
		else colc = 0;
	
		
		if (rowc == 3 || colc == 3)
			jc.setBorder(BorderFactory.createMatteBorder(0, 0, rowc, colc, Color.BLACK)); 
		else
			  jc.setBorder(null);
	
		
		
		if (!this.isCellEditable(row, col)) {
			c.setFont(  c.getFont().deriveFont(Font.BOLD) );
			c.setFont(  c.getFont().deriveFont((float) 15.0) );
		}
		else {
			c.setFont(  c.getFont().deriveFont(Font.PLAIN) );
			c.setFont(  c.getFont().deriveFont((float) 15.0) );
		}

		return c;
	}
	
}
