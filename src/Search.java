import java.util.ArrayList;
import java.util.Arrays;

public class Search {

    MoveGenerator MoveGenerator = new MoveGenerator();
    Integer[] board = new Integer[64];
    boolean selectedPieceForMove = false;
    int selectedPiece = 0;
    int selectedCoordination = 0;
    Move previosMove;
    public boolean[] isCastling = new boolean[6];
    int counterNodes = 0;
    boolean gameFlow = true;


    public Search() {
        MoveGenerator.movesForSlidingPieces();
        for(int i = 0;i<6;i++){
            isCastling[i] = false;
        }
    }

    public ArrayList<Move> getPossibleMoves(boolean color,Integer[] grid,Move previousMove,boolean[] castle){
        return MoveGenerator.generateMoves(color,grid,previousMove,castle);
    }

    public void setFenCheckDepth(String fen,int depth,boolean color){
        FenBoardUtilities.setupBoardFromFEN(fen,board);
        this.movePieceRecursive(board,color,0,depth, null,isCastling);

    }

    public boolean selectPiece(int coordination, boolean color) {
        int piece = board[coordination];
        boolean pieceColor = piece > 0; // true for white, false for black

        if (pieceColor == color && piece != PiecesInt.None1) {
            selectedPiece = piece;
            selectedCoordination = coordination;
            selectedPieceForMove = true;
            return true;
        }
        selectedPieceForMove = false;
        return false;
    }
    public boolean makeMove(boolean color,Move move) {

        if(move == null){
            var moves = MoveGenerator.generateMoves(color, board, previosMove, isCastling);
            if(moves.isEmpty()){
                gameFlow = false;
                return false;
            }
            return false;
        }
        var moves = MoveGenerator.generateMoves(color, board, previosMove, isCastling);
        boolean canMove = false;

        for (Move m : moves) {
            if (m != null) {
                if (m.piece == move.piece && m.start == move.start && m.target == move.target) {
                    canMove = true;
                    move.captureScore_ = m.captureScore_;
                    break;
                }
            }
        }
        if(moves.isEmpty()){
            gameFlow = false;
        }
        if(canMove){
            int coll = color ? -8 : +8;
            if (move.piece == PiecesInt.Pawn1 || move.piece == PiecesInt.Pawn2) {
                if ((move.start <16 && move.start>7 && move.piece<0)||(move.start <56 && move.start>47&& move.piece>0)) {
                    board[move.start] = PiecesInt.None1;
                    board[move.target] = move.promotion;
                } else {
                    if (previosMove != null) {
                        if (Math.abs(move.target - move.start) != 8 && board[move.target] == PiecesInt.None1) {
                            board[move.target + coll] = PiecesInt.None1;
                            board[move.start] = PiecesInt.None1;
                            int clr = color ? PiecesInt.Pawn1 : PiecesInt.Pawn2;
                            board[move.target] = clr;
                        } else {
                            board[move.target] = move.piece;
                            board[move.start] = 0;
                           // previosMove = move;
                        }
                    } else {
                        board[move.target] = move.piece;
                        board[move.start] = 0;
                      //  previosMove = move;
                    }
                }
            } else {
                if (Math.abs(move.piece) == PiecesInt.King1) {
                    if (move.start == 4 && move.target == 2) {
                        board[move.target] = move.piece;
                        board[move.start] = PiecesInt.None1;
                        board[0] = PiecesInt.None1;
                        board[move.target + 1] = PiecesInt.Rook1;
                        isCastling[0] = true;
                        isCastling[1] = true;
                        isCastling[2] = true;
                        return true;
                    } else if (move.start == 4 && move.target == 6) {
                        board[move.target] = move.piece;
                        board[move.start] = PiecesInt.None1;
                        board[7] = PiecesInt.None1;
                        board[move.target - 1] = PiecesInt.Rook1;
                        isCastling[0] = true;
                        isCastling[2] = true;
                        isCastling[1] = true;
                        return true;
                    } else if (move.start == 60 && move.target == 58) {
                        board[move.target] = move.piece;
                        board[move.start] = PiecesInt.None1;
                        board[56] = PiecesInt.None1;
                        board[move.target + 1] = PiecesInt.Rook2;
                        isCastling[3] = true;
                        isCastling[4] = true;
                        isCastling[5] = true;
                        return true;
                    } else if (move.start == 60 && move.target == 62) {
                        board[move.target] = move.piece;
                        board[move.start] = PiecesInt.None1;
                        board[63] = PiecesInt.None1;
                        board[move.target - 1] = PiecesInt.Rook2;
                        isCastling[3] = true;
                        isCastling[4] = true;
                        isCastling[5] = true;
                        return true;
                    } else {

                        board[move.target] = move.piece;
                        board[move.start] = 0;
                       // previosMove = move;
                    }
                } else {
                    board[move.target] = move.piece;
                    board[move.start] = 0;
                   // previosMove = move;
                }
            }

            if (move.piece == PiecesInt.King1) {
            isCastling[0] = true;
            }
            if (move.piece == PiecesInt.King2) {
                isCastling[3] = true;
            }
            if (move.piece == PiecesInt.Rook1 && move.start == 0) {
                isCastling[1] = true;
            }
            if (move.piece == PiecesInt.Rook1 && move.start == 7) {
                isCastling[2] = true;
            }
            if (move.piece == PiecesInt.Rook2 && move.start == 56) {
                isCastling[4] = true;
            }
            if (move.piece == PiecesInt.Rook2 && move.start == 63) {
                isCastling[5] = true;
            }
            return true;
        }
        return false;
    }

