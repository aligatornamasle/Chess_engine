class PieceMovePair {
    int start;
    int target;
    Move move;
    double score;
    Integer promotion;

    PieceMovePair(int start,int target, Move move, double score,Integer promotion) {
        this.start = start;
        this.target = target;
        this.move = move;
        this.score = score;
        this.promotion = promotion;
    }
}