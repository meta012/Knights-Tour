/**
 * @author Meta Bambalaite, 2 grupe
 * Uzduotis: apeiti kvadrata N x N zirgo ejimais. Vartotojas nurodo kvadrato dydi N.
 * Igyvendinta pasinaudojant backtrackingu ir pritaikant Warnsdorff'o algoritma.
 * -------------------------------------------------------------------------------------
 *Problem: walk the N x N chess board in knight moves, using backtracking. User sets the board size. 
 */

import java.util.*;

public class KnightsTour {
    private final static int base = 8;  // maximum - 68, afterwards program crashes
    private final static int[][] moves = {{1,-2},{2,-1},{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2}};
    private static int[][] grid;
    private static int total;
    private static int row; // y
    private static int col; // x
    private static int countPossibleMoves = 0;

    public static void main(String[] args) {

        // set of grid - add 2 extra columns/rows to each side
        grid = new int[base + 4][base + 4];
        total = (base) * (base);
        int gridBase = base + 4;

        // fill the unused grid(+2 each side) with -1's
        for (int r = 0; r < gridBase; r++) {
            for (int c = 0; c < gridBase; c++) {
                if (r < 2 || r > gridBase - 3 || c < 2 || c > gridBase - 3) {
                    grid[r][c] = -1;
                }
            }
        }

        // tour is not possible if base is less than 5. Base = 1 is possible
        if ((base != 1 && base < 5) || isTourNotPossible()) {
            System.exit(-1);
        }
/*
        // to count all possible moves of a knight
        for (int everyRow = 0; everyRow < base; everyRow++) {
            for (int everyCol = 0; everyCol < base; everyCol++) {
                row = everyRow + 2;
                col = everyCol + 2;
                if (!isTourNotPossible()) {
                    grid[row][col] = 1;
                    if (solve(row, col, 2)) {
                        countPossibleMoves++;
                        printBoard();
                        nullBoard();
                    }
                }
            }
        }
        System.out.println("Possible moves are from " + countPossibleMoves + " positions");
*/
        
        // random coordinates for knight start position
        generateCoordinates();
        grid[row][col] = 1;
        if (solve(row, col, 2)) {
            printBoard();
        }
        else {
            System.out.println("The knight does not walk the whole chess board");
            System.exit(-2);
        }

    }

    public static void generateCoordinates() {
        KnightsTour.row = 2 + (int) (Math.random() * base);
        KnightsTour.col = 2 + (int) (Math.random() * base);

    }

    private static boolean solve(int r, int c, int count) {
        if (total == 0) {
            return false;
        }
        // board 1x1, knight is standing
        if (count > total) {
            return true;
        }

        // make a list to host neighbors(positions for knight to jump) and how many possible neighbors there are
        List<int[]> neighbors = getNeighbors(r, c);

        // false is there are no neighbours and the board is not fully crossed
        if (neighbors.isEmpty() && count != total) {
            return false;
        }

        // lines up neighbors by the Warnsdorff rule - to find out which neighbor has lowest number of neighbors
        Collections.sort(neighbors, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {

                return a[2] - b[2];
            }
        });

        // r - lowest number of neighbors x, c - y coordinate.
        for (int[] nb : neighbors) {
            r = nb[0];
            c = nb[1];
            grid[r][c] = count; // assign coordinates as the next move
            if (!orphanDetected(count, r, c) && solve(r, c, count + 1)) {
                return true;
            }
            grid[r][c] = 0;
        }

        return false;
    }

    // count number of possible moves fot a knight
    private static List<int[]> getNeighbors(int r, int c) {

        List<int[]> nbrs = new ArrayList<>();

        // if free spot to go(0) - put in that place possible future neighbors
        for (int[] m : moves) {
            int x = m[0];
            int y = m[1];
            if (grid[r + y][c + x] == 0) {
                int num = getNeighborsNeighborsCount(r + y, c + x);
                nbrs.add(new int[]{r + y, c + x, num});
            }
        }
        return nbrs;
    }

    // find a number of possible moves from a perspective (future) position
    private static int getNeighborsNeighborsCount(int r, int c) {
        int num = 0;
        for (int[] m : moves) {
            if (grid[r + m[1]][c + m[0]] == 0) {
                num++;
            }
        }
        return num;
    }

    private static boolean orphanDetected(int cnt, int r, int c) {
        if (cnt < total - 1) {
            List<int[]> nbrs = getNeighbors(r, c);
            for (int[] nb : nbrs) {
                if (getNeighborsNeighborsCount(nb[0], nb[1]) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void printBoard() {
        for (int[] row : grid) {
            for (int i : row) {
                if (i == -1) continue;
                System.out.printf("%-4d ", i);
            }
            System.out.println();
        }
    }

    public static void nullBoard() {
        for (int r = 2; r < base+2; r++) {
            for (int c = 2; c < base+2; c++) {
                    grid[r][c] = 0;
                }
            }
    }
    // possible failures if the board size is uneven number
    public static boolean isTourNotPossible() {
        if (base > 4) {
            if (base % 2 != 0) {
                if ((row == 3 && col == 2) || (row == 2 && col == 3)) { // left top
                    return true;
                }
                else if ((row == 2 && col == base) || (row == 3 && col == (base + 1))) { // right top
                    return true;
                }
                else if ((row == base && col == 2) || (row == (base + 1) && col == 3)) { // left bottom
                    return true;
                }
                else if ((row == base && col == (base + 1)) || (row == (base + 1) && col == base)) { // right bottom
                    return true;
                }
                else {
                    int num = Math.abs(base - (row-2 + col-2));
                    return (num == 0 || (num % 2 == 0 && num != 1));
                    }
                }
            }
        return false;
    }
}
