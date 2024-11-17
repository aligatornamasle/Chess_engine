import java.util.ArrayList;





public class sliding_pieces {

    boolean whiteKingMoved = false;
    boolean whiteRookLeftMoved = false;
    boolean whiteRookRightMoved = false;
    boolean blackKingMoved = false;
    boolean blackRookLeftMoved = false;
    boolean blackRookRightMoved = false;


    int enPassantSquare = -1;  // The square where En Passant is available
    boolean enPassantColor = false;  // Which color's turn it is for En Passant


    int[] directionOffset = {8,-8,-1,1,7,-7,9,-9};
int[][] numToEndge = new int[64][];


    public void movesForSlidingPieces() {


        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {

                int north = 7 - rank;
                int south = rank;
                int west = file;
                int east = 7 - file;

                int square = rank * 8 + file;

                numToEndge[square] = new int[]{
                        north,
                        south,
                        west,
                        east,
                        Math.min(north, west),
                        Math.min(south, east),
                        Math.min(north, east),
                        Math.min(south, west),

                };


            }


        }


    }

    public ArrayList<Move> generateMoves1(boolean color, Integer[] grid,Move move,boolean evenIfnot){
        ArrayList<Move> moves = new ArrayList<>();


        for(int start = 0;start <64;start++){
            int piece = grid[start];
            if((piece%2) ==0 && PiecesInt.getColor(piece)==color && piece!= PiecesInt.None1){
                moves.addAll(generateSlidingMoves(start,piece,PiecesInt.getColor(piece),grid));
            }

            if(Math.abs(piece) == PiecesInt.Knight1 && PiecesInt.getColor(piece)==color){
                moves.addAll(generateKnightMoves(start,piece,PiecesInt.getColor(piece),grid));

            }

            if(Math.abs(piece) == PiecesInt.Pawn1 && PiecesInt.getColor(piece)==color){
                moves.addAll(generatePawnMoves(start,piece,PiecesInt.getColor(piece),grid,move,evenIfnot));

            }
            if(Math.abs(piece) == PiecesInt.King1 && PiecesInt.getColor(piece)==color){
                moves.addAll(generateKingMoves(start,piece,PiecesInt.getColor(piece),grid));

            }



        }
        return moves;
    }


    public ArrayList<Move> generateMoves(boolean color, Integer[] grid,Move move,boolean[] castle){
        ArrayList<Move> moves = new ArrayList<>();


        for(int start = 0;start <64;start++){
            int piece = grid[start];
            if((piece%2) ==0 && PiecesInt.getColor(piece)==color && piece!= PiecesInt.None1){
                moves.addAll(generateSlidingMoves(start,piece,PiecesInt.getColor(piece),grid));
            }

            if(Math.abs(piece) == PiecesInt.Knight1 && PiecesInt.getColor(piece)==color){
               moves.addAll(generateKnightMoves(start,piece,PiecesInt.getColor(piece),grid));

            }

            if(Math.abs(piece) == PiecesInt.Pawn1 && PiecesInt.getColor(piece)==color){
                moves.addAll(generatePawnMoves(start,piece,PiecesInt.getColor(piece),grid,move,false));

            }
            if(Math.abs(piece) == PiecesInt.King1 && PiecesInt.getColor(piece)==color){
                moves.addAll(generateKingMoves(start,piece,PiecesInt.getColor(piece),grid));
                var castling = castling(start,piece,color,grid,castle);
                moves.addAll(castling);


            }



        }
        ArrayList<Move> helpm = new ArrayList<>();
        for(Move m :moves){
           if(!isValidMove(grid,m.target,m.piece,m.start,color,move)){
               helpm.add(m);
           };
        }
        for(int i = 0;i<moves.size();i++){
            for(Move ma :helpm){
                if(moves.get(i)==ma){
                    moves.set(i,null);
                }
            }
        }
        ArrayList<Move> fd = new ArrayList<>();
        for(Move m : moves){
            if(m!=null){
                fd.add(m);
            }
        }

        return fd;
    }

    public ArrayList<Move> generateSlidingMoves(int start,int piece,boolean color1,Integer[] grid){

        int startDir = piece%4 == 0?4:0;
        int endDir = piece%6 == 0?4:8;


        ArrayList<Move> moves = new ArrayList<>();
       // int color = (color1 < 0) ? -1 : 1;


        for(int direction =startDir;direction<endDir;direction++){
            for(int n = 0;n<numToEndge[start][direction];n++){
            int target = start+directionOffset[direction]* (n+1);
            int pieceOnTargetSq = grid[target];

            if(PiecesInt.getColor(pieceOnTargetSq)== color1 && pieceOnTargetSq != PiecesInt.None1){
                break;

            }

                moves.add(new Move(start,target,piece,null));

                if (PiecesInt.getColor(pieceOnTargetSq) != color1 && pieceOnTargetSq !=PiecesInt.None1) {
                    break;
                }
            }


        }
    return moves;
    }


    public boolean isChecked(Integer[] grid,boolean color, Move enpasant){
        boolean ischecked= false;



        Integer[] gridCopy = new Integer[64];

        for(int i = 0;i<64;i++){
            gridCopy[i]= grid[i];
        }

        var moves = generateMoves1(!color,gridCopy,enpasant,false);
        int king = color?PiecesInt.King1:PiecesInt.King2;
        for(Move mv: moves){
            if(gridCopy[mv.target] == king)
                ischecked = true;
        }



        return ischecked;
    }






    public boolean isValidMove(Integer[] grid, int move,int piece,int start,boolean color,Move enpasant){
        Integer[] gridCopy = new Integer[64];



        for(int i = 0;i<64;i++){
            gridCopy[i]= grid[i];
        }

        int coll = color?-8:+8;
        if(piece== PiecesInt.Pawn1 || piece== PiecesInt.Pawn2){
            if(Math.abs(move - start)!=8 && grid[move] ==PiecesInt.None1){


                gridCopy[move] = piece;
                gridCopy[start] = PiecesInt.None1;
                gridCopy[move+coll] = PiecesInt.None1;
            }else{
                gridCopy[move] = piece;
                gridCopy[start]= PiecesInt.None1;
            }

        }else{

            gridCopy[move] = piece;
        gridCopy[start]= PiecesInt.None1;
        }
        ///asndjkasdkbflnaerdfjklncjnijnfodlkfajsild
        var moves = generateMoves1(!color,gridCopy,enpasant,false);
        int king = color?PiecesInt.King1:PiecesInt.King2;
        for(Move mv: moves){
            if(gridCopy[mv.target] == king)
                return false;
        }

        return true;
    }

    public ArrayList<Move> generateKnightMoves(int start,int piece,boolean color1,Integer[] grid){
        int[] knightOffsets = {-17, -15, -10, -6, 6, 10, 15, 17};

        ArrayList<Move> moves = new ArrayList<>();
        for (int offset : knightOffsets) {
            int target = start + offset;

              if (target >= 0 && target < 64) {
              if ((grid[target] == 0) || color1!= PiecesInt.getColor(grid[target])) {
                   int targetRow = target / 8;
                   int targetCol = target % 8;

                int squareRow = start / 8;
                int squareCol = start % 8;

                if (Math.abs(squareCol - targetCol) == 1 && Math.abs(squareRow - targetRow) == 2) {
                    moves.add(new Move(start,target,piece,null));
                } else if (Math.abs(squareCol - targetCol) == 2 && Math.abs(squareRow - targetRow) == 1) {
                    moves.add(new Move(start,target,piece,null));
                }
            }}
        }
        return moves;
    }


    public ArrayList<Move> generateKingMoves(int start,int piece,boolean color1,Integer[] grid){
        ArrayList<Move> moves = new ArrayList<>();

        int[] up = {7,8,9,-7,-8,-9};
        int[] down = {-7,-8,-9};
        int left = -1;
        int right = 1;

// Check if the piece is on the left edge
        boolean isLeftEdge = (start % 8 == 0);

// Check if the piece is on the right edge
        boolean isRightEdge = (start % 8 == 7);

// Check possible moves
        if (!isRightEdge && (start + 1) < 64) {
            // Move to the right
            if(grid[start+1] == PiecesInt.None1|| PiecesInt.getColor(grid[start+1])!= color1){
            moves.add(new Move(start, start + 1, grid[start],null));
            }
        }
        if (!isLeftEdge && (start - 1) >= 0) {  // Move to the left
            if(grid[start-1] == PiecesInt.None1|| PiecesInt.getColor(grid[start-1])!= color1){
                moves.add(new Move(start, start - 1, grid[start],null));
        }
        }
        if ((start + 8) < 64) {  // Move downward
            if(grid[start+8] == PiecesInt.None1|| PiecesInt.getColor(grid[start+8])!= color1) {
                moves.add(new Move(start, start + 8, grid[start],null));
            }}
        if ((start - 8) >= 0) {  // Move upward
            if(grid[start-8] == PiecesInt.None1|| PiecesInt.getColor(grid[start-8])!= color1){

                moves.add(new Move(start, start - 8, grid[start],null));
        }
        }
        if (!isLeftEdge && (start + 7) < 64) {  // Move down-left
            if(grid[start+7] == PiecesInt.None1|| PiecesInt.getColor(grid[start+7])!= color1){
                moves.add(new Move(start, start + 7, grid[start],null));
        }}
        if (!isRightEdge && (start + 9) < 64) {  // Move down-right
            if(grid[start+9] == PiecesInt.None1|| PiecesInt.getColor(grid[start+9])!= color1){

                moves.add(new Move(start, start + 9, grid[start],null));
        }}
        if (!isLeftEdge && (start - 9) >= 0) {  // Move up-left
            if(grid[start-9] == PiecesInt.None1|| PiecesInt.getColor(grid[start-9])!= color1){

                moves.add(new Move(start, start - 9, grid[start],null));
        }}
        if (!isRightEdge && (start - 7) >= 0) {  // Move up-right
            if (grid[start  -7] == PiecesInt.None1 || PiecesInt.getColor(grid[start -7]) != color1) {

                moves.add(new Move(start, start - 7, grid[start],null));
            }
        }





return moves;

    }


    public ArrayList<Move> generatePawnMoves(int start,int piece,boolean color1,Integer[] grid,Move move,boolean moveIf){

        int twoSquareB = color1? 7: 47;
        int twoSquareE = color1? 16: 56;
        int twoSquareMove = color1?16:-16;
        int oneSquare = color1?8:-8;
        int captureLeft = color1?7:-9;
        int moveToSide = color1? 1:-1;
        int pawn = color1? PiecesInt.Pawn2:PiecesInt.Pawn1;
        int captureRight = color1?9:-7;

        int dontCountB = color1?55:-1;
        int dontCountE = color1?64:8;

        ArrayList<Move> moves = new ArrayList<>();



    if (start > twoSquareB && start < twoSquareE && grid[start + twoSquareMove] == 0) {
        if(grid[start + oneSquare]==PiecesInt.None1){
        moves.add(new Move(start, (start + twoSquareMove), piece,null));
        }
    }
    if(start+oneSquare <64 && start+oneSquare>-1){
    if (grid[start + oneSquare] == 0) {
        if(piece>0){

            if(start <56 && start >47){
                moves.add(new Move(start,start + oneSquare, piece,2));
                moves.add(new Move(start,start + oneSquare, piece,3));
                moves.add(new Move(start,start + oneSquare, piece,4));
                moves.add(new Move(start,start + oneSquare, piece,6));
            }else{
                moves.add(new Move(start, (start + oneSquare), piece,null));

            }

        }else{
            if(start <16 && start >7){
                moves.add(new Move(start,start + oneSquare, piece,-2));
                moves.add(new Move(start,start + oneSquare, piece,-3));
                moves.add(new Move(start,start + oneSquare, piece,-4));
                moves.add(new Move(start,start + oneSquare, piece,-6));

            }
            else{
                moves.add(new Move(start, (start + oneSquare), piece,null));

            }

        }


       // moves.add(new Move(start, (start + oneSquare), piece,null));

    }



    }
    if (start % 8 != 7) {
        if(start+oneSquare <64 && start+oneSquare>-1){


            if(moveIf){
                if(grid[start + captureRight] == 0){
                    moves.add(new Move(start,(start + captureRight), piece,null));
                }
            }
        if (PiecesInt.getColor(grid[start + captureRight]) != color1 && grid[start + captureRight] != 0) {




            if(piece>0){
                if(start <56 && start >47){
                    moves.add(new Move(start,start + captureRight, piece,2));
                    moves.add(new Move(start,start + captureRight, piece,3));
                    moves.add(new Move(start,start + captureRight, piece,4));
                    moves.add(new Move(start,start + captureRight, piece,6));
                }else{
                    moves.add(new Move(start, (start + captureRight), piece,null));

                }

            }else{
                if(start <16 && start >7){
                    moves.add(new Move(start,start + captureRight, piece,-2));
                    moves.add(new Move(start,start + captureRight, piece,-3));
                    moves.add(new Move(start,start + captureRight, piece,-4));
                    moves.add(new Move(start,start + captureRight, piece,-6));

                }
                else{
                    moves.add(new Move(start, (start + captureRight), piece,null));

                }
            }





        }
        if(move !=null)
            if(grid[start+1] == pawn && (move.target-1) == start && Math.abs(move.start -move.target)==16){


                moves.add(new Move(start, (start + captureRight), piece,null));
            }


        }
    }
    if (start % 8 != 0) {
        if(start+oneSquare <64 && start+oneSquare>-1){
            if(moveIf){
                if( grid[start + captureLeft] == 0){
                    moves.add(new Move(start,(start + captureLeft), piece,null));
                }
            }
        if (PiecesInt.getColor(grid[start + captureLeft]) != color1 && grid[start + captureLeft] != 0) {




            if(piece>0){
                if(start <56 && start >47){
                    moves.add(new Move(start,(start + captureLeft), piece,2));
                    moves.add(new Move(start,(start + captureLeft), piece,3));
                    moves.add(new Move(start,(start + captureLeft), piece,4));
                    moves.add(new Move(start,(start + captureLeft), piece,6));
                }else{

                    moves.add(new Move(start, (start + captureLeft), piece,null));

                }

            }else{
                if(start <16 && start >7){
                    moves.add(new Move(start,(start + captureLeft), piece,-2));
                    moves.add(new Move(start,(start + captureLeft), piece,-3));
                    moves.add(new Move(start,(start + captureLeft), piece,-4));
                    moves.add(new Move(start,(start + captureLeft), piece,-6));

                }
                else{

                    moves.add(new Move(start, (start + captureLeft), piece,null));

                }

            }












           // moves.add(new Move(start, (start + captureLeft), piece,null));
        }
            if(move !=null) {
                if (grid[start - 1] == pawn && move.target == (start - 1) && Math.abs(move.start -move.target)==16) {

                    moves.add(new Move(start, (start + captureLeft), piece,null));
                }
            }


        }
    }




        return moves;

    }


    public ArrayList<Move> castling(int start,int piece,boolean color1,Integer[] grid,boolean[] boolCastling){
/*
        if(color1){
            if(boolCastling[0]){
                ArrayList<Move> mv =  new ArrayList<>();
                return mv;
            }

        }else{
            if(boolCastling[3]){
                ArrayList<Move> mv =  new ArrayList<>();
                return mv;
            }
        }

 */




        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        int fse = 0;
        int fe = 0;
        int fn = 0;
        int ss = 0;
        int st = 0;
        int so = 0;
        ArrayList<Move> moves = new ArrayList<>();
        int begin = color1?4:60;
        if(start !=begin){
            return moves;
        }
        var moves1 = generateMoves1(!color1,grid,null,true);
        for(Move m :moves1){
            switch(m.target){
                case 1: one = 1;
                break;
                case 2: two = 1;
                break;
                case 3: three =1;
                break;
                case 4: four =1;
                break;
                case 5: five =1;
                    break;
                case 6: six =1;
                    break;
                case 57: fse =1;
                    break;
                case 58: fe =1;
                    break;
                case 59: fn =1;
                    break;
                case 60: ss =1;
                    break;
                case 62: st =1;
                    break;
                case 61: so =1;
                    break;
            }


        }
        // Check for castling
        if (color1) {  // White castling
            if (!boolCastling[0]) {
               // if(grid[1]==PiecesInt.None1 && grid[2]==PiecesInt.None1&&grid[3]==PiecesInt.None1  )
                if (!boolCastling[1] && grid[0] == PiecesInt.Rook1 && grid[1] == 0 && grid[2] == 0 && grid[3] == 0) {
                    // Check if the king's path (2 and 3) is under attack (implement this check based on your engine)
                    if (one ==0 && two == 0 && three ==0&& four==0) {
                        moves.add(new Move(start, 2, piece,null));  // Left castling for white
                    }
                }
                if (!boolCastling[2] && grid[7] == PiecesInt.Rook1 && grid[5] == 0 && grid[6] == 0) {
                    if (four ==0 && five == 0&& six ==0) {
                        moves.add(new Move(start, 6, piece,null));  // Right castling for white
                    }
                }
            }






        } else {  // Black castling
            if (!boolCastling[3]) {
                if (!boolCastling[4] && grid[56] == PiecesInt.Rook2 && grid[57] == 0 && grid[58] == 0 && grid[59] == 0) {
                    if (fe ==0 && fn == 0 && ss ==0) {
                        moves.add(new Move(start, 58, piece,null));  // Left castling for black
                    }
                }
                if (!boolCastling[5] && grid[63] == PiecesInt.Rook2 && grid[61] == 0 && grid[62] == 0) {
                    if ( ss ==0 && so ==0 && st ==0) {
                        moves.add(new Move(start, 62, piece,null));  // Right castling for black
                    }
                }
            }
        }


return moves;

    }











    }









