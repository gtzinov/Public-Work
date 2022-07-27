package BlokusPackage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class GUI {

    private JFrame mainFrame;
    private piecesHolder piecesHolderPanel;
    private controlPanel controlPanel;
    private boardPanel boardPanel;
    private scoreboardPanel scoreboardPanel;

//    The main JComponents are referenced here, they are updated over and over with what elements they contain and the style of them, so can't create new instances each time

    private GameController GC;
    private GameController.InventoryPiecePressed inventoryPiecePressed;
    private GameController.GridSquarePressed gridSquarePressed;
    private GameController.RotatePiecePressed rotatePiecePressed;
    private GameController.GiveUpPressed giveUpPressed;
    private GameController.FlipPiecePressed flipPressed;

//    Since we have some subclassing going on, have some slightly, just created reference here for easier addition of actionHandlers, ultimately it's just interaction with GameController

    private final int dim = 21;
    public static int size = 1000;

    public GUI(GameController GC){
        this.GC = GC;
        inventoryPiecePressed = GC.new InventoryPiecePressed();
        gridSquarePressed = GC.new GridSquarePressed();
        rotatePiecePressed = GC.new RotatePiecePressed();
        giveUpPressed = GC.new GiveUpPressed();
        flipPressed = GC.new FlipPiecePressed();

    }

    //main initalize function that adds main components to our mainFrame, sets some frame defaults for how large the Java Application to be and where it's set on the screen
    public void initializeGUI(Game game){
        mainFrame = new JFrame("Blokus Game");
        mainFrame.setLayout(new GridBagLayout());
        boardPanel = new boardPanel();
        controlPanel = new controlPanel();
        piecesHolderPanel = new piecesHolder();
        scoreboardPanel = new scoreboardPanel();

        boardPanel.initialize(game);
        controlPanel.initialize(game);
        scoreboardPanel.initialize(game);


        controlPanel.add(scoreboardPanel);
        mainFrame.add(piecesHolderPanel);
        mainFrame.add(controlPanel);
        mainFrame.add(boardPanel);
        mainFrame.setResizable(false);
        mainFrame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    //The following are all creating classes for our components, this allows us more effective overwriting of some of JComponents base behavior and of style as well. We can polymorphically
    //create instances of the classes the extend as well if needed.
    //Having classes for these also allows us to add labels to these components that we can set and refer to, which helped in getting the board position identification
    //from the GUI to the controller.
    private class scoreboardPanel extends JPanel implements DisplayUpdates {

        JLabel[] playersScores;

        public scoreboardPanel(){
            this.setLayout(new GridLayout(5,1));
            JLabel title = new JLabel("Players scores");
            title.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
            Border border = BorderFactory.createLineBorder(Color.darkGray,2);
            title.setBorder(border);
            this.add(title);

            playersScores = new JLabel[4];
        }
        public void initialize(Game game) {
            for (int i = 0; i < game.getPlayers().length;i++){
                playersScores[i] = new JLabel("<html><center>   89</center><html>");
                playersScores[i].setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
                playersScores[i].setForeground(returnColor(game.getPlayers()[i].getColor()));
                playersScores[i].setHorizontalAlignment(SwingConstants.CENTER);
                Border border = BorderFactory.createLineBorder(Color.darkGray,2);
                playersScores[i].setBorder(border);
                this.add(playersScores[i]);
            }
        }

        public void update(Game game) {
            for (int i = 0; i < game.getPlayers().length;i++){
                int score = GC.countPointsForPlayer(game.getPlayers()[i]);
                String stringedScore = Integer.toString(score);
                playersScores[i].setText(stringedScore);
            }
        }
    }

    private class boardPanel extends JPanel implements DisplayUpdates{
        boardPanel(){
            this.setLayout(new GridLayout(dim,dim));
            this.setBackground(Color.gray);
            this.setPreferredSize(new Dimension(size,size));
        }

        public void initialize(Game game){
            Piece.Color[][] state = game.getBoard().getState();
            for (int i = 0; i < state.length; i++){
                for (int j = 0; j < state[0].length;j++){
                    Square tempSquare = new Square(state[i][j].toString());
                    tempSquare.x = i;
                    tempSquare.y = j;
                    boardPanel.add(tempSquare);
                }
            }
            mainFrame.pack();
            boardPanel.repaint();
            boardPanel.revalidate();
        }

        public void update(Game game){
            for (int i = 0; i < game.getBoard().getState().length;i++) {
                for (int j = 0; j < game.getBoard().getState()[0].length; j++) {
                    if (game.getBoard().getState()[i][j] != Piece.Color.BLANK) {
                        boardPanel.getComponents()[i * dim + j].setForeground(returnColor(game.getBoard().getState()[i][j]));
                    }
                }
                boardPanel.repaint();
                boardPanel.revalidate();
            }
            highlightPiece(null);
        }

    }

    public class controlPanel extends JPanel implements DisplayUpdates{

        controlPanel(){
            this.setPreferredSize(new Dimension(size/(8) , size/2));
            this.setLayout(new GridLayout(5,1));
        }

        public void initialize(Game game){
            Piece.Color color = game.getPlayers()[game.getCurrentPlayerTurn()].getColor();
            JButton rotatePiecesButton = new RotatePiecesButton(color);
            controlPanel.add(rotatePiecesButton);
            JButton flipButton = new FlipPiecesButton(color);
            controlPanel.add(flipButton);
            JButton giveUpButton = new GiveUpButton(color);
            controlPanel.add(giveUpButton);
            JButton blankPlaceHolder = new JButton();
            blankPlaceHolder.setBorderPainted(false);
            blankPlaceHolder.setFocusPainted(false);
            blankPlaceHolder.setEnabled(false);
            blankPlaceHolder.setBackground(UIManager.getColor("Panel.background"));
            controlPanel.add(blankPlaceHolder);
            //have a blank button for some better spacing, basic way to do that when sometimes messing with layouts can be tricky.

        }

        public void update(Game game){
            RotatePiecesButton rbutton = (RotatePiecesButton) controlPanel.getComponent(0);
            rbutton.setForeground(returnColor(game.getPlayers()[game.getCurrentPlayerTurn()].getColor()));
            FlipPiecesButton fbutton = (FlipPiecesButton) controlPanel.getComponent(1);
            fbutton.setForeground(returnColor(game.getPlayers()[game.getCurrentPlayerTurn()].getColor()));
            GiveUpButton gbutton = (GiveUpButton) controlPanel.getComponent(2);
            gbutton.setForeground(returnColor(game.getPlayers()[game.getCurrentPlayerTurn()].getColor()));

        }

    }

    public class piecesHolder extends JPanel implements DisplayUpdates {

        piecesHolder(){
            this.setLayout(new GridLayout(7,3));
            this.setPreferredSize(new Dimension((int)(size/(7/3)),size));

        }

        public void initialize(Game game){

        }
        public void update(Game game){

            Component[] components = this.getComponents();
            for (Component comp: components){
                this.remove(comp);
            }
            ArrayList<Piece> currentPlayersPieces = game.getPlayers()[game.getCurrentPlayerTurn()].getPieces();

            for (int i = 0; i < currentPlayersPieces.size();i++){
                Piece currentPiece = currentPlayersPieces.get(i);
                JButton newPiece = new InventoryPiece(currentPiece.getColor().toString(), currentPiece.getLayout());
                newPiece.setBackground(Color.LIGHT_GRAY);
                newPiece.setName(currentPiece.getLayoutType().toString());
                this.add(newPiece);
            }

            for (int i = 0; i < 21-currentPlayersPieces.size();i++){
                JButton blankPlaceHolder = new JButton();
                blankPlaceHolder.setBorderPainted(false);
                blankPlaceHolder.setFocusPainted(false);
                blankPlaceHolder.setEnabled(false);
                blankPlaceHolder.setBackground(Color.LIGHT_GRAY);
                this.add(blankPlaceHolder);
            }

            mainFrame.pack();
            this.repaint();
            this.revalidate();
        }
    }

    private class InventoryPiece extends JButton {
        String color;
        boolean[][] pieceLayout;

        InventoryPiece(String color, boolean[][] pieceLayout){
            this.color = color;
            this.pieceLayout = pieceLayout;
            this.addActionListener(inventoryPiecePressed);
        }

        private void drawPiece(Graphics g, boolean layout[][]){
            int x, y, x2, y2;
            int x_max = getWidth();
            int y_max = getHeight();


            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    g.drawRect(i*(int)(x_max/5), j*(int)(y_max/5),(int)(x_max/5), (int)(y_max/5));
                }
            }

            for (int j = 0; j < layout.length; j++){
                for (int i = 0; i < layout[0].length;i++ ){

                    if (layout[j][i] == true){
                        g.fillRect(i*(int)(x_max/5),j*(int)(y_max/5),(int)((int)(x_max/5)*.95),(int)((int)(y_max/5)*.95));

                    }
                }

            }
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            //This if else is kind of a code smell, not the cleanest, but couldn't figure out how to insert, WE could do if we convert to hex values
            if (color == "RED"){
                g.setColor(Color.red);
            }
            else if (color == "BLUE" ){
                g.setColor(Color.BLUE);
            }

            else if (color == "YELLOW" ){
                g.setColor(Color.YELLOW);
            }

            else if (color == "GREEN" ){
                g.setColor(Color.GREEN);
            }

            drawPiece(g, pieceLayout);

        }
    }

    public class Square extends JButton {

        String color;
        int x;
        int y;

        Square (String color){
            this.addActionListener(gridSquarePressed);
            this.setForeground(Color.lightGray);
        }

        @Override
        public void paintComponent(Graphics g) {

            if (color == "RED"){
                g.setColor(Color.red);
            }
            else if (color == "BLUE" ){
                g.setColor(Color.BLUE);
            }

            else if (color == "YELLOW" ){
                g.setColor(Color.YELLOW);
            }

            else if (color == "GREEN" ){
                g.setColor(Color.GREEN);
            }

            super.paintComponent(g);
            g.fillRect(0,0, getWidth(), getHeight());
        }

    }

    public class RotatePiecesButton extends JButton  {

        RotatePiecesButton(Piece.Color color){
            this.addActionListener(rotatePiecePressed);
            this.setPreferredSize(new Dimension(size/10,size/10));
            this.setForeground(returnColor(color));
            this.setText("<html><center>Rotate <br> Inventory <br> Pieces</center></html>");


        }
    }

    public class FlipPiecesButton extends JButton {
        FlipPiecesButton(Piece.Color color){
            this.addActionListener(flipPressed);
            this.setPreferredSize(new Dimension(size/10,size/10));
            this.setForeground(returnColor(color));
            this.setText("<html><center>Flip <br> Inventory <br> Pieces</center></html>");

        }
    }

    public class GiveUpButton extends JButton {

        GiveUpButton(Piece.Color color){
            this.addActionListener(giveUpPressed);
            this.setPreferredSize(new Dimension(size/10,size/10));
            this.setForeground(returnColor(color));
            this.setText("<html><center>GIVE <br> UP</center></html>");

        }
    }

    public void highlightPiece(Piece piece ){

        for (Component c : piecesHolderPanel.getComponents()){
            if (piece != null && c.getName() == piece.getLayoutType().toString()){
                c.setBackground(Color.pink);
            }
            else {
                c.setBackground(Color.lightGray);
            }
        }
    }

    //helper function that helps convert from Java.awt.color class to our enums for pieces, Adapter like function but not full scale so not including as pattern for project
    public Color returnColor(Piece.Color color){
        if (color == Piece.Color.BLUE){
            return Color.BLUE;
        }
        else if (color == Piece.Color.RED){
            return Color.RED;
        }

        else if (color == Piece.Color.GREEN){
            return Color.GREEN;
        }

        else if (color == Piece.Color.YELLOW){
            return Color.YELLOW;
        }

        return Color.lightGray;
    }

    //catch statement for displayPopup codes to display right popup with relevant information
    public void displayPopup(int caseNumber){

        switch(caseNumber) {
            case 0:
                JOptionPane.showMessageDialog(null, "Please select a piece first!");
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Invalid Move! Try move again.");
                break;
            case 10:
                JOptionPane.showMessageDialog(null, "Congratulations! Player 1 won!");
                break;
            case 11:
                JOptionPane.showMessageDialog(null, "Congratulations! Player 2 won!");
                break;
            case 12:
                JOptionPane.showMessageDialog(null, "Congratulations! Player 3 won!");
                break;
            case 13:
                JOptionPane.showMessageDialog(null, "Congratulations! Player 4 won!");
                break;
            case 3:
                JLabel label = new JLabel("<html><center>Welcome to Java Blokus!</center><br> <center>If you haven't played before, the rules of the game are relatively simple. Feel free to look up rules to see how its played" +
                        "<br>A simple guide as follows: Each player's aim is to get rid of as many pieces in their inventory as possible. Sounds easy in the beginning, but as other players" +
                        "<br>play and the board fills up, it will get harder and harder. Hence, how a player plays their pieces is very strategic. Sometimes a goal is to push into the" +
                        "<br>center as quick as possible, sometimes a goal is to play each piece very efficiently and not waste space. For the first move, each player must play a piece that touches a corner. After that, for each piece that is played," +
                        "<br>a tile of that piece must be diagonally adjacent to a tile of a previous piece that player played, and none of the tiles may be directly adjacent to a tile of a previous piece that player played. " +
                        "<br> Player with the least amount of squares when the last player gives up when they don't have any available moves wins." +
                        "<br>Usability / implementation note: to place a piece down, the user must be align the top left square of the piece with where they want to play it on the board  " +
                        "<br>(if you imagine grabbing the top left of the piece where there is a square filled in always, it will be intuitive for placing." +
                        "<br>Players can choose which corner to start the game. " +
                        "<br>Click 'OK' to start playing!</center></html)");
                label.setFont(new Font("Trebuchet MS", Font.BOLD, 14));

                JOptionPane.showMessageDialog(null,label,"Welcome!", JOptionPane.INFORMATION_MESSAGE);


        }
    }

    public void updateAll(Game game){
        boardPanel.update(game);
        piecesHolderPanel.update(game);
        controlPanel.update(game);
        scoreboardPanel.update(game);
    }

}

