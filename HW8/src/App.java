import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.UF;
import java.util.Arrays;
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
}

class ImageMerge {
    private double[][] bbs;
    private double iou_thresh;
    private IntervalST<Double, double[]> IST;

    private double getIoU(double[] box1, double[] box2) {
        double leftX = Math.max(box1[0], box2[0]);
        double rightX = Math.min(box1[0] + box1[2], box2[0] + box2[2]);
        double upY = Math.max(box1[1], box2[1]);
        double downY = Math.min(box1[1] + box1[3], box2[1] + box2[3]);

        if (rightX > leftX && downY > upY) {
            double intersectionArea = (rightX - leftX) * (downY - upY);
            double box1Area = box1[2] * box1[3];
            double box2Area = box2[2] * box2[3];
            return intersectionArea / (box1Area + box2Area - intersectionArea);
        }
        return 0;
    }

    private double[] mergeTwoBoxes(double[] box1, double[] box2) {
        double[] merged = new double[4];
        merged[0] = Math.min(box1[0], box2[0]);
        merged[1] = Math.min(box1[1], box2[1]);
        merged[2] = Math.max(box1[0] + box1[2], box2[0] + box2[2]) - merged[0];
        merged[3] = Math.max(box1[1] + box1[3], box2[1] + box2[3]) - merged[1];
        return merged;
    }

    public double[][] mergeBox() {
        double[][] events = new double[2 * this.bbs.length][6];
        List<int[]> mergedIdx = new ArrayList<int[]>();

        for (int i = 0; i < this.bbs.length; i++) {
            events[2 * i] = new double[] { this.bbs[i][0], this.bbs[i][1], this.bbs[i][2], this.bbs[i][3], 0, i };
            events[2 * i + 1] = new double[] { this.bbs[i][0] + this.bbs[i][2], this.bbs[i][1], this.bbs[i][2],
                    this.bbs[i][3], 1, i };
        }

        Arrays.sort(events, (a, b) -> Double.compare(a[0], b[0]));
        for (double[] row : events) {
            if (row[4] == 0) {
                List<double[]> interboxes = this.IST.intersects(row[1], row[1] + row[3]);
                for (double[] interbox : interboxes) {
                    if (getIoU(interbox, row) > iou_thresh) {
                        mergedIdx.add(new int[] { (int) interbox[5], (int) row[5] });
                    }
                }
                this.IST.put(Double.valueOf(row[1]), Double.valueOf(row[1] + row[3]), row);
            } else { // if (row[4] == 1)
                this.IST.delete(Double.valueOf(row[1]), Double.valueOf(row[1] + row[3]));
            }
        }

        UF set = new UF(this.bbs.length);
        for (int i = 0; i < mergedIdx.size(); i++) {
            set.union(mergedIdx.get(i)[0], mergedIdx.get(i)[1]);
        }

        List<double[]> mergedBoxes = new ArrayList<double[]>();
        for (int i = 0; i < this.bbs.length; i++) {
            if (i == set.find(i)) {
                double[] tmp = this.bbs[i];
                for (int j = 0; j < this.bbs.length; j++) {
                    if (set.find(j) == i)
                        tmp = mergeTwoBoxes(tmp, this.bbs[j]);
                }
                mergedBoxes.add(tmp);
            }
        }

        double result[][] = new double[mergedBoxes.size()][];
        for (int i = 0; i < mergedBoxes.size(); i++) {
            result[i] = mergedBoxes.get(i);
        }
        Arrays.sort(result, (a, b) -> {
            if (a[0] != b[0]) // Compare leftX
                return Double.compare(a[0], b[0]);
            else if (a[1] != b[1]) // Compare upY
                return Double.compare(a[1], b[1]);
            else if (a[2] != b[2]) // Compare width
                return Double.compare(a[2], b[2]);
            else // Compare height
                return Double.compare(a[3], b[3]);
        });
        return result;
        // return merged bounding boxes just as input in the format of
        // [up_left_x,up_left_y,width,height]
    }

    public ImageMerge(double[][] bbs, double iou_thresh) {
        this.bbs = bbs;
        this.iou_thresh = iou_thresh;
        this.IST = new IntervalST<>();
        // bbs(bounding boxes): [up_left_x,up_left_y,width,height]
        // iou_threshold: [0.0,1.0]
    }

    public static void draw(double[][] bbs) {
        // ** NO NEED TO MODIFY THIS FUNCTION, WE WON'T CALL THIS **
        // ** DEBUG ONLY, USE THIS FUNCTION TO DRAW THE BOX OUT**
        StdDraw.setCanvasSize(960, 540);
        for (double[] box : bbs) {
            double half_width = (box[2] / 2.0);
            double half_height = (box[3] / 2.0);
            double center_x = box[0] + half_width;
            double center_y = box[1] + half_height;
            // StdDraw use y = 0 at the bottom, 1-center_y to flip
            StdDraw.rectangle(center_x, 1 - center_y, half_width, half_height);
        }
    }

    public static void main(String[] args) {
        ImageMerge sol = new ImageMerge(
                new double[][] {
                        {
                                0.00961497918560058, 0.6762365330207978, 0.6386276088202811,
                                0.8682644356564726
                        },
                        {
                                0.10879881020790369, 0.17549330666737795, 0.6348163780800717,
                                0.5064639831744495
                        },
                        {
                                0.1337370370891277, 0.2759339650963081, 0.6175021781940115,
                                0.8414187459672646
                        },
                        {
                                0.21795284233261225, 0.08032870574331774, 0.4910172720898085,
                                0.83640267934744
                        },
                        {
                                0.2323579517214868, 0.7589274136694993, 0.7608432819768459,
                                0.6777591466040125
                        }
                },
                0.4771823614121024);
        double[][] temp = sol.mergeBox();
        ImageMerge.draw(temp);
    }
}