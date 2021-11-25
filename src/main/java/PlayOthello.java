package main.java;

import java.util.Scanner;

public class PlayOthello extends Othello {
	private final Boolean player = BLACK;
	private final Boolean ai = WHITE;

	public PlayOthello(){
		super(false);
	}

	public void play() {
		Scanner input = new Scanner(System.in);
		String[] move;
		startBoard();
		int freeSpots = 60;
		drawBoard();
		System.out.println("Je speelt als BLACK (B)");
		while (freeSpots > 0) {
			if(validMoves.hasValidMove()){
				System.out.println("Het is jouw beurt: voer je beurt hier onder in");
				move = input.nextLine().split(" ");
				try {
					int x = Integer.parseInt(move[0]);
					int y = Integer.parseInt(move[1]);
					validMoves.checkAll();
					if(x == 8 && y == 8) continue;
					else if (validMoves.makeMove(x, y, player, false)) {
						freeSpots--;
					} else {
						drawBoard();
						System.out.println("Geen geldige zet!");
						continue;
					}
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
					System.out.println("Voer AUB 2 getallen in met één spatie er tussen");
					continue;
				}
				drawBoard();
			}
			if(validMoves.AIMove()[0] != 8) {
				freeSpots--;
			} else{
				System.out.println("No move left for AI");
				if(!(validMoves.hasValidMove())) break;
			}
			drawBoard();
		}
		Boolean winner = getWinner();
		if(winner == player) System.out.println("Player wins!");
		else if(winner == ai) System.out.println("AI wins!");
		else System.out.println("It is a tie!");
	}

	public static void main(String[] args) {
		PlayOthello othello = new PlayOthello();
		othello.play();
	}
}