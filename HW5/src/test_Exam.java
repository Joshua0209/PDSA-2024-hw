import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import com.google.gson.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

// class Exam {
//     private static void exch(Object[] a, int i, int j) {
//         Object swap = a[i];
//         a[i] = a[j];
//         a[j] = swap;
//     }

//     public static int select(Integer[][] a, int k, int lo, int hi) {
//         if (hi <= lo)
//             return hi;
//         int lt = lo, gt = hi;
//         Integer v = a[lo][1];
//         int i = lo;
//         while (i <= gt) {
//             int cmp = a[i][1].compareTo(v);
//             if (cmp < 0) {
//                 exch(a, lt++, i++);
//             } else if (cmp > 0) {
//                 exch(a, i, gt--);
//             } else
//                 i++;
//         }
//         if (lt > k)
//             lt = select(a, k, lo, lt - 1);
//         else if (gt < k)
//             lt = select(a, k, gt + 1, hi);
//         return lt;
//     }

//     public static int findArray(Integer[][] scores_idx, int value) {
//         for (int i = 0; i < scores_idx.length; i++) {
//             if (scores_idx[i][0] == value) {
//                 return i;
//             }
//         }
//         return -1;
//     }

//     public static List<int[]> getPassedList(Integer[][] scores) {
//         int N = scores.length; // Number Of Subjects
//         int M = scores[0].length; // Number Of Students
//         int k = (int) Math.ceil(M * 0.2);
//         List<int[]> result = new ArrayList<>();

//         for (int i = 0; i < N; i++) {
//             Integer[][] scores_idx = new Integer[M][2];
//             for (int j = 0; j < M; j++) {
//                 scores_idx[j] = new Integer[] { j, scores[i][j] };
//             }

//             List<Integer[]> list = new ArrayList<>(Arrays.asList(scores_idx));
//             Collections.shuffle(list);
//             scores_idx = list.toArray(new Integer[0][0]);

//             int lt = select(scores_idx, M - k, 0, M - 1); // select k max

//             if (i != 0) {
//                 int tmp = result.size();
//                 for (int j = tmp - 1; j >= 0; j--) { // reversed order so can be removed later
//                     int find = findArray(scores_idx, result.get(j)[0]);
//                     // System.out.print(find);
//                     // System.out.print(' ');
//                     if (find < lt) {
//                         result.remove(j); // add to a list which need to be removed later
//                     } else {
//                         result.get(j)[1] += scores_idx[find][1];
//                     }
//                 }
//             } else {
//                 for (int j = lt; j < M; j++) {
//                     result.add(new int[] { scores_idx[j][0], scores_idx[j][1] });
//                 }
//             }
//             for (int[] student : result)
//                 System.out.print(Arrays.toString(student));
//             System.out.println();
//         }
//         result.sort((a, b) -> {
//             if (!(a[1] == b[1])) {
//                 return b[1] - a[1];
//             } else {
//                 return a[0] - b[0];
//             }
//         });
//         for (int i = result.size() - 1; i >= k; i++) {
//             result.remove(i);
//         }
//         return result;
//     }

//     // public static void main(String[] args) {
//     // List<int[]> ans = getPassedList(new Integer[][] {
//     // { 73, 62, 75, 43, 15, 74, 67, 24, 36, 47, 13, 73, 54, 5, 71, 24,
//     // 45, 39, 30, 15, 74, 73, 21, 18, 45, 67, 89, 36, 82, 69 },
//     // { 84, 70, 69, 48, 18, 2, 68, 29, 42, 94, 18, 77, 40, 44, 73, 27,
//     // 90, 38, 27, 18, 32, 60, 11, 17, 54, 68, 87, 31, 77, 75 },
//     // { 77, 63, 63, 46, 18, 69, 64, 6, 48, 26, 12, 77, 48, 80, 73, 24,
//     // 92, 43, 36, 24, 12, 71, 16, 18, 54, 63, 94, 35, 76, 71 }
//     // });
//     // for (int[] student : ans)
//     // System.out.print(Arrays.toString(student));
//     // // Output -> [26, 270], [28, 235], [0, 234], [11, 227]

//     // System.out.println(); // For typesetting
//     // }
// }

class OutputFormat {
    Integer[][] scores;
    List<int[]> answer;
}

class test_Exam {
    static boolean deepEquals(List<int[]> answer, List<int[]> answer2) {
        if (answer.size() != answer2.size())
            return false;
        for (int i = 0; i < answer.size(); ++i) {
            int[] a = answer.get(i);
            int[] b = answer2.get(i);
            if (!Arrays.equals(a, b)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        OutputFormat[] datas;
        int num_ac = 0;
        List<int[]> user_ans;
        OutputFormat data;

        try {
            datas = gson.fromJson(new FileReader("test_Exam.json"), OutputFormat[].class);
            for (int i = 0; i < datas.length; ++i) {
                data = datas[i];
                user_ans = Exam.getPassedList(data.scores);
                System.out.print("Sample" + i + ": ");

                if (deepEquals(user_ans, data.answer)) {
                    System.out.println("AC");
                    num_ac++;
                } else {
                    System.out.println("WA");
                    System.out.println("Data:      " + Arrays.deepToString(data.scores));
                    System.out.println("Test_ans:  " + Arrays.deepToString(data.answer.toArray()));
                    System.out.println("User_ans:  " + Arrays.deepToString(user_ans.toArray()));
                    System.out.println("");
                }
            }
            System.out.println("Score: " + num_ac + "/" + datas.length);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
