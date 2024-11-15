import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class Canvass extends JFrame {
    private final static int size = 400;

///creating JFrame and adding JPanel(chessboard) into it
public Canvass(){
    setTitle("Chess");
    setSize(size,size);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ChessBoard chessBoard = new ChessBoard();
    add(chessBoard);
    pack();
    setVisible(true);

    //chessBoard.test();
}

///creating JPanel that is chessboard
public static class ChessBoard extends JPanel implements MouseListener, MouseMotionListener {
   // GameGrid gameGrid = new GameGrid();

    Point point;
    int col;
    int row;
  //  Piece piece;
   // MiniMaxAlgorithm minimax = new MiniMaxAlgorithm(gameGrid);
    boolean selectedPiece =false;
    Boolean turn = true;
    int movesPlayed = 0;
    Integer[] grid;
    Run run;
    MiniMaxAlgorithm minimax = new MiniMaxAlgorithm(run);
    public ChessBoard(){
        //gameGrid.setupBoardFromFEN("4k3/1r6/2r5/8/8/K7/8/8 w - - 0 1");
        //gameGrid.setupBoardFromFEN("4k3/8/2r5/8/8/K7/8/8 w HAha - 0 1");
       // gameGrid.setupBoardFromFEN("8/8/5p2/1P1K1k2/8/2r5/8/7R w - - 0 0");
     //   gameGrid.setupBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        //gameGrid.setupBoardFromFEN("4k3/4p3/8/8/8/8/8/R3K2R w KQha - 0 1");
        /// endgame -
        this.grid = grid;
        run = new Run();


        //run.setupBoardFromFEN("8/8/3k4/8/8/8/3K4/8 w - - 0 1");
       // run.setupBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
run.setupBoardFromFEN("4k3/6p1/8/8/8/PP6/KPp3N1/NP6 w HAha - 0 1");
        setPreferredSize(new Dimension(size,size));
        addMouseListener(this);
        addMouseMotionListener(this);


    }

    @Override
    public void paint(Graphics g)
    {
        int sizeRect = size/8;
        super.paintComponent(g);
        for(int i = 0;i<8;++i){
            for(int j = 0;j<8;++j){
                if((i+j)%2 ==0){
                    g.setColor(new Color(210,180,140));
                } else{

                    g.setColor(new Color(75,61,48));

                }

                g.fillRect(j* sizeRect,  getInsets().top + i* sizeRect, sizeRect, sizeRect);
            }
        }

        g.setColor(new Color(255, 160, 122)); // Yellow color for highlighting

        for(int i =0;i<64;i++){
            int a = i / 8;
            int b = i % 8;
            Image image;
            switch (run.getTestGrid()[i]){
                case 1:
                     image = Toolkit.getDefaultToolkit().getImage("White-king.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case 5:
                     image = Toolkit.getDefaultToolkit().getImage("White-pawn.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case 4:
                    image = Toolkit.getDefaultToolkit().getImage("White-bishop.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case 3:
                    image = Toolkit.getDefaultToolkit().getImage("White-knight.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case 6:
                    image = Toolkit.getDefaultToolkit().getImage("White-rook.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case 2:
                    image = Toolkit.getDefaultToolkit().getImage("White-queen.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;

                case -1:
                    image = Toolkit.getDefaultToolkit().getImage("Black-king.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case -5:
                    image = Toolkit.getDefaultToolkit().getImage("Black-pawn.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case -4:
                    image = Toolkit.getDefaultToolkit().getImage("Black-bishop.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case -3:
                    image = Toolkit.getDefaultToolkit().getImage("Black-knight.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case -6:
                    image = Toolkit.getDefaultToolkit().getImage("Black-rook.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;
                case -2:
                    image = Toolkit.getDefaultToolkit().getImage("Black-queen.png");
                    g.drawImage(image, sizeRect * b, (7-a) * sizeRect, sizeRect, sizeRect, this);

                    break;




            }




        }
       /* if(selectedPiece){
            for(int i =0 ;i<8;i++){
                for(int j =0 ;j<8;j++){

                    if(piece instanceof Pawn){
                    if(piece.IsValidMove(j,i,gameGrid.grid)||piece.isCapture(j,i,gameGrid.grid)){

                        g.fillRect(j* sizeRect,  getInsets().top + i* sizeRect, sizeRect, sizeRect);
                    }
                    }else {
                        if(piece.IsValidMove(j,i,gameGrid.grid)&&piece.isCapture(j,i,gameGrid.grid)&&!gameGrid.wouldBeChecked2(j,i,piece,gameGrid.grid)){

                            g.fillRect(j* sizeRect,  getInsets().top + i* sizeRect, sizeRect, sizeRect);
                        }

                    }

                }

            }



        }






        for(int a = 0;a<8;a++){
            for(int b= 0;b<8;b++) {
                if (gameGrid.getPiece(b, a) != null) {
                    Image image = Toolkit.getDefaultToolkit().getImage(gameGrid.getPiece(b, a).getPicture());
                    g.drawImage(image, sizeRect * b, a * sizeRect, sizeRect, sizeRect, this);
                }
            }
        }

        */
    }

    public void mouseDragged(MouseEvent e)
    {

    }

    public void mouseClicked(MouseEvent e) {
        boolean gameFlow = true;
        if (gameFlow) { // Assuming gameFlow represents if the game is ongoing
            point = e.getPoint();
            int sizeRect = size / 8;
            row = 7 - (point.y / sizeRect); // Flip the board's perspective
            col = point.x / sizeRect;
            int index = row * 8 + col;

            try {
                // If a piece is already selected and it's the player's turn
                if (run.selected && turn) { // White's turn is true
                   Move move = new Move(run.selcoor,index,run.selectedPiece,0);
                    if (run.makeMove(index, turn,move)) {
                        movesPlayed++; // Assuming you want to track moves
                        run.selected = false;
                        turn = !turn; // Toggle turn (white <-> black)
                        this.repaint();
                        Move mv = new Move(run.selcoor,index,run.selectedPiece,0);
                        run.previosMove = mv;
                        // Trigger AI/Minimax logic if needed
                        MinimaxWorker worker = new MinimaxWorker(run, this.minimax, this, movesPlayed,run.getIsCastling(),mv);
                        worker.execute();
                        movesPlayed++;
                    }
                }

                // Select a piece if none is currently selected and it's the player's turn
                if (turn) { // White's turn
                    if (run.selectPiece(index, turn)) {
                        run.selected = true;
                        this.repaint();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(); // Log exceptions for debugging
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e){}

}
}






