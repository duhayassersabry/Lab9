// SequentialSudokuVerifier.java
package Soduku;

public class SequentialSudokuVerifier extends BaseSudokuVerifier {
    
    public SequentialSudokuVerifier(int[][] board) {
        super(board);
    }

    @Override
    public boolean isValid() {
        if (!processed) {
            processBoard();
        }
        return validationResults.isEmpty();
    }

    @Override
    public void processBoard() {
        // Check all rows
        for (int i = 0; i < 9; i++) {
            int[] row = new int[9];
            System.arraycopy(board[i], 0, row, 0, 9);
            validateUnit(row, "ROW", i);
        }

        // Check all columns
        for (int j = 0; j < 9; j++) {
            int[] col = new int[9];
            for (int i = 0; i < 9; i++) {
                col[i] = board[i][j];
            }
            validateUnit(col, "COL", j);
        }

        // Check all boxes
        for (int box = 0; box < 9; box++) {
            int[] boxValues = new int[9];
            int rowOffset = (box / 3) * 3;
            int colOffset = (box % 3) * 3;
            int idx = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    boxValues[idx++] = board[rowOffset + i][colOffset + j];
                }
            }
            validateUnit(boxValues, "BOX", box);
        }
        
        processed = true;
    }
}