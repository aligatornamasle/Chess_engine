import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.*;

public class MiniMaxAlgorithm {
    int a = 0;
    Integer[] grid;
    Run game;
    //int test;
    MoveScore move1;
    int test;
    int prom;
    int sorting=0;
    int movesPlayed=0;
    Evaluations evaluations;




    //private static final int MAX_DEPTH = 1;
    private int MAX_DEPTH;
    public MiniMaxAlgorithm(Run game) {

        this.game = game;
        this.evaluations = new Evaluations(game);
    }
    public MoveScore getMoveScore(){

        return move1;
    }

    public void createTree(Integer[] grid,int maxDepth,boolean maximinizing,int movesPlayed,boolean[] iscastling,Move previousMove, Run game){
        this.movesPlayed=movesPlayed;
        MAX_DEPTH = maxDepth;
        test = 0;
        this.grid = grid;
        this.game = game;
      //  move1 = minimax(grid,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,MAX_DEPTH,maximinizing,iscastling,previousMove);
        move1 = minimax(grid,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,maxDepth,maximinizing,iscastling,previousMove);
        //System.out.println(move1.score);
        if(move1.move!=null)
            System.out.println(move1.move.toString());
        System.out.println(test);


    }
/*
    public MoveScore minimax(Integer[] currentGrid, double alpha, double beta, int maxDepth, boolean maximizingPlayer,boolean[] castling,Move latestMove) {

        if (maxDepth == 0) {
            test++;
            return new MoveScore(evaluations.evaluateBoard(currentGrid, maximizingPlayer,movesPlayed), latestMove);
        }


        List<PieceMovePair> prioritizedMoves = new ArrayList<>();
        var possibleMoves = game.getPossibleMoves(maximizingPlayer, currentGrid, latestMove,castling);
        if (possibleMoves.size() ==0) {

                    double score = maximizingPlayer ? -10000-maxDepth : 10000-maxDepth;
                    return new MoveScore(score, null);
        }

        int calculating = 0;
        for (Move m: possibleMoves) {
                sorting++;
            Integer[] nextGrid = game.copyGridWithMove(grid,m,maximizingPlayer,latestMove, castling);
                double moveScore = evaluations.evaluateBoard(nextGrid, maximizingPlayer,movesPlayed);
                prioritizedMoves.add(new PieceMovePair(m.start,m.target, m, moveScore,calculating));
              //  calculating++;

        }

        if (maximizingPlayer) {
            prioritizedMoves.sort((a, b) -> Double.compare(b.score, a.score)); // Higher scores first
        } else {
            prioritizedMoves.sort((a, b) -> Double.compare(a.score, b.score)); // Lower scores first
        }


        if (maximizingPlayer) {
            double maxEval = Double.NEGATIVE_INFINITY;
            Move bestMove = null;

            for (PieceMovePair pair : prioritizedMoves) {
                Move move = pair.move;
                int promote = pair.promotion;


                Move movee = new Move(latestMove.start,latestMove.target,latestMove.piece,latestMove.promotion);
                Integer[] nextGrid = game.copyGridWithMove(grid,move,maximizingPlayer,latestMove, castling);
                MoveScore evalResult = minimax(nextGrid, alpha, beta, maxDepth - 1, false, Arrays.copyOf(castling,castling.length),movee);

                if (evalResult.score > maxEval) {
                    maxEval = evalResult.score;
                    bestMove = move;
                    bestMove.setPromote(promote);
                }

                alpha = Math.max(alpha, evalResult.score);
                if (beta <= alpha) break;
            }
            return new MoveScore(maxEval, bestMove);
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            Move bestMove = null;

            for (PieceMovePair pair : prioritizedMoves) {
                Move move = pair.move;
                int promote = pair.promotion;

                Move movee = new Move(latestMove.start,latestMove.target,latestMove.piece,latestMove.promotion);
                Integer[] nextGrid = game.copyGridWithMove(grid,move,maximizingPlayer,latestMove, castling);
                MoveScore evalResult = minimax(nextGrid, alpha, beta, maxDepth - 1, true, Arrays.copyOf(castling,castling.length),movee);


                if (evalResult.score < minEval) {
                    minEval = evalResult.score;
                    bestMove = move;
                    bestMove.setPromote(promote);
                }

                beta = Math.min(beta, evalResult.score);
                if (beta <= alpha) break;
            }
            return new MoveScore(minEval, bestMove);
        }
    }



 */


