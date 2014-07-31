package Sudoku;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class SudokuTableModel extends DefaultTableModel implements TableModel {
int lockedrows[] = new int[81];
int lockedcols[] = new int[81];
int index = 0;
	@Override
	public void addTableModelListener(TableModelListener arg0) {
		super.addTableModelListener(arg0);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		return super.getColumnClass(arg0);
	}

	@Override
	public int getColumnCount() {
		return 9;
	}

	@Override
	public String getColumnName(int arg0) {
		return super.getColumnName(arg0);
	}

	@Override
	public int getRowCount() {
		return 9;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return super.getValueAt(arg0, arg1);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (Game.finalize) {
			for(int a = 0; a < 9; a++) {
				for (int b = 0; b < 9; b++) {
					if(this.getValueAt(a, b) != null) {
						lockedrows[index] = a;
						lockedcols[index] = b;
						index++;
						Game.table.setRowSelectionInterval(0, 8);
					}
				}
			}
			Game.finalize = false;
		}
		
		for(int i = 0; i <= index; i++) {
			if(this.getValueAt(row, col) != null && row == lockedrows[i] && col == lockedcols[i]) return false;
		}
		return true;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
		super.removeTableModelListener(arg0);
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		super.setValueAt(arg0, arg1, arg2);
		}
	
	public void reset() {
		for(int a = 0; a < 9; a++)
			for (int b = 0; b < 9; b++)
				if(this.isCellEditable(a, b))
					this.setValueAt(null, a, b);
	}
	
	public void emptygrid() {
		for(int a = 0; a < 9; a++)
			for (int b = 0; b < 9; b++)
				this.setValueAt(null, a, b);
		for (int i = 0; i <= index; i++) {
			lockedrows[i] =  -1;
			lockedcols[i] = -1;
		}
		index = 0;
	}

}
