import javax.swing.JOptionPane;

public class GameAI {
	
	/* Stores the mode of the AI */
	private static char computerXoMark;
	
	/* Stores the mode of the player */
	private static char playerXoMark;
	
	/* Decides if it is X for this game */
	private static boolean centerIsX;
	
	/** 
	 * The getter method for the private static field xoMark. 
	 * @return a char that represents the value of the field xoMark 
	 */
	public static char getXoMark() {
		return computerXoMark;
	}
	
	/**
	 * The method that determines the side of the computer. 
	 */
	public static void setupComputer() {
		Object[] options = {
			"X", 
			"O", 
			"Cancel"	
		};
		
		int input = JOptionPane.showOptionDialog(null, 
				"Would you like to be X or O? ", 
				"Select Order",
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE,
				null, 
				options, 
				options[0]);
		
		switch(input) {
		case 0: 
			computerXoMark = 'o';
			playerXoMark = 'x';
			break;
		case 1: 
			computerXoMark = 'x';
			playerXoMark = 'o';
			break;
		default:
			Main.playerPrompt();
		}
			
	}
	
	/**
	 * The static method that launches the computer as the X side. 
	 * @param col the column index of the button the user last clicked
	 * @param row the row index of the button the user last clicked
	 */
	public static void launchX(int col, int row) {
		System.out.println("AI computer for X is called.");
		switch (Main.getCounter()) {
		
		/* The AI always clicks on the sweet middle spot */
		case 0: 
			theAIClicks(1, 1);
			Main.setCounter(Main.getCounter() + 1);
			break; 
			
		case 2: 
			// If the player clicks on one of the four corners
			if (row % 2 == 0 && col % 2 == 0) {
				if ((int)(Math.random() * 2) == 0) 
					theAIClicks(2 - col, row);
				else 
					theAIClicks(col, 2 - row);
			}
			else {
				// Should get rid of the possibility of landing on the button from across but it did not seem to matter. 
				randomClicks();
				
			}
			
			Main.setCounter(Main.getCounter() + 1);
			break; 
			
		case 4: 
		case 6:
		case 8:
			if (foundOneWayWin()) {
				// Throw
			}
			else if (blockedOneWayWin()) {
				Main.setCounter(Main.getCounter() + 1);
			}
			else {
				randomClicks();
				Main.setCounter(Main.getCounter() + 1);
			}
		}
		
		System.out.println("your turn: " + Main.getCounter());
	}
	
	/**
	 * The static method that launches the computer as the O side. 
	 * @param col the column index of the button the user last clicked
	 * @param row the row index of the button the user last clicked
	 */
	public static void launchO(int col, int row) {
		System.out.println("AI computer for O is called.");
		
		
		switch (Main.getCounter()) {
		
		/* The AI tries to get the middle spot if available */
		case 1:
			if (Main.getButtonGrid(1, 1) == 'x') {
				/* Random click on the four corners */
				theAIClicks(2 * (int)(Math.random()*2), 2 * (int)(Math.random()*2));
				centerIsX = true;
			}
			else {
				theAIClicks(1, 1);
				centerIsX = false;
			}
			
			Main.setCounter(Main.getCounter() + 1);
			break;
			
		case 3: 
			if (centerIsX) {
				if (Main.getButtonGrid(2 - col, 2 - row) == ' ')
					theAIClicks(2 - col, 2 - row);
				else 
					randomClicks();
			}
			
			/* If center is O */
			else { 
				/* The following four are blocking attempts of making a two-way win. 
				 * Pattern - a middle click followed by a corner click from across
				 */
				
				if (Main.getButtonGrid(0, 1) == 'x' && Main.getButtonGrid(2, 2) == 'x') 
					theAIClicks(0, 2);
				else if (Main.getButtonGrid(0, 1) == 'x' && Main.getButtonGrid(2, 0) == 'x') 
					theAIClicks(0, 0);
				else if (Main.getButtonGrid(1, 0) == 'x' && Main.getButtonGrid(2, 2) == 'x') 
					theAIClicks(2, 0);
				else if (Main.getButtonGrid(1, 0) == 'x' && Main.getButtonGrid(0, 2) == 'x') 
					theAIClicks(0, 0);	
				else if (Main.getButtonGrid(2, 1) == 'x' && Main.getButtonGrid(0, 0) == 'x') 
					theAIClicks(2, 0);	
				else if (Main.getButtonGrid(2, 1) == 'x' && Main.getButtonGrid(0, 2) == 'x') 
					theAIClicks(2, 2);	
				else if (Main.getButtonGrid(1, 2) == 'x' && Main.getButtonGrid(0, 0) == 'x') 
					theAIClicks(0, 2);	
				else if (Main.getButtonGrid(1, 2) == 'x' && Main.getButtonGrid(2, 0) == 'x') 
						theAIClicks(2, 2);	
				
				/* For the dumb people who are wasting their turns */
				else if (Main.getButtonGrid(2 - col, 2 - row) == 'x') {
					randomClicks(); 
				}
				
				/* Smart people who avoided the above strategies */
				else {
					if (blockedOneWayWin());
				}
			}
			
			Main.setCounter(Main.getCounter() + 1);
			break;
			
		case 5:
		case 7:
			// Broken 
			if (Main.getTextOnTop() == "Your Turn") {
				System.out.print(Main.getTextOnTop());
				if (foundOneWayWin()) {
					// JOptionPane.showMessageDialog(null, computerXoMark == 'x' ? "X Wins!" : "O Wins!");
					// throw new UnsupportedOperationException();
				}
				else if (blockedOneWayWin())
					Main.setCounter(Main.getCounter() + 1);
				else {
					randomClicks();
					Main.setCounter(Main.getCounter() + 1);
				}
			}
			
			break;
		}
		
		System.out.println("your turn: " + Main.getCounter());
	}
	
