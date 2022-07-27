package BlokusPackage;

import java.util.ArrayList;

// Factory pattern for generating Piece objects
public class PieceFactory {
    // Implements singleton pattern as multiple instances are unnecessary
    // Eager instantiation as Piece objects will be needed at some point
    private static final PieceFactory instance = new PieceFactory();

    private PieceFactory() {}

    public static PieceFactory getInstance() {
        return instance;
    }

    // Generates a Piece object using the given parameters
    public Piece generatePiece(Piece.Layout layout_type, Piece.Color color) {
        boolean[][] layout;
        int dim;

        switch (layout_type) {
            case FIVE -> {
                dim = 5;
                layout = new boolean[][]{
                        {true, false, false, false, false},
                        {true, false, false, false, false},
                        {true, false, false, false, false},
                        {true, false, false, false, false},
                        {true, false, false, false, false}
                };
            }
            case FOUR -> {
                dim = 4;
                layout = new boolean[][]{
                        {true, false, false, false},
                        {true, false, false, false},
                        {true, false, false, false},
                        {true, false, false, false}
                };
            }
            case THREE -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, false, false},
                        {true, false, false},
                        {true, false, false}
                };
            }
            case TWO -> {
                dim = 2;
                layout = new boolean[][]{
                        {true, false},
                        {true, false}
                };
            }
            case ONE -> {
                dim = 1;
                layout = new boolean[][]{
                        {true}
                };
            }
            case L_TALL -> {
                dim = 4;
                layout = new boolean[][]{
                        {true, false, false, false},
                        {true, false, false, false},
                        {true, false, false, false},
                        {true,true, false, false}
                };
            }
            case L_MED -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, false, false},
                        {true, false, false},
                        {true, true, false}
                };
            }
            case L_SHORT -> {
                dim = 2;
                layout = new boolean[][]{
                        {true, false},
                        {true, true}
                };
            }
            case L_WIDE -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, false, false},
                        {true, false, false},
                        {true, true, true}
                };
            }
            case T_TALL -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, true, true},
                        {false, true, false},
                        {false, true, false}
                };
            }
            case T_SHORT -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, true, true},
                        {false, true, false},
                        {false, false, false}
                };
            }
            case SQUARE -> {
                dim = 5;
                layout = new boolean[][]{
                        {true,true,false,false, false},
                        {true,true,false,false, false},
                        {false,false,false,false,false},
                        {false,false,false,false,false},
                        {false,false,false,false,false}
                };
            }
            case PLUS -> {
                dim = 3;
                layout = new boolean[][]{
                        {false, true, false},
                        {true, true, true},
                        {false, true, false}
                };
            }
            case U -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, false, true},
                        {true, true, true},
                        {false, false, false}
                };
            }
            case Z_TALL -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, true, false},
                        {false, true, false},
                        {false, true, true}
                };
            }
            case Z_SHORT -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, true, false},
                        {false, true, true},
                        {false, false, false}
                };
            }
            case NUGGET -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, false, false},
                        {true, true, false},
                        {true, true, false}
                };
            }
            case W -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, false, false},
                        {true, true, false},
                        {false, true, true}
                };
            }
            case WEIRD1 -> {
                dim = 3;
                layout = new boolean[][]{
                        {true, true, false},
                        {false, true, true},
                        {false, true, false}
                };
            }
            case WEIRD2 -> {
                dim = 4;
                layout = new boolean[][]{
                        {true, false, false, false},
                        {true, true,false, false},
                        {true, false, false, false},
                        {true, false, false, false}
                };
            }
            case WEIRD3 -> {
                dim = 4;
                layout = new boolean[][]{
                        {true, false, false, false},
                        {true, true, false, false},
                        {false, true, false, false},
                        {false, true, false, false}
                };
            }
            default -> {
                dim = 0;
                layout = new boolean[][]{{false}};
            }
        }

        return new Piece(layout, layout_type, dim, color);
    }

    // Generates an ArrayList of all Piece layouts with the given color
    public ArrayList<Piece> generateAllPieces(Piece.Color color) {
        Piece.Layout[] all_layouts = Piece.Layout.values();
        ArrayList<Piece> pieces = new ArrayList<>();
        for (Piece.Layout layout : all_layouts) {
            pieces.add(this.generatePiece(layout, color));
        }
        return pieces;
    }
}
