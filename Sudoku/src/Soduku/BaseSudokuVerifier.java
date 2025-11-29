// BaseSudokuVerifier.java
package Soduku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseSudokuVerifier implements SudokuVerifier {
    protected final int[][] board;
    protected final Map<String, List<String>> validationResults;
    protected boolean processed = false;

    public BaseSudokuVerifier(int[][] board) {
        if (board == null || board.length != 9 || board[0].length != 9) {
            throw new IllegalArgumentException("Invalid Sudoku board size. Must be 9x9.");
        }
        this.board = new int[9][9];
        // Deep copy of the board
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, 9);
        }
        this.validationResults = new HashMap<>();
    }

    @Override
    public abstract boolean isValid();

    @Override
    public Map<String, List<String>> getValidationResults() {
        if (!processed) {
            processBoard();
        }
        return new HashMap<>(validationResults);
    }

    @Override
    public int[][] getBoard() {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

    protected boolean validateUnit(int[] unit, String unitType, int index) {
        boolean[] seen = new boolean[10]; // 1-9
        boolean isValid = true;
        
        for (int i = 0; i < 9; i++) {
            int num = unit[i];
            if (num < 1 || num > 9) {
                addValidationResult(unitType, index, "Invalid number: " + num);
                isValid = false;
            } else if (seen[num]) {
                addValidationResult(unitType, index, "Duplicate: " + num);
                isValid = false;
            }
            seen[num] = true;
        }
        return isValid;
    }

    protected void addValidationResult(String unitType, int index, String message) {
        String key = String.format("%s %d", unitType, index + 1);
        validationResults.computeIfAbsent(key, k -> new ArrayList<>()).add(message);
    }
}