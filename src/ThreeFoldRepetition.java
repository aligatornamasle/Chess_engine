import java.util.ArrayList;
import java.util.Arrays;

public class ThreeFoldRepetition {


    public static ArrayList<Move> getMovesCausingThreefoldRepetition(Search search, ArrayList<String> positionHistory, boolean maximizingPlayer, Integer[] board, Move previosMove, boolean[] castling, boolean turn) {
        ArrayList<Move> movesToReturn = new ArrayList<>();
        var moves = search.getPossibleMoves(maximizingPlayer, board, previosMove, castling);
        for(Move move: moves) {
            Integer[] boardWithMove = search.copyGridWithMove(Arrays.copyOf(board,board.length),move,maximizingPlayer,previosMove,Arrays.copyOf(castling,castling.length));
            String position = FenBoardUtilities.getBoardPosition(boardWithMove,turn,castling);
            int counter = 0;
            for (String s : positionHistory) {
                if (s.equals(position)) {
                    counter++;
                }
            }
            if(counter >= 2){
                movesToReturn.add(move);
            }
        } int index = 1;
        if(!positionHistory.isEmpty()) {

            while ((positionHistory.size()-index) > 0) {
                var ah = positionHistory.get(positionHistory.size() - index);

                int counterEnemyMakeDraw = 0;
                for (String s : positionHistory) {
                    if (s.equals(ah)) {
                        counterEnemyMakeDraw++;
                    }
                }
                if (counterEnemyMakeDraw >= 2) {
                    for (Move move1 : moves) {
                        Integer[] boardWithMove = search.copyGridWithMove(Arrays.copyOf(board, board.length), move1, maximizingPlayer, previosMove, Arrays.copyOf(castling, castling.length));
                        String position = FenBoardUtilities.getBoardPosition(boardWithMove, turn, castling);
                        if (position.equals(positionHistory.get(positionHistory.size() - 4))) {
                            movesToReturn.add(move1);
                        }
                    }
                }
                index = index + 2;
            }

        }
        return movesToReturn;

    }

}
