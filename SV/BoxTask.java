import java.util.List;

public class BoxTask extends VerificationTask {
    private final int startR, startC;
    private final int boxNumber;

    public BoxTask(int[][] board, int startR, int startC) {
        super(board);
        this.startR = startR;
        this.startC = startC;
        this.boxNumber = (startR / 3) * 3 + (startC / 3) + 1;
    }

    @Override
    public List<String> call() {
        int[] values = new int[9];
        String[] positions = new String[9];
        int index = 0;

        for (int r = startR; r < startR + 3; r++) {
            for (int c = startC; c < startC + 3; c++) {
                values[index] = board[r][c];
                positions[index] = "(" + (r + 1) + "," + (c + 1) + ")";
                index++;
            }
        }

        return findDuplicates(values, positions, "BOX", boxNumber);
    }
}
