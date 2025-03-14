
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveGenerator {
    int[] directionOffset = {8, -8, -1, 1, 7, -7, 9, -9};
    int[][] numToEdge = new int[64][];
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, // Rook
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    int[] knightOffsets = {-17, -15, -10, -6, 6, 10, 15, 17};


    public void movesForSlidingPieces() {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                //file = west
                //south = rank
                int north = 7 - rank;
                int east = 7 - file;
                int square = rank * 8 + file;

                numToEdge[square] = new int[]{
                        north,
                        rank,
                        file,
                        east,
                        Math.min(north, file),
                        Math.min(rank, east),
                        Math.min(north, east),
                        Math.min(rank, file),
                };
            }
        }
    }

    //generates all the moves
    public ArrayList<Move> generateMoves(boolean color, Integer[] board, Move move, boolean[] castle) {
        //make arraylist of moves
        ArrayList<Move> moves = new ArrayList<>();
        for (int start = 0; start < 64; start++) {
            if (board[start] == 0) {
                continue;
            }
            int piece = board[start];
            //if piece is one of sliding piece then compute all possible move for that piece
            if ((piece % 2) == 0 && PiecesInt.getColor(piece) == color && piece != PiecesInt.None1) {
                moves.addAll(generateSlidingMoves(start, piece, PiecesInt.getColor(piece), board));
            }
            //if piece is Knight compute all possible moves for knight
            if (Math.abs(piece) == PiecesInt.Knight1 && PiecesInt.getColor(piece) == color) {
                moves.addAll(generateKnightMoves(start, piece, PiecesInt.getColor(piece), board));
            }
            //if piece is Pawn compute all possible moves for knight
            if (Math.abs(piece) == PiecesInt.Pawn1 && PiecesInt.getColor(piece) == color) {
                moves.addAll(generatePawnMoves(start, piece, PiecesInt.getColor(piece), board, move, false));
            }
            //if piece is King compute all possible moves for knight
            if (Math.abs(piece) == PiecesInt.King1 && PiecesInt.getColor(piece) == color) {
                moves.addAll(generateKingMoves(start, PiecesInt.getColor(piece), board));
                var castling = castling(start, piece, color, board, castle);
                moves.addAll(castling);
            }
        }

        ArrayList<Move> legalMoves = new ArrayList<>();
        for (Move move1 : moves) {
            if (isValidMove(board, move1.target, move1.piece, move1.start, color)) {
                legalMoves.add(move1);
            }
        }
        return legalMoves;
    }

    public ArrayList<Move> movesForDisplay(ArrayList<Move> moves, Integer[] board, boolean color) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (Move move1 : moves) {
            if (isValidMove(board, move1.target, move1.piece, move1.start, color)) {
                legalMoves.add(move1);
            }
        }
        return legalMoves;
    }

    public ArrayList<Move> generateSlidingMoves(int start, int piece, boolean color, Integer[] board) {
        //if the piece is rook then it only need to know 4 direction and not all 8
        //same for bishop it needs only diagonal 4 directions
        // and for queen it needs all 8 directions
        int startDir = piece % 4 == 0 ? 4 : 0;
        int endDir = piece % 6 == 0 ? 4 : 8;
        //makes
        ArrayList<Move> moves = new ArrayList<>();
        //goes through each direction and checks if the move is
        for (int direction = startDir; direction < endDir; direction++) {
            for (int n = 0; n < numToEdge[start][direction]; n++) {
                int target = start + directionOffset[direction] * (n + 1);
                int pieceOnTargetSq = board[target];

                if (PiecesInt.getColor(pieceOnTargetSq) == color && pieceOnTargetSq != PiecesInt.None1) {
                    break;
                }

                moves.add(new Move(start, target, piece, null, Math.abs(pieceOnTargetSq)));
                if (PiecesInt.getColor(pieceOnTargetSq) != color && pieceOnTargetSq != PiecesInt.None1) {
                    break;
                }
            }
        }
        return moves;
    }

    //checks if enemy king is in check
    public boolean isChecked(Integer[] board, boolean color) {
        int kingSquare = findKingSquare(board, color);
        if (kingSquare == -1) return false;
        return isSquareUnderAttack(board, kingSquare, color);
    }

    private int findKingSquare(Integer[] board, boolean color) {
        int king = color ? PiecesInt.King1 : PiecesInt.King2;
        for (int i = 0; i < 64; i++) {
            if (board[i] == king) return i;
        }
        return -1;
    }

    // Centralized attack checking
    private boolean isSquareUnderAttack(Integer[] board, int square, boolean color) {
        return checkPawnAttacks(board, square, color) ||
                checkKnightAttacks(board, square, color) ||
                checkSlidingAttacks(board, square, color) ||
                checkKingAttacks(board, square, color);
    }

    // Pawn attack check (optimized)
    private boolean checkPawnAttacks(Integer[] board, int square, boolean color) {
        int[] pawnOffsets = color ? new int[]{7, 9} : new int[]{-7, -9};
        for (int offset : pawnOffsets) {
            int target = square + offset;
            if (isValidSquare(target) &&
                    Math.abs(target % 8 - square % 8) == 1 &&
                    board[target] == (color ? PiecesInt.Pawn2 : PiecesInt.Pawn1)) {
                return true;
            }
        }
        return false;
    }

    // Knight attack check
    private boolean checkKnightAttacks(Integer[] board, int square, boolean color) {
        for (int move : knightOffsets) {
            int target = square + move;
            if (isValidSquare(target) &&
                    board[target] == (color ? PiecesInt.Knight2 : PiecesInt.Knight1)) {
                int dx = Math.abs((target % 8) - (square % 8));
                int dy = Math.abs((target / 8) - (square / 8));
                if (dx + dy == 3 && dx != 0 && dy != 0) return true;
            }
        }
        return false;
    }

    // Sliding pieces attack check
    private boolean checkSlidingAttacks(Integer[] board, int square, boolean color) {
        // Bishop

        for (int i = 0; i < directions.length; i++) {
            int dx = directions[i][0];
            int dy = directions[i][1];
            int current = square;

            while (true) {
                int newFile = (current % 8) + dx;
                int newRank = (current / 8) + dy;
                if (newFile < 0 || newFile > 7 || newRank < 0 || newRank > 7) break;

                current = newRank * 8 + newFile;
                int piece = board[current];
                if (piece == PiecesInt.None1) continue;

                if (PiecesInt.getColor(piece) != color) {
                    if (isValidSliderAttack(piece, i)) return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean isValidSliderAttack(int piece, int directionIndex) {
        int type = Math.abs(piece);
        if (type == PiecesInt.Queen1) return true;
        if (type == PiecesInt.Rook1 && directionIndex < 4) return true;
        if (type == PiecesInt.Bishop1 && directionIndex >= 4) return true;
        return false;
    }

    // King attack check
    private boolean checkKingAttacks(Integer[] board, int square, boolean color) {

        for (int move : directionOffset) {
            if (square % 8 == 0 && (move == -1 || move == -9 || move == 7)) {
                continue;
            }
            if (square % 8 == 7 && (move == 1 || move == 9 || move == -7)) {
                continue;
            }
            int target = square + move;
            if (isValidSquare(target) &&
                    board[target] == (color ? PiecesInt.King2 : PiecesInt.King1)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidSquare(int square) {
        return square >= 0 && square < 64;
    }

    public boolean isValidMove(Integer[] board, int targetSquare, int movedPiece, int startSquare, boolean color) {
        Integer[] boardCopy = Arrays.copyOf(board, board.length);
        applyMoveToBoard(boardCopy, startSquare, targetSquare, movedPiece, color);
        int kingSquare = findKingSquare(boardCopy, color);
        if (kingSquare == -1) return false;
        return !isSquareUnderAttack(boardCopy, kingSquare, color);
    }

    // Helper method to apply moves including special cases
    private void applyMoveToBoard(Integer[] boardCopy, int start, int move, int piece, boolean color) {
        // Handle en passant

        int coll = color ? -8 : +8;
        if (piece == PiecesInt.Pawn1 || piece == PiecesInt.Pawn2) {
            if (Math.abs(move - start) != 8 && boardCopy[move] == PiecesInt.None1) {
                boardCopy[move] = piece;
                boardCopy[start] = PiecesInt.None1;
                boardCopy[move + coll] = PiecesInt.None1;
            } else {
                boardCopy[move] = piece;
                boardCopy[start] = PiecesInt.None1;
            }

        } else {
            boardCopy[move] = piece;
            boardCopy[start] = PiecesInt.None1;
        }
    }


    //generate moves for knight
    public ArrayList<Move> generateKnightMoves(int start, int piece, boolean color, Integer[] board) {

        ArrayList<Move> moves = new ArrayList<>();
        for (int offset : knightOffsets) {
            int target = start + offset;
            if (target >= 0 && target < 64) {
                if ((board[target] == 0) || color != PiecesInt.getColor(board[target])) {
                    int targetRow = target / 8;
                    int targetCol = target % 8;
                    int squareRow = start / 8;
                    int squareCol = start % 8;

                    if (Math.abs(squareCol - targetCol) == 1 && Math.abs(squareRow - targetRow) == 2) {
                        moves.add(new Move(start, target, piece, null, -board[target]));
                    } else if (Math.abs(squareCol - targetCol) == 2 && Math.abs(squareRow - targetRow) == 1) {
                        moves.add(new Move(start, target, piece, null, -board[target]));
                    }
                }
            }
        }
        return moves;
    }

    //generate king moves
    public ArrayList<Move> generateKingMoves(int start, boolean color, Integer[] board) {

        ArrayList<Move> moves = new ArrayList<>();
// Check if the piece is on the left edge
        boolean isLeftEdge = (start % 8 == 0);
// Check if the piece is on the right edge
        boolean isRightEdge = (start % 8 == 7);
// Check possible moves
        if (!isRightEdge && (start + 1) < 64) {
            // Move to the right
            if (board[start + 1] == PiecesInt.None1 || PiecesInt.getColor(board[start + 1]) != color) {
                moves.add(new Move(start, start + 1, board[start], null, -board[start + 1]));
            }
        }
        if (!isLeftEdge && (start - 1) >= 0) {
            // Move to the left
            if (board[start - 1] == PiecesInt.None1 || PiecesInt.getColor(board[start - 1]) != color) {
                moves.add(new Move(start, start - 1, board[start], null, -board[start - 1]));
            }
        }
        if ((start + 8) < 64) {
            // Move downward
            if (board[start + 8] == PiecesInt.None1 || PiecesInt.getColor(board[start + 8]) != color) {
                moves.add(new Move(start, start + 8, board[start], null, -board[start + 8]));
            }
        }
        if ((start - 8) >= 0) {
            // Move upward
            if (board[start - 8] == PiecesInt.None1 || PiecesInt.getColor(board[start - 8]) != color) {
                moves.add(new Move(start, start - 8, board[start], null, -board[start - 8]));
            }
        }
        if (!isLeftEdge && (start + 7) < 64) {
            // Move down-left
            if (board[start + 7] == PiecesInt.None1 || PiecesInt.getColor(board[start + 7]) != color) {
                moves.add(new Move(start, start + 7, board[start], null, -board[start + 7]));
            }
        }
        if (!isRightEdge && (start + 9) < 64) {
            // Move down-right
            if (board[start + 9] == PiecesInt.None1 || PiecesInt.getColor(board[start + 9]) != color) {
                moves.add(new Move(start, start + 9, board[start], null, -board[start + 9]));
            }
        }
        if (!isLeftEdge && (start - 9) >= 0) {
            // Move up-left
            if (board[start - 9] == PiecesInt.None1 || PiecesInt.getColor(board[start - 9]) != color) {
                moves.add(new Move(start, start - 9, board[start], null, -board[start - 9]));
            }
        }
        if (!isRightEdge && (start - 7) >= 0) {
            // Move up-right
            if (board[start - 7] == PiecesInt.None1 || PiecesInt.getColor(board[start - 7]) != color) {
                moves.add(new Move(start, start - 7, board[start], null, -board[start - 7]));
            }
        }
        return moves;
    }

    //generate pawn moves
    public ArrayList<Move> generatePawnMoves(int start, int piece, boolean color, Integer[] board, Move move, boolean moveIf) {

        int twoSquareB = color ? 7 : 47;
        int twoSquareE = color ? 16 : 56;
        int twoSquareMove = color ? 16 : -16;
        int oneSquare = color ? 8 : -8;
        int captureLeft = color ? 7 : -9;
        int pawn = color ? PiecesInt.Pawn2 : PiecesInt.Pawn1;
        int captureRight = color ? 9 : -7;
        ArrayList<Move> moves = new ArrayList<>();
        //so basically all of this is there to check if the double/single square move are possible, if capture
        //to right and left is possible, and also if en passant is possible
        //moveIF is in there because if we are checking for illegal moves we have to know if we are checking for it

        if (start > twoSquareB && start < twoSquareE && board[start + twoSquareMove] == 0) {
            if (board[start + oneSquare] == PiecesInt.None1) {
                moves.add(new Move(start, (start + twoSquareMove), piece, null, -board[start + oneSquare]));
            }
        }
        if (start + oneSquare < 64 && start + oneSquare > -1) {
            if (board[start + oneSquare] == 0) {
                if (piece > 0) {

                    if (start < 56 && start > 47) {
                        moves.add(new Move(start, start + oneSquare, piece, 2, -board[start + oneSquare]));
                        moves.add(new Move(start, start + oneSquare, piece, 3, -board[start + oneSquare]));
                        moves.add(new Move(start, start + oneSquare, piece, 4, -board[start + oneSquare]));
                        moves.add(new Move(start, start + oneSquare, piece, 6, -board[start + oneSquare]));
                    } else {
                        moves.add(new Move(start, (start + oneSquare), piece, null, -board[start + oneSquare]));
                    }

                } else {
                    if (start < 16 && start > 7) {
                        moves.add(new Move(start, start + oneSquare, piece, -2, -board[start + oneSquare]));
                        moves.add(new Move(start, start + oneSquare, piece, -3, -board[start + oneSquare]));
                        moves.add(new Move(start, start + oneSquare, piece, -4, -board[start + oneSquare]));
                        moves.add(new Move(start, start + oneSquare, piece, -6, -board[start + oneSquare]));
                    } else {
                        moves.add(new Move(start, (start + oneSquare), piece, null, -board[start + oneSquare]));
                    }
                }
            }
        }

        if (start % 8 != 7) {
            if (start + oneSquare < 64 && start + oneSquare > -1) {
                if (moveIf) {
                    if (board[start + captureRight] == 0) {
                        moves.add(new Move(start, (start + captureRight), piece, null, -board[start + captureRight]));
                    }
                }
                if (PiecesInt.getColor(board[start + captureRight]) != color && board[start + captureRight] != 0) {
                    if (piece > 0) {
                        if (start < 56 && start > 47) {
                            moves.add(new Move(start, start + captureRight, piece, 2, -board[start + captureRight]));
                            moves.add(new Move(start, start + captureRight, piece, 3, -board[start + captureRight]));
                            moves.add(new Move(start, start + captureRight, piece, 4, -board[start + captureRight]));
                            moves.add(new Move(start, start + captureRight, piece, 6, -board[start + captureRight]));
                        } else {
                            moves.add(new Move(start, (start + captureRight), piece, null, -board[start + captureRight]));
                        }
                    } else {
                        if (start < 16 && start > 7) {
                            moves.add(new Move(start, start + captureRight, piece, -2, -board[start + captureRight]));
                            moves.add(new Move(start, start + captureRight, piece, -3, -board[start + captureRight]));
                            moves.add(new Move(start, start + captureRight, piece, -4, -board[start + captureRight]));
                            moves.add(new Move(start, start + captureRight, piece, -6, -board[start + captureRight]));
                        } else {
                            moves.add(new Move(start, (start + captureRight), piece, null, -board[start + captureRight]));
                        }
                    }
                }
                if (move != null)
                    if (board[start + 1] == pawn && (move.target - 1) == start && Math.abs(move.start - move.target) == 16) {
                        moves.add(new Move(start, (start + captureRight), piece, null, -board[start + captureRight]));
                    }
            }
        }

        if (start % 8 != 0) {
            if (start + oneSquare < 64 && start + oneSquare > -1) {
                if (moveIf) {
                    if (board[start + captureLeft] == 0) {
                        moves.add(new Move(start, (start + captureLeft), piece, null, -board[start + captureLeft]));
                    }
                }
                if (PiecesInt.getColor(board[start + captureLeft]) != color && board[start + captureLeft] != 0) {
                    if (piece > 0) {
                        if (start < 56 && start > 47) {
                            moves.add(new Move(start, (start + captureLeft), piece, 2, -board[start + captureLeft]));
                            moves.add(new Move(start, (start + captureLeft), piece, 3, -board[start + captureLeft]));
                            moves.add(new Move(start, (start + captureLeft), piece, 4, -board[start + captureLeft]));
                            moves.add(new Move(start, (start + captureLeft), piece, 6, -board[start + captureLeft]));
                        } else {
                            moves.add(new Move(start, (start + captureLeft), piece, null, -board[start + captureLeft]));
                        }
                    } else {
                        if (start < 16 && start > 7) {
                            moves.add(new Move(start, (start + captureLeft), piece, -2, -board[start + captureLeft]));
                            moves.add(new Move(start, (start + captureLeft), piece, -3, -board[start + captureLeft]));
                            moves.add(new Move(start, (start + captureLeft), piece, -4, -board[start + captureLeft]));
                            moves.add(new Move(start, (start + captureLeft), piece, -6, -board[start + captureLeft]));
                        } else {
                            moves.add(new Move(start, (start + captureLeft), piece, null, -board[start + captureLeft]));
                        }
                    }
                }
                if (move != null) {
                    if (board[start - 1] == pawn && move.target == (start - 1) && Math.abs(move.start - move.target) == 16) {
                        moves.add(new Move(start, (start + captureLeft), piece, null, -board[start + captureLeft]));
                    }
                }
            }
        }
        return moves;
    }


    public ArrayList<Move> castling(int start, int piece, boolean color, Integer[] board, boolean[] boolCastling) {

// we need to know if some piece is not looking on specific squares, so if the castling is possible
        ArrayList<Move> moves = new ArrayList<>();
        int begin = color ? 4 : 60;

        if (start != begin) {
            return moves;
        }
        // Check for castling
        if (color) {
            int one = !isSquareUnderAttack(board, 1, true) ? 0 : 1;
            int two = !isSquareUnderAttack(board, 2, true) ? 0 : 1;
            int three = !isSquareUnderAttack(board, 3, true) ? 0 : 1;
            int four = !isSquareUnderAttack(board, 4, true) ? 0 : 1;
            int five = !isSquareUnderAttack(board, 5, true) ? 0 : 1;
            int six = !isSquareUnderAttack(board, 6, true) ? 0 : 1;
            // White castling
            if (!boolCastling[0]) {
                if (!boolCastling[1] && board[0] == PiecesInt.Rook1 && board[1] == 0 && board[2] == 0 && board[3] == 0) {
                    // Check if the king's path (2 and 3) is under attack (implement this check based on your engine)
                    if (one == 0 && two == 0 && three == 0 && four == 0) {
                        moves.add(new Move(start, 2, piece, null, 0));  // Left castling for white
                    }
                }
                if (!boolCastling[2] && board[7] == PiecesInt.Rook1 && board[5] == 0 && board[6] == 0) {
                    if (four == 0 && five == 0 && six == 0) {
                        moves.add(new Move(start, 6, piece, null, 0));  // Right castling for white
                    }
                }
            }
        } else {
            // Black castling
            int fe = !isSquareUnderAttack(board, 58, false) ? 0 : 1;
            int fn = !isSquareUnderAttack(board, 59, false) ? 0 : 1;
            int s = !isSquareUnderAttack(board, 60, false) ? 0 : 1;
            int st = !isSquareUnderAttack(board, 62, false) ? 0 : 1;
            int so = !isSquareUnderAttack(board, 61, false) ? 0 : 1;
            if (!boolCastling[3]) {
                if (!boolCastling[4] && board[56] == PiecesInt.Rook2 && board[57] == 0 && board[58] == 0 && board[59] == 0) {
                    if (fe == 0 && fn == 0 && s == 0) {
                        moves.add(new Move(start, 58, piece, null, 0));  // Left castling for black
                    }
                }
                if (!boolCastling[5] && board[63] == PiecesInt.Rook2 && board[61] == 0 && board[62] == 0) {
                    if (s == 0 && so == 0 && st == 0) {
                        moves.add(new Move(start, 62, piece, null, 0));  // Right castling for black
                    }
                }
            }
        }
        return moves;
    }


    // Get all squares of pieces attacking the target square
    public List<Integer> getAttackers(int targetSquare, Integer[] board, boolean targetColor) {
        List<Integer> attackers = new ArrayList<>();
        boolean attackerColor = !targetColor;

        // Check pawn attacks
        attackers.addAll(collectPawnAttackers(board, targetSquare, attackerColor));

        // Check knight attacks
        attackers.addAll(collectKnightAttackers(board, targetSquare, attackerColor));

        // Check sliding attacks (bishops, rooks, queens)
        attackers.addAll(collectSlidingAttackers(board, targetSquare, attackerColor));

        // Check king attacks
        attackers.addAll(collectKingAttackers(board, targetSquare, attackerColor));

        return attackers;
    }

    // Get all squares of pieces defending the target square
    public List<Integer> getDefenders(int targetSquare, Integer[] board, boolean targetColor) {
        // Simply reuse getAttackers but with same color
        return getAttackers(targetSquare, board, !targetColor);
    }

    // Modified attack checkers to return attacker positions
    private List<Integer> collectPawnAttackers(Integer[] board, int square, boolean attackerColor) {
        List<Integer> attackers = new ArrayList<>();
        int[] pawnOffsets = attackerColor ? new int[]{7, 9} : new int[]{-7, -9};
        int pawnType = attackerColor ? PiecesInt.Pawn2 : PiecesInt.Pawn1;

        for (int offset : pawnOffsets) {
            int target = square + offset;
            if (isValidSquare(target) &&
                    Math.abs(target % 8 - square % 8) == 1 &&
                    board[target] == pawnType) {
                attackers.add(target);
            }
        }
        return attackers;
    }

    private List<Integer> collectKnightAttackers(Integer[] board, int square, boolean attackerColor) {
        List<Integer> attackers = new ArrayList<>();
        int knightType = attackerColor ? PiecesInt.Knight2 : PiecesInt.Knight1;

        for (int move : knightOffsets) {
            int target = square + move;
            if (isValidSquare(target) &&
                    board[target] == knightType) {
                int dx = Math.abs((target % 8) - (square % 8));
                int dy = Math.abs((target / 8) - (square / 8));
                if (dx + dy == 3 && dx != 0 && dy != 0) {
                    attackers.add(target);
                }
            }
        }
        return attackers;
    }

    private List<Integer> collectSlidingAttackers(Integer[] board, int square, boolean attackerColor) {
        List<Integer> attackers = new ArrayList<>();
        int[] slidingTypes = {PiecesInt.Bishop1, PiecesInt.Rook1, PiecesInt.Queen1};

        for (int i = 0; i < directions.length; i++) {
            int dx = directions[i][0];
            int dy = directions[i][1];
            int current = square;

            while (true) {
                int newFile = (current % 8) + dx;
                int newRank = (current / 8) + dy;
                if (newFile < 0 || newFile > 7 || newRank < 0 || newRank > 7) break;

                current = newRank * 8 + newFile;
                int piece = board[current];
                if (piece == PiecesInt.None1) continue;

                if (PiecesInt.getColor(piece) == attackerColor &&
                        isValidSliderAttack(piece, i)) {
                    attackers.add(current);
                }
                break;
            }
        }
        return attackers;
    }

    private List<Integer> collectKingAttackers(Integer[] board, int square, boolean attackerColor) {
        List<Integer> attackers = new ArrayList<>();
        int kingType = attackerColor ? PiecesInt.King2 : PiecesInt.King1;

        for (int move : directionOffset) {
            if (square % 8 == 0 && (move == -1 || move == -9 || move == 7)) continue;
            if (square % 8 == 7 && (move == 1 || move == 9 || move == -7)) continue;

            int target = square + move;
            if (isValidSquare(target) && board[target] == kingType) {
                attackers.add(target);
            }
        }
        return attackers;
    }

}