	/**
	 * The method that resembles the click by the AI computer. 
	 * @param col the column index of the button clicked by the AI
	 * @param row the row index of the button clicked by the AI
	 */
	public static boolean theAIClicks(int col, int row) {
		Main.setButtonGrid(col, row);
		return true;
	}
	
	/**
	 * The method that creates random clicks for the AI. 
	 */
	public static void randomClicks() {
		boolean clicked = false;
		while (!clicked) {
			int randomEmptyCol = (int)(3 * Math.random());
			int randomEmptyRow = (int)(3 * Math.random());
			if (Main.getButtonGrid(randomEmptyCol, randomEmptyRow) == ' ') {
				theAIClicks(randomEmptyCol, randomEmptyRow);
				clicked = true; 
			}
		}
	}
	
	/**
	 * The method that checks and clicks for two in a row with a blank. 
	 * @return true if such pattern is found 
	 */
	public static boolean blockedOneWayWin() {
		
		for (int i = 0; i < 3; i++) {
			if (Main.getButtonGrid(i, 0) == ' ') {
				if (Main.getButtonGrid(i, 1) == playerXoMark && Main.getButtonGrid(i, 2) == playerXoMark) {
					theAIClicks(i, 0);
					return true;
				}
			}
			else if (Main.getButtonGrid(i, 1) == ' ') {
				if (Main.getButtonGrid(i, 0) == playerXoMark && Main.getButtonGrid(i, 2) == playerXoMark) {
					theAIClicks(i, 1);
					return true;
				}
			}
			else if (Main.getButtonGrid(i, 2) == ' ') {
				if (Main.getButtonGrid(i, 0) == playerXoMark && Main.getButtonGrid(i, 1) == playerXoMark) {
					theAIClicks(i, 2);
					return true;
				}
			}
		}
		
		for (int i = 0; i < 3; i++) {
			if (Main.getButtonGrid(0, i) == ' ') {
				if (Main.getButtonGrid(1, i) == playerXoMark && Main.getButtonGrid(2, i) == playerXoMark) {
					theAIClicks(0, i);
					return true;
				}
			}
			else if (Main.getButtonGrid(1, i) == ' ') {
				if (Main.getButtonGrid(0, i) == playerXoMark && Main.getButtonGrid(2, i) == playerXoMark) {
					theAIClicks(1, i);
					return true;
				}
			}
			else if (Main.getButtonGrid(2, i) == ' ') {
				if (Main.getButtonGrid(0, i) == playerXoMark && Main.getButtonGrid(1, i) == playerXoMark) {
					theAIClicks(2, i);
					return true;
				}
			}
		}
		
		// Diagonal to the left 
		if (Main.getButtonGrid(0, 0) == ' ') {
			if (Main.getButtonGrid(1, 1) == playerXoMark && Main.getButtonGrid(2, 2) == playerXoMark) {
				theAIClicks(0, 0);
				return true;
			}
		}
		else if (Main.getButtonGrid(1, 1) == ' ') {
			if (Main.getButtonGrid(0, 0) == playerXoMark && Main.getButtonGrid(2, 2) == playerXoMark) {
				theAIClicks(1, 1);
				return true;
			}
		}
		else if (Main.getButtonGrid(2, 2) == ' ') {
			if (Main.getButtonGrid(0, 0) == playerXoMark && Main.getButtonGrid(1, 1) == playerXoMark) {
				theAIClicks(2, 2);
				return true;
			}
		}
		
		// Diagonal to the right
		if (Main.getButtonGrid(0, 2) == ' ') {
			if (Main.getButtonGrid(1, 1) == playerXoMark && Main.getButtonGrid(2, 0) == playerXoMark) {
				theAIClicks(0, 2);
				return true;
			}
		}
		else if (Main.getButtonGrid(1, 1) == ' ') {
			if (Main.getButtonGrid(0, 2) == playerXoMark && Main.getButtonGrid(2, 0) == playerXoMark) {
				theAIClicks(1, 1);
				return true;
			}
		}
		else if (Main.getButtonGrid(2, 0) == ' ') {
			if (Main.getButtonGrid(1, 1) == playerXoMark && Main.getButtonGrid(0, 2) == playerXoMark) {
				theAIClicks(2, 0);
				return true;
			}
		}
		
		return false; 
	}
	
