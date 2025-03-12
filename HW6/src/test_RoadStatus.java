import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import com.google.gson.*;

import edu.princeton.cs.algs4.In;

class OutputFormat {
    int[] answer;
    String func;
    int[] args;
}

class test {
    static boolean run_and_check(OutputFormat[] data, RoadStatus roadStat) {
        for (OutputFormat cmd : data) {
            if (cmd.func.equals("addCar")) {
                roadStat.addCar(cmd.args[0], cmd.args[1], cmd.args[2]);
            } else if (cmd.func.equals("roadStatus")) {
                int[] arr = roadStat.roadStatus(cmd.args[0]);
                if (!Arrays.equals(arr, cmd.answer))
                    return false;
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
            datas = gson.fromJson(new FileReader("test_RoadStatus.json"), OutputFormat[][].class);
            for (int i = 0; i < datas.length; ++i) {
                data = datas[i];

                System.out.print("Sample" + i + ": ");
                if (run_and_check(data, new RoadStatus())) {
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
