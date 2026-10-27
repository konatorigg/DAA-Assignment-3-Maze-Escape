import java.util.*;

public class Zones {
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] forward = Graphs.slidesFrom(s);
        int[][] reverse = Graphs.slidesInto(s);
        boolean[] seen = new boolean[n];
        int[] finish = new int[n], stack = new int[n], next = new int[n];
        int count = 0;

        for (int start = 0; start < n; start++) {
            if (seen[start]) continue;
            int top = 0;
            stack[top] = start;
            seen[start] = true;
            while (top >= 0) {
                int u = stack[top];
                if (next[u] == forward[u].length) {
                    finish[count++] = u;
                    top--;
                } else {
                    int v = s.to()[forward[u][next[u]++]];
                    if (!seen[v]) {
                        seen[v] = true;
                        stack[++top] = v;
                    }
                }
            }
        }

        int[] zone = new int[n];
        Arrays.fill(zone, -1);
        int zoneCount = 0;
        for (int i = count - 1; i >= 0; i--) {
            int start = finish[i];
            if (zone[start] != -1) continue;
            int top = 0;
            stack[top] = start;
            zone[start] = zoneCount;
            while (top >= 0) {
                int u = stack[top--];
                for (int edge : reverse[u]) {
                    int v = s.from()[edge];
                    if (zone[v] == -1) {
                        zone[v] = zoneCount;
                        stack[++top] = v;
                    }
                }
            }
            zoneCount++;
        }
        return zone;
    }

    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        // There may be several slides between two zones; keeping all of them
        // is fine because taking the minimum during relaxation has the same result.
        int[] longest = new int[zoneCount];
        long[] dist = new long[zoneCount];
        Arrays.fill(longest, 1);
        Arrays.fill(dist, Long.MAX_VALUE / 4);
        dist[zoneOf[0]] = 0;
        int[] degree = new int[zoneCount];
        for (int i = 0; i < s.from().length; i++) {
            int a = zoneOf[s.from()[i]], b = zoneOf[s.to()[i]];
            if (a != b) degree[a]++;
        }
        int[][] outgoing = new int[zoneCount][];
        for (int i = 0; i < zoneCount; i++) outgoing[i] = new int[degree[i]];
        int[] at = new int[zoneCount];
        for (int i = 0; i < s.from().length; i++) {
            int a = zoneOf[s.from()[i]], b = zoneOf[s.to()[i]];
            if (a != b) outgoing[a][at[a]++] = i;
        }
        int max = 0;
        for (int u = 0; u < zoneCount; u++) {
            max = Math.max(max, longest[u]);
            for (int edge : outgoing[u]) {
                int v = zoneOf[s.to()[edge]];
                longest[v] = Math.max(longest[v], longest[u] + 1);
                if (dist[u] != Long.MAX_VALUE / 4) {
                    dist[v] = Math.min(dist[v], dist[u] + s.time()[edge]);
                }
            }
        }
        for (int x : longest) max = Math.max(max, x);
        return new long[]{max, dist[zoneOf[s.n() - 1]]};
    }
}
