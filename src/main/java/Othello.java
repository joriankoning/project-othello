package main.java;

public class Othello {

	protected ValidMoves validMoves;

	protected final boolean WHITE = false;
	protected final boolean BLACK = true;
	protected boolean AI;

	protected Boolean[][] board = new Boolean[8][8];

	public Othello(boolean AI){
		this.AI = AI;
		validMoves = new ValidMoves(board, AI);
	}

	public Othello(){
		// anders stackoverflow
		validMoves = null;
	}

	public void drawBoard(){
		System.out.println();
		// print een bord in de terminal
		System.out.println("x  0  1  2  3  4  5  6  7" );
		for(int i = 0; i < 8; i++){
			System.out.print(i + " ");
			for(int j = 0; j < 8; j++){
				if(board[j][i] == null) System.out.print(" . ");
				else if(board[j][i] == WHITE) System.out.print(" W ");
				else if(board[j][i] == BLACK) System.out.print(" B ");
			}
			System.out.print("\n");
		}
		System.out.println();
	}

	public void startBoard(){
		// een begin board
		board[3][4] = board[4][3] = BLACK;
		board[3][3] = board[4][4] = WHITE;
	}

	public void resetBoard(){
		board = new Boolean[8][8];
		startBoard();
	}

	public Boolean getWinner(){
		int whitePoints = 0;
		int blackPoints = 0;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[j][i] == WHITE) whitePoints++;
				else if(board[j][i] == BLACK) blackPoints++;
			}
		}
		System.out.println("scores:");
		System.out.println("White scored: " + whitePoints + " points");
		System.out.println("Black scored: " + blackPoints + " points");
		if(whitePoints > blackPoints) return WHITE;
		else if (blackPoints > whitePoints) return BLACK;
		else return null;
	}
}