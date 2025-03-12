import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import edu.princeton.cs.algs4.Point2D;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.*;

class ObservationStationAnalysis {
    private ArrayList<Point2D> boundary;

    public ObservationStationAnalysis(ArrayList<Point2D> stations) {
        this.boundary = graham_scan(stations);
        // System.out.println(boundary);

    }

    public Point2D[] findFarthestStations() {
        Point2D[] farthest = new Point2D[] { new Point2D(0, 0), new Point2D(1, 1) }; // Example
        // find the farthest two stations
        ArrayList<Point2D> tmp = new ArrayList<Point2D>();
        double max_dist = 0;
        for (int i = 0; i < boundary.size(); i++) {
            for (int j = i + 1; j < boundary.size(); j++) {
                double dist = boundary.get(i).distanceSquaredTo(boundary.get(j));
                if (max_dist < dist) {
                    max_dist = dist;
                    tmp = new ArrayList<Point2D>();
                    tmp.add(boundary.get(i));
                    tmp.add(boundary.get(j));
                }
            }

        }

        tmp.sort(Point2D.Y_ORDER);
        tmp.sort(Point2D.R_ORDER);
        farthest[0] = tmp.get(0);
        farthest[1] = tmp.get(tmp.size() - 1);
        return farthest; // it should be sorted (ascendingly) by polar radius; please sort (ascendingly)
                         // by y coordinate if there are ties in polar radius.

    }

    public ArrayList<Point2D> graham_scan(ArrayList<Point2D> pool) {
        // Choose point p with smallest y-coordinate
        Collections.sort(pool, Point2D.Y_ORDER);
        Collections.sort(pool, pool.get(0).polarOrder());

        // Consider points in order; discard unless it create a ccw turn
        ArrayList<Point2D> pick = new ArrayList<Point2D>();
        for (int i = 0; i < pool.size(); i++) {
            while ((pick.size() > 1) &&
                    (Point2D.ccw(pick.get(pick.size() - 2),
                            pick.get(pick.size() - 1), pool.get(i)) != 1)) {
                pick.remove(pick.size() - 1);
            }
            pick.add(pool.get(i));
        }
        return pick;
    }

    public double coverageArea() {
        double area = 0.0;
        // calculate the area surrounded by the existing stations
        for (int i = 1; i < boundary.size() - 1; i++) {
            area += Point2D.area2(boundary.get(0), boundary.get(i), boundary.get(i + 1));
        }
        return area / 2;
    }

    public void addNewStation(Point2D newStation) {
        boundary.add(newStation);
        boundary = graham_scan(boundary);
    }
}

class OutputFormat {
    ArrayList<Point2D> stations;
    ObservationStationAnalysis OSA;
    Point2D[] farthest;
    double area;
    Point2D[] farthestNew;
    double areaNew;
    ArrayList<Point2D> newStations;
}

class TestCase {
    int Case;
    int score;
    ArrayList<OutputFormat> data;
}

class test_ObservationStationAnalysis {
    public static void main(String[] args) {
        Gson gson = new Gson();
        int num_ac = 0;
        int i = 1;

        try {
            // TestCase[] testCases = gson.fromJson(new FileReader(args[0]),
            // TestCase[].class);
            TestCase[] testCases = gson.fromJson(new FileReader("test_updated.json"), TestCase[].class);
            for (TestCase testCase : testCases) {
                System.out.println("Sample" + i + ": ");
                i++;
                for (OutputFormat data : testCase.data) {
                    ObservationStationAnalysis OSA = new ObservationStationAnalysis(data.stations);
                    Point2D[] farthest;
                    double area;
                    Point2D[] farthestNew;
                    double areaNew;

                    farthest = OSA.findFarthestStations();
                    area = OSA.coverageArea();

                    if (data.newStations != null) {
                        for (Point2D newStation : data.newStations) {
                            OSA.addNewStation(newStation);
                        }
                        farthestNew = OSA.findFarthestStations();
                        areaNew = OSA.coverageArea();
                    } else {
                        farthestNew = farthest;
                        areaNew = area;
                    }

                    if (farthest[0].equals(data.farthest[0]) && farthest[1].equals(data.farthest[1])
                            && Math.abs(area - data.area) < 0.0001
                            && farthestNew[0].equals(data.farthestNew[0]) && farthestNew[1].equals(data.farthestNew[1])
                            && Math.abs(areaNew - data.areaNew) < 0.0001) {
                        System.out.println("AC");
                        num_ac++;
                    } else {
                        System.out.println("WA");
                        System.out.println("Ans-farthest: " + Arrays.toString(data.farthest));
                        System.out.println("Your-farthest:  " + Arrays.toString(farthest));
                        System.out.println("Ans-area:  " + data.area);
                        System.out.println("Your-area:  " + area);

                        System.out.println("Ans-farthestNew: " + Arrays.toString(data.farthestNew));
                        System.out.println("Your-farthestNew:  " + Arrays.toString(farthestNew));
                        System.out.println("Ans-areaNew:  " + data.areaNew);
                        System.out.println("Your-areaNew:  " + areaNew);
                        System.out.println("");
                    }
                }
                System.out.println("Score: " + num_ac + "/ 8");
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}