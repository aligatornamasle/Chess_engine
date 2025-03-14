import java.util.ArrayList;

public class Draw {



    public static boolean isInsufficientMaterial(Integer[] board) {
        int whitePieces = 0;
        int blackPieces = 0;
        boolean whiteHasPawnOrMajorPiece = false;
        boolean blackHasPawnOrMajorPiece = false;

        for (int piece : board) {
            if (piece > 0) {
                whitePieces++;
                if (piece == PiecesInt.Pawn1 || piece == PiecesInt.Queen1 || piece == PiecesInt.Rook1) {
                    whiteHasPawnOrMajorPiece = true;
                }
            } else if (piece < 0) {
                blackPieces++;
                if (piece == -PiecesInt.Pawn1 || piece == -PiecesInt.Queen1 || piece == -PiecesInt.Rook1) {
                    blackHasPawnOrMajorPiece = true;
                }
            }
        }

        // Check for insufficient material conditions
        if (whitePieces == 1 && blackPieces == 1) {
            return true; // King vs King
        }
        if (whitePieces == 2 && blackPieces == 1) {
            for (int piece : board) {
                if (piece == PiecesInt.Bishop1 || piece == PiecesInt.Knight1) {
                    return true; // King and Bishop/Knight vs King
                }
            }
        }
        if (whitePieces == 1 && blackPieces == 2) {
            for (int piece : board) {
                if (piece == -PiecesInt.Bishop1 || piece == -PiecesInt.Knight1) {
                    return true; // King vs King and Bishop/Knight
                }
            }
        }
        return false;
    }

    public static boolean isDraw(boolean turn,int movesSinceLastPawnMoveOrCapture,Search search,boolean isBoardFlipped,ArrayList<String> positionHistory) {
        return isStalemate(turn,search,isBoardFlipped) || isThreefoldRepetition(positionHistory) || isFiftyMoveRule(movesSinceLastPawnMoveOrCapture) || isInsufficientMaterial(search.board);
    }

    public static boolean isFiftyMoveRule(int movesSinceLastPawnMoveOrCapture) {
        return movesSinceLastPawnMoveOrCapture >= 100;
    }

    public static boolean isStalemate(boolean turn,Search search,boolean isBoardFlipped) {


        // Find indices of both kings
        int whiteKingIndex = -1;
        int blackKingIndex = -1;
        for (int i = 0; i < 64; i++) {
            if (search.board[i] == 1) {
                whiteKingIndex = i;
            } else if (search.board[i] == -1) {
                blackKingIndex = i;
            }
        }

        // Calculate display positions considering board flip
        int whiteRow = 7 - (whiteKingIndex / 8);
        int whiteCol = whiteKingIndex % 8;
        int blackRow = 7 - (blackKingIndex / 8);
        int blackCol = blackKingIndex % 8;

        if (isBoardFlipped) {
            // Flip the coordinates for rendering
            whiteRow = 7 - whiteRow;
            whiteCol = 7 - whiteCol;
            blackRow = 7 - blackRow;
            blackCol = 7 - blackCol;
        }

        // Check if each king is in check
        boolean isWhiteChecked = search.isChecked(search.getBoard(), true);
        boolean isBlackChecked = search.isChecked(search.getBoard(), false);

        // Check if there are any legal moves left (checkmate condition)
        if (turn && isBoardFlipped) {
            search.makeMove(false, null);
        }
        if (!turn && isBoardFlipped) {
            search.makeMove(true, null);
        }
        if (turn && !isBoardFlipped) {
            search.makeMove(true, null);
        }
        if (!turn && !isBoardFlipped) {
            search.makeMove(false, null);
        }

        boolean whiteHasMoves = search.gameFlow;
        boolean blackHasMoves = search.gameFlow;

        boolean whiteCheckmate = isWhiteChecked && !whiteHasMoves;
        boolean blackCheckmate = isBlackChecked && !blackHasMoves;

        // Stalemate detection
        boolean isStalemate = false;
        if (!whiteCheckmate && !blackCheckmate) {
            if (turn && !isWhiteChecked && !whiteHasMoves) {
                isStalemate = true; // White is in stalemate
            } else if (!turn && !isBlackChecked && !blackHasMoves) {
                isStalemate = true; // Black is in stalemate
            }
        }
        return isStalemate;

    }

    public static int fiftyMoveRule(Move move,int movesSinceLastPawnMoveOrCapture) {
        int help = movesSinceLastPawnMoveOrCapture;
        if (Math.abs(move.piece) == PiecesInt.Pawn1 || move.captureScore_ != 0) {
            help = 0;
           return help;
        } else {
            help++;
           return help;
        }
    }
    public static boolean isThreefoldRepetition(ArrayList<String> positionHistory) {
        for(String state1: positionHistory){
            int count = 0;
            for (int i = 0; i < positionHistory.size(); i++) {
                if (state1.equals(positionHistory.get(i))) {
                    count++;
                    if (count >= 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
