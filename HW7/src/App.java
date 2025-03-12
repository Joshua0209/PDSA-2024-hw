import java.util.ArrayList;
import java.util.List;

class IntervalST<Key extends Comparable<Key>, Value> {
    private Node root;

    private class Node {
        Key lo, hi; // Interval bounds
        Key max; // Maximum high endpoint in the subtree rooted at this node
        Value val; // Value associated with this interval
        Node left, right;

        public Node(Key lo, Key hi, Value val) {
            this.lo = lo;
            this.hi = hi;
            this.val = val;
            this.max = hi;
        }
    }

    public IntervalST() {
        // Initially, the root is null
        root = null;
    }

    public void put(Key lo, Key hi, Value val) {
        root = put(root, lo, hi, val);
    }

    private Node put(Node x, Key lo, Key hi, Value val) {
        if (x == null)
            return new Node(lo, hi, val);
        int cmpLo = lo.compareTo(x.lo);
        int cmpHi = hi.compareTo(x.hi);
        if (cmpLo < 0)
            x.left = put(x.left, lo, hi, val);
        else if (cmpLo > 0)
            x.right = put(x.right, lo, hi, val);
        else if (cmpLo == 0 && cmpHi == 0) {
            // Both lo and hi match, update the value
            x.val = val;
        } else {
            // Same lo but different hi, treat as a different interval
            x.right = put(x.right, lo, hi, val);
        }
        x.max = max(x.hi, max(getMax(x.left), getMax(x.right)));
        return x;
    }

    private Key getMax(Node x) {
        if (x == null)
            return null;
        return x.max;
    }

    private Key max(Key a, Key b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return (a.compareTo(b) > 0) ? a : b;
    }

    public void delete(Key lo, Key hi) {
        root = delete(root, lo, hi);
    }

    private Node delete(Node x, Key lo, Key hi) {
        if (x == null)
            return null;
        int cmpLo = lo.compareTo(x.lo);
        int cmpHi = hi.compareTo(x.hi);
        if (cmpLo < 0) {
            x.left = delete(x.left, lo, hi);
        } else if (cmpLo > 0) {
            x.right = delete(x.right, lo, hi);
        } else if (cmpLo == 0 && cmpHi == 0) {
            // Match found, remove this node
            if (x.right == null)
                return x.left;
            if (x.left == null)
                return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        } else {
            // Same lo but different hi
            x.right = delete(x.right, lo, hi);
        }
        x.max = max(x.hi, max(getMax(x.left), getMax(x.right)));
        return x;
    }

    private Node min(Node x) {
        if (x.left == null)
            return x;
        return min(x.left);
    }

    private Node deleteMin(Node x) {
        if (x.left == null)
            return x.right;
        x.left = deleteMin(x.left);
        x.max = max(x.hi, max(getMax(x.left), getMax(x.right)));
        return x;
    }

    public List<Value> intersects(Key lo, Key hi) {
        List<Value> result = new ArrayList<>();
        intersects(root, lo, hi, result);
        return result;
    }

    private void intersects(Node x, Key lo, Key hi, List<Value> result) {
        if (x == null)
            return;
        boolean intersect = (x.lo.compareTo(hi) <= 0) && (x.hi.compareTo(lo) >= 0);
        if (intersect)
            result.add(x.val);
        // Check the left subtree only if there might be an overlapping interval
        if (x.left != null && x.left.max.compareTo(lo) >= 0)
            intersects(x.left, lo, hi, result);
        // Check the right subtree only if there might be an overlapping interval
        if (x.right != null && x.right.max.compareTo(lo) >= 0)
            intersects(x.right, lo, hi, result);
    }

    public static void main(String[] args) {
        IntervalST<Integer, String> IST = new IntervalST<>();
        IST.put(2, 5, "badminton");
        IST.put(1, 5, "PDSA HW7");
        IST.put(3, 5, "Lunch");
        IST.put(3, 6, "Workout");
        IST.put(3, 7, "Do nothing");
        IST.delete(2, 5); // delete "badminton"
        System.out.println(IST.intersects(1, 2));

        IST.put(8, 8, "Dinner");
        System.out.println(IST.intersects(6, 10));

        IST.put(3, 7, "Do something"); // If an interval is identical to an existing node, then the value of that node
                                       // is updated accordingly
        System.out.println(IST.intersects(7, 7));

        IST.delete(3, 7); // delete "Do something"
        System.out.println(IST.intersects(7, 7));
    }
}
