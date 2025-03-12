import java.util.*;
import edu.princeton.cs.algs4.IndexMinPQ;

class RoadToCastle {
    ArrayList<int[]> path;
    int shortestDistance;
    int mod;
    int[] init_pos;
    int[][] distTo;
    int[][][] edgeTo;
    IndexMinPQ<Integer> pq;

    public List<int[]> shortest_path() {
        // return int[] in the format of {Y,X}
        return path;
    }

    private void path(int[] pos) {
        if (pos[0] != init_pos[0] || pos[1] != init_pos[1])
            path(edgeTo[pos[0]][pos[1]]);
        path.add(pos);
    }

    public int shortest_path_len() {
        return this.shortestDistance;
    }

    public RoadToCastle(int[][] map, int[] init_pos, int[] target_pos) {
        // map: [Y][X]
        // init_pos: 0:Y, 1:X
        // target_pos: 0:Y, 1:X
        int h = map.length, w = map[0].length;
        mod = w - 2;
        path = new ArrayList<int[]>();
        distTo = new int[h][];
        edgeTo = new int[h][][];
        this.init_pos = init_pos;
        for (int i = 0; i < h; i++) {
            distTo[i] = new int[w];
            edgeTo[i] = new int[w][];
            for (int j = 0; j < w; j++) {
                edgeTo[i][j] = new int[2];
                distTo[i][j] = Integer.MAX_VALUE;
            }
        }

        distTo[init_pos[0]][init_pos[1]] = 0;
        pq = new IndexMinPQ<Integer>((h - 1) * (w - 1));
        pq.insert(square2Line(init_pos[0], init_pos[1]), 0);
        while (!pq.isEmpty()) {
            int[] tmp = line2Square(pq.delMin());
            int i = tmp[0], j = tmp[1];
            if (i == target_pos[0] && j == target_pos[1])
                break;

            if (map[i - 1][j] != 0)
                relax(new int[] { i, j }, new int[] { i - 1, j }, cost(map[i - 1][j]));
            if (map[i + 1][j] != 0)
                relax(new int[] { i, j }, new int[] { i + 1, j }, cost(map[i + 1][j]));
            if (map[i][j - 1] != 0)
                relax(new int[] { i, j }, new int[] { i, j - 1 }, cost(map[i][j - 1]));
            if (map[i][j + 1] != 0)
                relax(new int[] { i, j }, new int[] { i, j + 1 }, cost(map[i][j + 1]));
        }
        shortestDistance = distTo[target_pos[0]][target_pos[1]];
        path(target_pos);
    }

    private void relax(int[] v, int[] w, int cost) {
        if (distTo[w[0]][w[1]] > distTo[v[0]][v[1]] + cost) {
            distTo[w[0]][w[1]] = distTo[v[0]][v[1]] + cost;
            edgeTo[w[0]][w[1]][0] = v[0];
            edgeTo[w[0]][w[1]][1] = v[1];
            if (pq.contains(square2Line(w[0], w[1])))
                pq.decreaseKey(square2Line(w[0], w[1]), distTo[w[0]][w[1]]);
            else
                pq.insert(square2Line(w[0], w[1]), distTo[w[0]][w[1]]);
        }
    }

    private int cost(int value) {
        if (value == 2)
            return 1;
        else if (value == 3)
            return 5;
        return 0;
    }

    private int square2Line(int x, int y) { // h,w
        return (x - 1) * this.mod + (y - 1);
    }

    private int[] line2Square(int x) {
        return new int[] { x / this.mod + 1, x % this.mod + 1 };
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