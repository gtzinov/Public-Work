package BlokusPackage;

import java.util.ArrayList;

// Contains the state of a Player
public class Player {
    public enum Transformation { ROTATE, FLIPHORIZONTAL, FLIPVERTICAL }

    private final Piece.Color color;
    private ArrayList<Piece> pieces;

    Player(Piece.Color color, ArrayList<Piece> pieces) {
        this.color = color;
        this.pieces = pieces;
    }

    public Piece.Color getColor() { return color; }
    public ArrayList<Piece> getPieces() { return pieces; }

    public Piece getPiece(Piece.Layout piece_layout) {
        for (Piece piece : pieces) {
            if (piece.getLayoutType() == piece_layout) {
                return piece;
            }
        }
        return null;
    }

    public Piece getPiece(String layout){
        for (Piece piece: pieces){
            if (piece.getLayoutType().toString() == layout){
                return piece;
            }
        }
        return null;
    }

    // Removes the first Piece in pieces with the given layout
    public void removePiece(Piece.Layout layout) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getLayoutType() == layout) {
                pieces.remove(i);
                return;
            }
        }
    }

    // Shifts a matrix of booleans by the specified amount
    private void translateMatrix(boolean[][] layout, int dim, int top, int left) {
            for (int i = top; i < dim; i++) {
            for (int j = left; j < dim; j++) {
                if (layout[i][j] && (top != 0 || left != 0)) {
                    layout[i-top][j-left] = layout[i][j];
                    layout[i][j] = false;
                }
            }
        }
    }

    // Adjusts the given matrix to a state where it cannot be shifted up or left
    private void justifyMatrixTopLeft(boolean[][] layout, int dim) {
        int top = -1;
        int left = dim;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (top < 0 && layout[i][j]) {
                    top = i;
                }
                if (j < left && layout[i][j]) {
                    left = j;
                }
            }
        }
        translateMatrix(layout, dim, top, left);
    }

    // Horizontally flips the given matrix
    private void flipMatrixHorizontal(boolean[][] layout, int dim) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim/2; j++) {
                boolean temp = layout[i][j];
                layout[i][j] = layout[i][dim-1-j];
                layout[i][dim-1-j] = temp;
            }
        }
        justifyMatrixTopLeft(layout, dim);
    }

    // Vertically flips the given matrix
    private void flipMatrixVertical(boolean[][] layout, int dim) {
        for (int j = 0; j < dim; j++) {
            for (int i = 0; i < dim/2; i++) {
                boolean temp = layout[i][j];
                layout[i][j] = layout[dim-1-i][j];
                layout[dim-1-i][j] = temp;
            }
        }
        justifyMatrixTopLeft(layout, dim);
    }

    // Rotates the given matrix 90 degrees clockwise
    // Algorithm sourced from https://www.javatpoint.com/rotate-matrix-by-90-degrees-in-java
    private void rotateMatrix(boolean[][] layout, int dim) {
        // Determine matrix transpose
        for (int i = 0; i < dim; i++) {
            for (int j = i; j < dim; j++) {
                boolean temp = layout[i][j];
                layout[i][j] = layout[j][i];
                layout[j][i] = temp;
            }
        }
        // Perform rotation using transpose
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim / 2; j++) {
                boolean temp = layout[i][j];
                layout[i][j] = layout[i][dim - 1 - j];
                layout[i][dim - 1 - j] = temp;
            }
        }

        justifyMatrixTopLeft(layout, dim);
    }

    // Executes the specified transformation on all Pieces in this Player's inventory
    public void transformAllPieces(Transformation transformation) {
        for (Piece p : pieces){
            int dim = p.getDim();
            boolean[][] layout = p.getLayout();
            switch (transformation) {
                case ROTATE -> { rotateMatrix(layout, dim); }
                case FLIPVERTICAL -> { flipMatrixVertical(layout, dim); }
                case FLIPHORIZONTAL -> { flipMatrixHorizontal(layout, dim); }
            }
        }
    }
}
