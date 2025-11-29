// ThreeThreadSudokuVerifier.java
package Soduku;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreeThreadSudokuVerifier extends BaseSudokuVerifier {
    private static final int NUM_THREADS = 3;
    
    public ThreeThreadSudokuVerifier(int[][] board) {
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
        
        // Thread 1: Check all rows
        executor.submit(() -> {
            for (int i = 0; i < 9; i++) {
                int[] row = new int[9];
                System.arraycopy(board[i], 0, row, 0, 9);
                validateUnit(row, "ROW", i);
            }
        });

        // Thread 2: Check all columns
        executor.submit(() -> {
            for (int j = 0; j < 9; j++) {
                int[] col = new int[9];
                for (int i = 0; i < 9; i++) {
                    col[i] = board[i][j];
                }
                validateUnit(col, "COL", j);
            }
        });

        // Thread 3: Check all boxes
        executor.submit(() -> {
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
        });

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