import java.util.*;

public class Maze {
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] result = new int[w.cells() - 1];
        int used = 0;
        for (int i = 0; i < w.count(); i++) {
            if (dsu.union(w.a()[i], w.b()[i])) result[used++] = i;
        }
        return Arrays.copyOf(result, used);
    }

    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];
        for (int i = 0; i < order.length; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> w.cost()[i]));
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] result = new int[w.cells() - 1];
        int used = 0;
        for (int i : order) {
            if (dsu.union(w.a()[i], w.b()[i])) {
                result[used++] = i;
                if (used == result.length) break;
            }
        }
        return Arrays.copyOf(result, used);
    }

    public static long primCost(Input.Walls w) {
        int n = w.cells();
        int[] degree = new int[n];
        for (int i = 0; i < w.count(); i++) {
            degree[w.a()[i]]++;
            degree[w.b()[i]]++;
        }
        int[][] edges = new int[n][];
        for (int i = 0; i < n; i++) edges[i] = new int[degree[i]];
        int[] pos = new int[n];
        for (int i = 0; i < w.count(); i++) {
            edges[w.a()[i]][pos[w.a()[i]]++] = i;
            edges[w.b()[i]][pos[w.b()[i]]++] = i;
        }
        boolean[] seen = new boolean[n];
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(i -> w.cost()[i]));
        seen[0] = true;
        int visited = 1;
        for (int edge : edges[0]) queue.add(edge);
        long total = 0;
        while (!queue.isEmpty() && visited < n) {
            int edge = queue.poll();
            int a = w.a()[edge], b = w.b()[edge];
            int next = seen[a] ? b : a;
            if (seen[next]) continue;
            seen[next] = true;
            visited++;
            total += w.cost()[edge];
            for (int e : edges[next]) queue.add(e);
        }
        return total;
    }
}
