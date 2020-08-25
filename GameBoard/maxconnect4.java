
import java.io.*;
import java.util.*;

//main class

public class maxconnect4
{
  public static void main(String[] args) 
  {
    // check for the correct number of arguments
    if( args.length != 4 ) 
    {
      System.out.println("Four command-line arguments are needed:\n"
                + "Usage: java [program name] interactive [input_file] [computer-next / human-next] [depth]\n"
                + " or:  java [program name] one-move [input_file] [output_file] [depth]\n");

      exit_function( 0 );
    }
    
    // parse the input arguments
    String game_mode = args[0].toString();        // the game mode
    //input game file 
    String input = args[1].toString();    
    // the depth level of the ai search      
    int depthLevel = Integer.parseInt( args[3] );     
      
    // create and initialize the game board
    GameBoard currentGame = new GameBoard( input );
    
    // create the Ai Player
    AiPlayer calculon = new AiPlayer();
      
    //  variables to keep up with the game
    int playColumn = 99;        //  the players choice of column to play
    boolean playMade = false;     //  set to true once a play has been made

    //interactive mode 
    if( game_mode.equalsIgnoreCase( "interactive" ) ) 
    {
      //store turn player
      String turn_player = args[2].toString();
      
      //Scan user move
      Scanner userMove = new Scanner(System.in);
      int move;
      
      //if the player is AI
      if(turn_player.equalsIgnoreCase("computer-next"))
      {
        calculon.aiNumber = currentGame.getCurrentTurn();
        if (calculon.aiNumber == 1)
          calculon.humanNumber = 2;
        else calculon.humanNumber = 1;
        
        //goto 2: print state and score. if full board, exit
        printBoard(currentGame);
        
        while (currentGame.getPieceCount() < 42)
        {
          //3: choose and make the next move
          playColumn = calculon.findBestPlay(currentGame, depthLevel, currentGame.getCurrentTurn());
          currentGame.playPiece(playColumn);
        
          //4: save current board in file called computer.txt
          currentGame.printGameBoardToFile("computer.txt");
        
          //5: print board state and score, if board is full then exit
          printBoard(currentGame);
          
          //6: ask human to make a move, then check validity of move
          System.out.println("Please enter the desired move(a number between 0 and 6):");
          move = userMove.nextInt();
          userMove.nextLine();
          while (!currentGame.isValidPlay(move))
          {
            System.out.println("Move not valid, please choose another.");
            move = userMove.nextInt();
            userMove.nextLine();
          }
          currentGame.playPiece(move);
          
          //7: save current board in file called human.txt
          currentGame.printGameBoardToFile("human.txt");
          
          //8: goto 2: display and check
          printBoard(currentGame);
        }
        
        return;
      }
      
      //if player is human
      else
      {
        if (currentGame.getCurrentTurn() == 1)
        {
          calculon.aiNumber = 2;
          calculon.humanNumber = 1;
        }
        else
        {
          calculon.aiNumber = 1;
          calculon.humanNumber = 2;
        }
        
        //goto 5: display and check
        printBoard(currentGame);
        
        while (currentGame.getPieceCount() < 42)
        {
          //6: get human's move, then check validity before playing
          System.out.println("Please enter the desired move (a number between 0 and 6):");
          move = userMove.nextInt();
          userMove.nextLine();
          while (!currentGame.isValidPlay(move))
          {
            System.out.println("Move not valid, please choose another.");
            move = userMove.nextInt();
            userMove.nextLine();
          }
          currentGame.playPiece(move);

          //7: save current board in file called human.txt
          currentGame.printGameBoardToFile("human.txt");

          //8: goto 2: display and check
          printBoard(currentGame);

          //3: computer's move
          playColumn = calculon.findBestPlay(currentGame, depthLevel, currentGame.getCurrentTurn());
          currentGame.playPiece(playColumn);

          //4: save current board in file called computer.txt
          currentGame.printGameBoardToFile("computer.txt");
        
          //5: print board state and score, if board is full then exit
          printBoard(currentGame);
        }
      } 
    }
    //one-move
    else if( game_mode.equalsIgnoreCase( "one-move" ) ) 
    {
      /////////////   one-move mode ///////////
      // get the output file name
      String output = args[2].toString();       // the output game file
      
      System.out.print("\nMaxConnect-4 game\n");
      System.out.print("game state before move:\n");
      
      //print the current game board
      currentGame.printGameBoard();
      // print the current scores
      System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
          ", Player2 = " + currentGame.getScore( 2 ) + "\n " );
      
      // ****************** this chunk of code makes the computer play

      if( currentGame.getPieceCount() < 42 )
      {
        int current_player = currentGame.getCurrentTurn();
        // AI play
        playColumn = calculon.findBestPlay( currentGame, depthLevel, current_player );
      
        // play the piece
        currentGame.playPiece( playColumn );
          
        // display the current game board
        System.out.println("move " + currentGame.getPieceCount() 
                   + ": Player " + current_player
                   + ", column " + playColumn);
        System.out.print("game state after move:\n");
        currentGame.printGameBoard();
      
        // print the current scores
        System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
                  ", Player2 = " + currentGame.getScore( 2 ) + "\n " );
        
        currentGame.printGameBoardToFile( output );
        //************************** end computer play
      } 
      else 
      {
        System.out.println("\nI can't play.\nThe Board is Full\n\nGame Over");
      }
    }
    //if game mode is invalid
    else if (!game_mode.equalsIgnoreCase( "interactive" ) && !game_mode.equalsIgnoreCase( "one-move" ))
    {
      System.out.println("Error! Please specify interactive mode or one move mode\n");
      return;
    }
    
    return;
    
} // end of main()
  
  //print board and check if board is full then exit
  private static void printBoard(GameBoard board)
  {
    board.printGameBoard();
    
    System.out.println( "Score: Player 1 = " + board.getScore( 1 ) +
            ", Player2 = " + board.getScore( 2 ) + "\n " );
    
    if (board.getPieceCount() == 42)
    {
      System.out.println("Board is full, exiting.");
      exit_function(0);
    }
    
  }

  //exit prematurely
    private static void exit_function( int value )
  {
    System.out.println("exiting from MaxConnectFour.java!\n\n");
    System.exit( value );
  }
} // end of class connectFour