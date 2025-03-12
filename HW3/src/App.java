import java.util.Arrays; // Used to print the arrays
import edu.princeton.cs.algs4.Stack;

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

    public static void main(String[] args) {
        Mafia sol = new Mafia();
        System.out.println(Arrays.toString(
                sol.result(new int[] { 384, 75, 146, 705, 610, 483, 376, 451, 796, 845 },
                        new int[] { 0, 0, 0, 2, 4, 4, 2, 0, 2, 2 })));
        // "level": [384, 75, 146, 705, 610, 483, 376, 451, 796, 845],
        // "range": [0, 0, 0, 2, 4, 4, 2, 0, 2, 2],
        // "answer": [0, 0, 1, 1, 2, 2, 1, #5, 4, 7, 5, 7, 6, 6, 7, 7, 6, 8, 7, 9]
    }
}