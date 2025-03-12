import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.*;

class OutputFormat {
    LabNetworkCabling l;
    Map<Integer, String> deviceTypes;
    List<int[]> links;

    int cablingCost;
    int serverToRouter;
    int mostPopularPrinter;
}

class TestCase {
    int Case;
    int score;
    ArrayList<OutputFormat> data;
}

class test_LabNetworkCabling {
    public static void main(String[] args) {
        Gson gson = new Gson();
        int num_ac = 0;

        try {
            TestCase[] testCases = gson.fromJson(new FileReader("test_LabNetworkCabling.json"), TestCase[].class);

            for (int i = 0; i < testCases.length; ++i) {
                for (OutputFormat data : testCases[i].data) {

                    LabNetworkCabling LNC = new LabNetworkCabling(data.deviceTypes, data.links);
                    int ans_cc = data.cablingCost;
                    int ans_sr = data.serverToRouter;
                    int ans_mpp = data.mostPopularPrinter;

                    int user_cc = LNC.cablingCost();
                    int user_sr = LNC.serverToRouter();
                    int user_mpp = LNC.mostPopularPrinter();

                    if (user_cc == ans_cc && user_sr == ans_sr && user_mpp == ans_mpp) {
                        System.out.println("AC");
                        num_ac++;
                    } else {
                        System.out.println("WA");
                        System.out.println("Input deviceTypes:\n" + data.deviceTypes);
                        System.out.println("Input links: ");
                        for (int[] link : data.links) {
                            System.out.print(Arrays.toString(link));
                        }

                        System.out.println("\nAns cablingCost: " + ans_cc);
                        System.out.println("Your cablingCost:  " + user_cc);
                        System.out.println("Ans serverToRouter:  " + ans_sr);
                        System.out.println("Your serverToRouter:  " + user_sr);
                        System.out.println("Ans mostPopularPrinter:  " + ans_mpp);
                        System.out.println("Your mostPopularPrinter:  " + user_mpp);
                        System.out.println("");
                    }
                }
            }
            System.out.println("Score: " + num_ac + "/10");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}