import javax.swing.*;

public class FenBoardUtilities {




    public static int convertEnPassantToIndex(String enPassantTarget) {
        if (enPassantTarget.equals("-")) {
            return -1; // No en passant target
        }

        // Extract file (column) and rank (row) from the target (e.g., "f3")
        char fileChar = enPassantTarget.charAt(0); // 'a' to 'h'
        int rank = Character.getNumericValue(enPassantTarget.charAt(1)); // 3 or 6

        // Calculate the index
        int file = fileChar - 'a'; // Convert 'a' to 0, 'b' to 1, ..., 'h' to 7

        return (rank - 1) * 8 + file;
    }


    public static boolean[] getCastlingBooleanArray(String castlingRights) {
        // Initialize the boolean array with all pieces moved (true)
        boolean[] castlingBoolean = new boolean[6];
        for (int i = 0; i < 6; i++) {
            castlingBoolean[i] = true;
        }
        if(castlingRights.equals("-")) {
            for (int i = 0; i < 6; i++) {
                castlingBoolean[i] = false;
            }
        }

        // White castling rights
        if (castlingRights.contains("K")) {
            castlingBoolean[2] = false; // White's right rook
            castlingBoolean[1] = false; // White's king
        }
        if (castlingRights.contains("Q")) {
            castlingBoolean[0] = false; // White's left rook
            castlingBoolean[1] = false; // White's king
        }

        // Black castling rights
        if (castlingRights.contains("k")) {
            castlingBoolean[5] = false; // Black's right rook
            castlingBoolean[3] = false; // Black's king



        }
        if (castlingRights.contains("q")) {
            castlingBoolean[3] = false; // Black's left rook
            castlingBoolean[4] = false; // Black's king
        }

        return castlingBoolean;
    }

    public static boolean setupBoardFromFEN(String fen, Integer[] board) {
        try {
            String[] parts = fen.split(" ");
            String piecePlacement = parts[0];
            String[] ranks = piecePlacement.split("/");

            // Check if there are exactly 8 ranks
            if (ranks.length != 8) {
                throw new IllegalArgumentException("FEN must have exactly 8 ranks.");
            }

            for (int row = 0; row < 8; row++) {
                String rank = ranks[7 - row];
                int column = 0;

                // Validate the rank length
                int rankLength = 0;
                for (char symbol : rank.toCharArray()) {
                    if (Character.isDigit(symbol)) {
                        int emptySquares = Character.getNumericValue(symbol);
                        rankLength += emptySquares;
                    } else {
                        rankLength++;
                    }
                }

                // Check if the rank length is exactly 8
                if (rankLength != 8) {
                    throw new IllegalArgumentException("Rank " + (row + 1) + " must have exactly 8 squares.");
                }

                // Process the rank
                for (char symbol : rank.toCharArray()) {
                    int index = column + row * 8;
                    if (Character.isDigit(symbol)) {
                        int emptySquares = Character.getNumericValue(symbol);
                        for (int i = 0; i < emptySquares; i++) {
                            board[index + i] = PiecesInt.None1;
                        }
                        column += emptySquares;
                    } else {
                        switch (symbol) {
                            case 'r': board[index] = PiecesInt.Rook2; break;
                            case 'n': board[index] = PiecesInt.Knight2; break;
                            case 'b': board[index] = PiecesInt.Bishop2; break;
                            case 'q': board[index] = PiecesInt.Queen2; break;
                            case 'k': board[index] = PiecesInt.King2; break;
                            case 'p': board[index] = PiecesInt.Pawn2; break;
                            case 'R': board[index] = PiecesInt.Rook1; break;
                            case 'N': board[index] = PiecesInt.Knight1; break;
                            case 'B': board[index] = PiecesInt.Bishop1; break;
                            case 'Q': board[index] = PiecesInt.Queen1; break;
                            case 'K': board[index] = PiecesInt.King1; break;
                            case 'P': board[index] = PiecesInt.Pawn1; break;
                            default:
                                throw new IllegalArgumentException("Invalid piece symbol: " + symbol);
                        }
                        column++;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            // Display an error dialog with a message
            JOptionPane.showMessageDialog(
                    null, // Parent component (null means default)
                    "Invalid FEN string: " + e.getMessage(), // Message
                    "FEN Error", // Dialog title
                    JOptionPane.ERROR_MESSAGE // Message type (error icon)
            );
            return false;
        }
    }

    public static String getBoardPosition(Integer[] board,boolean turn, boolean[] isCastling) {
        // Convert the board (Integer[64]) to a string
        StringBuilder boardString = new StringBuilder();
        for (int piece : board) {
            boardString.append(piece).append(",");
        }

        // Append the turn (w or b)
        boardString.append(turn ? "w" : "b").append(",");

        // Append the castling boolean array itself
        for (boolean b : isCastling) {
            boardString.append(b ? "1" : "0").append(",");
        }

        // Remove the trailing comma (if any)
        if (boardString.charAt(boardString.length() - 1) == ',') {
            boardString.deleteCharAt(boardString.length() - 1);
        }

        return boardString.toString();
    }




}
