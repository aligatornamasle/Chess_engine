
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.abs;

public class Evaluations {
    Integer[] gridd;
    Run game;
    int moves;
    int wP;
    int bP;
    public Evaluations(Run game){

        this.game = game;
        this.moves = moves;
    }

    private static double calculateDistance(int friendly,int enemy) {
           int  friendlyCol = friendly %8;
           int friendlyRow = friendly/8;


        int  enemyCol = enemy %8;
        int enemyRow = enemy/8;



        return Math.max(abs(friendlyCol - enemyCol), abs(friendlyRow - enemyRow));

    }

    public double evaluateBoard(Integer[] grid, boolean maximizingPlayer,int movesPlayed) {
        double score = 0.0;
        this.gridd=grid;
        int wPieces = 0;
        int bPieces = 0;
        for(int i = 0;i<64;i++){
            if(grid[i] >0){
                wPieces++;
            }
            if(grid[i]<0){
                bPieces++;
            }

        }
        wP = wPieces;
        bP = bPieces;
        //var gridInfo = game.boardStats(grid);



        if((bPieces+wPieces)<14){
            //System.out.println("endgame");
            return endGame(grid,maximizingPlayer)+score;


        }

        if(movesPlayed<15){
            return earlyGame(grid,maximizingPlayer);
        }


        return midGame(grid,maximizingPlayer)+ score;





    }




    public double endGame(Integer[] grid,boolean maximizingPlayer){
        double score = 0.0;





        HashMap<Integer, Double> pieceValues = new HashMap<>();
        pieceValues.put(5, 2.5);
        pieceValues.put(3, 3.2);
        pieceValues.put(4, 3.3);
        pieceValues.put(6, 5.0);
        pieceValues.put(2, 9.0);
        pieceValues.put(1, 0.0);

        int bKing=0;
        int wKing=0;
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


        for (int i = 0; i < grid.length; i++) {
            int piece = grid[i];
            if (piece == PiecesInt.None1) continue; // Skip empty squares

            int row1 = i / 8; // Calculate row (0-7)
            int col1 = i % 8; // Calculate column (0-7)

            int row = 7-row1;
            int col = 7-col1;

            double value = pieceValues.getOrDefault(Math.abs(piece), 0.0);

            // Position-based score
            double positionalValue = 0.0;
            if (Math.abs(piece) == PiecesInt.Pawn1) {
                if(piece >0){
                positionalValue = wpawnTable[row][col];
                }else{
                    positionalValue = bpawnTable[row][col];
                }
            } else if (Math.abs(piece) == PiecesInt.Knight1) {
                positionalValue = knightTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.Bishop1) {
                positionalValue = bishopTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.Rook1) {
                positionalValue = rookTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.Queen1) {
                positionalValue = queenTable[row][col];
            } else if (Math.abs(piece) == PiecesInt.King1) {

                if( piece>0){
                    wKing = (row*8) + col;
                }else{
                    bKing = (row*8) + col;
                }

                positionalValue = kingTable[row][col];
            }

            // Adjust the score based on the color of the piece
            if (PiecesInt.getColor(piece)) { // White piece
                score += value + positionalValue;
            } else { // Black piece
                score -= value + positionalValue;
            }
        }


int piece = 3;
for(int i = 0;i<2;i++) {

    double helpScore = 0;
    if (wP > bP) {

        if (piece > 0) {
            helpScore = 2 - (calculateDistance(bKing,wKing)) / 7;
            score += helpScore;
            piece = -3;
        } else {

            helpScore = 2 - (calculateDistance(wKing,bKing)) / 7;
            score += helpScore;
            piece = -3;
        }

    } else {
        if (piece > 0) {
            helpScore = 2 +(calculateDistance(bKing,wKing)) / 7;
            score += helpScore;
            piece = -3;

        } else {
            helpScore = -2 + (calculateDistance(wKing,bKing)) / 7;
            score += helpScore;
            piece = -3;
        }


    }


}






        return score;
    }










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

            int row1 = i / 8; // Calculate row (0-7)
            int col1 = i % 8; // Calculate column (0-7)
            int row = 7-row1;
            int col = 7-col1;

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








    public double earlyGame(Integer[] grid,boolean maximizingPlayer){
        double score = 0;


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
                {-0.2,  0, 0, 0.5, 0., 0.5, 0, -0.2},
                {-0.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.05, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.05},
                {-0.2,  0, 0, 0.5, 0., 0.5, 0, -0.2},
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





        for (int i = 0; i < grid.length; i++) {
            int piece = grid[i];
            if (piece == PiecesInt.None1) continue; // Skip empty squares

            int row1 = i / 8; // Calculate row (0-7)
            int col1 = i % 8; // Calculate column (0-7)
            int row = 7-row1;
            int col = 7-col1;

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

        // Add penalties for lack of king safety and rewards for central control here (not shown)
        return score;
    }


}
