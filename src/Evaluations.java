
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.abs;

public class Evaluations {
    Integer[] gridd;
    Run game;
    int moves;
    public Evaluations(Run game){

        this.game = game;
        this.moves = moves;
    }

    private static double calculateDistance(int friendly, int enemy) {


    //    return Math.max(abs(friendly.column - enemy.column), abs(friendly.row - enemy.row));
        return 0;
    }

    public double evaluateBoard(Integer[] grid, boolean maximizingPlayer,int movesPlayed) {
        double score = 0.0;
        this.gridd=grid;
        //var gridInfo = game.boardStats(grid);


/*
        if(nPieces<14){
            //System.out.println("endgame");
            return endGame()+score;
        }

        if(movesPlayed<15){
            return earlyGame(grid,maximizingPlayer);
        }

 */
        return midGame(grid,maximizingPlayer)+ score;





    }


/*

    public double endGame(){
        double score = 0.0;




        HashMap<String, Double> pieceValues = new HashMap<>();
        pieceValues.put("Pawn", 2.5);
        pieceValues.put("Knight", 3.2);
        pieceValues.put("Bishop", 3.3);
        pieceValues.put("Rook", 5.0);
        pieceValues.put("Queen", 9.0);
        pieceValues.put("King", 0.0);

        //var gridInfo = game.boardStats(grid1);
        //double endGameWeight = 4/gridInfo.get("pieces");
        //int nPieces = gridInfo.get("pieces");

        double pat = 0;




        double[][] bpawnTable = {
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                {0.3, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.3},
                {0.4, 0.7, 0.8, 0.8, 0.8, 0.8, 0.7, 0.4},
                {0.6, 0.8, 0.9, 1.0, 1.0, 0.9, 0.8, 0.6},
                {0.7, 0.9, 1.0, 1.1, 1.1, 1.0, 0.9, 0.7},
                {0.8, 1.0, 1.2, 1.3, 1.3, 1.2, 1.0, 0.8},
                {9, 9, 9,9, 9,9, 9, 9},
        };
        double[][] wpawnTable = {
                {9, 9, 9,9, 9,9, 9, 9},
                {0.8, 1.0, 1.2, 1.3, 1.3, 1.2, 1.0, 0.8},
                {0.7, 0.9, 1.0, 1.1, 1.1, 1.0, 0.9, 0.7},
                {0.6, 0.8, 0.9, 1.0, 1.0, 0.9, 0.8, 0.6},
                {0.4, 0.7, 0.8, 0.8, 0.8, 0.8, 0.7, 0.4},
                {0.3, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.3},
                {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        };

        double[][] knightTable = {
                {-0.5, -0.3, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5},
                {-0.4, -0.2, 0.0, 0.1, 0.1, 0.0, -0.2, -0.4},
                {-0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3},
                {-0.3, 0.1, 0.2, 0.25, 0.25, 0.2, 0.1, -0.3},
                {-0.3, 0.1, 0.2, 0.25, 0.25, 0.2, 0.1, -0.3},
                {-0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3},
                {-0.4, -0.2, 0.0, 0.1, 0.1, 0.0, -0.2, -0.4},
                {-0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5}
        };

        double[][] bishopTable = {
                {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2},
                {-0.1, 0.15, 0.05, 0.05, 0.05, 0.05, 0.15, -0.1},
                {-0.1, 0.05, 0.1, 0.1, 0.1, 0.1, 0.05, -0.1},
                {-0.1, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.1},
                {-0.1, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.1},
                {-0.1, 0.15, 0.1, 0.1, 0.1, 0.1, 0.15, -0.1},
                {-0.1, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
                {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2}
        };

        double[][] rookTable = {
                {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1},
                {0.1,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1},
                {0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1},
                {0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1},
                {0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1},
                {0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1},
                {0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1},
                {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1}


        };
        double[][] queenTable = {
                {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2},
                {-0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1},
                {-0.1, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
                {-0.05, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.05},
                {0.0, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.05},
                {-0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
                {-0.1, 0.0, 0.05, 0.0, 0.0, 0.0, 0.0, -0.1},
                {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2}
        };
        double[][] kingTable = {
                // {-2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,  -2},
                {-2, -2, -2, -2,-2, -2, -2, -2},
                {-2, 0.2, 0.4, 0.5, 0.5, 0.4, 0.2,  -2},
                {-2, 0.4, 0.6, 0.7, 0.7, 0.6, 0.4,  -2},
                {-2, 0.5, 0.7, 1.0, 1.0, 0.7, 0.5,  -2},
                {-2, 0.5, 0.7, 1.0, 1.0, 0.7, 0.5, -2},
                {-2, 0.4, 0.6, 0.7, 0.7, 0.6, 0.4,  -2},
                {-2, 0.2, 0.4, 0.5, 0.5, 0.4, 0.2,  -2},
                {-2, -2, -2, -2,-2, -2, -2, -2},

        };


        ArrayList<Piece> pieces = gridInfo.get("allPieces");
        for(int i =0;i<gridInfo.get("allPieces").size();i++) {
            String pieceType = pieces.get(i).getClass().getSimpleName();

            double value = pieceValues.getOrDefault(pieceType, 0.0);
            int col = pieces.get(i).getRow();
            int row = pieces.get(i).getColumn();

            double positionalValue = 0.0;

            if (pieceType.equals("Pawn")) {
                if (pieces.get(i).getColor().equals("black")) {

                    //positionalValue = pawnTable[col][row];
                    //positionalValue+=((double)piece.getRow())/3;
                    positionalValue += bpawnTable[col][row];
                } else {
                    //positionalValue = pawnTable[col][row];
                    // positionalValue-=((double)piece.getRow())/3;
                    positionalValue += wpawnTable[col][row];

                }
            } else if (pieceType.equals("Knight")) positionalValue += knightTable[col][row];
            else if (pieceType.equals("Bishop")) positionalValue += bishopTable[col][row];
            else if (pieceType.equals("Rook")){


                pieces.get(i).ifChecked(gridd);
                // double open =  ((Rook) pieces.get(i)).getPohlad();




                //       int a = ((Rook)pieces.get(i)).getPohlad();
                // positionalValue += rookTable[col][row];
                // positionalValue += open;

            }
            else if (pieceType.equals("Queen")) positionalValue += queenTable[col][row];
            else if (pieceType.equals("King")) {

                double helpScore = 0.0;
                int whitewinning = gridInfo.get("wPieces").size();
                int blackwinning = gridInfo.get("bPieces").size();
                if (whitewinning > blackwinning) {
                    if (pieces.get(i).getColor().equals("white")) {

                        helpScore = 2 - (calculateDistance(gridInfo.get("bKing").get(0), gridInfo.get("whiteKing").get(0))) / 6;
                    } else {
                        helpScore = -2 + (calculateDistance(gridInfo.get("wKing").get(0), gridInfo.get("wKing").get(0))) / 6;
                    }


                } else {

                    if (pieces.get(i).getColor().equals("black")) {
                        helpScore = 2 - (calculateDistance(gridInfo.get("bKing").get(0), gridInfo.get("wKing").get(0))) / 6;
                    } else {
                        helpScore = -2 + (calculateDistance(gridInfo.get("wKing").get(0), gridInfo.get("wKing").get(0))) / 6;
                    }

                }
                positionalValue += kingTable[col][row] + helpScore;

            }

            // System.out.println(positionalValue);
            if (pieces.get(i).getColor().equals("white")) {
                score += value + positionalValue;
            } else {
                score -= value + positionalValue;
            }


        }


        return score;
    }





 */





