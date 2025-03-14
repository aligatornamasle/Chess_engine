
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;


public  class ChessBoard extends JPanel implements MouseListener, MouseMotionListener {
    private final MoveGenerator slPieces = new MoveGenerator();
    private final int size;
    public Boolean turn = true;
    private int movesPlayed = 0;
    public boolean isBoardFlipped = false; // Flag to track board orientation
    public int halfMoveClock = 0;
    public int fullMoveNumber =0;
    private Search search;
    public ArrayList<String> positionHistory = new ArrayList<>();
    private final ArrayList<Move> repeatingMoves = new ArrayList<>();
    private final MiniMaxAlgorithm minimax;
    private int depth = 3;
    private int time = 1000;

    private MinimaxWorker currentWorker;


    public ChessBoard(int size) {
        this.size = size;
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size)); // Prevent the chessboard from shrinking
        addMouseListener(this);
        addMouseMotionListener(this);
        slPieces.movesForSlidingPieces();
        search = new Search();
        minimax = new MiniMaxAlgorithm();
    }

    public void newSearch() {
        this.search = new Search();
    }
    public void worker(Move move) {

        if (currentWorker != null && !currentWorker.isDone()) {
            currentWorker.cancel(true);
            currentWorker = null;
        }

        // Start new worker
        currentWorker = new MinimaxWorker(
                search, this.minimax, this,
                search.isCastling, move, repeatingMoves,
                 depth, time, isBoardFlipped
        );
        currentWorker.addPropertyChangeListener(evt -> {
            if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                currentWorker = null;
            }
        });
        currentWorker.execute();
    }

    public void setupGame(String fen, int depth, int time, boolean randomNoise) {
        positionHistory.clear();
        halfMoveClock = 0;
        this.depth = Math.max(depth, 1);
        this.time = Math.max(time, 10);
        this.repeatingMoves.clear();
        ChessHeuristics.randomNoise = randomNoise;
        if(FenBoardUtilities.setupBoardFromFEN(fen, search.board)){

        String activeColor = "";
        String[] parts = fen.split("\\s+"); // Split by whitespace
        if(parts.length >1) {
            activeColor = parts[1];
        }
        if(parts.length >2) {
            String castlingRights = parts[2];
            search.isCastling = FenBoardUtilities.getCastlingBooleanArray(castlingRights);
        }
        if(parts.length >3) {
            String enpasantPart = parts[3];
            int target = FenBoardUtilities.convertEnPassantToIndex(enpasantPart);
            Move move;
            if(target> -1) {
                if(target<=31){
                    move = new Move(target-8,target+8,5,0,0);
                }else{
                    move = new Move(target+8,target-8,-5,0,0);
                }
                search.previosMove = move;

            }
        }
        if(parts.length >5) {
             halfMoveClock = Integer.parseInt(parts[4]); // 5th part (index 4)
             fullMoveNumber = Integer.parseInt(parts[5]);

        }
        // Get the second field (index 1)
        if(!activeColor.equals("w")&&!activeColor.equals("b")){
            activeColor = "w";
        }
        search.gameFlow = true;
        this.repaint();
        if(!isBoardFlipped) {
            if(activeColor.equals("w")){
                turn = true;
            }else{
                turn = false;
                workerInitStarting();
            }
        }else{
            if(activeColor.equals("w")){
                this.turn = false;
                workerInitStarting();
            }else{
                turn = true;
            }
        }
        }else {
            FenBoardUtilities.setupBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", search.board);
        }
    }

    public void workerInitStarting(){
        if (currentWorker != null && !currentWorker.isDone()) {
            currentWorker.cancel(true); // Forcefully terminate
            currentWorker = null;
        }
        worker(this.search.previosMove);
        movesPlayed++;
    }

    public void toggleBoardOrientation() {
        isBoardFlipped = !isBoardFlipped;
    }

    @Override
    public void paint(Graphics g) {
        int sizeRect = size / 8;
        super.paintComponent(g);

        // Find indices of both kings
        int whiteKingIndex = -1;
        int blackKingIndex = -1;
        for (int i = 0; i < 64; i++) {
            if (search.board[i] == 1) {
                whiteKingIndex = i;
            } else if (search.board[i] == -1) {
                blackKingIndex = i;
            }
        }

// Calculate display positions considering board flip
        int whiteRow = 7 - (whiteKingIndex / 8);
        int whiteCol = whiteKingIndex % 8;
        int blackRow = 7 - (blackKingIndex / 8);
        int blackCol = blackKingIndex % 8;

        if (isBoardFlipped) {
            // Flip the coordinates for rendering
            whiteRow = 7 - whiteRow;
            whiteCol = 7 - whiteCol;
            blackRow = 7 - blackRow;
            blackCol = 7 - blackCol;
        }

// Check if each king is in check
        boolean isWhiteChecked = search.isChecked(search.getBoard(), true);
        boolean isBlackChecked = search.isChecked(search.getBoard(), false);


// Check if there are any legal moves left (checkmate condition)
        if(turn && isBoardFlipped){
            search.makeMove(false, null);

        }
        if(!turn && isBoardFlipped){
            search.makeMove(true, null);

        }
        if(turn && !isBoardFlipped){
            search.makeMove( true, null);

        }
        if(!turn && !isBoardFlipped){
            search.makeMove( false, null);

        }

        boolean whiteHasMoves = search.gameFlow;
        boolean blackHasMoves = search.gameFlow;
        if(Draw.isDraw(turn, halfMoveClock,search,isBoardFlipped,positionHistory)){
            isWhiteChecked = true;
            isBlackChecked = true;
            whiteHasMoves = false;
            blackHasMoves = false;
            search.gameFlow = false;
        }

        boolean whiteCheckmate = isWhiteChecked && !whiteHasMoves;
        boolean blackCheckmate = isBlackChecked && !blackHasMoves;


// Paint the board
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // Check for checkmated kings first
                if (whiteCheckmate && i == whiteRow && j == whiteCol) {
                    g.setColor(new Color(139, 0, 0)); // Dark red for white checkmate
                } else if (blackCheckmate && i == blackRow && j == blackCol) {
                    g.setColor(new Color(139, 0, 0)); // Dark red for black checkmate
                } else if (isWhiteChecked && i == whiteRow && j == whiteCol) {
                    g.setColor(new Color(255, 0, 0)); // Red if white is in check (not checkmate)
                } else if (isBlackChecked && i == blackRow && j == blackCol) {
                    g.setColor(new Color(255, 0, 0)); // Red if black is in check (not checkmate)
                } else {
                    // Normal tile colors
                    if ((i + j) % 2 == 0) {
                        g.setColor(new Color(210, 180, 140));
                    } else {
                        g.setColor(new Color(75, 61, 48));
                    }
                }
                g.fillRect(j * sizeRect, getInsets().top + i * sizeRect, sizeRect, sizeRect);
            }
        }

        for (int i = 0; i < 64; i++) {
            int a = i / 8;
            int b = i % 8;
            if (isBoardFlipped) {
                a = 7 - a;
                b = 7 - b;
            }
            Image image;
            switch (search.getBoard()[i]) {
                case 1:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/White-king.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case 5:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/White-pawn.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case 4:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/White-bishop.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case 3:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/White-knight.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case 6:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/White-rook.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case 2:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/White-queen.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case -1:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/Black-king.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case -5:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/Black-pawn.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case -4:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/Black-bishop.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case -3:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/Black-knight.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case -6:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/Black-rook.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
                case -2:
                    image = Toolkit.getDefaultToolkit().getImage("src/resources_chess_pieces/Black-queen.png");
                    g.drawImage(image, sizeRect * b, (7 - a) * sizeRect, sizeRect, sizeRect, this);
                    break;
            }
        }

        g.setColor(new Color(255, 120, 122));
        if ((search.selectedPieceForMove && search.selectedPiece > 0 ) ||(search.selectedPieceForMove &&  (isBoardFlipped && search.selectedPiece < 0))) {
            ArrayList<Move> moves = new ArrayList<>();
            switch (Math.abs(search.selectedPiece)) {
                case PiecesInt.King1 -> moves = slPieces.generateKingMoves(search.selectedCoordination,!isBoardFlipped, search.board);
                case PiecesInt.Queen1, PiecesInt.Bishop1, PiecesInt.Rook1 -> moves = slPieces.generateSlidingMoves(search.selectedCoordination, search.selectedPiece, !isBoardFlipped, search.board);
                case PiecesInt.Knight1 -> moves = slPieces.generateKnightMoves(search.selectedCoordination, search.selectedPiece, !isBoardFlipped, search.board);
                case PiecesInt.Pawn1 -> moves = slPieces.generatePawnMoves(search.selectedCoordination, search.selectedPiece, !isBoardFlipped, search.board, search.previosMove, false);
            }
            if (Math.abs(search.selectedPiece) == PiecesInt.King1) {
                var castling = slPieces.castling(search.selectedCoordination, search.selectedPiece, !isBoardFlipped, search.board, Arrays.copyOf(search.isCastling, search.isCastling.length));
                moves.addAll(castling);
            }
            ArrayList<Move> mv = slPieces.movesForDisplay(moves, search.board, !isBoardFlipped);

            int row;
            int col;
            for (Move move : mv) {
                row = 7 - (move.target / 8);
                col = (move.target % 8);
                if (isBoardFlipped) {
                    row = 7 - row;
                    col = 7 - col;
                }

                g.fillRoundRect((col * sizeRect) + 15, (getInsets().top + row * sizeRect) + 15, 20, 20, 50, 50);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        if (!search.gameFlow) {
            return; // Exit if the game is not in progress
        }

        Point point = e.getPoint();
        int sizeRect = size / 8;

        // Check if the click is within the bounds of the chessboard
        if (point.x < 0 || point.x >= size || point.y < 0 || point.y >= size) {
            return; // Ignore clicks outside the chessboard
        }


        int row = 7 - (point.y / sizeRect); // Calculate the row (0-7)
        int col = point.x / sizeRect; // Calculate the column (0-7)


        if (isBoardFlipped) {
            row = 7 - row;
            col = 7- col;
        }

        int index = row * 8 + col; // Calculate the index (0-63)

        try {
            if (search.selectedPieceForMove && turn) {
                Move move;
                if(isBoardFlipped){
                   move = new Move(search.selectedCoordination, index, search.selectedPiece, -2, 0);

                }else{
                move = new Move(search.selectedCoordination, index, search.selectedPiece, 2, 0);
                }

                if (search.makeMove(!isBoardFlipped, move)) {
                    System.out.println("{" + move.start + "," + move.target + "}" + move.piece + "," + move.captureScore_);
                    movesPlayed++;
                    repeatingMoves.add(move);
                    search.selectedPieceForMove = false;
                    this.repaint();
                    if(Math.abs(move.captureScore_) > 0 ){
                        SoundPlayer.sound("src/sounds/capture.wav");
                    }else{
                        SoundPlayer.sound("src/sounds/move.wav");
                    }
                    halfMoveClock =  Draw.fiftyMoveRule(move, halfMoveClock);
                    search.previosMove = new Move(search.selectedCoordination, index, search.selectedPiece, 2, 0);
                    positionHistory.add(FenBoardUtilities.getBoardPosition(search.board,turn,search.isCastling));
                    turn = !turn;

                    if(!Draw.isDraw(turn, halfMoveClock,search,isBoardFlipped,positionHistory) && search.gameFlow) {
                        worker(search.previosMove);
                    }
                    movesPlayed++;
                    System.out.println(halfMoveClock);
                }
            }

            if (turn && ((search.board[index] > 0) || (isBoardFlipped && search.board[index] < 0))) {
                if (search.selectPiece(index, !isBoardFlipped)) {
                    search.selectedPieceForMove = true;
                    this.repaint();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}