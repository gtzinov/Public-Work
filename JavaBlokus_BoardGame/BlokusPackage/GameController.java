package BlokusPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.time.temporal.JulianFields;
import java.util.ArrayList;

public class GameController {

    private GUI gui;
    private Game game;
    private Player[] players;
    private Board board;
    private int victoryStatus;

    private ArrayList<Observer> observers;

    GameController(){
        observers = new ArrayList<>();
        Observer new_logger = new Logger();
        registerObserver(new_logger);
        game = new Game();
        Piece.Color[] colors = {Piece.Color.RED, Piece.Color.BLUE, Piece.Color.GREEN, Piece.Color.YELLOW};
        game.initializePlayers(4, colors);
        players =  game.getPlayers();
        board = game.getBoard();
        game.setCurrentPlayerTurn(0);
        gui = new GUI(this);
        gui.initializeGUI(game);
    }

    private void registerObserver(Observer obs) {
        observers.add(obs);
    }

    private void updateAllObservers(Observer.Event event, String[] args) {
        for (Observer obs : observers) {
            obs.update(event, args);
        }
    }

    public void run(){
        gui.updateAll(game);
        gui.displayPopup(3);
    }

    public void setPlayersTurn(){

    }


    //Our MVC is seen with our GameController class. It is the messenger between the view and the model. It takes board state information and passes it to the view to display correctly.
    //As the view is interacted with through our GUI, the actioninfo is handled by the actionListener, and updates the model as needed.

    // Action handlers in the Controller, this is a super important aspect of our game as when a button is clicked on the GUI, these actionHandlers are able to identify
    //which button was clicked, gather that information, see what is needed from the model, and overall handle the interaction of when something is clicked
    //with the data of the game. We have a handler with it's own logic for when a gridSquare is pressed, so when playing a piece, one for when the inventorypiece is clicked, the piece
    //and the piece operations

    public class GridSquarePressed implements ActionListener {
        @Override
        //This event handler will handle lots of game playing logic, since this is when player places a piece onto the board
        //This represents a big "state", with lots of different paths of action.
        public void actionPerformed(ActionEvent e) {
            //we first get the details of which gridsquare was clicked on
            GUI.Square gridSquare = (GUI.Square)e.getSource();
            int x = gridSquare.x;
            int y = gridSquare.y;

            //first we must check whether there is a "currentlySelectedPiece", because otherwise the gridsquare was placed without having a piece to play
            if (game.getCurrentSelectedPiece() == null){
                gui.displayPopup(0);
            }
            //case where a piece is selected
            else {
                boolean firstMove = game.getPlayersTurnNumber()[game.getCurrentPlayerTurn()] == 0;
                int successfulMove = board.placePiece(game.getCurrentSelectedPiece(), x, y, firstMove);

                //see whether piece that is placed is successful, if so, update board state, reset currentlySelectedPiece, remove that piece from that playersInventory, inc playersTurnCounter,
                //and switch to next players turn
                if (successfulMove == 0){
                    updateAllObservers(Observer.Event.PIECEPLACED, new String[]{players[game.getCurrentPlayerTurn()].getColor().name(), game.getCurrentSelectedPiece().getLayoutType().name(), Integer.toString(x),Integer.toString(y)});
                    players[game.getCurrentPlayerTurn()].removePiece(game.getCurrentSelectedPiece().getLayoutType());
                    game.resetCurrentSelectedPiece();
                    game.incPlayersTurnNumber(game.getCurrentPlayerTurn());
                    game.incCurrentPlayerTurn(game.getCurrentPlayerTurn());
                    gui.updateAll(game);
                }
                else {
                    gui.displayPopup(1);
                }
            }
        }
    }

    public class InventoryPiecePressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton inventoryPiece = (JButton)e.getSource();
            String pieceLayout = inventoryPiece.getName();
            int currentPlayer = game.getCurrentPlayerTurn();
            game.setCurrentSelectedPiece(currentPlayer, players[currentPlayer].getPiece(pieceLayout));
            Piece currentPiece = game.getCurrentSelectedPiece();
            gui.highlightPiece(currentPiece);
        }
    }

    public class RotatePiecePressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateAllObservers(Observer.Event.ROTATE, new String[]{players[game.getCurrentPlayerTurn()].getColor().name()});
            Player currentPlayer = players[game.getCurrentPlayerTurn()];
            currentPlayer.transformAllPieces(Player.Transformation.ROTATE);
            gui.updateAll(game);

        }
    }

    public class FlipPiecePressed implements ActionListener {

        FlipPiecePressed(){

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateAllObservers(Observer.Event.FLIP, new String[]{players[game.getCurrentPlayerTurn()].getColor().name()});
            Player currentPlayer = players[game.getCurrentPlayerTurn()];
            currentPlayer.transformAllPieces(Player.Transformation.FLIPHORIZONTAL);
            gui.updateAll(game);
        }
    }

    public class GiveUpPressed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int currentPlayerTurn = game.getCurrentPlayerTurn();
            updateAllObservers(Observer.Event.GIVEUP, new String[]{players[game.getCurrentPlayerTurn()].getColor().name()});
            game.setPlayerStatus(currentPlayerTurn, 99);
            game.decreasePlayersRemaining();
            victoryStatus = game.incCurrentPlayerTurn(currentPlayerTurn);
            gui.updateAll(game);
            if (game.getPlayersRemaining() < 1){
                victoryRoutine();
            }
//            determineWinner();

        }
    }

    //loop through each piece and count number of trues in the layout
    public int countPointsForPlayer(Player player){

        int currentSum = 0;

        ArrayList<Piece> playerPieces = player.getPieces();

        for (int i = 0; i < playerPieces.size(); i++){
            for (int j = 0; j < playerPieces.get(i).getDim(); j++){
                for (int k = 0; k < playerPieces.get(i).getDim(); k++){
                    if (playerPieces.get(i).getLayout()[j][k] == true){
                        currentSum+=1;
                    }
                }
            }
        }

        return currentSum;
    }

    //logic for determining winner,  by looking at their score which is the number of tiles for left, so for each piece, we look at its 2D array storing the layout, and sum up all of the trues
    private int determineWinner(){

        int currentWinner = 0;
        int currentWinnerValue = 99;

        for (int i = 0; i < players.length; i++){
            if (countPointsForPlayer(players[i]) < currentWinnerValue){
                currentWinner = i;
                currentWinnerValue = countPointsForPlayer(players[i]);
            }
        }

        return currentWinner;
    }

    public void victoryRoutine(){
        int winner = determineWinner();
        updateAllObservers(Observer.Event.VICTORY, new String[]{players[winner].getColor().name()});
        System.out.println(winner+10);
        gui.displayPopup(winner+10);
        System.exit(0);

    }

    //used during debugging at some states
    public void printCurrentData(){
        System.out.println("\n");
        System.out.println("HERE IS THE CURRENT BOARD STATE\n");
        System.out.println("Current players turn: " + game.getCurrentPlayerTurn());
        System.out.println("Currently selected piece: " + game.getCurrentSelectedPiece().getLayoutType());
        System.out.println("Current players turn number: " + game.playersTurnNumber[game.getCurrentPlayerTurn()]);
        System.out.println("Current boardState:");
        board.printBoard();
    }
}
