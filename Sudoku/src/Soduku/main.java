
package Soduku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar SudokuVerifier.jar <csv_file> <mode>");
            System.out.println("Modes: 0 (sequential), 3 (3 threads), 27 (27 threads)");
            System.exit(1);
        }

        String csvFile = args[0];
        int mode;
        try {
            mode = Integer.parseInt(args[1]);
            if (mode != 0 && mode != 3 && mode != 27) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid mode. Please use 0, 3, or 27");
            System.exit(1);
            return;
        }

        try {
            int[][] board = readBoardFromCSV(csvFile);
            SudokuVerifier verifier = SudokuVerifierFactory.createVerifier(mode, board);
            verifier.processBoard();

            if (verifier.isValid()) {
                System.out.println("VALID");
            } else {
                System.out.println("INVALID");
                Map<String, List<String>> results = verifier.getValidationResults();
                for (Map.Entry<String, List<String>> entry : results.entrySet()) {
                    System.out.printf("%s: %s%n", entry.getKey(), String.join(", ", entry.getValue()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static int[][] readBoardFromCSV(String filename) throws IOException {
        int[][] board = new int[9][9];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 9) {
                String[] values = line.trim().split("\\s*,\\s*");
                if (values.length != 9) {
                    throw new IllegalArgumentException("Invalid CSV format: Each row must have exactly 9 numbers");
                }
                for (int col = 0; col < 9; col++) {
                    try {
                        board[row][col] = Integer.parseInt(values[col]);
                        if (board[row][col] < 1 || board[row][col] > 9) {
                            throw new IllegalArgumentException("Numbers must be between 1 and 9");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid number format in CSV");
                    }
                }
                row++;
            }
            if (row != 9) {
                throw new IllegalArgumentException("CSV must have exactly 9 rows");
            }
        }
        return board;
    }
}