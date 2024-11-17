import javax.swing.*;
import java.util.Arrays;

class MinimaxWorker extends SwingWorker<Move, Void> {
    private Run gameGrid;
    private MiniMaxAlgorithm minimax;
    private Canvass.ChessBoard chessBoard;
    private int movesPlayed;
    private boolean[] isCastling;
    private Move previousMove;

    public MinimaxWorker(Run gameGrid, MiniMaxAlgorithm minimax, Canvass.ChessBoard chessBoard,int movesPlayed,boolean[] isCastlinng,Move previousMove) {
        this.gameGrid = gameGrid;
        this.minimax = minimax;
        this.chessBoard = chessBoard;
        this.movesPlayed = movesPlayed;
        this.isCastling = isCastlinng;
        this.previousMove = previousMove;

    }

    @Override
    protected Move doInBackground() throws Exception {
        // Perform Minimax calculation
        int a = 0;
        Integer[] ggrid = gameGrid.getGrid();
        for(int i = 0;i<64;i++){
         if(ggrid[i]!=0){
             a++;
         }
        }


        Move mv;

            if(a<5){
                minimax.createTree(ggrid, 7, false,movesPlayed, Arrays.copyOf(isCastling,isCastling.length),previousMove,gameGrid);

            }else

            if(a< 12){
                minimax.createTree(ggrid, 6, false,movesPlayed, Arrays.copyOf(isCastling,isCastling.length),previousMove,gameGrid);


            } else{
                long startTime = System.nanoTime();
                System.out.println("depth: "+4);
                minimax.createTree(ggrid, 5, false,movesPlayed, Arrays.copyOf(isCastling,isCastling.length),previousMove,gameGrid);
                long endTime = System.nanoTime();

                // Calculate the elapsed time in milliseconds and seconds
                long elapsedTimeInNano = endTime - startTime;
                double elapsedTimeInMilli = elapsedTimeInNano / 1_000_000.0; // Convert to milliseconds
                double elapsedTimeInSeconds = elapsedTimeInMilli / 1000.0;    // Convert to seconds
                System.out.println("CEly algoritmus --Elapsed time in seconds: " + elapsedTimeInSeconds + " s");

            }
            System.out.println(minimax.sorting);
            minimax.sorting = 0;
            return minimax.getMoveScore().move;


    }


    @Override
    protected void done() {
        try {
            Move move = get();
            if (move != null) {

                gameGrid.selectPiece(move.start,false);
                if(move.promotion ==null){
                    move.promotion=0;
                }
                gameGrid.makeMove(move.target,false,move);
                chessBoard.turn = true;
                System.out.println("---------------------");
                System.out.println(minimax.test);
                System.out.println("---------------------");
                chessBoard.repaint();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
