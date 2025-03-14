import java.util.List;
import java.util.Random;

public class ChessBoardEvaluator {
    Search game;
    int blackPoints=0;
    int whitePoints = 0;
    MoveGenerator moveGenerator;

    public ChessBoardEvaluator(Search game) {
        this.game = game;
        this.moveGenerator = new MoveGenerator();
    }

    public int MopUpEval(int friendlyIndex, int opponentIndex, int myMaterial, int opponentMaterial, double endgameWeight) {
        int mopUpScore = 0;
        if (endgameWeight <= 0) {
            return 0;
        }
        int friendlyFile = friendlyIndex % 8;
        int friendlyRank = friendlyIndex / 8;
        int opponentFile = opponentIndex % 8;
        int opponentRank = opponentIndex / 8;
        int fileDiff = Math.abs(friendlyFile - opponentFile);
        int rankDiff = Math.abs(friendlyRank - opponentRank);
        int kingDistance = Math.max(fileDiff, rankDiff); // Chebyshev distance
        // If winning, encourage friendly king to approach
        if (myMaterial > opponentMaterial + 200) {
            mopUpScore += (7 - kingDistance) * 10 * 4;
        } else if (opponentMaterial > myMaterial + 200) { // If losing, penalize friendly king for being close
            mopUpScore -= (7 - kingDistance) * 10 * 4;
        }
        return (int) (mopUpScore * endgameWeight);
    }

    double EndgamePhaseWeight(int materialCountWithoutPawns) {
        double multiplier = 1.0/7800;
        return 1 - Math.min(1, (materialCountWithoutPawns * multiplier));
    }



    public double evaluateBoard(Integer[] grid, Move lastMove) {
        int score = 0;

        whitePoints = 0;
        blackPoints = 0;


        for (int i = 0; i < 64; i++) {
            if (grid[i] > 0) {
                switch (grid[i]) {
                    case 2 ->
                        whitePoints += 900;

                    case 3 ->
                        whitePoints += 320;

                    case 4 ->
                        whitePoints += 330;

                    case 5 ->
                        whitePoints += 100;

                    case 6 ->
                        whitePoints += 500;

                }
            }
            if (grid[i] < 0) {
                switch (grid[i]) {
                    case -2 ->
                        blackPoints += 900;

                    case -3 ->
                        blackPoints += 320;

                    case -4 ->
                        blackPoints += 330;

                    case -5 ->
                       blackPoints += 100;

                    case -6 ->
                        blackPoints += 500;


                }
            }
        }

        // **Improved Endgame Condition**
        boolean endgame = ((whitePoints + blackPoints) < 2700);
        double randomNoise = 0;

        if(ChessHeuristics.randomNoise){
            randomNoise = -0.1 + (0.2 * new Random().nextDouble());
        }

        if (endgame) {
            return endGame(grid) + score+ randomNoise;
        }

        return midGame(grid) + score+ randomNoise;
    }

    public int endGame(Integer[] grid) {
        int score = 0;
        int bKing = 0;
        int wKing = 0;

        for (int i = 0; i < grid.length; i++) {
            int piece = grid[i];
            if (piece == PiecesInt.None1) continue; // Skip empty squares
            int row1 = i / 8; // Calculate row (0-7)
            double value = ChessHeuristics.pieceValues.getOrDefault(Math.abs(piece), 0);
            double positionalValue = 0.0;

            if (Math.abs(piece) == PiecesInt.Pawn1) {
                positionalValue+= pawnEvaluate(piece,row1,i);
                positionalValue += PawnStructureEvaluation.evaluatePawn(grid,i,PiecesInt.getColor(piece));

            } else if (Math.abs(piece) == PiecesInt.Knight1) {
                if (piece < 0) {
                    positionalValue = ChessHeuristics.knightTable[i];
                } else {
                    positionalValue = ChessHeuristics.knightTable[63 - i];
                }
            } else if (Math.abs(piece) == PiecesInt.Bishop1) {
                if (piece < 0) {
                    positionalValue = ChessHeuristics.bishopTable[i];
                } else {
                    positionalValue = ChessHeuristics.bishopTable[63 - i];
                }
            } else if (Math.abs(piece) == PiecesInt.Rook1) {

                if (piece < 0) {
                    positionalValue = ChessHeuristics.rookTable[i];
                } else {
                    positionalValue = ChessHeuristics.rookTable[63 - i];
                }
            } else if (Math.abs(piece) == PiecesInt.Queen1) {
                if (piece < 0) {
                    positionalValue = ChessHeuristics.queenTable[i];
                } else {
                    positionalValue = ChessHeuristics.queenTable[63 - i];

                }
            } else if (Math.abs(piece) == PiecesInt.King1) {


                if (piece > 0) {
                    wKing = i;
                } else {
                    bKing = i;
                }
                if (piece < 0) {
                    positionalValue = ChessHeuristics.endKingTable[i];

                } else {
                    positionalValue = ChessHeuristics.endKingTable[63 - i];
                    //  System.out.println(positionalValue);
                }
            }

            // Adjust the score based on the color of the piece

            if (PiecesInt.getColor(piece)) { // White piece
                score += (int) (value + positionalValue);
            } else { // Black piece
                score -= (int) (value + positionalValue);
            }
        }

// Inside the endGame method:
        int whiteMopUp = MopUpEval(wKing, bKing, whitePoints, blackPoints, EndgamePhaseWeight(whitePoints));
        int blackMopUp = MopUpEval(bKing, wKing, blackPoints, whitePoints, EndgamePhaseWeight(blackPoints));
        score += whiteMopUp - blackMopUp;

        return score;
    }

