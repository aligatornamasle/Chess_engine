import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestingDepths {

    @Test
    void initialPosition(){
        Search search = new Search();
        search.setFenCheckDepth("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",6,true);
        assertEquals( 119_060_324,search.counterNodes);
    }

    @Test
    void position2(){
        Search search = new Search();
        search.setFenCheckDepth("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ",4,true);
        assertEquals( 4_085_603,search.counterNodes);
    }

    @Test
    void position3(){
        Search search = new Search();
        search.setFenCheckDepth("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ",6,true);
        assertEquals( 11_030_083,search.counterNodes);
    }

    @Test
    void position4(){
        Search search = new Search();
        search.setFenCheckDepth("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1",5,true);
        assertEquals( 15_833_292,search.counterNodes);
    }

    @Test
    void position5(){
        Search search = new Search();
        search.setFenCheckDepth("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8  ",5,true);
        assertEquals( 89_941_194,search.counterNodes);
    }

    @Test
    void position6(){
        Search search = new Search();
        search.setFenCheckDepth("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10",5,true);
        assertEquals( 164_075_551,search.counterNodes);
    }

}
