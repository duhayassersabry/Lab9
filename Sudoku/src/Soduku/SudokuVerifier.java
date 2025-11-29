// SudokuVerifier.java
package Soduku;

import java.util.List;
import java.util.Map;

public interface SudokuVerifier {
    boolean isValid();
    Map<String, List<String>> getValidationResults();
    void processBoard();
    int[][] getBoard();
}