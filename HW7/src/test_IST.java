import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import com.google.gson.*;

import edu.princeton.cs.algs4.In;

class OutputFormat {
    List<String> answer;
    String func;
    String[] args;
}

class test {
    static boolean deepEquals(List<String> a, List<String> b) {
        return Arrays.deepEquals(a.toArray(), b.toArray());
    }

    static boolean run_and_check(OutputFormat[] data, IntervalST<Integer, String> IST) {
        for (OutputFormat cmd : data) {
            if (cmd.func.equals("intersects")) {
                int lo = Integer.parseInt(cmd.args[0]);
                int hi = Integer.parseInt(cmd.args[1]);

                List<String> student_answer = IST.intersects(lo, hi);
                Collections.sort(cmd.answer);
                Collections.sort(student_answer);
                if (!deepEquals(student_answer, cmd.answer)) {
                    return false;
                }
            } else if (cmd.func.equals("put")) {
                IST.put(Integer.parseInt(cmd.args[0]), Integer.parseInt(cmd.args[1]), cmd.args[2]);
            } else if (cmd.func.equals("delete")) {
                IST.delete(Integer.parseInt(cmd.args[0]), Integer.parseInt(cmd.args[1]));
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        OutputFormat[][] datas;
        OutputFormat[] data;
        int num_ac = 0;

        try {
            datas = gson.fromJson(new FileReader("test_IST.json"), OutputFormat[][].class);
            for (int i = 0; i < datas.length; ++i) {
                data = datas[i];

                System.out.print("Sample" + i + ": ");
                if (run_and_check(data, new IntervalST<>())) {
                    System.out.println("AC");
                    num_ac++;
                } else {
                    System.out.println("WA");
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
