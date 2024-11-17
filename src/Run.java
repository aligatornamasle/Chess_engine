import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Run {

    sliding_pieces sliding_pieces = new sliding_pieces();
    Integer[] testGrid = new Integer[64];
    boolean selected = false;
    int selectedPiece = 0;
    int selcoor = 0;
Move previosMove;
boolean[] isCastling = new boolean[6];
    int counter = 0;
    int counter2 = 0;
    ArrayList<Move> movesss= new ArrayList<>();
    boolean gameflow = true;

    public Run() {
//setupBoardFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ");
  //    setupBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/7P/PPPPPPP1/RNBQKBNR b KQkq - 0 1");
        long startTime = System.nanoTime();
        sliding_pieces.movesForSlidingPieces();
        for(int i = 0;i<6;i++){
            isCastling[i] = false;

        }

     //   movePieceRecursive(testGrid,false,0,3,counter,null,isCastling);

        boolean whiteKingMoved = false;
        boolean whiteRookLeftMoved = false;
        boolean whiteRookRightMoved = false;
        boolean blackKingMoved = false;
        boolean blackRookLeftMoved = false;
        boolean blackRookRightMoved = false;


//var a =sliding_pieces.generateMoves(false,testGrid,null,null);
    //    System.out.println(a.size());
        int j=0;
        int d = 0;
        int t = 0;
        int s = 0;
        int p =0;
        int ss =0;

        System.out.println(counter);
        long endTime = System.nanoTime();

        // Calculate the elapsed time in milliseconds and seconds
        long elapsedTimeInNano = endTime - startTime;
        double elapsedTimeInMilli = elapsedTimeInNano / 1_000_000.0; // Convert to milliseconds
        double elapsedTimeInSeconds = elapsedTimeInMilli / 1000.0;    // Convert to seconds

        //System.out.println("Elapsed time in nanoseconds: " + elapsedTimeInNano + " ns");
       // System.out.println("Elapsed time in milliseconds: " + elapsedTimeInMilli + " ms");
        System.out.println("Elapsed time in seconds: " + elapsedTimeInSeconds + " s");





    }
    public boolean[] getIsCastling(){
        return isCastling;
    }


    public ArrayList<Move> getPossibleMoves(boolean color,Integer[] grid,Move previousMove,boolean[] castle){

        return sliding_pieces.generateMoves(color,grid,previousMove,castle);
    }


    public static String convertToChessNotation(int index,Move move,boolean isit) {
       // int index = move.target;
        int file = index % 8;               // Column (file)
        int rank = (index / 8) + 1;         // Row (rank), from bottom to top

        // Convert file number to letter (0 -> 'a', ..., 7 -> 'h')
        char fileChar = (char) ('a' + file);




if(move!=null)
        if(move.promotion!= null && isit){
            char help = switch (Math.abs(move.promotion)) {
                case 2 -> 'q';
                case 3 -> 'n';
                case 4 -> 'b';
                case 6 -> 'r';
                default -> ' ';
            };
            // fileChar = (char)(fileChar+help);
           return "" + fileChar + rank + help;
        }
        return "" + fileChar + rank;
    }


    public boolean selectPiece(int coordination,boolean color){

        if(PiecesInt.getColor(testGrid[coordination])==color && testGrid[coordination]!=PiecesInt.None1 ){
            selectedPiece=testGrid[coordination];
            selcoor = coordination;
            selected = true;
            return true;

        }

        selected = false;
        return false;
    }
    public boolean makeMove(int movee,boolean color,Move move) {

        var moves = sliding_pieces.generateMoves(color, testGrid, previosMove, isCastling);


        boolean castling = false;
        boolean canMove = false;
        for (Move m : moves) {
            if (m != null) {
                if (m.piece == move.piece && m.start == move.start && m.target == move.target) {
                    canMove = true;
                    break;
                }
            }
        }
        if(moves.size() == 0){
            gameflow = false;
        }


if(canMove){
        int coll = color ? -8 : +8;
        if (move.piece == PiecesInt.Pawn1 || move.piece == PiecesInt.Pawn2) {

            if (move.promotion != 0) {
                testGrid[move.start] = PiecesInt.None1;
                testGrid[move.target] = move.promotion;
            } else {


                if (previosMove != null) {
                    //if((testGrid[move.target] == PiecesInt.None1) && (previosMove.target == move.start + 1  && Math.abs(previosMove.start -previosMove.target)==16&& move.target == previosMove.target+colmove  ||(previosMove.target == move.start -1&& move.target == previosMove.target+colmove ))){

                    if (Math.abs(move.target - move.start) != 8 && testGrid[move.target] == PiecesInt.None1) {

                        testGrid[move.target + coll] = PiecesInt.None1;
                        testGrid[move.start] = PiecesInt.None1;
                        int clr = color ? PiecesInt.Pawn1 : PiecesInt.Pawn2;
                        testGrid[move.target] = clr;

                    } else {
                        testGrid[move.target] = move.piece;
                        testGrid[move.start] = 0;
                        previosMove = move;
                    }
                } else {
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = 0;
                    previosMove = move;
                }
            }
        } else {
            if (Math.abs(move.piece) == PiecesInt.King1) {
                if (move.start == 4 && move.target == 2) {
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = PiecesInt.None1;
                    testGrid[0] = PiecesInt.None1;
                    testGrid[move.target + 1] = PiecesInt.Rook1;
                    isCastling[0] = true;
                    isCastling[1] = true;
                    isCastling[2] = true;
                    return true;

                } else if (move.start == 4 && move.target == 6) {
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = PiecesInt.None1;
                    testGrid[7] = PiecesInt.None1;
                    testGrid[move.target - 1] = PiecesInt.Rook1;
                    isCastling[0] = true;
                    isCastling[2] = true;
                    isCastling[1] = true;
                    return true;
                } else if (move.start == 60 && move.target == 58) {
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = PiecesInt.None1;
                    testGrid[56] = PiecesInt.None1;
                    testGrid[move.target + 1] = PiecesInt.Rook2;
                    isCastling[3] = true;
                    isCastling[4] = true;
                    isCastling[5] = true;
                    return true;
                } else if (move.start == 60 && move.target == 62) {
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = PiecesInt.None1;
                    testGrid[63] = PiecesInt.None1;
                    testGrid[move.target - 1] = PiecesInt.Rook2;
                    isCastling[3] = true;
                    isCastling[4] = true;
                    isCastling[5] = true;
                    return true;
                } else {


                    if (move != null) {
                        testGrid[move.target] = move.piece;
                        testGrid[move.start] = 0;
                        previosMove = move;
                    }

                }


            } else {


                if (move != null) {
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = 0;
                    previosMove = move;
                }
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


    public void printer(Move move){
        ArrayList<Move> moves = sliding_pieces.generateMoves(true,testGrid,move,null);
        for(Move m :moves){
            System.out.println(m.start+", "+m.target+", "+m.piece);
        }

    }


    public Integer[] getGrid(){
        return testGrid;
    }



    Integer[] getTestGrid(){
        return testGrid;
    }

    public void setupBoardFromFEN(String fen) {

        String[] parts = fen.split(" ");
        String piecePlacement = parts[0];

        String[] ranks = piecePlacement.split("/");

                for (int row = 0; row < 8; row++) {
                    // Map FEN rank starting from rank 1 (ranks[7]) to row 7 in testGrid, up to rank 8 (ranks[0]) to row 0
                    String rank = ranks[7 - row];
                    int column = 0;

                    for (char symbol : rank.toCharArray()) {
                        int colrow = column + row * 8;  // No adjustment needed for row since we're loading opposite

                        if (Character.isDigit(symbol)) {
                            // If symbol is a digit, it represents empty squares.
                            int emptySquares = Character.getNumericValue(symbol);
                            for (int i = 0; i < emptySquares; i++) {
                                testGrid[colrow + i] = PiecesInt.None1;
                            }
                            // Move the column by the number of empty squares
                            column += emptySquares;
                        } else {
                            // Place each piece at the current position
                            switch (symbol) {
                                case 'r': testGrid[colrow] = PiecesInt.Rook2; break;
                                case 'n': testGrid[colrow] = PiecesInt.Knight2; break;
                                case 'b': testGrid[colrow] = PiecesInt.Bishop2; break;
                                case 'q': testGrid[colrow] = PiecesInt.Queen2; break;
                                case 'k': testGrid[colrow] = PiecesInt.King2; break;
                                case 'p': testGrid[colrow] = PiecesInt.Pawn2; break;
                                case 'R': testGrid[colrow] = PiecesInt.Rook1; break;
                                case 'N': testGrid[colrow] = PiecesInt.Knight1; break;
                                case 'B': testGrid[colrow] = PiecesInt.Bishop1; break;
                                case 'Q': testGrid[colrow] = PiecesInt.Queen1; break;
                                case 'K': testGrid[colrow] = PiecesInt.King1; break;
                                case 'P': testGrid[colrow] = PiecesInt.Pawn1; break;
                            }
                            column++; // Move to the next column for the next piece or empty square
                        }
                    }
                }
        //printer();
        /*
        for(int i = 0;i<64;i++){
            System.out.print(testGrid[i]);

        }

         */
    }








    public void movePieceRecursive(Integer[] grid,Boolean color,int currentDepth, int maxDepth, int counter1,Move enpasantMove,boolean[] castling) {

        if (currentDepth == maxDepth) {
            counter++;
            counter2++;

            return;
        }
if(grid[15]==6&& grid[4] == 1){
 //   System.out.println("A");
}
        var possibleMoves = sliding_pieces.generateMoves(color,grid,enpasantMove,castling);
        //HashMap<Piece,ArrayList<Move>> possibleMoves = allPossibleMoves(currentGrid, currentPlayer);
         int totalPositions = 0;
        int number = 0;
        boolean[] castle = Arrays.copyOf(castling, castling.length);
        for(Move m : possibleMoves){
            // = new boolean[6];
            /*
            for(int i = 0;i<6;i++) {
                if(castling[i]){
                    castle[i] = true;
                } else{
                    castle[i] = false;
                }
            }

             */
            boolean[] castle1 = Arrays.copyOf(castling, castling.length);


            if(currentDepth==1 && castling[2] && !castling[1]){
           //     System.out.println("A");
            }
           // boolean[] castle = Arrays.copyOf(castling,castling.length);
            Integer[] nextGrid = copyGridWithMove(grid,m,color,enpasantMove, castle1);
            if(currentDepth==1 && castle[2] && !castle[1]){
              //  System.out.println("A");
            }

                enpasantMove = m;
                Move es = new Move(m.start,m.target,m.piece,null);





            //previosMove = m;

            if(m.target==34 && m.start ==50){
             //   System.out.println("a");
            }
           // Integer[] nextGrid = copyGridWithMove(grid,m,color,enpasantMove,castling);

            movePieceRecursive(nextGrid,!color,currentDepth+1,maxDepth,counter,es,Arrays.copyOf(castle1, castle1.length));

            if (currentDepth == 0) {
             //   System.out.println("-------------------------------");
            //   System.out.print(PiecesInt.getColor(m.piece));
                System.out.print(convertToChessNotation(m.start,m,false));
                System.out.print(convertToChessNotation(m.target,m,true));
                System.out.print(": "+ counter2);
                System.out.println(" ");
              //  System.out.println("-------------------------------");
                counter2 = 0;
            }

/*
            if (currentDepth == 1) {
                // System.out.print(PiecesInt.getColor(m.piece));
                System.out.print(convertToChessNotation(m.start,null,false));
                System.out.print(convertToChessNotation(m.target,null,false));
                System.out.print(": "+ counter2);
                System.out.println(" ");
                counter2 = 0;
            }

 */














/*
            if (currentDepth == 2) {
               // System.out.print(PiecesInt.getColor(m.piece));
                System.out.print(convertToChessNotation(m.start,null,false));
                System.out.print(convertToChessNotation(m.target,null,false));
                System.out.print(": "+ counter2);
                System.out.println(" ");

                counter2 = 0;
            }

 */


            /*
            /*
            if (currentDepth == 3) {
                //System.out.print(PiecesInt.getColor(m.piece));
                System.out.print(convertToChessNotation(m.start));
                System.out.print(convertToChessNotation(m.target));
                System.out.print(": "+ counter2);
                System.out.println(" ");

                counter2 = 0;
            }

 */






        }
    }












    public Integer[] copyGridWithMove(Integer[] grid,Move move,Boolean color,Move previosMove,boolean[] castling){
        Integer[] testGrid = new Integer[64];

        for(int i = 0;i<64;i++){
            testGrid[i] = grid[i];

        }








        int coll = color?-8:+8;
        if(move.piece== PiecesInt.Pawn1 || move.piece== PiecesInt.Pawn2){

            if(move.promotion!=null){
                testGrid[move.start] = PiecesInt.None1;
                testGrid[move.target] = move.promotion;
            }else {


                if (previosMove != null) {
                    //if((testGrid[move.target] == PiecesInt.None1) && (previosMove.target == move.start + 1  && Math.abs(previosMove.start -previosMove.target)==16&& move.target == previosMove.target+colmove  ||(previosMove.target == move.start -1&& move.target == previosMove.target+colmove ))){

                    if (Math.abs(move.target - move.start) != 8 && grid[move.target] == PiecesInt.None1) {

                        testGrid[move.target + coll] = PiecesInt.None1;
                        testGrid[move.start] = PiecesInt.None1;
                        int clr = color ? PiecesInt.Pawn1 : PiecesInt.Pawn2;
                        testGrid[move.target] = clr;

                    } else {
                        testGrid[move.target] = move.piece;
                        testGrid[move.start] = 0;
                        previosMove = move;
                    }
                } else {
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = 0;
                    previosMove = move;
                }
            }
        }else {
            if(Math.abs(move.piece) ==PiecesInt.King1){
                if(move.start == 4 && move.target ==2){
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = PiecesInt.None1;
                    testGrid[0]= PiecesInt.None1;
                    testGrid[move.target+1] = PiecesInt.Rook1;
                    castling[0] = true;
                    castling[1] = true;
                    castling[2] = true;

                }else
                if(move.start == 4 && move.target ==6){
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = PiecesInt.None1;
                    testGrid[7]= PiecesInt.None1;
                    testGrid[move.target-1] = PiecesInt.Rook1;
                    castling[0] = true;
                    castling[2] = true;
                    castling[1] = true;
                }else
                if(move.start == 60 && move.target ==58){
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = PiecesInt.None1;
                    testGrid[56]= PiecesInt.None1;
                    testGrid[move.target+1] = PiecesInt.Rook2;
                    castling[3] = true;
                    castling[4] = true;
                    castling[5] = true;
                }else
                if(move.start == 60 && move.target ==62){
                    testGrid[move.target] = move.piece;
                    testGrid[move.start] = PiecesInt.None1;
                    testGrid[63]= PiecesInt.None1;
                    testGrid[move.target-1] = PiecesInt.Rook2;
                    castling[3] = true;
                    castling[4] = true;
                    castling[5] = true;
                }else{


                    if(move !=null){
                        testGrid[move.target] = move.piece;
                        testGrid[move.start] = 0;
                        previosMove = move;}
                }





            }else{


            if(move !=null){
            testGrid[move.target] = move.piece;
            testGrid[move.start] = 0;
            previosMove = move;}
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
















        return testGrid;
    }


    boolean isChecked(Integer[] grid, boolean color,Move previosMove){
        //sliding_pieces.isValidMove(grid,target,piece,start,color,previosMove);
       return sliding_pieces.isChecked(grid,color,previosMove);
    }

}
