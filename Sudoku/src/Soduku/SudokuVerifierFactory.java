// SudokuVerifierFactory.java
package Soduku;

public class SudokuVerifierFactory {
    public static final int SEQUENTIAL = 0;
    public static final int THREE_THREADS = 3;
    public static final int TWENTY_SEVEN_THREADS = 27;

    public static SudokuVerifier createVerifier(int mode, int[][] board) {
        switch (mode) {
            case SEQUENTIAL:
                return new SequentialSudokuVerifier(board);
            case THREE_THREADS:
                return new ThreeThreadSudokuVerifier(board);
            case TWENTY_SEVEN_THREADS:
                return new TwentySevenThreadSudokuVerifier(board);
            default:
                throw new IllegalArgumentException("Invalid mode. Use 0, 3, or 27");
        }
    }
}