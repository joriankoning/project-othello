package main.java;

import java.util.*;

public class ValidMoves extends Othello {

	private HashMap<Integer[], Integer[][]> whiteMoves = new HashMap<>();
	private HashMap<Integer[], Integer[][]> blackMoves = new HashMap<>();

	public ValidMoves(Boolean[][] board, boolean AI){
		super();
		this.AI = AI;
		this.board = board;
	}

	public boolean hasValidMove(){
		checkAll();
		if(AI == BLACK){
			return whiteMoves.size() > 0;
		}
		if(AI == WHITE){
			return blackMoves.size() > 0;
		}
		return false;
	}

	public boolean makeMove(int x, int y, boolean turn, boolean checkOnly){

		if(x > 7) return false;
		if(board[x][y] != null) return false;

		Integer[] key = new Integer[]{x, y};
		Integer[][] values = getByKey(key, turn);

		if(values != null) {
			if(checkOnly) return true; // return true als alleen gecheckt moet worden of een move valid is
			board[x][y] = turn;
			for(Integer[] integers : values){
				try{
					board[integers[0]] [integers[1]] = turn;
				}catch (NullPointerException e){
					System.err.println("NullPointerException");
				}
			}return true;
		}
		else return false;
	}

	public void checkAll(){
		whiteMoves = new HashMap<>();	// reset huidige geldige zetten
		blackMoves = new HashMap<>();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j] != null){
					boolean turn = board[i][j];

					boolean[] upDownLeftRight = new boolean[4];

					for (int k = 1; k < 11; k++) {	// iedere kant op kijken

						int x = i;
						int y = j;

						if (k % 4 == 3) continue;    // 3 gaat nergens heen, 7 is redundant

						if(upDownLeftRight[0] = (k / 8) % 2 == 1) y--;
						if(upDownLeftRight[1] = (k / 4) % 2 == 1) y++;
						if(upDownLeftRight[2] = (k / 2) % 2 == 1) x--;
						if(upDownLeftRight[3] = k % 2 == 1) x++;

						if (x < 0 || x > 7 || y < 0 || y > 7) continue;

						if(board[x][y] != null) if(board[x][y] != turn){	// x/y is een vakje naast een leeg vakje
							ArrayList<Integer[]> coordinates = new ArrayList<>(recursiveSlaan(x, y, upDownLeftRight, turn));
							setMoveValues(coordinates, turn);
						}
					}
				}
			}
		}
	}

	private ArrayList<Integer[]> recursiveSlaan(int x, int y, boolean[] direction, boolean turn){
		// onderdeel van checkAll()
		ArrayList<Integer[]> coordinates = new ArrayList<>();

		if (board[x][y] == null) {	// base case
			Integer[] endpoint = {x, y};
			coordinates.add(endpoint);
			return coordinates;
		}

		else if(board[x][y] != turn) {
			Integer[] opponent = {x, y};
			coordinates.add(opponent);

			if (direction[0]) y--;
			if (direction[1]) y++;
			if (direction[2]) x--;
			if (direction[3]) x++;
			if (x < 0 || x > 7 || y < 0 || y > 7) return new ArrayList<>();

			coordinates.addAll(recursiveSlaan(x, y, direction, turn));
			return coordinates;
		}
		else return new ArrayList<>();
	}

	private void setMoveValues(ArrayList<Integer[]> coordinates, boolean turn){

		if(coordinates.size() > 0) {
			Integer[] endpoint = coordinates.get(coordinates.size() - 1);
			if(board[endpoint[0]][endpoint[1]] != null) return;
			Integer[][] slagen = new Integer[coordinates.size() - 1][2];

			for (int i = 0; i < coordinates.size() - 1; i++) {
				slagen[i] = coordinates.get(i);
			}

			if (containsKey(endpoint, turn)) {
				addByKey(endpoint, slagen, turn);
			}
			else if (turn == WHITE) whiteMoves.put(endpoint, slagen);
			else if (turn == BLACK) blackMoves.put(endpoint, slagen);
		}
	}

	public Integer[] AIMove(){
		checkAll();
		Integer[] move = new AIMoveBackEnd().getAIMove();
		makeMove(move[0], move[1], AI, false);
		return move;
	}

	@Override
	public void resetBoard() {
		board = new Boolean[8][8];
		board[3][4] = board[4][3] = BLACK;
		board[3][3] = board[4][4] = WHITE;
		super.resetBoard();
	}

	private Integer[][] getByKey(Integer[] key, boolean turn){
		if(turn == WHITE){
			for(Map.Entry<Integer[], Integer[][]> maps : whiteMoves.entrySet()){
				if(maps.getKey()[0].equals(key[0]) && maps.getKey()[1].equals(key[1])) {
					return maps.getValue();
				}
			}
		}
		else if(turn == BLACK){
			for(Map.Entry<Integer[], Integer[][]> maps : blackMoves.entrySet()){
				if(maps.getKey()[0].equals(key[0]) && maps.getKey()[1].equals(key[1])){
					return maps.getValue();
				}
			}
		}
		return null;
	}

	private void removeOld(Integer[] key, boolean turn){
		if(turn == BLACK){
			Iterator<Integer[]> iterator = blackMoves.keySet().iterator();
			while (iterator.hasNext()){
				Integer[] i = iterator.next();
				if(i[0].equals(key[0]) && i[1].equals(key[1])){
					iterator.remove();
				}
			}
		}
		else if(turn == WHITE){
			Iterator<Integer[]> iterator = whiteMoves.keySet().iterator();
			while (iterator.hasNext()){
				Integer[] i = iterator.next();
				if (i[0].equals(key[0]) && i[1].equals(key[1])) {
					iterator.remove();
				}
			}
		}
	}

	private boolean containsKey(Integer[] key, boolean turn){
		if(turn == WHITE){
			for(Integer[] integers : whiteMoves.keySet()){
				if(integers[0].equals(key[0]) && integers[1].equals(key[1])) return true;
			}
		}
		else if(turn == BLACK){
			for(Integer[] integers : blackMoves.keySet()){
				if(integers[0].equals(key[0]) && integers[1].equals(key[1])) return true;
			}
		}
		return false;
	}

	private void addByKey(Integer[] key, Integer[][] toAdd, boolean turn){

		if(!(containsKey(key, turn))) return;

		Integer[][] addTO = getByKey(key, turn);

		if(addTO != null) {
			Integer[][] adder = new Integer[addTO.length + toAdd.length][2];

			for (int i = 0; i < addTO.length; i++) {
				adder[i] = new Integer[]{addTO[i][0], addTO[i][1]};
			}
			int j = 0;
			for (int i = addTO.length; i < addTO.length + toAdd.length ; i++) {
				adder[i] = toAdd[j];
				j++;
			}
			removeOld(key, turn);
			if (turn == WHITE) whiteMoves.put(key, adder);
			else if (turn == BLACK) blackMoves.put(key, adder);
		}
	}

	private class AIMoveBackEnd{
		private Integer[] getAIMove(){
			Integer[] move = null;
			if((move = checkCorners()) != null) return move;
			if((move = checkNextToCorner()) != null) return move;
			if((move = checkCenter()) != null) return move;
			if((move = checkNotNearCorner()) != null) return move;
			if((move = checkRest()) != null) return move;
			System.out.println("no Move");
			return new Integer[]{8,8};	// 8,8 wordt expres gebruikt als foutcode
		}

		private Integer[] getShortestFromKySet(Set<Integer[]> keySet){
			if(keySet.size() < 1) return null;
			int maxLength = 100;
			Integer[] maxKey = new Integer[2];
			for(Integer[] integers : keySet){
				if(board[integers[0]][integers[1]] == null){
					Integer[][] current = getByKey(integers, AI);
					if(current != null){
						int length = current.length;
						if(length < maxLength){
							maxLength = length;
							maxKey = integers;
						}
					}
				}
			}
			if(maxLength < 100) return maxKey;
			else return null;
		}

		private Integer[] checkCorners(){

			Set<Integer[]> keySet = new HashSet<>();

			if(makeMove(0, 0, AI, true)) keySet.add(new Integer[]{0, 0});
			if(makeMove(0, 7, AI, true)) keySet.add(new Integer[]{0, 7});
			if(makeMove(7, 0, AI,true)) keySet.add(new Integer[]{7, 0});
			if(makeMove(7, 7, AI, true)) keySet.add(new Integer[]{7, 7});
			if(keySet.size() > 0) return getShortestFromKySet(keySet);
			else return null;
		}

		private Integer[] checkNextToCorner(){
			ArrayList<Integer[]> corners = new ArrayList<>();
			if(board[0][0] != null && board[0][0] == AI) corners.add(new Integer[]{0,0});
			if(board[7][0] != null && board[7][0] == AI) corners.add(new Integer[]{7,0});
			if(board[0][7] != null && board[0][7] == AI) corners.add(new Integer[]{0,7});
			if(board[7][7] != null && board[7][7] == AI) corners.add(new Integer[]{7,7});


			if(corners.size() > 0){
				Set<Integer[]> keySet = new HashSet<>();

				for(Integer[] integers : corners){
					if(integers[0] == 0){
						outerLoop:
						for(int i = 0; i < 7; i++){
							if(board[i][integers[1]] != null){
								if(board[i][integers[1]] != AI){
									for(int j = i; j < 7; j++){
										if(board[j][integers[1]] == null){
											if(makeMove(j, integers[1], AI,true)) keySet.add(new Integer[]{j, integers[1]});
											break outerLoop;
										}
										if(board[j][integers[1]] == !AI) continue;
										if(board[j][integers[1]] == AI) break outerLoop;
									}
								}
							} else break;
						}
					}
					if(integers[0] == 7){
						outerLoop:
						for(int i = 6; i > 0; i--){
							if(board[i][integers[1]] != null){
								if(board[i][integers[1]] != AI){
									for(int j = i; j > 0; j--){
										if(board[j][integers[1]] == null){
											if(makeMove(j, integers[1], AI,true)) keySet.add(new Integer[]{j, integers[1]});
											break outerLoop;
										}
										if(board[j][integers[1]] == !AI) continue;
										if(board[j][integers[1]] == AI) break outerLoop;
									}
								}
							} else break;
						}
					}
					if(integers[1] == 0){
						outerLoop:
						for(int i = 1; i < 7; i++){
							if(board[integers[0]][i] != null){
								if(board[integers[0]][i] != AI){
									for(int j = i; j < 7; j++){
										if(board[integers[0]][j] == null){
											if(makeMove(integers[0], j, AI,true)) keySet.add(new Integer[]{j, integers[1]});
											break outerLoop;
										}
										if(board[integers[0]][j] == !AI) continue;
										if(board[integers[0]][j] == AI) break outerLoop;
									}
								}
							} else break;
						}
					}
					if(integers[1] == 7){
						outerLoop:
						for(int i = 6; i > 0; i--){
							if(board[integers[0]][i] != null){
								if(board[integers[0]][i] != AI){
									for(int j = i; j > 0; j--){
										if(board[integers[0]][j] == null){
											if(makeMove(integers[0], j, AI,true)) keySet.add(new Integer[]{j, integers[1]});
											break outerLoop;
										}
										if(board[integers[0]][j] == !AI) continue;
										if(board[integers[0]][j] == AI) break outerLoop;
									}
								}
							} else break;
						}
					}
				}
				if(keySet.size() > 0) return getShortestFromKySet(keySet);
			}
			return null;
		}

		private Integer[] checkCenter(){
			Set<Integer[]> keySet = new HashSet<>();

			if(AI == WHITE) keySet.addAll(whiteMoves.keySet());
			else if(AI == BLACK) keySet.addAll(blackMoves.keySet());

			Iterator<Integer[]> iterator = keySet.iterator();
			while(iterator.hasNext()){
				Integer[] move = iterator.next();
				if(!(move[0] > 1 && move[0] < 6 && move[1] > 1 && move[1] < 6)) iterator.remove();
			}
			if(keySet.size() > 0) return getShortestFromKySet(keySet);
			else return null;
		}

		private Integer[] checkNotNearCorner(){
			Set<Integer[]> keySet = new HashSet<>();

			if(AI == WHITE) keySet.addAll(whiteMoves.keySet());
			else if(AI == BLACK) keySet.addAll(blackMoves.keySet());

			Iterator<Integer[]> iterator = keySet.iterator();
			while(iterator.hasNext()){
				Integer[] move = iterator.next();
				int x = move[0];
				int y = move[1];
				if((x < 2 || x > 5) && (y < 2 || y > 5)) iterator.remove();
				if(x > 1 && x < 6 && y > 1 && y < 6) iterator.remove();
			}
			if(keySet.size() > 0) return getShortestFromKySet(keySet);
			else return null;
		}

		private Integer[] checkRest(){
			Set<Integer[]> keySet = new HashSet<>();

			if(AI == WHITE) keySet.addAll(whiteMoves.keySet());
			else if(AI == BLACK) keySet.addAll(blackMoves.keySet());

			Iterator<Integer[]> iterator = keySet.iterator();
			while(iterator.hasNext()){
				Integer[] move = iterator.next();
				int x = move[0];
				int y = move[1];
				if(!((x < 2 || x > 5) && (y < 2 || y > 5))) iterator.remove();
			}
			if(keySet.size() > 0) return getShortestFromKySet(keySet);
			else return null;
		}
	}
}