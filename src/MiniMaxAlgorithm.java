
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public class MiniMaxAlgorithm {
    public Integer[] board;
    public Search search;
    public int endNodes;
    public int pruned = 0;
    public int transpositionCount = 0;
    public int maxTimeMs = 0;
    public int leafNodes = 0;
    public int currentDepth;
    public MoveScore bestMoveSoFar;
    long startTime;
    public ChessBoardEvaluator chessBoardEvaluator;
    BooleanSupplier isInterrupted;
    ArrayList<Move> threeFoldRepMoves;
    private final int quiescenceDepth = 5;
    public MiniMaxAlgorithm() {
        search = new Search();
        this.chessBoardEvaluator = new ChessBoardEvaluator(search);
    }

    public void createTree(Integer[] board, int maxDepth, int maxTimeMs,
                           boolean maximizingPlayer, boolean[] isCastling,
                           Move previousMove, Search game,
                           BooleanSupplier isInterrupted, boolean turn,ArrayList<String> positionHistory) {
        startTime = System.currentTimeMillis();
        endNodes = 0;
        this.maxTimeMs = maxTimeMs;
        this.board = board;
        this.search = game;
        this.isInterrupted = isInterrupted;
        transpositionCount = 0;
        threeFoldRepMoves = ThreeFoldRepetition.getMovesCausingThreefoldRepetition(search,positionHistory,maximizingPlayer, board,previousMove,isCastling,turn);


        for (int currentDepth = 1; currentDepth <= maxDepth; currentDepth++) {

            if (System.currentTimeMillis() - startTime >= maxTimeMs) {
                System.out.println("Time limit reached. Exiting at depth " + (currentDepth - 1));
                break;
            }
            this.currentDepth = currentDepth;

            long startTime = System.nanoTime();
            MoveScore currentMove = minimax(board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, currentDepth, maximizingPlayer, isCastling, previousMove);
            //MoveScore currentMove = cleanMinimax(board, currentDepth, maximizingPlayer, isCastling, previousMove);
            //MoveScore currentMove = cleanMinimaxAB(board,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,currentDepth, maximizingPlayer, isCastling, previousMove);
            //MoveScore currentMove = cleanMinimaxABM(board,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,currentDepth, maximizingPlayer, isCastling, previousMove);
            long endTime = System.nanoTime();
            long elapsedTimeInNano_ = endTime - startTime;
            double elapsedTimeInMilli = elapsedTimeInNano_ / 1_000_000.0;
            double elapsedTimeInSeconds = elapsedTimeInMilli / 1000.0;
            System.out.println("hlbka "+ currentDepth+":" + elapsedTimeInSeconds + " s" + ",pruned: "+pruned+ ",leaftnodes: "+leafNodes);
            pruned = 0;
            leafNodes = 0;
            if(currentMove == null) {
                System.out.println("Search aborted due to time limit at depth " + currentDepth);
                break;
            }
            bestMoveSoFar = currentMove;
            if (isInterrupted.getAsBoolean()) {
                break;
            }
            System.out.println("Depth " + currentDepth + " completed. Best score: " + bestMoveSoFar.score+",move: "+bestMoveSoFar.move.start+","+bestMoveSoFar.move.target+"(piece)"+bestMoveSoFar.move.piece);
        }
    }

    public MoveScore minimax(Integer[] currentGrid, double alpha, double beta, int maxDepth, boolean maximizingPlayer, boolean[] castling, Move latestMove) {
        if (isInterrupted.getAsBoolean()||System.currentTimeMillis() - startTime >= maxTimeMs) {
            return null;
        }
        if(latestMove !=null &&latestMove.repetitive){
            return new MoveScore(0, latestMove);
        }
        // Base case: if we reach the maximum depth, evaluate the board
        if (maxDepth == 0) {
           // endNodes++;
            //leafNodes++;
            return quiescence(
                    currentGrid,
                    alpha,
                    beta,
                    maximizingPlayer,
                    castling,
                    latestMove,
                    5  // Initial quiescence depth
            );
        }


        // Generate all possible moves for the current player
        var possibleMoves = search.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling);
        List<MoveScore> prioritizedMoves = new ArrayList<>();

        // If no moves are possible, return a very low or high score depending on the player
        if (possibleMoves.isEmpty()) {
            double score;
            if (search.isChecked(currentGrid, maximizingPlayer)) {
                score = maximizingPlayer ? -100000 - maxDepth - quiescenceDepth : 100000 + maxDepth + quiescenceDepth;
            } else {
                score = 0;
            }

            // transpositionTable.put(zobristHash, moveScore); // Store the result in the transposition table
            return new MoveScore(score, null);
        }

        // Evaluate and prioritize moves using the transposition table
        for (Move move : possibleMoves) {
            //sorting++;
            //checks if the move will end up in three-fold repetition draw
            if (maxDepth == currentDepth && !threeFoldRepMoves.isEmpty()) {
                for(Move moveThreeFoldRepMove : threeFoldRepMoves) {
                    if(moveThreeFoldRepMove.target == move.target && moveThreeFoldRepMove.start == move.start){
                        move.repetitive = true;
                    }
                }
            }

           // Move mv = new Move(move.start, move.target, move.piece, move.promotion);
            Move mv = new Move(move.start, move.target, move.piece, move.promotion,move.captureScore_);
            Integer[] nextGrid = search.copyGridWithMove(Arrays.copyOf(currentGrid, currentGrid.length), mv, maximizingPlayer, latestMove, Arrays.copyOf(castling, castling.length));

                // Evaluate the move if it's not in the transposition table
                double moveScore = chessBoardEvaluator.evaluateBoard(nextGrid,null);
                prioritizedMoves.add(new MoveScore(moveScore,move));

        }

        // Sort moves based on their scores
        if (maximizingPlayer) {
            prioritizedMoves.sort((a, b) -> Double.compare(b.score, a.score)); // Higher scores first
        } else {
            prioritizedMoves.sort((a, b) -> Double.compare(a.score, b.score)); // Lower scores first
        }

        // Variable to track the best move and its score
        Move bestMove = null;
        double bestScore = maximizingPlayer ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        // Iterate through prioritized moves
        for (MoveScore move : prioritizedMoves) {

           // Integer[] nextGrid = game.copyGridWithMove(currentGrid, move1, maximizingPlayer, latestMove, castling);
            Integer[] nextGrid = search.copyGridWithMove(Arrays.copyOf(currentGrid, currentGrid.length), move.move, maximizingPlayer, latestMove, Arrays.copyOf(castling, castling.length));

            // Recursive call to minimax
            MoveScore evalResult = minimax(nextGrid, alpha, beta, maxDepth - 1, !maximizingPlayer, Arrays.copyOf(castling, castling.length), move.move);

            // If the search was aborted due to time, return null
            if (evalResult == null) {
                return null;
            }

            // Update best score and move
            if (maximizingPlayer) {
                if (evalResult.score > bestScore) {
                    bestScore = evalResult.score;
                    bestMove = move.move;
                }
                alpha = Math.max(alpha, evalResult.score);
            } else {
                if (evalResult.score < bestScore) {
                    bestScore = evalResult.score;
                    bestMove = move.move;
                }
                beta = Math.min(beta, evalResult.score);
            }

            // Alpha-beta pruning
            if (beta <= alpha) {
              //  pruned++;
                break;
            }
        }
        return new MoveScore(bestScore, bestMove);
    }


    private MoveScore quiescence(Integer[] currentGrid, double alpha, double beta, boolean maximizingPlayer,
                                 boolean[] castling, Move latestMove, int depth) {
        if (depth <= 0 || isInterrupted.getAsBoolean() || System.currentTimeMillis() - startTime >= maxTimeMs) {
            double score = chessBoardEvaluator.evaluateBoard(currentGrid, latestMove);
            return new MoveScore(score, latestMove);
        }

        boolean inCheck = search.isChecked(currentGrid, maximizingPlayer);
        double standPat = chessBoardEvaluator.evaluateBoard(currentGrid, latestMove);

        if (!inCheck) {
            if (maximizingPlayer) {
                if (standPat >= beta) return new MoveScore(beta, latestMove);
                alpha = Math.max(alpha, standPat);
            } else {
                if (standPat <= alpha) return new MoveScore(alpha, latestMove);
                beta = Math.min(beta, standPat);
            }
        }

        List<Move> movesToConsider;
        if (inCheck) {
            movesToConsider = search.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling);
            if (movesToConsider.isEmpty()) {
                // Confirm checkmate not stalemate
                if (search.isChecked(currentGrid, maximizingPlayer)) {
                    double score = maximizingPlayer ? -100000 - depth : 100000 + depth;
                    return new MoveScore(score, latestMove);
                } else {
                    return new MoveScore(0, latestMove); // Stalemate
                }
            }
        } else {
            movesToConsider = search.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling)
                    .stream()
                    .filter(move -> move.captureScore_ != 0 || move.promotion != null)
                    .sorted((a, b) -> {
                        // MVV: Higher victim value first
                        int victimA = ChessHeuristics.pieceValues.getOrDefault(Math.abs(a.captureScore_), 0);
                        int victimB = ChessHeuristics.pieceValues.getOrDefault(Math.abs(b.captureScore_), 0);
                        int cmp = Integer.compare(victimB, victimA);
                        if (cmp != 0) return cmp;
                        // LVA: Lower aggressor value next
                        int aggressorA = ChessHeuristics.pieceValues.getOrDefault(Math.abs(a.piece), 0);
                        int aggressorB = ChessHeuristics.pieceValues.getOrDefault(Math.abs(b.piece), 0);
                        return Integer.compare(aggressorA, aggressorB);
                    })
                    .collect(Collectors.toList());
        }
        MoveScore mv = new MoveScore(standPat, null);
        for (Move move : movesToConsider) {
            // Delta calculation with promotion support
            int capturedValue = ChessHeuristics.pieceValues.getOrDefault(Math.abs(move.captureScore_), 0);
            int promotionGain = 0;
            if (move.promotion != null) {
                promotionGain = ChessHeuristics.pieceValues.getOrDefault(Math.abs(move.promotion), 0)
                        - ChessHeuristics.pieceValues.getOrDefault(Math.abs(move.piece), 0);
            }

            double delta = maximizingPlayer ?
                    (standPat + capturedValue + promotionGain) :
                    (standPat - capturedValue - promotionGain);

            // Delta pruning (only when not in check)
            if (!inCheck) {
                if ((maximizingPlayer && delta < alpha) || (!maximizingPlayer && delta > beta)) {
                    continue;
                }
            }

            // Make the move on a copied board
            Integer[] newGrid = search.copyGridWithMove(
                    Arrays.copyOf(currentGrid, currentGrid.length),
                    move,
                    maximizingPlayer,
                    latestMove,
                    Arrays.copyOf(castling, castling.length)
            );

            // Recursive quiescence call
            MoveScore result = quiescence(
                    newGrid,
                    alpha,
                    beta,
                    !maximizingPlayer,
                    castling,
                    move,
                    depth - 1
            );

            if (result == null) return null;
            mv = result;
            // Update alpha/beta
            double score = result.score;
            if (maximizingPlayer) {
                if (score >= beta) {
                    return new MoveScore(beta, move);
                }
                alpha = Math.max(alpha, score);
            } else {
                if (score <= alpha) {
                    return new MoveScore(alpha, move);
                }
                beta = Math.min(beta, score);
            }

            // Alpha-beta pruning
            if (beta <= alpha) {
                //pruned++;
                break;
            }
        }

        return new MoveScore(mv.score, latestMove);
    }



    public MoveScore cleanMinimax(Integer[] currentGrid, int maxDepth, boolean maximizingPlayer, boolean[] castling, Move latestMove) {
        // Base case: if we reach the maximum depth, evaluate the board
        if (isInterrupted.getAsBoolean()||System.currentTimeMillis() - startTime >= maxTimeMs) {
            return null;
        }
        if (maxDepth == 0) {
            double score = chessBoardEvaluator.evaluateBoard(currentGrid, null);
            return new MoveScore(score, null);
        }

        // Generate all possible moves for the current player
        var possibleMoves = search.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling);

        // If no moves are possible, return a very low or high score depending on the player
        if (possibleMoves.isEmpty()) {
            double score;
            if (search.isChecked(currentGrid, maximizingPlayer)) {
                score = maximizingPlayer ? -100000 : 100000;
            } else {
                score = 0;
            }
            return new MoveScore(score, null);
        }

        // Variable to track the best move and its score
        Move bestMove = null;
        double bestScore = maximizingPlayer ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        // Iterate through all possible moves
        for (Move move : possibleMoves) {
            // Create a copy of the grid and apply the move
            Integer[] nextGrid = search.copyGridWithMove(Arrays.copyOf(currentGrid, currentGrid.length), move, maximizingPlayer, latestMove, Arrays.copyOf(castling, castling.length));

            // Recursive call to minimax
            MoveScore evalResult = cleanMinimax(nextGrid, maxDepth - 1, !maximizingPlayer, Arrays.copyOf(castling, castling.length), move);
            if (evalResult == null) {
                return null;
            }
            // Update best score and move
            if (maximizingPlayer) {
                if (evalResult.score > bestScore) {
                    bestScore = evalResult.score;
                    bestMove = move;
                }
            } else {
                if (evalResult.score < bestScore) {
                    bestScore = evalResult.score;
                    bestMove = move;
                }
            }
        }

        return new MoveScore(bestScore, bestMove);
    }


    public MoveScore cleanMinimaxAB(Integer[] currentGrid, double alpha, double beta, int maxDepth, boolean maximizingPlayer, boolean[] castling, Move latestMove) {
        if (isInterrupted.getAsBoolean()||System.currentTimeMillis() - startTime >= maxTimeMs) {
            return null;
        }
        // Base case: if we reach the maximum depth, evaluate the board
        if (maxDepth == 0) {
            //leafNodes++;
            double score = chessBoardEvaluator.evaluateBoard(currentGrid, null);
            return new MoveScore(score, null);
        }

        // Generate all possible moves for the current player
        var possibleMoves = search.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling);

        // If no moves are possible, return a very low or high score depending on the player
        if (possibleMoves.isEmpty()) {
            double score;
            if (search.isChecked(currentGrid, maximizingPlayer)) {
                score = maximizingPlayer ? -100000 : 100000; // Checkmate score
            } else {
                score = 0; // Stalemate score
            }
            return new MoveScore(score, null);
        }

        // Variable to track the best move and its score
        Move bestMove = null;
        double bestScore = maximizingPlayer ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        // Iterate through all possible moves
        for (Move move : possibleMoves) {
            // Create a copy of the grid and apply the move
            Integer[] nextGrid = search.copyGridWithMove(Arrays.copyOf(currentGrid, currentGrid.length), move, maximizingPlayer, latestMove, Arrays.copyOf(castling, castling.length));

            // Recursive call to minimax
            MoveScore evalResult = cleanMinimaxAB(nextGrid, alpha, beta, maxDepth - 1, !maximizingPlayer, Arrays.copyOf(castling, castling.length), move);

            // If the search was aborted due to time, return null
            if (evalResult == null) {
                return null;
            }

            // Update best score and move
            if (maximizingPlayer) {
                if (evalResult.score > bestScore) {
                    bestScore = evalResult.score;
                    bestMove = move;
                }
                alpha = Math.max(alpha, evalResult.score); // Update alpha
            } else {
                if (evalResult.score < bestScore) {
                    bestScore = evalResult.score;
                    bestMove = move;
                }
                beta = Math.min(beta, evalResult.score); // Update beta
            }

            // Alpha-beta pruning
            if (beta <= alpha) {
              //  pruned++;
                break; // Prune the remaining branches
            }
        }

        return new MoveScore(bestScore, bestMove);
    }



    public MoveScore cleanMinimaxABM(Integer[] currentGrid, double alpha, double beta, int maxDepth, boolean maximizingPlayer, boolean[] castling, Move latestMove) {
        // Base case: if we reach the maximum depth, evaluate the board
        if (isInterrupted.getAsBoolean()||System.currentTimeMillis() - startTime >= maxTimeMs) {
            return null;
        }
        if (maxDepth == 0) {
           // leafNodes++;
            double score = chessBoardEvaluator.evaluateBoard(currentGrid, null);
            return new MoveScore(score, null);
        }

        // Generate all possible moves for the current player
        var possibleMoves = search.getPossibleMoves(maximizingPlayer, currentGrid, latestMove, castling);

        // If no moves are possible, return a very low or high score depending on the player
        if (possibleMoves.isEmpty()) {
            double score;
            if (search.isChecked(currentGrid, maximizingPlayer)) {
                score = maximizingPlayer ? -100000 : 100000; // Checkmate score
            } else {
                score = 0; // Stalemate
            }
            return new MoveScore(score, null);
        }

        // Prioritize moves based on their immediate evaluation (move ordering)
        List<MoveScore> prioritizedMoves = new ArrayList<>();
        for (Move move : possibleMoves) {
            Integer[] nextGrid = search.copyGridWithMove(Arrays.copyOf(currentGrid, currentGrid.length), move, maximizingPlayer, latestMove, Arrays.copyOf(castling, castling.length));
            double moveScore = chessBoardEvaluator.evaluateBoard(nextGrid, null);
            prioritizedMoves.add(new MoveScore(moveScore, move));
        }

        // Sort moves based on their scores (higher scores first for maximizing player, lower for minimizing)
        if (maximizingPlayer) {
            prioritizedMoves.sort((a, b) -> Double.compare(b.score, a.score));
        } else {
            prioritizedMoves.sort((a, b) -> Double.compare(a.score, b.score));
        }

        // Variable to track the best move and its score
        Move bestMove = null;
        double bestScore = maximizingPlayer ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        // Iterate through prioritized moves
        for (MoveScore moveScore : prioritizedMoves) {
            Move move = moveScore.move;

            // Create a copy of the grid and apply the move
            Integer[] nextGrid = search.copyGridWithMove(Arrays.copyOf(currentGrid, currentGrid.length), move, maximizingPlayer, latestMove, Arrays.copyOf(castling, castling.length));

            // Recursive call to minimax
            MoveScore evalResult = cleanMinimaxABM(nextGrid, alpha, beta, maxDepth - 1, !maximizingPlayer, Arrays.copyOf(castling, castling.length), move);

            // If the search was aborted due to time, return null
            if (evalResult == null) {
                return null;
            }

            // Update best score and move
            if (maximizingPlayer) {
                if (evalResult.score > bestScore) {
                    bestScore = evalResult.score;
                    bestMove = move;
                }
                alpha = Math.max(alpha, evalResult.score); // Update alpha
            } else {
                if (evalResult.score < bestScore) {
                    bestScore = evalResult.score;
                    bestMove = move;
                }
                beta = Math.min(beta, evalResult.score); // Update beta
            }

            // Alpha-beta pruning
            if (beta <= alpha) {
               // pruned++;
                break; // Prune the remaining branches
            }
        }

        return new MoveScore(bestScore, bestMove);
    }
}
