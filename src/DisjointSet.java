public class DisjointSet {
    private final int[] parent;
    private final int[] size;
    private int groups;

    public DisjointSet(int n) {
        parent = new int[n];
        size = new int[n];
        groups = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    public boolean union(int a, int b) {
        int x = find(a), y = find(b);
        if (x == y) return false;
        if (size[x] < size[y]) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        parent[y] = x;
        size[x] += size[y];
        groups--;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int count() {
        return groups;
    }
}
