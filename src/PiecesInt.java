public class PiecesInt {
//representation of white pieces
    public static final int None1 = 0;
    public static final int King1 = 1;
    public static final int Pawn1 = 5;
    public static final int Knight1 = 3;
    public static final int Bishop1 = 4;
    public static final int Rook1 = 6;
    public static final int Queen1 = 2;

//representation of black pieces
    public static final int King2 = -1;
    public static final int Pawn2 = -5;
    public static final int Knight2 = -3;
    public static final int Bishop2 = -4;
    public static final int Rook2 = -6;
    public static final int Queen2 = -2;



    //checking which piece is what color black -false and white-true, whether we are using this method, we must also
    //check if the piece is not None1 because None1 is not piece, but it anyway returns false as it is black piece
public static boolean getColor(int piece){
    return piece >= 0;
}


}
