import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import edu.princeton.cs.algs4.Point2D;

class ObservationStationAnalysis {
    private ArrayList<Point2D> boundary;

    public ObservationStationAnalysis(ArrayList<Point2D> stations) {
        this.boundary = graham_scan(stations);
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
        Collections.sort(pool, Collections.reverseOrder(Point2D.X_ORDER));
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

    public static void main(String[] args) throws Exception {

        ArrayList<Point2D> stationCoordinates = new ArrayList<>();
        stationCoordinates.add(new Point2D(2, 31));
        stationCoordinates.add(new Point2D(34, 35));
        stationCoordinates.add(new Point2D(21, 22));
        stationCoordinates.add(new Point2D(48, 11));
        stationCoordinates.add(new Point2D(47, 1));
        stationCoordinates.add(new Point2D(20, 45));
        stationCoordinates.add(new Point2D(15, 34));
        stationCoordinates.add(new Point2D(38, 2));
        ObservationStationAnalysis Analysis = new ObservationStationAnalysis(stationCoordinates);
        Analysis.findFarthestStations();
        System.out.println("Farthest Station A: " +
                Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: " +
                Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: " + Analysis.coverageArea());
        Analysis.addNewStation(new Point2D(20, 5));
        Analysis.addNewStation(new Point2D(22, 6));

        System.out.println("Farthest Station A: " +
                Analysis.findFarthestStations()[0]);
        System.out.println("Farthest Station B: " +
                Analysis.findFarthestStations()[1]);
        System.out.println("Coverage Area: " + Analysis.coverageArea());
    }
}