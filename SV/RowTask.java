

import java.util.List;

public class RowTask extends VerificationTask {
    private final int rowIndex;

    public RowTask(int[][] board, int r) {
        super(board);
        this.rowIndex = r;
    }

    @Override
    public List<String> call() {
        int[] values = new int[9];
        String[] positions = new String[9];

        for (int c = 0; c < 9; c++) {
            values[c] = board[rowIndex][c];
            positions[c] = "(" + (rowIndex + 1) + "," + (c + 1) + ")";
        }

        return findDuplicates(values, positions, "ROW", rowIndex + 1);
    }
}