	/**
	 * The method that finds the click to create a row to win the game. 
	 * @return true if such pattern is found
	 */
	public static boolean foundOneWayWin() {
		
		for (int i = 0; i < 3; i++) {
			if (Main.getButtonGrid(i, 0) == ' ') {
				if (Main.getButtonGrid(i, 1) == computerXoMark && Main.getButtonGrid(i, 2) == computerXoMark) {
					theAIClicks(i, 0);
					return true;
				}
			}
			else if (Main.getButtonGrid(i, 1) == ' ') {
				if (Main.getButtonGrid(i, 0) == computerXoMark && Main.getButtonGrid(i, 2) == computerXoMark) {
					theAIClicks(i, 1);
					return true;
				}
			}
			else if (Main.getButtonGrid(i, 2) == ' ') {
				if (Main.getButtonGrid(i, 0) == computerXoMark && Main.getButtonGrid(i, 1) == computerXoMark) {
					theAIClicks(i, 2);
					return true;
				}
			}
		}
		
		for (int i = 0; i < 3; i++) {
			if (Main.getButtonGrid(0, i) == ' ') {
				if (Main.getButtonGrid(1, i) == computerXoMark && Main.getButtonGrid(2, i) == computerXoMark) {
					theAIClicks(0, i);
					return true;
				}
			}
			else if (Main.getButtonGrid(1, i) == ' ') {
				if (Main.getButtonGrid(0, i) == computerXoMark && Main.getButtonGrid(2, i) == computerXoMark) {
					theAIClicks(1, i);
					return true;
				}
			}
			else if (Main.getButtonGrid(2, i) == ' ') {
				if (Main.getButtonGrid(0, i) == computerXoMark && Main.getButtonGrid(1, i) == computerXoMark) {
					theAIClicks(2, i);
					return true;
				}
			}
		}
		
		// Diagonal to the left 
		if (Main.getButtonGrid(0, 0) == ' ') {
			if (Main.getButtonGrid(1, 1) == computerXoMark && Main.getButtonGrid(2, 2) == computerXoMark) {
				theAIClicks(0, 0);
				return true;
			}
		}
		else if (Main.getButtonGrid(1, 1) == ' ') {
			if (Main.getButtonGrid(0, 0) == computerXoMark && Main.getButtonGrid(2, 2) == computerXoMark) {
				theAIClicks(1, 1);
				return true;
			}
		}
		else if (Main.getButtonGrid(2, 2) == ' ') {
			if (Main.getButtonGrid(0, 0) == computerXoMark && Main.getButtonGrid(1, 1) == computerXoMark) {
				theAIClicks(2, 2);
				return true;
			}
		}
		
		// Diagonal to the right
		if (Main.getButtonGrid(0, 2) == ' ') {
			if (Main.getButtonGrid(1, 1) == computerXoMark && Main.getButtonGrid(2, 0) == computerXoMark) {
				theAIClicks(0, 2);
				return true;
			}
		}
		else if (Main.getButtonGrid(1, 1) == ' ') {
			if (Main.getButtonGrid(0, 2) == computerXoMark && Main.getButtonGrid(2, 0) == computerXoMark) {
				theAIClicks(1, 1);
				return true;
			}
		}
		else if (Main.getButtonGrid(2, 0) == ' ') {
			if (Main.getButtonGrid(1, 1) == computerXoMark && Main.getButtonGrid(0, 2) == computerXoMark) {
				theAIClicks(2, 0);
				return true;
			}
		}
		
		return false; 
	}
}
