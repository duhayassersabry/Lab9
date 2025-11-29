// TwentySevenThreadSudokuVerifier.java
package Soduku;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TwentySevenThreadSudokuVerifier extends BaseSudokuVerifier {
    private static final int NUM_THREADS = 27;
    
    public TwentySevenThreadSudokuVerifier(int[][] board) {
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
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // 9 threads for rows
        for (int i = 0; i < 9; i++) {
            final int row = i;
            executor.submit(() -> {
                int[] rowData = new int[9];
                System.arraycopy(board[row], 0, rowData, 0, 9);
                validateUnit(rowData, "ROW", row);
            });
        }

        // 9 threads for columns
        for (int j = 0; j < 9; j++) {
            final int col = j;
            executor.submit(() -> {
                int[] colData = new int[9];
                for (int i = 0; i < 9; i++) {
                    colData[i] = board[i][col];
                }
                validateUnit(colData, "COL", col);
            });
        }

        // 9 threads for boxes
        for (int box = 0; box < 9; box++) {
            final int boxNum = box;
            executor.submit(() -> {
                int[] boxValues = new int[9];
                int rowOffset = (boxNum / 3) * 3;
                int colOffset = (boxNum % 3) * 3;
                int idx = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        boxValues[idx++] = board[rowOffset + i][colOffset + j];
                    }
                }
                validateUnit(boxValues, "BOX", boxNum);
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        processed = true;
    }
}