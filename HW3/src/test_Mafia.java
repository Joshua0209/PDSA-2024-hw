// import java.io.FileNotFoundException;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.Arrays; // Used to print the arrays

// import com.google.gson.*;

import edu.princeton.cs.algs4.Stack;

// class test {
//     public static void main(String[] args) {
//         Mafia sol = new Mafia();
//         Gson gson = new Gson();
//         try (FileReader reader = new FileReader("Mafia.json")) {
//             JsonArray all = gson.fromJson(reader, JsonArray.class);
//             for (JsonElement caseInList : all) {
//                 JsonArray a = caseInList.getAsJsonArray();
//                 int q_cnt = 0, wa = 0, ac = 0;
//                 for (JsonElement o : a) {
//                     q_cnt++;
//                     JsonObject person = o.getAsJsonObject();
//                     JsonArray arg_lvl = person.getAsJsonArray("level");
//                     JsonArray arg_rng = person.getAsJsonArray("range");
//                     JsonArray arg_ans = person.getAsJsonArray("answer");
//                     int LVL[] = new int[arg_lvl.size()];
//                     int RNG[] = new int[arg_lvl.size()];
//                     int Answer[] = new int[arg_ans.size()];
//                     int Answer_W[] = new int[arg_ans.size()];
//                     for (int i = 0; i < arg_ans.size(); i++) {
//                         Answer[i] = (arg_ans.get(i).getAsInt());
//                         if (i < arg_lvl.size()) {
//                             LVL[i] = (arg_lvl.get(i).getAsInt());
//                             RNG[i] = (arg_rng.get(i).getAsInt());
//                         }
//                     }
//                     Answer_W = sol.result(LVL, RNG);
//                     for (int i = 0; i < arg_ans.size(); i++) {
//                         if (Answer_W[i] == Answer[i]) {
//                             if (i == arg_ans.size() - 1) {
//                                 System.out.println(q_cnt + ": AC");
//                             }
//                         } else {
//                             wa++;
//                             System.out.println(q_cnt + ": WA");
//                             break;
//                         }
//                     }

//                 }
//                 System.out.println("Score: " + (q_cnt - wa) + "/" + q_cnt);

//             }
//         } catch (FileNotFoundException e) {
//             e.printStackTrace();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }

class member {
    int level;
    int range;
    int index;

    public member(int _level, int _range, int i) {
        level = _level;
        range = _range;
        index = i;
    }
}

class Mafia {
    public int[] result(int[] levels, int[] ranges) {
        // Given the traits of each member and output
        // the leftmost and rightmost index of member
        // can be attacked by each member.
        int n = levels.length;
        int[] output = new int[2 * n];
        member[] members = new member[n];
        for (int i = 0; i < n; i++) {
            members[i] = new member(levels[i], ranges[i], i);
        }

        Stack<member> stack = new Stack<member>();
        member temp;
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && stack.peek().level <= levels[i]) {
                temp = stack.pop();
                if (i - 1 <= temp.index + temp.range) {
                    output[2 * temp.index + 1] = i - 1;
                } else {
                    output[2 * temp.index + 1] = temp.index + temp.range;
                }
            }
            stack.push(members[i]);
        }
        while (!stack.isEmpty()) {
            temp = stack.pop();
            if (n - 1 <= temp.index + temp.range) {
                output[2 * temp.index + 1] = n - 1;
            } else {
                output[2 * temp.index + 1] = temp.index + temp.range;
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && stack.peek().level <= levels[i]) {
                temp = stack.pop();
                if (i + 1 >= temp.index - temp.range) {
                    output[2 * temp.index] = i + 1;
                } else {
                    output[2 * temp.index] = temp.index - temp.range;
                }
            }
            stack.push(members[i]);
        }
        while (!stack.isEmpty()) {
            temp = stack.pop();
            if (0 >= temp.index - temp.range) {
                output[2 * temp.index] = 0;
            } else {
                output[2 * temp.index] = temp.index - temp.range;
            }
        }

        return output;
        // complete the code by returning an int[]
        // flatten the results since we only need an 1-dimentional array.
    }

    // public static void main(String[] args) {
    // Mafia sol = new Mafia();
    // System.out.println(Arrays.toString(
    // sol.result(new int[] { 11, 13, 11, 7, 15 },
    // new int[] { 1, 8, 1, 7, 2 })));
    // Output: [0, 0, 0, 3, 2, 3, 3, 3, 2, 4]
    // => [a0, b0, a1, b1, a2, b2, a3, b3, a4, b4]
    // }
}