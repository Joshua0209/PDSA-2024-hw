import java.util.*;
import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;

class RoadToCastle {
    EdgeWeightedDigraph graph;
    DijkstraSP dijkstra; ///
    int mod;
    int[] target_pos;

    public List<int[]> shortest_path() {
        // return int[] in the format of {Y,X}
        ArrayList<int[]> path = new ArrayList<int[]>();
        for (DirectedEdge edge : this.dijkstra.pathTo(square2Line(this.target_pos[0], this.target_pos[1]))) {
            path.add(line2Square(edge.from()));
        }
        path.add(this.target_pos);
        return path;
    }

    public int shortest_path_len() {
        return (int) this.dijkstra.distTo(square2Line(this.target_pos[0], this.target_pos[1]));
    }

    public RoadToCastle(int[][] map, int[] init_pos, int[] target_pos) {
        // map: [Y][X]
        // init_pos: 0:Y, 1:X
        // target_pos: 0:Y, 1:X
        int h = map.length, w = map[0].length;
        this.mod = w - 2;
        this.graph = new EdgeWeightedDigraph((h - 2) * (w - 2));
        this.target_pos = target_pos;

        for (int i = 1; i < h - 1; i++) {
            for (int j = 1; j < w - 1; j++) {
                if (map[i][j] == 0)
                    continue;
                if (map[i - 1][j] != 0) {
                    graph.addEdge(new DirectedEdge(
                            square2Line(i, j),
                            square2Line(i - 1, j),
                            cost(map[i - 1][j])));
                }
                if (map[i + 1][j] != 0) {
                    graph.addEdge(new DirectedEdge(
                            square2Line(i, j),
                            square2Line(i + 1, j),
                            cost(map[i + 1][j])));
                }
                if (map[i][j - 1] != 0) {
                    graph.addEdge(new DirectedEdge(
                            square2Line(i, j),
                            square2Line(i, j - 1),
                            cost(map[i][j - 1])));
                }
                if (map[i][j + 1] != 0) {
                    graph.addEdge(new DirectedEdge(
                            square2Line(i, j),
                            square2Line(i, j + 1),
                            cost(map[i][j + 1])));
                }
            }
        }
        // System.out.println(graph);
        this.dijkstra = new DijkstraSP(graph, square2Line(init_pos[0], init_pos[1]));
    }

    private int square2Line(int x, int y) { // h,w
        return (x - 1) * this.mod + (y - 1);
    }

    private int[] line2Square(int x) {
        return new int[] { x / this.mod + 1, x % this.mod + 1 };
    }

    private int cost(int value) {
        if (value == 2)
            return 1;
        else if (value == 3)
            return 5;
        return 0;
    }

    public static void main(String[] args) {
        RoadToCastle sol = new RoadToCastle(new int[][] {
                { 0, 0, 0, 0, 0 },
                { 0, 2, 3, 2, 0 }, // map[1][2]=3
                { 0, 2, 0, 2, 0 },
                { 0, 2, 0, 2, 0 },
                { 0, 2, 2, 2, 0 },
                { 0, 0, 0, 0, 0 }
        },
                new int[] { 1, 1 },
                new int[] { 1, 3 });
        System.out.println(sol.shortest_path_len());
        List<int[]> path = sol.shortest_path();
        for (int[] coor : path)
            System.out.println("x: " + Integer.toString(coor[0]) + " y: " + Integer.toString(coor[1]));

        // ans: best_path:{{1, 1}, {1, 2}, {1, 3}}
        // Path 1 (the best): [1, 1] [1, 2] [1, 3] -> 0+5+1 = 6, cost to reach init_pos
        // is zero!
        // Path 2 (example of other paths): [1, 1] [2, 1] [3, 1] [4, 1] [4, 2] [4, 3]
        // [3, 3] [2, 3] [1, 3] -> 8
    }
}