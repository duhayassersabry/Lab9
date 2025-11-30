import java.util.concurrent.Callable;
import java.util.List;
import java.util.ArrayList;

public abstract class VerificationTask implements Callable<List<String>> {
    protected final int[][] board;

    public VerificationTask(int[][] board) {
        this.board = board;
    }

    @Override
    public abstract List<String> call() throws Exception;

    protected List<String> findDuplicates(int[] values, String[] positions, String regionType, int identifier) {
        ArrayList<String>[] numPositions = new ArrayList[10]; 
        for (int i = 1; i <= 9; i++) numPositions[i] = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            int v = values[i];
            if (v >= 1 && v <= 9) { 
                numPositions[v].add(positions[i]);
            }
        }

        ArrayList<String> found = new ArrayList<>();

        for (int num = 1; num <= 9; num++) {
            if (numPositions[num].size() > 1) {
               
                found.add(regionType + " " + identifier + ", number " + num + ", positions = " + numPositions[num]);
            }
        }
        return found;
    }
}
