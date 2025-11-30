
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class SudokuVerifier {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java SudokuVerifier <csv-file> <mode>");
            System.err.println("mode: 0 (sequential), 3 (three threads), or 27 (twenty-seven threads)");
            return;
        }

        String path = args[0];
        int mode;
        try {
            mode = Integer.parseInt(args[1]);
            if (mode != 0 && mode != 3 && mode != 27) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.err.println("Mode must be 0, 3, or 27.");
            return;
        }

        int[][] board;
        try {
            board = readCSV(path);
        } catch (Exception e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            return;
        }

        List<String> duplicates;

        if (mode == 0) {
            duplicates = runSequential(board);
        } else if (mode == 3) {
            duplicates = runThreeThreads(board);
        } else { 
            duplicates = runTwentySevenThreads(board);
        }

        if (duplicates.isEmpty()) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            for (String d : duplicates) System.out.println(d);
        }
    }

    public static List<String> runSequential(int[][] board) {
        ArrayList<String> duplicates = new ArrayList<>();

        for (int r = 0; r < 9; r++)
            duplicates.addAll(new RowTask(board, r).call());

        for (int c = 0; c < 9; c++)
            duplicates.addAll(new ColumnTask(board, c).call());

        for (int sr = 0; sr < 9; sr += 3)
            for (int sc = 0; sc < 9; sc += 3)
                duplicates.addAll(new BoxTask(board, sr, sc).call());

        return duplicates;
    }

    public static List<String> runThreeThreads(int[][] board) {
       
        ConcurrentLinkedQueue<String> results = new ConcurrentLinkedQueue<>();

        ExecutorService ex = Executors.newFixedThreadPool(3);
        List<Future<?>> futures = new ArrayList<>();


        futures.add(ex.submit(() -> {
            for (int r = 0; r < 9; r++) {
                results.addAll(new RowTask(board, r).call());
            }
        }));
        futures.add(ex.submit(() -> {
            for (int c = 0; c < 9; c++) {
                results.addAll(new ColumnTask(board, c).call());
            }
        }));

        futures.add(ex.submit(() -> {
            for (int sr = 0; sr < 9; sr += 3) {
                for (int sc = 0; sc < 9; sc += 3) {
                    results.addAll(new BoxTask(board, sr, sc).call());
                }
            }
        }));
        
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (Exception ignored) { }
        }

        ex.shutdown();
        return new ArrayList<>(results);
    }

    public static List<String> runTwentySevenThreads(int[][] board) {
       
        List<VerificationTask> tasks = TaskFactory.createTwentySevenTasks(board);

        ExecutorService ex = Executors.newFixedThreadPool(27);
        List<String> duplicates = new ArrayList<>();

        try {
            List<Future<List<String>>> futures = ex.invokeAll(tasks);
            for (Future<List<String>> future : futures) {
                duplicates.addAll(future.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            System.err.println("An error occurred during concurrent execution: " + e.getMessage());
        } finally {
            ex.shutdown();
        }

        return duplicates;
    }

    public static int[][] readCSV(String filename) throws Exception {
        int[][] board = new int[9][9];

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int r = 0;

            while ((line = br.readLine()) != null && r < 9) {
             
                String[] parts = line.trim().split("[, ]+");
                if (parts.length < 9) {
                    throw new IOException("Line " + (r + 1) + " does not contain 9 numbers.");
                }
                for (int c = 0; c < 9; c++) {
                    int val = Integer.parseInt(parts[c]);
                    if (val < 1 || val > 9) {
                        throw new IOException("Value out of range 1..9 at row " + (r + 1) + " col " + (c + 1));
                    }
                    board[r][c] = val;
                }
                r++;
            }

            if (r != 9) throw new IOException("CSV must contain exactly 9 rows (found " + r + ").");
        }
        return board;
    }
}
