import java.util.Arrays;

/**
 * Transpozičná tabuľka: pamätá si výsledky už prehľadaných pozícií.
 *
 * Pre každú pozíciu ukladá:
 *  - kľúč (Zobrist), aby sa dali odlíšiť rôzne pozície na tom istom mieste v tabuľke,
 *  - hĺbku, do ktorej bola pozícia prehľadaná,
 *  - skóre a jeho typ (presné / dolná hranica / horná hranica),
 *  - najlepší nájdený ťah (na zlepšenie poradia ťahov).
 *
 * Dáta sú v obyčajných poliach (nie v objektoch), aby tabuľka nevytvárala milióny objektov.
 */
public class TranspositionTable {
    public static final byte EXACT = 0;        // skóre je presná hodnota pozície
    public static final byte LOWER_BOUND = 1;  // skutočná hodnota je >= skóre (nastalo orezanie, "fail high")
    public static final byte UPPER_BOUND = 2;  // skutočná hodnota je <= skóre (žiadny ťah neprekonal alfa, "fail low")

    // skóre nad touto hranicou je mat (engine používa ±100000 + hĺbka)
    private static final double MATE_THRESHOLD = 90000;

    private final int mask;
    private final long[] keys;
    private final double[] scores;
    private final int[] depths;
    private final byte[] flags;
    private final int[] moves;

    /** @param sizePowerOfTwo veľkosť tabuľky ako mocnina dvoch, napr. 20 = 1 048 576 položiek (~25 MB) */
    public TranspositionTable(int sizePowerOfTwo) {
        int size = 1 << sizePowerOfTwo;
        mask = size - 1;
        keys = new long[size];
        scores = new double[size];
        depths = new int[size];
        flags = new byte[size];
        moves = new int[size];
    }

    public void clear() {
        Arrays.fill(keys, 0L);
        Arrays.fill(moves, 0);
    }

    private int index(long key) {
        return (int) (key ^ (key >>> 32)) & mask;
    }

    /** Vráti index položky pre danú pozíciu, alebo -1, ak v tabuľke nie je. */
    public int probe(long key) {
        int idx = index(key);
        return keys[idx] == key ? idx : -1;
    }

    public int depthAt(int idx) { return depths[idx]; }
    public byte flagAt(int idx) { return flags[idx]; }
    public int moveAt(int idx)  { return moves[idx]; }

    /**
     * Skóre prepočítané pre uzol, ktorému zostáva 'remainingDepth' hĺbky.
     * Pri bežnom skóre sa nič nemení. Pri mate sa upraví vzdialenosť, lebo engine
     * kóduje mat ako ±(100000 + zostávajúca hĺbka) a tá je v inom uzle iná.
     */
    public double scoreAt(int idx, int remainingDepth) {
        double s = scores[idx];
        if (s > MATE_THRESHOLD) return s + remainingDepth;
        if (s < -MATE_THRESHOLD) return s - remainingDepth;
        return s;
    }

    /** Uloží výsledok prehľadávania (nová položka vždy nahradí starú na tom istom mieste). */
    public void store(long key, int depth, double score, byte flag, int move) {
        int idx = index(key);
        // mat uložíme relatívne k uzlu (bez jeho zostávajúcej hĺbky), pozri scoreAt
        if (score > MATE_THRESHOLD) score -= depth;
        else if (score < -MATE_THRESHOLD) score += depth;
        keys[idx] = key;
        depths[idx] = depth;
        scores[idx] = score;
        flags[idx] = flag;
        moves[idx] = move;
    }

    /** Zakóduje ťah do jedného čísla (0 = žiadny ťah). */
    public static int encodeMove(Move m) {
        if (m == null) return 0;
        int promo = (m.promotion == null) ? 0 : m.promotion + 8;   // -6..6 -> 2..14, bez premeny 0
        return (1 << 20) | (promo << 12) | (m.target << 6) | m.start;
    }

    public static boolean sameMove(int code, Move m) {
        return code != 0 && code == encodeMove(m);
    }
}