    public MoveScore minimax(Integer[] currentGrid, double alpha, double beta, int maxDepth, boolean maximizingPlayer, boolean[] castling, Move latestMove) {

        // Base case: if we reach the maximum depth, evaluate the board
        if (maxDepth == 0) {
            test++;
            return new MoveScore(evaluations.evaluateBoard(currentGrid, maximizingPlayer, movesPlayed), latestMove);
        }

        // Generate all possible moves for the current player
        var possibleMoves = game.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling);
        List<PieceMovePair> prioritizedMoves = new ArrayList<>();
        if (possibleMoves.size() == 0) {
            // If no moves are possible, return a very low or high score depending on the player
            double score;
            if(game.isChecked(currentGrid,maximizingPlayer,latestMove)){
            score = maximizingPlayer ? -10000 - maxDepth : 10000 - maxDepth;
            } else{
                score = 0;
            }
            return new MoveScore(score, null);
        }
        for (Move move : possibleMoves) {

            sorting++;
            Move mv = new Move(move.start,move.target,move.piece,move.promotion);
            Integer[] nextGrid = game.copyGridWithMove(Arrays.copyOf(currentGrid,currentGrid.length),mv,maximizingPlayer,latestMove,Arrays.copyOf(castling,castling.length));

            double moveScore = evaluations.evaluateBoard(nextGrid, maximizingPlayer,movesPlayed);
            prioritizedMoves.add(new PieceMovePair(move.start,move.target,move,moveScore,move.promotion));

        }
        if (maximizingPlayer) {
            prioritizedMoves.sort((a, b) -> Double.compare(b.score, a.score)); // Higher scores first
        } else {
            prioritizedMoves.sort((a, b) -> Double.compare(a.score, b.score)); // Lower scores first
        }

        // Variable to track the best move and its score
        Move bestMove = null;

