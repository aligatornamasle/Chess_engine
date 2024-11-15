public class Move {
   int start;
   int target;
   int piece;
   Integer promotion;
   int promote;

    public Move(int start, int target,int piece,Integer promotion) {
        this.start = start;
        this.target = target;
        this.piece=piece;
        this.promotion = promotion;
    }
    public int getPiece(){
        return piece;
    }
    public void setPromote(int promote){
        this.promote = promote;
    }
    public int getPromote(){
        return promote;
    }
}
