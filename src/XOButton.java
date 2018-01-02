import javafx.scene.control.*;

public class XOButton extends Button {
	private char xoMark = ' ';
	
	private int col;
	private int row;
	
	
	/**
	 * The constructor that calls Button 
	 * @param buttonText
	 */
	public XOButton(String buttonText, int col, int row) {
		super(buttonText);
		this.col = col;
		this.row = row;
	}
	
	/**
	 * The method that returns the marking of the button. 
	 * @return the value of xoMark;
	 */
	public char getXoMark() {
		return xoMark;
	}
	
	/**
	 * The method that sets either 'X' or 'O' to the button. 
	 * @param inputXoMark 'X' or 'O'
	 */
	public void setXoMark(char inputXoMark) {
		xoMark = inputXoMark;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row; 
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
}
