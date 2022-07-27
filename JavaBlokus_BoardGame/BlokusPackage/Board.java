package BlokusPackage;

import java.util.Arrays;

// Contains the state of a Blokus Board
// This serves as the model in our MVC pattern
public class Board {
    private final int dim = 21; // board dimensions
    private Piece.Color[][] state; // board state

    // Initializes a Board of size dim*dim with a blank state
    public Board() {
        state = new Piece.Color[dim][dim];
        for (Piece.Color[] row : state) {
            Arrays.fill(row, Piece.Color.BLANK);
        }
    }

    public Piece.Color[][] getState()  { return state; }

    // Returns the color at the given indices
    public Piece.Color getColorAt(int x, int y) {
        return state[x][y];
    }

    // Places a Piece on the Board at the given indices
    //
    // x and y refer to the spot at which the top left of the Piece will be placed
    // This may be a BLANK space depending on the Piece Layout
    //
    // Returns 0 if a Piece was successfully placed, -1 if the placement is invalid
    public int placePiece(Piece piece, int x, int y, boolean firstPlace) {
        // Initialize temporary array to operate on before verification
        Piece.Color[][] temp_state = new Piece.Color[dim][dim];
        for (int i = 0; i < dim; i++) {
            temp_state[i] = state[i].clone();
        }

        boolean corner = false;
        boolean diagonal = false;

        // Iterate within the bounds of where the Piece will be placed
        for (int i = x; i < x+piece.getDim(); i++) {
            for (int j = y; j < y+piece.getDim(); j++) {
                // Piece is placed outside of Board dimensions
                if (((i < 0 || j < 0) || (i >= dim || j >= dim)) && piece.getLayout()[i-x][j-y]) {
                    System.out.println("Outside of board dimensions play");
                    return -1;
                }
                // true tile is inside Board dimensions
                else if (i >= 0 && j >= 0 && piece.getLayout()[i-x][j-y]) {
                    // This placement overlaps with a previously placed Piece
                    if (state[i][j] != Piece.Color.BLANK) {
                        System.out.println("Overlap with prev tile play");
                        return -1;
                    }
                    else {
                        // A same color Piece tile occupies a directly adjacent spot
                        if (i > 0 && state[i-1][j] == piece.getColor()) {
                            System.out.println("Tile occupies spot directly adjacent");
                            return -1;
                        }
                        else if (i < dim-1 && state[i+1][j] == piece.getColor()) {
                            System.out.println("Tile occupies spot directly adjacent");
                            return -1;
                        }
                        else if (j > 0 && state[i][j-1] == piece.getColor()) {
                            System.out.println("Tile occupies spot directly adjacent");
                            return -1;
                        }
                        else if (j < dim-1 && state[i][j+1] == piece.getColor()) {
                            System.out.println("Tile occupies spot directly adjacent");
                            return -1;
                        }
                        else {
                            // This is the first Piece of this color placed and this Piece occupies a corner tile
                            if (firstPlace && ((i == 0 && j == 0) || (i == dim-1 && j == 0) || (i == 0 && j == dim-1) || (i == dim-1 && j == dim-1))) {
                                corner = true;
                            }

                            // A same color Piece tile occupies a diagonally adjacent spot
                            if (i-1 >= 0 && j-1 >= 0 && state[i-1][j-1] == piece.getColor()) {
                                diagonal = true;
                            }
                            else if (i+1 < dim && j-1 >= 0 && state[i+1][j-1] == piece.getColor()) {
                                diagonal = true;
                            }
                            else if (i+1 < dim && j+1 < dim && state[i+1][j+1] == piece.getColor()) {
                                diagonal = true;
                            }
                            else if (i-1 >= 0 && j+1 < dim && state[i-1][j+1] == piece.getColor()) {
                                diagonal = true;
                            }

                            // Update temporary array
                            temp_state[i][j] = piece.getColor();
                        }
                    }
                }
            }
        }

        // Invalid placement
        if ((firstPlace && !corner) || (!firstPlace && !diagonal)) {
            System.out.println("Just invalid play because its the first play and its not touching a corner, or because it's not a first play and its not diagonal");
            return -1;
        }
        else {
            state = temp_state.clone();
            return 0;
        }
    }

    // Prints the Board state to console
    public void printBoard() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                System.out.printf("%8s", state[i][j].name());
            }
            System.out.println();
            System.out.println();
        }
    }
}
