import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;


public class MinimaxWorker extends SwingWorker<Move, Void> {
    private final Search search;
    private final MiniMaxAlgorithm minimax;
    private final ChessBoard chessBoard;
    private final boolean[] isCastling_;
    private final Move previousMove_;
    private final ArrayList<Move> repeatingMoves_;
    private final int Depth_;
    private final int Time_;
    private final boolean maximalizing_;

    public MinimaxWorker(Search search, MiniMaxAlgorithm minimax, ChessBoard chessBoard,
                          boolean[] isCastling, Move previousMove,
                         ArrayList<Move> repeatingMoves,
                         int depth_, int time_, boolean maximalizing_) {
        this.search = search;
        this.minimax = minimax;
        this.chessBoard = chessBoard;
        this.isCastling_ = isCastling;
        this.previousMove_ = previousMove;
        this.repeatingMoves_ = repeatingMoves;
        Depth_ = depth_;
        Time_ = time_;
        this.maximalizing_ = maximalizing_;
    }

    @Override
    protected Move doInBackground() {
        Integer[] board = search.getBoard();
        long startTime = System.nanoTime();

        // Pass interruption check to minimax
        minimax.createTree(
                board, Depth_, Time_, maximalizing_,
                Arrays.copyOf(isCastling_, isCastling_.length), previousMove_,
                search, () -> Thread.currentThread().isInterrupted(),
                chessBoard.turn,chessBoard.positionHistory
        );

        if (Thread.currentThread().isInterrupted()) {
            System.out.println("MinimaxWorker interrupted");
            return null;
        }

        long endTime = System.nanoTime();
        long elapsedTimeInNano_ = endTime - startTime;
        double elapsedTimeInMilli = elapsedTimeInNano_ / 1_000_000.0;
        double elapsedTimeInSeconds = elapsedTimeInMilli / 1000.0;
        System.out.println("Algorithm time: " + elapsedTimeInSeconds + " s");

        return  new Move(0,0,0,0,0);
    }

    @Override
    protected void done() {
        try {

            if (isCancelled()) {
                System.out.println("Worker cancelled");
                return;
            }

            //Move move = get();
            if (minimax.bestMoveSoFar != null) {
                Move move = minimax.bestMoveSoFar.move;
                repeatingMoves_.add(move);
                if (move.promotion == null) move.promotion = 0;

                search.makeMove(maximalizing_, move);
                search.previosMove = move;
                chessBoard.halfMoveClock = Draw.fiftyMoveRule(move, chessBoard.halfMoveClock);
                chessBoard.positionHistory.add(FenBoardUtilities.getBoardPosition(search.getBoard(), chessBoard.turn, search.isCastling));
                System.out.println("{" + move.start + "," + move.target + "}" + move.piece + "," + move.captureScore_);
                System.out.println("_____________________________________");
                chessBoard.turn = !chessBoard.turn;
                chessBoard.repaint();

                if(Math.abs(move.captureScore_) > 0 ){
                        SoundPlayer.sound("src/sounds/capture.wav");
                }else{
                    SoundPlayer.sound("src/sounds/move.wav");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}