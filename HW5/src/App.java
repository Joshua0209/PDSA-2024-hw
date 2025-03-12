import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Comparator;

class Exam {
    public static List<int[]> getPassedList(Integer[][] scores) {
        int N = scores.length; // Number Of Subjects
        int M = scores[0].length; // Number Of Students
        int k = (int) Math.ceil(M * 0.2);
        List<int[]> result = new ArrayList<>();
        int[][] tmp = new int[M][2];
        for (int i = 0; i < M; i++) {
            tmp[i] = new int[2];
        }

        for (int _i = 0; _i < N; _i++) {
            int i = _i;
            PriorityQueue<Integer> pq = new PriorityQueue<Integer>(M,
                    (a, b) -> scores[i][b].compareTo(scores[i][a]));

            for (int j = 0; j < M; j++) {
                pq.add((Integer) j);
            }

            int j = 0;
            Integer out = -1;
            while (j < k) {
                j++;
                out = pq.poll();
                tmp[(int) out][0] += 1;
                tmp[(int) out][1] += scores[i][(int) out];
            }
            while (!pq.isEmpty() && scores[i][(int) pq.peek()] == scores[i][(int) out]) {
                out = pq.poll();
                tmp[(int) out][0] += 1;
                tmp[(int) out][1] += scores[i][(int) out];
            }
        }

        for (int i = 0; i < M; i++) {
            if (tmp[i][0] == N) {
                result.add(new int[] { i, tmp[i][1] });
            }
        }

        result.sort((a, b) -> {
            if (!(a[1] == b[1])) {
                return b[1] - a[1];
            } else {
                return a[0] - b[0];
            }
        });
        for (int i = result.size() - 1; i >= k; i--) {
            result.remove(i);
        }
        return result;
    }

    public static void main(String[] args) {
        List<int[]> ans = getPassedList(new Integer[][] {
                // ID:[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
                { 67, 82, 98, 32, 65, 76, 87, 12, 43, 75, 25 },
                { 42, 90, 80, 12, 76, 58, 95, 30, 67, 78, 10 }
        });
        for (int[] student : ans)
            System.out.print(Arrays.toString(student));
        // 11 students * 0.2 = 2.2 -> Top 3 students
        // Output -> [6, 182][2, 178][1, 172]

        System.out.println(); // For typesetting

        ans = getPassedList(new Integer[][] {
                // ID:[0, 1, 2, 3, 4, 5]
                { 67, 82, 64, 32, 65, 76 },
                { 42, 90, 80, 12, 76, 58 }
        });
        for (int[] student : ans)
            System.out.print(Arrays.toString(student));
        // 6 students * 0.2 = 1.2 -> Top 2 students
        // Output -> [1, 172]
    }
}
