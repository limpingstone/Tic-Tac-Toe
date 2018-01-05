import javax.swing.JOptionPane;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class Main extends Application {

	/** Store the array of buttons in a 2-dimensional array */
	private static XOButton[][] buttonGrid = new XOButton[3][3];
	
	/** Determine if the game is single or two player mode */
	private static int mode;
	
	/** Stores the turn of the players */
	private static int counter = 0;
	
	private static String textOnTop;
	
	/** The display of the 3 x 3 Tic Tac Toe grid */
	private static GridPane gridPane;
	
	/** The setup of the interface */
	private static BorderPane borderPane = new BorderPane();
	
	/**
	 * The method that overrides the start method in javafx Application. 
	 * @param primaryStage the stage to be started 
	 */
	@Override 
	public void start(Stage primaryStage) {
		
		playerPrompt();
		
		setTextOnTop("Your Turn");
		borderPane.setTop(new Text(getTextOnTop()));
		// borderPane.setCenter(new Button("Reset"));
		borderPane.setBottom(gridPane);
		
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
	/**
	 * The method that prompts for the choice of the player and initializes the game board.
	 */
	public static void playerPrompt() throws NumberFormatException {
		Object[] options = {
				"Single Player", 
				"Two Players", 
				"Cancel"
		};
		
		mode = JOptionPane.showOptionDialog(null, 
				"How many players for the game?", 
				"Tic Tac Toe Game Mode", 
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE, 
				null, 
				options, 
				options[0]);
		
		System.out.println(mode);
		
		switch (mode) {
		case 0: 
			GameAI.setupComputer();
			break;
		case 1: 
			break;
		default:
			System.exit(0);
		}
		
		/* Launch the game board after prompt */
		gridPane = newGameBoard();
		
		/* First step for the computer AI if player chooses O in single mode */
		if (GameAI.getXoMark() == 'x')
			GameAI.launchX(-1, -1); 
	}
	
	/**
	 * The method that creates the game board
	 * @return a grid pane that is passed to the scene
	 */
	public static GridPane newGameBoard() {
		GridPane gridPane = new GridPane();
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				/* Initialize the buttons */
				
				buttonGrid[i][j] = new XOButton("  ", i, j);
				buttonGrid[i][j].setOnAction(e -> {
					
					/* Launch the button with different properties based on the mode */
					XOButton clickedButton = (XOButton)e.getSource(); 
					if (mode == 0) 
						launchSinglePlayerMode(clickedButton);
					else 
						launchTwoPlayerMode(clickedButton);
				});
				
				/* Display each button on the grid pane */
				gridPane.add(buttonGrid[i][j], i, j + 1);
			}
		}
		
		return gridPane; 
	}
	
	/**
	 * The helper method that enables single player mode. 
	 * @param clickedButton the button passed from the event handler
	 */
	public static void launchSinglePlayerMode(XOButton clickedButton) {
		if (GameAI.getXoMark() == 'o') {
			if (clickedButton.getXoMark() == ' ') {
				clickedButton.setText("x");
				clickedButton.setXoMark('x');
				counter++;
				GameAI.launchO(clickedButton.getCol(), clickedButton.getRow());
				
			}
		}
		else if (GameAI.getXoMark() == 'x') {
			
			if (clickedButton.getXoMark() == ' ') {
				clickedButton.setText("o");
				clickedButton.setXoMark('o');
				counter++;
				GameAI.launchX(clickedButton.getCol(), clickedButton.getRow());
			}
		}
		
		showStatus();
	}
	
	/**
	 * The helper method that enables two players in front of the computer. 
	 * @param clickedButton the button passed from the event handler
	 */
	public static void launchTwoPlayerMode(XOButton clickedButton) {
		if (clickedButton.getXoMark() == ' ') {
			clickedButton.setText(counter % 2 == 0 ? "x" : "o");
			clickedButton.setXoMark(counter++ % 2 == 0 ? 'x' : 'o');
			System.out.println("Grid: [" + clickedButton.getRow() + "][" + clickedButton.getCol() + "]");
		}
		
		showStatus();
	}
	
	/**
	 * The method that changes the status displayed on the top of the program. 
	 */
	public static boolean showStatus() {
		
		if (xWins()) {
			setTextOnTop("X Wins!");
			borderPane.setTop(new Text(getTextOnTop()));
			// throw new UnsupportedOperationException();
			return true;
		}
		else if (yWins()) {
			setTextOnTop("O Wins!");
			borderPane.setTop(new Text(getTextOnTop()));
			return true;
		}
		
		if (counter == 9 && !xWins() && !yWins()) {
			setTextOnTop("Tie!");
			borderPane.setTop(new Text(getTextOnTop()));
			return true;
		}
		
		return false; 
	}
	
	/**
	 * The method that checks if player X wins. 
	 * @return true if a row of three 'X'es is identified 
	 */
	public static boolean xWins() {
		for (int i = 0; i < 3; i++) {
			if (buttonGrid[0][i].getXoMark() == 'x' && 
					buttonGrid[1][i].getXoMark() == 'x' && 
					buttonGrid[2][i].getXoMark() == 'x')
				return true;
		}
		
		for (int i = 0; i < 3; i++) {
			if (buttonGrid[i][0].getXoMark() == 'x' && 
					buttonGrid[i][1].getXoMark() == 'x' && 
					buttonGrid[i][2].getXoMark() == 'x')
				return true;
		}
		
		if (buttonGrid[0][0].getXoMark() == 'x' && 
				buttonGrid[1][1].getXoMark() == 'x' && 
				buttonGrid[2][2].getXoMark() == 'x')
			return true;
		
		if (buttonGrid[0][2].getXoMark() == 'x' && 
				buttonGrid[1][1].getXoMark() == 'x' && 
				buttonGrid[2][0].getXoMark() == 'x')
			return true;
		
		return false;
	}
	
	/**
	 * The method that checks if player O wins. 
	 * @return true if a row of three 'O's is identified. 
	 */
	public static boolean yWins() {
		for (int i = 0; i < 3; i++) {
			if (buttonGrid[0][i].getXoMark() == 'o' && 
					buttonGrid[1][i].getXoMark() == 'o' && 
					buttonGrid[2][i].getXoMark() == 'o')
				return true;
		}
		
		for (int i = 0; i < 3; i++) {
			if (buttonGrid[i][0].getXoMark() == 'o' && 
					buttonGrid[i][1].getXoMark() == 'o' && 
					buttonGrid[i][2].getXoMark() == 'o')
				return true;
		}
		
		if (buttonGrid[0][0].getXoMark() == 'o' && 
				buttonGrid[1][1].getXoMark() == 'o' && 
				buttonGrid[2][2].getXoMark() == 'o')
			return true;
		
		if (buttonGrid[0][2].getXoMark() == 'o' && 
				buttonGrid[1][1].getXoMark() == 'o' && 
				buttonGrid[2][0].getXoMark() == 'o')
			return true;
		
		return false;
	}
	
	/**
	 * The static getter method for the private integer field counter. 
	 * @return the field counter as type int 
	 */
	public static int getCounter() {
		return counter;
	}
	
	/**
	 * The static setter method for the private integer field counter. 
	 * @param inputCounter a value of type int for the counter
	 */
	public static void setCounter(int inputCounter) {
		counter = inputCounter;
	}
	
	public static void setButtonGrid(int col, int row) {
		buttonGrid[col][row].setText(counter % 2 == 0 ? "x" : "o");
		buttonGrid[col][row].setXoMark(counter % 2 == 0 ? 'x' : 'o');
	}
	
	public static char getButtonGrid(int col, int row) {
		return buttonGrid[col][row].getXoMark();
	}
	
	public static void setTextOnTop(String input) {
		textOnTop = input;
	}
	
	public static String getTextOnTop() {
		return textOnTop;
	}
	
	/**
	 * The main method. 
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
}
