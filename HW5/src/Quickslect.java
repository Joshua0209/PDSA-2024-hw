import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Exam {
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static int medianOf3(Integer[][] a, int lo, int mid, int hi) {
        return (a[lo][1] < a[mid][1] ? (a[mid][1] < a[hi][1] ? mid : a[lo][1] < a[hi][1] ? hi : lo)
                : (a[lo][1] < a[hi][1] ? lo : a[mid][1] < a[hi][1] ? hi : mid));
    }

    public static int select(Integer[][] a, int k, int lo, int hi) {
        if (hi <= lo)
            return hi;
        int mid = lo + (hi - lo) / 2;
        int medianIndex = medianOf3(a, lo, mid, hi);
        exch(a, lo, medianIndex);

        int lt = lo, gt = hi;
        Integer v = a[lo][1];
        int i = lo;
        while (i <= gt) {
            int cmp = a[i][1].compareTo(v);
            if (cmp < 0) {
                exch(a, lt++, i++);
            } else if (cmp > 0) {
                exch(a, i, gt--);
            } else
                i++;
        }
        if (lt > k)
            lt = select(a, k, lo, lt - 1);
        else if (gt < k)
            lt = select(a, k, gt + 1, hi);
        return lt;
    }

    public static List<int[]> getPassedList(Integer[][] scores) {
        int N = scores.length; // Number Of Subjects
        int M = scores[0].length; // Number Of Students
        int k = (int) Math.ceil(M * 0.2);
        int mk = M - k;
        int[][] tmp = new int[M][2];
        for (int i = 0; i < M; i++) {
            tmp[i] = new int[2];
        }

        List<int[]> result = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            Integer[][] scores_idx = new Integer[M][2];
            for (int j = 0; j < M; j++) {
                scores_idx[j] = new Integer[] { j, scores[i][j] };
            }
            int lt = select(scores_idx, mk, 0, M - 1); // select k max
            for (int j = lt; j < M; j++) {
                tmp[scores_idx[j][0]][0] += 1;
                tmp[scores_idx[j][0]][1] += scores_idx[j][1];
            }

            // for (int[] student : result)
            // System.out.print(Arrays.toString(student));
            // System.out.println();
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
        for (int i = result.size() - 1; i >= k; i++) {
            result.remove(i);
        }
        return result;
    }

    public static void main(String[] args) {
        List<int[]> ans = getPassedList(new Integer[][] {
                {
                        30, 55, 94, 79, 16, 21, 20, 39, 55, 68, 81, 15, 76, 9, 62, 44,
                        44, 85, 90, 20
                }
        });
        for (int[] student : ans)
            System.out.print(Arrays.toString(student));
        // Output -> [2, 94],[18, 90],[17, 85],[10, 81]

        System.out.println(); // For typesetting
    }
}
