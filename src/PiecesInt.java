public class PiecesInt {

    public static final int None1 = 0;
    public static final int King1 = 1;
    public static final int Pawn1 = 5;
    public static final int Knight1 = 3;
    public static final int Bishop1 = 4;
    public static final int Rook1 = 6;
    public static final int Queen1 = 2;


    public static final int King2 = -1;
    public static final int Pawn2 = -5;
    public static final int Knight2 = -3;
    public static final int Bishop2 = -4;
    public static final int Rook2 = -6;
    public static final int Queen2 = -2;



public static boolean getColor(int piece){
    if(piece<0){
        return false;
    }
    return true;
}


}
