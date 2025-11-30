

import java.util.ArrayList;
import java.util.List;

public class TaskFactory {


    public static List<VerificationTask> createTwentySevenTasks(int[][] board) {
        List<VerificationTask> tasks = new ArrayList<>();

        for (int r = 0; r < 9; r++) {
            tasks.add(new RowTask(board, r));
        }


        for (int c = 0; c < 9; c++) {
            tasks.add(new ColumnTask(board, c));
        }

        for (int sr = 0; sr < 9; sr += 3) {
            for (int sc = 0; sc < 9; sc += 3) {
                tasks.add(new BoxTask(board, sr, sc));
            }
        }

        return tasks;
    }
}
