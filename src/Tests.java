

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**

public class Tests {

@Test
void testCastling(){
    GameGrid game = new GameGrid();
    game.setFenCheckDepth("4k3/4p3/8/8/8/8/8/R3K2R w KQha - 0 1",5,"white");
assertEquals( 672733,game.test);
}
@Test
void testPromotion(){

    GameGrid game = new GameGrid();
    game.setFenCheckDepth("8/3p4/5k2/8/8/8/1p2PPPN/5K2 w - - 0 1",6,"white");
    assertEquals( 1825373,game.test);
}


    @Test
    void TestingPositions(){
        GameGrid game = new GameGrid();
        long startTime = System.nanoTime();
        game.setFenCheckDepth("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",4,"white");
        assertEquals(197281,game.test);
        long endTime = System.nanoTime();

        // Calculate the elapsed time in milliseconds and seconds
        long elapsedTimeInNano = endTime - startTime;
        double elapsedTimeInMilli = elapsedTimeInNano / 1_000_000.0; // Convert to milliseconds
        double elapsedTimeInSeconds = elapsedTimeInMilli / 1000.0;    // Convert to seconds

        System.out.println("Elapsed time in nanoseconds: " + elapsedTimeInNano + " ns");
        System.out.println("Elapsed time in milliseconds: " + elapsedTimeInMilli + " ms");
        System.out.println("Elapsed time in seconds: " + elapsedTimeInSeconds + " s");


    }
    @Test
    void TestingPositions1()
    {
        GameGrid game = new GameGrid();
        game.setFenCheckDepth("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ",3,"white");
        assertEquals(  97862,game.test);


    }   @Test
    void TestingPositions12()
    {
        GameGrid game = new GameGrid();
        game.setFenCheckDepth("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ",3,"white");

        MiniMaxAlgorithm minimax = new MiniMaxAlgorithm(game);
        long startTime = System.nanoTime();
        minimax.createTree(game.getGrid(), 4, false,0);
        System.out.println(minimax.test);
        long endTime = System.nanoTime();
        long elapsedTimeInNano = endTime - startTime;
        double elapsedTimeInMilli = elapsedTimeInNano / 1_000_000.0; // Convert to milliseconds
        double elapsedTimeInSeconds = elapsedTimeInMilli / 1000.0;    // Convert to seconds

        System.out.println("Elapsed time in nanoseconds: " + elapsedTimeInNano + " ns");
        System.out.println("Elapsed time in milliseconds: " + elapsedTimeInMilli + " ms");
        System.out.println("Elapsed time in seconds: " + elapsedTimeInSeconds + " s");

       //assertEquals(  97862,game.test);


    }



/*
    @Test
    void TestingPositions2()
    {
        GameGrid game = new GameGrid();
        long startTime = System.nanoTime();
        game.setFenCheckDepth("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ",6,"white");
        long endTime = System.nanoTime();
        assertEquals(  11030083,game.test);
        long elapsedTimeInNano = endTime - startTime;
        double elapsedTimeInMilli = elapsedTimeInNano / 1_000_000.0; // Convert to milliseconds
        double elapsedTimeInSeconds = elapsedTimeInMilli / 1000.0;    // Convert to seconds

        System.out.println("Elapsed time in nanoseconds: " + elapsedTimeInNano + " ns");
        System.out.println("Elapsed time in milliseconds: " + elapsedTimeInMilli + " ms");
        System.out.println("Elapsed time in seconds: " + elapsedTimeInSeconds + " s");
    }




    @Test
    void TestingPositions20()
    {GameGrid game = new GameGrid();
        MiniMaxAlgorithm miniMaxAlgorithm = new MiniMaxAlgorithm(game);
        //GameGrid game = new GameGrid();
        long startTime = System.nanoTime();
        game.setupBoardFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ");
        miniMaxAlgorithm.createTree(game.grid,8,true,0);
        //game.setFenCheckDepth("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ",6,"white");
        long endTime = System.nanoTime();
        //assertEquals(  11030083,game.test);
        long elapsedTimeInNano = endTime - startTime;
        double elapsedTimeInMilli = elapsedTimeInNano / 1_000_000.0; // Convert to milliseconds
        double elapsedTimeInSeconds = elapsedTimeInMilli / 1000.0;    // Convert to seconds

        System.out.println("Elapsed time in nanoseconds: " + elapsedTimeInNano + " ns");
        System.out.println("Elapsed time in milliseconds: " + elapsedTimeInMilli + " ms");
        System.out.println("Elapsed time in seconds: " + elapsedTimeInSeconds + " s");
    }


    @Test
    void TestingPositions3()
    {
        GameGrid game = new GameGrid();
        game.setFenCheckDepth("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1",4,"white");
        assertEquals(  422333,game.test);

    }
    @Test
    void TestingPositions4()
    {
        GameGrid game = new GameGrid();
        game.setFenCheckDepth("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8 ",3,"white");
        assertEquals(  62379,game.test);

    }
    @Test
    void TestingPositions5()
    {
        GameGrid game = new GameGrid();
        game.setFenCheckDepth("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10",3,"white");

        assertEquals(  89890,game.test);

    }
    @Test
    void TestingPositions6()
    {
        GameGrid game = new GameGrid();
        game.setFenCheckDepth("8/k1P5/8/1K6/8/8/8/8 w - - 0 1",7,"white");

        assertEquals(  567584,game.test);

    }


}
*/