    public int midGame(Integer[] grid){
        int score= 0;

        for (int i = 0; i < grid.length; i++) {

            int piece = grid[i];
            if (piece == PiecesInt.None1) continue; // Skip empty squares
            int row1 = i / 8; // Calculate row (0-7)
            int col1 = i % 8; // Calculate column (0-7)
            int value = ChessHeuristics.pieceValues.getOrDefault(Math.abs(piece), 0);
            double positionalValue = 0.0;

            if (Math.abs(piece) == PiecesInt.Pawn1) {
                positionalValue+= pawnEvaluate(piece,row1,i);


                //positionalValue += ChessEvaluator.evaluatePawn(grid,i,PiecesInt.getColor(piece));
            } else if (Math.abs(piece) == PiecesInt.Knight1) {
                if(piece<0){
                    positionalValue = ChessHeuristics.knightTable[i];
                }else{
                    positionalValue = ChessHeuristics.knightTable[63-i];
                }
            } else if (Math.abs(piece) == PiecesInt.Bishop1) {
                if(piece<0){
                    positionalValue = ChessHeuristics.bishopTable[i];}
                else{
                    positionalValue = ChessHeuristics.bishopTable[63-i];
                }
            } else if (Math.abs(piece) == PiecesInt.Rook1) {
                if(piece<0){
                    positionalValue = ChessHeuristics.rookTable[i];
                }else{
                    positionalValue = ChessHeuristics.rookTable[63-i];
                }
                // The column of the rook
                if(isFileOpen(grid, col1)) {
                    if (isFileOpen(grid, col1)) {
                        positionalValue += 20;  // Give a bonus if the rook is on an open file
                    }
                }
            } else if (Math.abs(piece) == PiecesInt.Queen1) {
                if(piece<0){
                    positionalValue = ChessHeuristics.queenTable[i];}
                else{
                    positionalValue = ChessHeuristics.queenTable[63-i];

                }
            } else if (Math.abs(piece) == PiecesInt.King1) {
                if(piece<0){
                    positionalValue = ChessHeuristics.kingTable[i];
                }
                else{
                    positionalValue = ChessHeuristics.kingTable[63-i];
                }
            }

            // Adjust the score based on the color of the piece
            if (PiecesInt.getColor(piece)) { // White piece
                score += (int)(value + positionalValue);
            } else { // Black piece
                score -= (int) (value + positionalValue);
            }
        }
        return score;
    }

    boolean isFileOpen(Integer[] grid,int col) {
        // Example: Check if the column is free of pawns
        for (int row = 0; row < 8; row++) {
            if (grid[row * 8 + col] == PiecesInt.Pawn1 || grid[row * 8 + col] == PiecesInt.Pawn2) {
                return false;
            }
        }
        return true;
    }
    double pawnEvaluate(int piece, int row1, int i){
        double positionalValue = 0.0;
        if(piece<0){
            int advancementBonus = (7 - row1) * 15; // Base bonus for pawn advancement
            double endgameWeight = EndgamePhaseWeight(blackPoints+whitePoints);
            positionalValue += advancementBonus * endgameWeight;
            positionalValue += ChessHeuristics.pawnTable[i];
        }else{
            int advancementBonus = row1 * 15; // Base bonus for pawn advancement
            double endgameWeight = EndgamePhaseWeight(whitePoints+blackPoints); // Get endgame weight
            positionalValue += advancementBonus * endgameWeight;
            positionalValue += ChessHeuristics.pawnTable[63-i];
        }
        return positionalValue;

    }


}