    public double midGame(Integer[] grid,boolean maximinizing){
        double score= 0.0;



        // Define piece values
        HashMap<Integer, Double> pieceValues = new HashMap<>();
        pieceValues.put(5, 1.0);
        pieceValues.put(3, 3.2);
        pieceValues.put(4, 3.3);
        pieceValues.put(6, 5.0);
        pieceValues.put(2, 9.0);
        pieceValues.put(1, 0.0);

        double[][] pawnTable = {
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4},
                {0.1, 0.6, 0.5, 0.5, 0.5, 0.5, 0.6, 0.1},
                {0.3, 0.4, 0.6, 0.9, 0.9, 0.6, 0.4, 0.3},
                {0.3, 0.4, 0.6, 0.9, 0.9, 0.6, 0.4, 0.3},
                {0.1, 0.4, 0.5, 0.5, 0.5, 0.5, 0.4, 0.1},
                {0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        };

        double[][] knightTable = {
                {-0.5, -0.3, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5},
                {-0.4, -0.2, 0.0, 0.1, 0.1, 0.0, -0.2, -0.4},
                {-0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3},
                {-0.3, 0.1, 0.2, 0.25, 0.25, 0.2, 0.1, -0.3},
                {-0.3, 0.1, 0.2, 0.25, 0.25, 0.2, 0.1, -0.3},
                {-0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3},
                {-0.4, -0.2, 0.0, 0.1, 0.1, 0.0, -0.2, -0.4},
                {-0.5, -0.3, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5},
        };

        double[][] bishopTable = {
                {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2},
                {-0.1, 0.15, 0.05, 0.05, 0.05, 0.05, 0.15, -0.1},
                {-0.1, 0.05, 0.1, 0.1, 0.1, 0.1, 0.05, -0.1},
                {-0.1, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.1},
                {-0.1, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.1},
                {-0.1, 0.15, 0.1, 0.1, 0.1, 0.1, 0.15, -0.1},
                {-0.1, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
                {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2}
        };
/*
        double[][] rookTable = {
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
        };

 */
        double[][] rookTable = {
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},
                {0,  0, 0, 0.0, 0.0, 0, 0, 0},

        };
        double[][] queenTable = {
                {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2},
                {-0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1},
                {-0.1, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
                {-0.05, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.05},
                {0.0, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.05},
                {-0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
                {-0.1, 0.0, 0.05, 0.0, 0.0, 0.0, 0.0, -0.1},
                {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2}
        };
        double[][] kingTable = {
                {0.5, 0.9, 0.6, 0.-1, -0.1, -0.1, 0.9, 0.5},
                {0.2, 0.3, 0.1, 0.0, 0.0, 0.1, 0.3, 0.2},
                {0.1, 0.2, 0.0, -0.1, -0.1, 0.0, 0.2, 0.1},
                {0.0, 0.1, -0.1, -0.2, -0.2, -0.1, 0.1, 0.0},
                {0.0, 0.1, -0.1, -0.2, -0.2, -0.1, 0.1, 0.0},
                {0.1, 0.2, 0.0, -0.1, -0.1, 0.0, 0.2, 0.1},
                {0.2, 0.3, 0.1, 0.0, 0.0, 0.1, 0.3, 0.2},
                {0.5, 0.4, 0.-1, 0.-1, -0.1,-0.1, 0.4,0.5}
        };


        for (int i = 0; i < grid.length; i++) {
            int piece = grid[i];
            if (piece == PiecesInt.None1) continue; // Skip empty squares

            int row = i / 8; // Calculate row (0-7)
            int col = i % 8; // Calculate column (0-7)

            double value = pieceValues.getOrDefault(Math.abs(piece), 0.0);

            // Position-based score
            double positionalValue = 0.0;
            if (Math.abs(piece) == PiecesInt.Pawn1) {
                positionalValue = pawnTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.Knight1) {
                positionalValue = knightTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.Bishop1) {
                positionalValue = bishopTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.Rook1) {
                positionalValue = rookTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.Queen1) {
                positionalValue = queenTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.King1) {
                positionalValue = kingTable[row][col];
            }

            // Adjust the score based on the color of the piece
            if (PiecesInt.getColor(piece)) { // White piece
                score += value + positionalValue;
            } else { // Black piece
                score -= value + positionalValue;
            }
        }

        return score;
    }






    /*

    public double earlyGame(Piece[][] grid,boolean maximizingPlayer){
        double score = 0;


        // Define piece values
        HashMap<String, Double> pieceValues = new HashMap<>();
        pieceValues.put("Pawn", 1.0);
        pieceValues.put("Knight", 3.0);
        pieceValues.put("Bishop", 3.0);
        pieceValues.put("Rook", 5.0);
        pieceValues.put("Queen", 9.0);
        pieceValues.put("King", 0.0);

        double[][] pawnTable = {
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4},
                {0.1, 0.6, 0.5, 0.5, 0.5, 0.5, 0.6, 0.1},
                {0.3, 0.4, 0.6, 0.9, 0.9, 0.6, 0.4, 0.3},
                {0.3, 0.4, 0.6, 0.9, 0.9, 0.6, 0.4, 0.3},
                {0.1, 0.4, 0.5, 0.5, 0.5, 0.5, 0.4, 0.1},
                {0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        };


        double[][] knightTable = {
                {-0.5, -0.3, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5},
                {-0.4, -0.2, 0.0, 0.1, 0.1, 0.0, -0.2, -0.4},
                {-0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3},
                {-0.3, 0.1, 0.2, 0.25, 0.25, 0.2, 0.1, -0.3},
                {-0.3, 0.1, 0.2, 0.25, 0.25, 0.2, 0.1, -0.3},
                {-0.3, 0.0, 0.15, 0.2, 0.2, 0.15, 0.0, -0.3},
                {-0.4, -0.2, 0.0, 0.1, 0.1, 0.0, -0.2, -0.4},
                {-0.5, -0.4, -0.3, -0.3, -0.3, -0.3, -0.4, -0.5}
        };



        double[][] bishopTable = {
                {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2},
                {-0.1, 0.15, 0.05, 0.05, 0.05, 0.05, 0.15, -0.1},
                {-0.1, 0.05, 0.1, 0.1, 0.1, 0.1, 0.05, -0.1},
                {-0.1, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.1},
                {-0.1, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.1},
                {-0.1, 0.05, 0.1, 0.15, 0.15, 0.1, 0.05, -0.1},
                {-0.1, 0.15, 0.05, 0.05, 0.05, 0.05, 0.15, -0.1},
                {-0.2, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.2}
        };


        double[][] rookTable = {
                {0,  0, 0, 0.5, 0., 0.5, 0, 0},
                {-0.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {0,  0, 0, 0.5, 0., 0.5, 0, 0},
        };


        double[][] queenTable = {
                {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2},
                {-0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1},
                {-0.1, 0.0, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
                {-0.05, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.05},
                {0.0, 0.0, 0.05, 0.1, 0.1, 0.05, 0.0, -0.05},
                {-0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.0, -0.1},
                {-0.1, 0.0, 0.05, 0.0, 0.0, 0.0, 0.0, -0.1},
                {-0.2, -0.1, -0.1, -0.05, -0.05, -0.1, -0.1, -0.2}
        };




        double[][] kingTable = {
                {0.2, 0.9, 0.3, 0.0, 0.0, 0.0, 0.9, 0.2},
                {0.2, 0.3, 0.1, 0.0, 0.0, 0.1, 0.3, 0.2},
                {0.1, 0.2, 0.0, -0.1, -0.1, 0.0, 0.2, 0.1},
                {0.0, 0.1, -0.1, -0.2, -0.2, -0.1, 0.1, 0.0},
                {0.0, 0.1, -0.1, -0.2, -0.2, -0.1, 0.1, 0.0},
                {0.1, 0.2, 0.0, -0.1, -0.1, 0.0, 0.2, 0.1},
                {0.2, 0.3, 0.1, 0.0, 0.0, 0.1, 0.3, 0.2},
                {0.2, 0.9, 0.3, 0.0, 0.0, 0.0, 0.9, 0.2},
        };




        // Iterate through the board and calculate score
        // for (int row = 0; row < grid.length; row++) {
        //   for (int col = 0; col < grid[row].length; col++) {
        for(int i =0;i<gridInfo.get("allPieces").size();i++){
            Piece piece = gridInfo.get("allPieces").get(i);


            int row = piece.getColumn();
            int col = piece.getRow();
            if (piece != null) {
                String pieceType = piece.getClass().getSimpleName();
                double value = pieceValues.getOrDefault(pieceType,0.0);

                // Position-based score
                double positionalValue = 0;
                if (pieceType.equals("Pawn")) positionalValue = pawnTable[col][row];
                else if (pieceType.equals("Knight")) positionalValue = knightTable[col][row];
                else if (pieceType.equals("Bishop")) positionalValue = bishopTable[col][row];
                else if (pieceType.equals("Rook")) {


                    piece.ifChecked(gridd);
                    double open =  ((Rook) piece).getPohlad();


                    positionalValue = rookTable[col][row];
                    positionalValue += open/20;




                }
                else if (pieceType.equals("Queen")) positionalValue = queenTable[col][row];
                else if (pieceType.equals("King")) positionalValue = kingTable[col][row];

                if (piece.getColor().equals("white")) {
                    score += value + positionalValue;
                } else {
                    score -= value + positionalValue;
                }
            }
            //   }
        }



        // Add penalties for lack of king safety and rewards for central control here (not shown)
        return score;
    }
*/

}