    Integer[] getBoard(){
        return board;
    }

    public void movePieceRecursive(Integer[] grid,Boolean color,int currentDepth, int maxDepth,Move enpasantMove,boolean[] castling) {

        if (currentDepth == maxDepth) {
            counterNodes++;
            return;
        }
        var possibleMoves = MoveGenerator.generateMoves(color,grid,enpasantMove,castling);
        for(Move move : possibleMoves){
            boolean[] castle = Arrays.copyOf(castling, castling.length);
            Integer[] nextBoard = copyGridWithMove(grid,move,color,enpasantMove, castle);
            movePieceRecursive(nextBoard,!color,currentDepth+1,maxDepth,move,Arrays.copyOf(castle, castle.length));

        }
    }

    public Integer[] copyGridWithMove(Integer[] grid,Move move,Boolean color,Move previosMove,boolean[] castling){

        Integer[] copyBoard = Arrays.copyOf(grid,grid.length);
        int coll = color?-8:+8;
        if(move.piece== PiecesInt.Pawn1 || move.piece== PiecesInt.Pawn2){
            if(move.promotion!=null){
                copyBoard[move.start] = PiecesInt.None1;
                copyBoard[move.target] = move.promotion;
            }else {
                if(previosMove != null) {
                    if(Math.abs(move.target - move.start) != 8 && grid[move.target] == PiecesInt.None1) {
                        copyBoard[move.target + coll] = PiecesInt.None1;
                        copyBoard[move.start] = PiecesInt.None1;
                        int clr = color ? PiecesInt.Pawn1 : PiecesInt.Pawn2;
                        copyBoard[move.target] = clr;
                    } else {
                        copyBoard[move.target] = move.piece;
                        copyBoard[move.start] = 0;
                       // this.previosMove = move;
                    }
                } else {
                    copyBoard[move.target] = move.piece;
                    copyBoard[move.start] = 0;
                    //this.previosMove = move;
                }
            }
        }else {
            if(Math.abs(move.piece) ==PiecesInt.King1){
                if(move.start == 4 && move.target ==2){
                    copyBoard[move.target] = move.piece;
                    copyBoard[move.start] = PiecesInt.None1;
                    copyBoard[0]= PiecesInt.None1;
                    copyBoard[move.target+1] = PiecesInt.Rook1;
                    castling[0] = true;
                    castling[1] = true;
                    castling[2] = true;
                }else if(move.start == 4 && move.target ==6){
                    copyBoard[move.target] = move.piece;
                    copyBoard[move.start] = PiecesInt.None1;
                    copyBoard[7]= PiecesInt.None1;
                    copyBoard[move.target-1] = PiecesInt.Rook1;
                    castling[0] = true;
                    castling[2] = true;
                    castling[1] = true;
                }else if(move.start == 60 && move.target ==58){
                    copyBoard[move.target] = move.piece;
                    copyBoard[move.start] = PiecesInt.None1;
                    copyBoard[56]= PiecesInt.None1;
                    copyBoard[move.target+1] = PiecesInt.Rook2;
                    castling[3] = true;
                    castling[4] = true;
                    castling[5] = true;
                }else if(move.start == 60 && move.target ==62){
                    copyBoard[move.target] = move.piece;
                    copyBoard[move.start] = PiecesInt.None1;
                    copyBoard[63]= PiecesInt.None1;
                    copyBoard[move.target-1] = PiecesInt.Rook2;
                    castling[3] = true;
                    castling[4] = true;
                    castling[5] = true;
                }else{
                    copyBoard[move.target] = move.piece;
                    copyBoard[move.start] = 0;
                   // this.previosMove = move;
                }
            }else{
                copyBoard[move.target] = move.piece;
                copyBoard[move.start] = 0;
                //this.previosMove = move;
            }
        }

        if(move.piece == PiecesInt.King1){
            castling[0]=true;
        }
        if(move.piece == PiecesInt.King2){
            castling[3] =true;
        }
        if(move.piece == PiecesInt.Rook1 &&move.start == 0){
            castling[1] =true;
        }
        if(move.piece == PiecesInt.Rook1&&move.start  == 7){
            castling[2] =true;
        }
        if(move.piece == PiecesInt.Rook2&&move.start  == 56){
            castling[4] =true;
        }
        if(move.piece == PiecesInt.Rook2&&move.start == 63){
            castling[5] =true;
        }
        return copyBoard;
    }

    boolean isChecked(Integer[] grid, boolean color){
       return MoveGenerator.isChecked(grid,color);
    }

}
