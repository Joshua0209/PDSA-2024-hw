import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

class OutputFormat2 {
    double[][] box;
    double iou;
    double[][] answer;
}

class test {
    private static boolean deepEquals(double[][] test_ans, double[][] user_ans) {
        if (test_ans.length != user_ans.length)
            return false;
        for (int i = 0; i < user_ans.length; ++i) {
            if (user_ans[i].length != test_ans[i].length)
                return false;
            for (int j = 0; j < user_ans[i].length; ++j) {
                if (Math.abs(test_ans[i][j] - user_ans[i][j]) > 0.00000000001)
                    return false;
            }
        }
        return true;
    }

    public static void draw(double[][] user, double[][] test) {
        StdDraw.setCanvasSize(960, 540);
        for (double[] box : user) {
            StdDraw.setPenColor(StdDraw.BLACK);
            double half_width = (box[2] / 2.0);
            double half_height = (box[3] / 2.0);
            double center_x = box[0] + half_width;
            double center_y = box[1] + half_height;
            // StdDraw use y = 0 at the bottom, 1-center_y to flip

            StdDraw.rectangle(center_x, 1 - center_y, half_width, half_height);
        }
        for (double[] box : test) {
            StdDraw.setPenColor(StdDraw.BOOK_RED);
            double half_width = (box[2] / 2.0);
            double half_height = (box[3] / 2.0);
            double center_x = box[0] + half_width;
            double center_y = box[1] + half_height;
            // StdDraw use y = 0 at the bottom, 1-center_y to flip

            StdDraw.rectangle(center_x, 1 - center_y, half_width, half_height);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Gson gson = new Gson();
        OutputFormat2[] datas;
        OutputFormat2 data;
        int num_ac = 0;

        double[][] user_ans;
        ImageMerge sol;

        try {
            datas = gson.fromJson(new FileReader("test_Merge.json"), OutputFormat2[].class);
            for (int i = 0; i < datas.length; ++i) {
                data = datas[i];
                sol = new ImageMerge(data.box, data.iou);
                user_ans = sol.mergeBox();
                System.out.print("Sample" + i + ": ");
                if (deepEquals(user_ans, data.answer)) {
                    System.out.println("AC");
                    num_ac++;
                } else {
                    System.out.println("WA");
                    System.out.println("Data:      " + "\n    iou: " + data.iou + "\n" +
                            "    box: " + Arrays.deepToString(data.box));
                    System.out.println("Test_ans:  " + Arrays.deepToString(data.answer));
                    System.out.println("User_ans:  " + Arrays.deepToString(user_ans));
                    System.out.println("");
                    draw(user_ans, data.answer);
                    Thread.sleep(5000);
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
