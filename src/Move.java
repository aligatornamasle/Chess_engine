public class Move {
   int start;
   int target;
   int piece;
   Integer promotion;
   int captureScore_;
   boolean repetitive = false;

    public Move(int start, int target,int piece,Integer promotion,int captureScore_) {
        this.start = start;
        this.target = target;
        this.piece=piece;
        this.promotion = promotion;
        this.captureScore_ = captureScore_;
    }

}
