package BlokusPackage;

// Contains the entire state of a game of Blokus
public class Game {
    private Board board;
    private Player[] players;
    private int currentPlayerTurn;
    private Piece currentSelectedPiece;
    int[] playersTurnNumber;
    private int[] playerStatus;
    private int playersRemaining;


    Game() {
        board = new Board();
        playersTurnNumber = new int[4];
        playerStatus = new int[4];
    }

    public int getPlayersRemaining() {
        return playersRemaining;
    }

    // Initializes players with colors in order of the given Piece.Color array, colors
    public void initializePlayers(int num_players, Piece.Color[] colors) {
        PieceFactory factory = PieceFactory.getInstance();
        playersRemaining = num_players;
        players = new Player[num_players];
        for (int i = 0; i < num_players; i++) {
            players[i] = new Player(colors[i], factory.generateAllPieces(colors[i]));
        }
    }

    public Board getBoard() { return board; }
    public Player[] getPlayers() { return players; }

    public Player getPlayer(Piece.Color player_color) {
        for (Player player : players) {
            if (player.getColor() == player_color) {
                return player;
            }
        }
        return null;
    }


    public int incCurrentPlayerTurn(int currentPlayerTurn){



        if (playerStatus[(currentPlayerTurn+1)%4] != 99){
            setCurrentPlayerTurn(currentPlayerTurn+1);
            this.currentPlayerTurn = (currentPlayerTurn+1)%4;
            if (playersRemaining < 1){
                return 1;
            }
            return 0;
        }
        else {
            this.currentPlayerTurn = (currentPlayerTurn + 1 + (4-playersRemaining)) % 4;
            return 0;
        }



    }
    public void setCurrentPlayerTurn(int turn){
        currentPlayerTurn = turn;
    }

    public int getCurrentPlayerTurn(){
        return currentPlayerTurn;
    }

    public void setCurrentSelectedPiece(int currentPlayerTurn, Piece piece){

        currentSelectedPiece = players[currentPlayerTurn].getPiece(piece.getLayoutType());
    }

    public void resetCurrentSelectedPiece(){

        currentSelectedPiece = null;
    }

    public Piece getCurrentSelectedPiece() {
        return currentSelectedPiece;
    }

    public int[] getPlayersTurnNumber(){
        return playersTurnNumber;
    }

    public void incPlayersTurnNumber(int player){
        playersTurnNumber[player] +=1;
    }

    public void setPlayerStatus(int player, int status){
        playerStatus[player] = status;
    }

    public void decreasePlayersRemaining(){
        playersRemaining -= 1;
    }
}
