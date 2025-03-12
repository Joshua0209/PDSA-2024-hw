import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
// import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class ImageSegmentation {
    private Set[][] imageSet;
    private int[][] inputImage;
    private int segmentCount;
    private int largestColor;
    private int largestSize;

    class Set {
        private Set parent;
        private int rank;
        private int size;

        public Set() {
            this.parent = this;
            this.rank = 0;
            this.size = 1;
        }
    }

    public void union(Set x, Set y) {
        link(findSet(x), findSet(y));
    }

    public Set findSet(Set x) {
        if (x.parent != x) {
            x.parent = findSet(x.parent);
        }
        return x.parent;
    }

    public void link(Set x, Set y) {
        if (x == y) {
            return;
        }
        if (x.rank > y.rank) {
            y.parent = x;
            x.size += y.size;
        } else {
            x.parent = y;
            y.size += x.size;
            if (x.rank == y.rank) {
                y.rank += 1;
            }
        }
    }

    public void printSet(Set[][] x) {
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                System.out.print(x[i][j].size);
            }
            System.out.println();
        }
        System.out.println();
    }

    public ImageSegmentation(int N, int[][] inputImage) {
        // Initialize a N-by-N image
        this.inputImage = inputImage;
        this.imageSet = new Set[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.imageSet[i][j] = new Set();
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i != 0 && inputImage[i][j] == inputImage[i - 1][j]) {
                    union(imageSet[i][j], imageSet[i - 1][j]);
                    // printSet(imageSet);
                }
                if (j != 0 && inputImage[i][j] == inputImage[i][j - 1]) {
                    union(imageSet[i][j], imageSet[i][j - 1]);
                    // printSet(imageSet);
                }
            }
        }
    }

    public int countDistinctSegments() {
        // Count the number of distinct segments in the image.
        for (int i = 0; i < imageSet.length; i++) {
            for (int j = 0; j < imageSet.length; j++) {
                if (imageSet[i][j].parent == imageSet[i][j] && inputImage[i][j] != 0) {
                    segmentCount += 1;
                }
            }
        }
        return segmentCount;
    }

    public int[] findLargestSegment() {
        // Find the largest connected segment and return an array
        // containing the number of pixels and the color of the segment.
        largestSize = -1;
        for (int i = 0; i < imageSet.length; i++) {
            for (int j = 0; j < imageSet.length; j++) {
                if (inputImage[i][j] != 0 && largestSize <= imageSet[i][j].size) {
                    if (largestSize < imageSet[i][j].size) {
                        largestSize = imageSet[i][j].size;
                        largestColor = inputImage[i][j];
                    } else if (inputImage[i][j] < largestColor) {
                        largestSize = imageSet[i][j].size;
                        largestColor = inputImage[i][j];
                    }
                }
            }
        }
        return new int[] { largestSize, largestColor };
    }

    // private object mergeSegment (object XXX, ...){
    // Maybe you can use user-defined function to
    // facilitate you implement mergeSegment method.
    // }

    public static void test(String[] args) {
        ImageSegmentation s;
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(args[0])) {
            JSONArray all = (JSONArray) jsonParser.parse(reader);
            int count = 0;
            for (Object CaseInList : all) {
                count++;
                JSONObject aCase = (JSONObject) CaseInList;
                JSONArray dataArray = (JSONArray) aCase.get("data");

                // JSONObject data = (JSONObject) aCase.get("data");
                // JSONArray dataArray = (JSONArray) data.get("data");

                int testSize = 0;
                int waSize = 0;
                System.out.print("Case ");
                System.out.println(count);
                for (Object dataObject : dataArray) {
                    JSONObject dataDetails = (JSONObject) dataObject;
                    int N = ((Long) dataDetails.get("N")).intValue();
                    JSONArray imageArray = (JSONArray) dataDetails.get("image");

                    int[][] image = new int[imageArray.size()][];
                    for (int i = 0; i < imageArray.size(); i++) {
                        JSONArray row = (JSONArray) imageArray.get(i);
                        image[i] = new int[row.size()];
                        for (int j = 0; j < row.size(); j++) {
                            image[i][j] = ((Long) row.get(j)).intValue();
                        }
                    }
                    // System.out.println("N: " + N);
                    // System.out.println("Image: " + Arrays.deepToString(image));

                    s = new ImageSegmentation(N, image);

                    int distinctSegments = ((Long) dataDetails.get("DistinctSegments")).intValue();

                    JSONArray largestSegmentArray = (JSONArray) dataDetails.get("LargestSegment");
                    int largestColor = ((Long) largestSegmentArray.get(0)).intValue();
                    int largestSize = ((Long) largestSegmentArray.get(1)).intValue();

                    int ans1 = s.countDistinctSegments();
                    int ans2 = s.findLargestSegment()[0];
                    int ans3 = s.findLargestSegment()[1];

                    testSize++;
                    if (ans1 == distinctSegments && ans2 == largestColor && ans3 == largestSize) {
                        // System.out.println("AC");

                    } else {
                        waSize++;
                        // System.out.println("WA");
                    }
                }
                System.out.println("Score: " + (testSize - waSize) + " / " + testSize + " ");

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static int[] JSONArraytoIntArray(JSONArray x) {
        int sizeLim = x.size();
        int MyInt[] = new int[sizeLim];
        for (int i = 0; i < sizeLim; i++) {
            MyInt[i] = Integer.parseInt(x.get(i).toString());
        }
        return MyInt;
    }

    public static void main(String[] args) {
        test(args);
    }
}