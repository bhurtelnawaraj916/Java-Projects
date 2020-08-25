import java.util.*;
import java.lang.*;

public class AiPlayer
{
	//depth cut-off
	public int maxDepth;

	//set alpha and beta
	public int alpha = -100;
	public int beta = 100;

	public int aiNumber;
	public int humanNumber;


	//instantiate an AiPlayer object
	public AiPlayer()
	{

	}
	//find next move based on depth limited search
	public int findBestPlay( GameBoard currentGame, int depthLimit, int currentTurn )
	{
		this.aiNumber = currentTurn;
		this.maxDepth = depthLimit;
		int playChoice = alphaBetaDecision(currentGame);
		return playChoice;
	}

	//terminal test function
	public boolean terminalTest(GameBoard currentBoard, int currentDepth)
	{
		if((currentBoard.getPieceCount() == 42) || (currentDepth == maxDepth))
			return true;
		else
			return false;
	}
	
	//successors function: make an arraylist and store all possible moves
	public ArrayList<GameBoard> successors (GameBoard board)
	{
		ArrayList<GameBoard> results = new ArrayList<GameBoard>();
		GameBoard currentBoard = new GameBoard(board.getGameBoard());
		for (int column = 0; column < 7 ; column++)
		{
			if (currentBoard.isValidPlay(column))
			{
				GameBoard nextMove = new GameBoard(currentBoard.getGameBoard());
				nextMove.playPiece(column);
				results.add(nextMove);
			}
			else;
		}
		return results;
	}
	
	//alpha-beta search
	public int alphaBetaDecision(GameBoard board)
	{
		int utilityValue = -100; //utility of a board
		int nextMove = 3;        //default move if no preferable move exists
		int value;
		for (int column = 0; column < 7; column++) //for each possible play
		{
			if (board.isValidPlay(column))
			{
				GameBoard nextBoard = new GameBoard(board.getGameBoard()); //make a copy that plays the move
				nextBoard.playPiece(column);
				
				//evaluate possible outcomes
				value = maxValue(nextBoard, alpha, beta, 1);
				
				//if an outcome is more favorable than the rest, return the move
				if(value > utilityValue)
				{
					utilityValue = value;
					nextMove = column;
				}
			}
		}
		return nextMove;
	}
	
	//maximizing function in alpha-beta
	public int maxValue (GameBoard currentBoard, int alpha, int beta, int depth)
	{
		if (terminalTest(currentBoard,depth))
		{
			if (aiNumber == 1)
			{
				return currentBoard.getScore(1)-currentBoard.getScore(2);
			}
			else if (aiNumber == 2)
			{
					return currentBoard.getScore(2)-currentBoard.getScore(1);
			}
		}
		
		int value = -100;
		
		ArrayList<GameBoard> children = successors(currentBoard);
		
		//for each successor,
		for (GameBoard board : children)
		{
			value = Math.max(value, minValue(board, alpha, beta, depth+1));
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
			if (alpha >= beta)
				break;
		}
		return value;
	}
	
	//minimizing value in alpha-beta
	public int minValue (GameBoard currentBoard, int alpha, int beta, int depth)
	{
		if (terminalTest(currentBoard,depth))
		{
			if (aiNumber == 1)
			{
				return currentBoard.getScore(1)-currentBoard.getScore(2);
			}
			else if (aiNumber == 2)
			{
					return currentBoard.getScore(2)-currentBoard.getScore(1);
			}
		}
		
		int value = 100;		
		
		ArrayList<GameBoard> children = successors(currentBoard);
		for (GameBoard board : children)
		{
			value = Math.min(value, maxValue(board, alpha, beta, depth+1));
			if (value <=alpha)
				return value;
			beta = Math.min(beta, value);
			if (alpha >= beta) break;
		}
		
		return value;
	}
}