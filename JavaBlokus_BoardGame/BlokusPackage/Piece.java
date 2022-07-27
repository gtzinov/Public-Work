package BlokusPackage;

import java.util.ArrayList;

public class Piece {

    public enum Layout { FIVE, FOUR, THREE, TWO, ONE, L_TALL, L_MED, L_SHORT, L_WIDE, T_TALL, T_SHORT, SQUARE, PLUS, U, Z_TALL, Z_SHORT, NUGGET, W, WEIRD1, WEIRD2, WEIRD3 }
    public enum Color { BLUE, RED, GREEN, YELLOW, BLANK }

    private boolean[][] layout;
    private Color color;
    private Layout layout_type;
    private int dim;

    public Piece(boolean[][] layout, Layout layout_type, int dim, Color color) {
        this.color = color;
        this.layout = layout;
        this.layout_type = layout_type;
        this.dim = dim;
    }

    public boolean[][] getLayout() { return layout; }
    public Color getColor() { return color; }
    public Layout getLayoutType() { return layout_type; }
    public int getDim() { return dim; }


    public void printLayout(){
        boolean[][] pieceLayout = layout;
        int pieceDimension = dim;
        for (int i = 0; i < pieceDimension; i++){
            for (int j = 0; j < pieceDimension; j++){
                int value = pieceLayout[i][j] ? 1 : 0;
                System.out.print(value);
                System.out.print("");
            }
            System.out.println();
        }
    }


}
