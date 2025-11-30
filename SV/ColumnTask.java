


import java.util.List;

public class ColumnTask extends VerificationTask {
    private final int columnIndex;

    public ColumnTask(int[][] board, int c) {
        super(board);
        this.columnIndex = c;
    }

    @Override
    public List<String> call() {
        int[] values = new int[9];
        String[] positions = new String[9];
        
        for (int r = 0; r < 9; r++) {
            values[r] = board[r][columnIndex];

            positions[r] = "(" + (r + 1) + "," + (columnIndex + 1) + ")";
        }
        return findDuplicates(values, positions, "COLUMN", columnIndex + 1);
    }
}