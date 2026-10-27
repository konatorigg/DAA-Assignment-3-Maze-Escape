import java.util.*;

public class Escape {
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells(), cols = w.cols();
        int[][] open = Graphs.open(w, broken);
        int deadEnds = 0;
        for (int[] neighbours : open) if (neighbours.length == 1) deadEnds++;

        boolean[] seen = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] stack = new int[n];
        int[] nextDirection = new int[n];
        int top = 0, visited = 1;
        stack[0] = 0;
        seen[0] = true;
        while (top >= 0 && !seen[n - 1]) {
            int cell = stack[top];
            if (nextDirection[cell] == 4) {
                top--;
                continue;
            }
            int direction = nextDirection[cell]++;
            int neighbour = switch (direction) {
                case 0 -> cell - cols;
                case 1 -> cell + 1;
                case 2 -> cell + cols;
                default -> cell - 1;
            };
            if (neighbour < 0 || neighbour >= n ||
                (direction == 1 && cell % cols == cols - 1) ||
                (direction == 3 && cell % cols == 0)) continue;
            boolean isOpen = false;
            for (int v : open[cell]) if (v == neighbour) { isOpen = true; break; }
            if (!isOpen || seen[neighbour]) continue;
            seen[neighbour] = true;
            visited++;
            parent[neighbour] = cell;
            stack[++top] = neighbour;
        }
        if (!seen[n - 1]) return new Result(new int[0], visited, deadEnds);
        int length = 0;
        for (int x = n - 1; x != -1; x = parent[x]) length++;
        int[] path = new int[length];
        for (int x = n - 1, i = length - 1; x != -1; x = parent[x]) path[i--] = x;
        return new Result(path, visited, deadEnds);
    }
}
