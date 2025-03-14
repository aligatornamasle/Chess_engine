public class PawnStructureEvaluation {


    public static int evaluatePawn(Integer[] grid, int index, boolean isWhite) {

        int pawnValue = 0;

        // Bonus for passed pawns
        if (isPassedPawn(grid, index, isWhite)) {
            pawnValue += 50;  // Significant bonus for passed pawns
        }

        // Penalty for isolated pawns
        if (isIsolatedPawn(grid, index, isWhite)) {
            pawnValue -= 30;  // Penalize isolated pawns
        }

        // Penalty for doubled pawns
        if (isDoubledPawn(grid, index, isWhite)) {
            pawnValue -= 20;  // Penalize doubled pawns
        }

        // Bonus for pawn chains
        if (isInPawnChain(grid, index, isWhite)) {
            pawnValue += 20;  // Reward pawn chains
        }

        return pawnValue;
    }

    private static boolean isIsolatedPawn(Integer[] grid, int index, boolean isWhite) {
        int x = index % 8;
        for (int i = 0; i < 8; i++) {
            if (i != x) {
                int squareIndex = (index / 8) * 8 + i;
                if (grid[squareIndex] == (isWhite ? 1 : -1) * PiecesInt.Pawn1) {
                    return false;  // Not isolated if there's a friendly pawn on an adjacent file
                }
            }
        }
        return true;
    }

    private static boolean isDoubledPawn(Integer[] grid, int index, boolean isWhite) {
        int x = index % 8;
        for (int i = 0; i < 8; i++) {
            if (i != index / 8) {
                int squareIndex = i * 8 + x;
                if (grid[squareIndex] == (isWhite ? 1 : -1) * PiecesInt.Pawn1) {
                    return true;  // Doubled pawn if there's another pawn on the same file
                }
            }
        }
        return false;
    }

    private static boolean isInPawnChain(Integer[] grid, int index, boolean isWhite) {
        int x = index % 8;
        int y = index / 8;
        int direction = isWhite ? 1 : -1;

        // Check diagonally backward for supporting pawns
        return (isOccupiedByPlayer(grid, x - 1, y - direction, isWhite) ||
                isOccupiedByPlayer(grid, x + 1, y - direction, isWhite));
    }

    private static boolean isOccupiedByPlayer(Integer[] grid, int x, int y, boolean isWhite) {
        if (x < 0 || y < 0 || x >= 8 || y >= 8) return false; // Out of bounds
        int index = y * 8 + x; // Convert (x, y) to 0-63 index
        int playerColor = isWhite ? 1 : -1;

        // Check if the square is occupied by a friendly piece
        return (grid[index] == playerColor * PiecesInt.Pawn1 ||
                grid[index] == playerColor * PiecesInt.Rook1 ||
                grid[index] == playerColor * PiecesInt.Bishop1 ||
                grid[index] == playerColor * PiecesInt.Knight1 ||
                grid[index] == playerColor * PiecesInt.Queen1);
    }

    // Method to check if the pawn is a passed pawn

    private static boolean isPassedPawn(Integer[] grid, int index, boolean isWhite) {
        int x = index % 8;       // Column (file)
        int y = index / 8;       // Row (rank)
        int direction = isWhite ? 1 : -1; // White pawns move "up" (y increases), Black "down" (y decreases)

        // Check the three files (current, left, right)
        for (int i = -1; i <= 1; i++) {
            int newX = x + i;
            // Iterate through all squares in the pawn's path in the current file
            for (int newY = y + direction; newY >= 0 && newY < 8; newY += direction) {
                if (newX >= 0 && newX < 8) { // Ensure within bounds
                    int newIndex = newY * 8 + newX;
                    // Check if there is an opposing pawn in the path
                    if (grid[newIndex] == (!isWhite ? 1 : -1) * PiecesInt.Pawn1) {
                        return false; // Opposing pawn blocks the path
                    }
                }
            }
        }
        return true; // No opposing pawns blocking the path
    }
}