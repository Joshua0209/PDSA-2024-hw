import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import edu.princeton.cs.algs4.PrimMST;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.Queue;

class LabNetworkCabling {
    EdgeWeightedGraph graph;
    int r, s;
    Integer[] printers;
    List<Integer> computers;
    int cost;

    private int dfs(int current, int target, boolean[] visited) {
        if (current == target) {
            return 0;
        }
        visited[current] = true;

        for (Edge edge : graph.adj(current)) {
            int next = edge.other(current);
            if (!visited[next]) {
                int dist = dfs(next, target, visited);
                if (dist != -1) {
                    return (int) edge.weight() + dist;
                }
            }
        }
        return -1; // Path not found
    }

    private int bfs(int s, Integer[] target) {
        boolean[] marked = new boolean[graph.V()];
        int[] distTo = new int[graph.V()];
        Queue<Integer> q = new Queue<Integer>();
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (Edge edge : graph.adj(v)) {
                int w = edge.other(v);
                if (!marked[w]) {
                    distTo[w] = distTo[v] + (int) edge.weight();
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }

        int idx = 0;
        int minDist = distTo[target[idx]];
        int dist;
        for (int i = 1; i < target.length; i++) {
            dist = distTo[target[i]];
            if (dist < minDist) {
                minDist = dist;
                idx = i;
            } else if (dist == minDist) {
                if (target[i] < target[idx]) {
                    minDist = dist;
                    idx = i;
                }
            }
        }
        return idx;
    }

    public LabNetworkCabling(Map<Integer, String> deviceTypes, List<int[]> links) {
        // create a Minimum Spanning Tree
        EdgeWeightedGraph graph = new EdgeWeightedGraph(deviceTypes.size());
        for (int i = 0; i < links.size(); i++) {
            int[] tmp = links.get(i);
            graph.addEdge(new Edge(tmp[0], tmp[1], tmp[2]));
        }
        PrimMST mst = new PrimMST(graph);
        this.graph = new EdgeWeightedGraph(deviceTypes.size());
        for (Edge edge : mst.edges()) {
            this.graph.addEdge(edge);
        }
        List<Integer> printerList = new ArrayList<Integer>(); // {printerIndex, Using Computer Count}
        this.computers = new ArrayList<Integer>(); // {printerIndex, Using Computer Count}
        this.r = -1;
        this.s = -1;
        for (Map.Entry<Integer, String> entry : deviceTypes.entrySet()) {
            if (entry.getValue().equals("Printer")) {
                printerList.add(entry.getKey());
            } else if (entry.getValue().equals("Computer")) {
                this.computers.add(entry.getKey());
            } else if (entry.getValue().equals("Router")) {
                this.r = entry.getKey();
            } else {
                this.s = entry.getKey();
            }
        }

        this.printers = printerList.toArray(new Integer[0]);

        this.cost = (int) mst.weight();
    };

    public int cablingCost() {
        // calculate the total cost
        return this.cost;
    }

    public int serverToRouter() {
        // find the path distance between the server and the router
        boolean[] visited = new boolean[graph.V()];
        int srDistance = dfs(r, s, visited);

        return srDistance;
    }

    public int mostPopularPrinter() {
        // find the most popular printer and return its index
        int[] printersUsed = new int[printers.length];
        for (int i = 0; i < computers.size(); i++) {
            printersUsed[bfs(computers.get(i), printers)] += 1;
        }

        int printerIndex = 0;
        for (int i = 1; i < printersUsed.length; i++) {
            if (printersUsed[i] < printersUsed[printerIndex]) {
                continue;
            } else if (printersUsed[i] > printersUsed[printerIndex]) {
                printerIndex = i;
            } else if (printers[printerIndex] > printers[i]) {
                printerIndex = i;
            }
        }
        printerIndex = printers[printerIndex];
        return printerIndex;
    }
}