        if (maximizingPlayer) {
            double maxEval = Double.NEGATIVE_INFINITY;

            for (PieceMovePair move : prioritizedMoves) {
                // Apply the move to create the next grid state
                Move move1 = move.move;
                Integer[] nextGrid = game.copyGridWithMove(currentGrid, move1, maximizingPlayer, latestMove, castling);

                // Recursively call minimax for the opponent
                MoveScore evalResult = minimax(nextGrid, alpha, beta, maxDepth - 1, false, Arrays.copyOf(castling, castling.length), move1);

                // Update the best move if this move has a higher score
                if (evalResult.score > maxEval) {
                    maxEval = evalResult.score;
                    bestMove = move1;
                }

                // Update alpha and prune if possible
                alpha = Math.max(alpha, evalResult.score);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }

            return new MoveScore(maxEval, bestMove);
        } else {
            double minEval = Double.POSITIVE_INFINITY;

            for (PieceMovePair move : prioritizedMoves) {
                // Apply the move to create the next grid state
                Move move1 = move.move;
                Integer[] nextGrid = game.copyGridWithMove(currentGrid, move1, maximizingPlayer, latestMove, castling);

                // Recursively call minimax for the opponent
                MoveScore evalResult = minimax(nextGrid, alpha, beta, maxDepth - 1, true, Arrays.copyOf(castling, castling.length), move1);

                // Update the best move if this move has a lower score
                if (evalResult.score < minEval) {
                    minEval = evalResult.score;
                    bestMove = move1;
                }

                // Update beta and prune if possible
                beta = Math.min(beta, evalResult.score);
                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }

            return new MoveScore(minEval, bestMove);
        }
    }

    /*
public MoveScore minimax(Integer[] currentGrid, int maxDepth, boolean maximizingPlayer, boolean[] castling, Move latestMove) {

    // Base case: if we reach the maximum depth, evaluate the board
    if (maxDepth == 0) {
        test++;
        return new MoveScore(evaluations.evaluateBoard(currentGrid, maximizingPlayer, movesPlayed), latestMove);

    }

    // Generate all possible moves for the current player
    var possibleMoves = game.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling);
    if (possibleMoves.size() == 0) {
        // If no moves are possible, return a very low or high score depending on the player
        double score = maximizingPlayer ? -10000 - maxDepth : 10000 - maxDepth;
        return new MoveScore(score, null);
    }

    // Variable to track the best move and its score
    Move bestMove = null;

    if (maximizingPlayer) {
        double maxEval = Double.NEGATIVE_INFINITY;

        for (Move move : possibleMoves) {
            // Apply the move to create the next grid state
            Integer[] nextGrid = game.copyGridWithMove(currentGrid, move, maximizingPlayer, latestMove, castling);

            // Recursively call minimax for the opponent
            MoveScore evalResult = minimax(nextGrid, maxDepth - 1, false, Arrays.copyOf(castling, castling.length), move);

            // Update the best move if this move has a higher score
            if (evalResult.score > maxEval) {
                maxEval = evalResult.score;
                bestMove = move;
            }
        }

        return new MoveScore(maxEval, bestMove);
    } else {
        double minEval = Double.POSITIVE_INFINITY;

        for (Move move : possibleMoves) {
            // Apply the move to create the next grid state
            Integer[] nextGrid = game.copyGridWithMove(currentGrid, move, maximizingPlayer, latestMove, castling);

            // Recursively call minimax for the opponent
            MoveScore evalResult = minimax(nextGrid, maxDepth - 1, true, Arrays.copyOf(castling, castling.length), move);

            // Update the best move if this move has a lower score
            if (evalResult.score < minEval) {
                minEval = evalResult.score;
                bestMove = move;
            }
        }

        return new MoveScore(minEval, bestMove);
    }
}

     */
    /*
public MoveScore minimax(Integer[] currentGrid, int maxDepth, boolean maximizingPlayer, boolean[] castling, Move latestMove) {

    if (maxDepth == 0) {
        test++;
        return new MoveScore(evaluations.evaluateBoard(currentGrid, maximizingPlayer, movesPlayed), latestMove);
    }

    List<PieceMovePair> prioritizedMoves = new ArrayList<>();
    var possibleMoves = game.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling);

    // No possible moves: Return extreme value based on the player.
    /*
    if (possibleMoves.size() == 0) {
        double score = maximizingPlayer ? -10000 - maxDepth : 10000 - maxDepth;
        return new MoveScore(score, null);
    }



    // Evaluate moves for ordering (optional optimization)

    // Sort moves for optional optimization

    if (maximizingPlayer) {
        prioritizedMoves.sort((a, b) -> Double.compare(b.score, a.score)); // Higher scores first
    } else {
        prioritizedMoves.sort((a, b) -> Double.compare(a.score, b.score)); // Lower scores first
    }

    // Core minimax logic
    if (maximizingPlayer) {
        double maxEval = Double.NEGATIVE_INFINITY;
        Move bestMove = null;

        for (PieceMovePair pair : prioritizedMoves) {
            Move move = pair.move;

            Integer[] nextGrid = game.copyGridWithMove(grid, move, maximizingPlayer, latestMove, castling);
            MoveScore evalResult = minimax(nextGrid, maxDepth - 1, false, Arrays.copyOf(castling, castling.length), move);

            if (evalResult.score > maxEval) {
                maxEval = evalResult.score;
                bestMove = move;
                bestMove.setPromote(pair.promotion);
            }
        }
        return new MoveScore(maxEval, bestMove);
    } else {
        double minEval = Double.POSITIVE_INFINITY;
        Move bestMove = null;

        for (PieceMovePair pair : prioritizedMoves) {
            Move move = pair.move;

            Move mv = new Move(latestMove.start,latestMove.target,latestMove.piece,0);
            Integer[] nextGrid = game.copyGridWithMove(grid, move, maximizingPlayer, mv, castling);
            MoveScore evalResult = minimax(nextGrid, maxDepth - 1, true, Arrays.copyOf(castling, castling.length), move);

            if (evalResult.score < minEval) {
                minEval = evalResult.score;
                bestMove = move;
                bestMove.setPromote(pair.promotion);
            }
        }
        return new MoveScore(minEval, bestMove);
    }
}
*/


    public class MoveScore {
        public double score;
        public Move move;
        public int promote;

        public MoveScore(double score, Move move) {
            this.score = score;
            this.move = move;

        }

    }





}





