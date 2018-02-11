package fu.kung.looper.solver;

import static org.junit.Assert.assertTrue;

import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Grid;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LoopSolverTest {

  @Test
  public void testSevenBySevenEasy() {
    Grid grid = new Grid(7, 7);
    grid.setGridClues(
        new int[]{0, 2, -1, 3, 3, -1, 3,
            -1, 2, 1, -1, -1, 3, -1,
            -1, -1, 2, -1, -1, 2, 1,
            3, -1, 2, -1, -1, 2, -1,
            -1, -1, -1, 3, -1, -1, -1,
            3, -1, -1, 3, 1, -1, -1,
            -1, 1, 1, 3, 1, 1, -1});

    LoopSolver solver = new LoopSolver(grid);
    solver.solve();
    // grid.outputSvg("loopout.svg");

    assertTrue(
        Arrays.deepEquals(grid.getFaceStatus(Status.IN_SOLUTION),
            new int[][]{
                {0, 6, 13, 14, 13, 12, 13},
                {6, 9, 2, 3, 4, 14, 12},
                {11, 4, 9, 5, 10, 3, 4},
                {7, 12, 10, 2, 1, 5, 12},
                {11, 0, 1, 7, 8, 6, 12},
                {7, 8, 4, 11, 4, 11, 6},
                {11, 2, 2, 7, 8, 1, 1}}));
    assertTrue(
        Arrays.deepEquals(grid.getFaceStatus(Status.OUT_SOLUTION),
            new int[][]{
                {15, 9, 2, 1, 2, 3, 2},
                {9, 6, 13, 12, 11, 1, 3},
                {4, 11, 6, 10, 5, 12, 11},
                {8, 3, 5, 13, 14, 10, 3},
                {4, 15, 14, 8, 7, 9, 3},
                {8, 7, 11, 4, 11, 4, 9},
                {4, 13, 13, 8, 7, 14, 14}}));
    assertTrue(
        Arrays.deepEquals(grid.getFaceStatus(Status.UNDECIDED),
            new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}}));
  }

  @Test
  public void testTenByTenTricky() {
    Grid grid = new Grid(10, 10);
    grid.setGridClues(
        new int[]{
            3, -1, 1, 2, -1, -1, 3, 1, -1, 3,
            -1, -1, -1, 3, -1, -1, -1, 2, 2, -1,
            -1, 2, 1, 2, 2, 1, -1, -1, 3, -1,
            2, 2, -1, 3, -1, 3, -1, 1, -1, 2,
            -1, 2, -1, -1, 1, -1, -1, -1, 2, -1,
            3, 1, 3, -1, -1, 2, 3, 1, 1, -1,
            2, -1, -1, 2, -1, -1, 2, -1, -1, -1,
            -1, -1, -1, -1, 1, -1, -1, -1, -1, 3,
            2, 3, -1, 2, 2, -1, 1, -1, -1, 3,
            2, 2, 2, 2, -1, 2, 2, -1, -1, 3
        }
    );

    LoopSolver solver = new LoopSolver(grid);
    solver.solve();
    // grid.outputSvg("loopout.svg");

    assertTrue(
        Arrays.deepEquals(grid.getFaceStatus(Status.IN_SOLUTION),
            new int[][]{
                {11, 3, 1, 3, 7, 12, 11, 1, 3, 7},
                {3, 7, 12, 11, 3, 0, 5, 12, 9, 3},
                {9, 3, 2, 3, 5, 8, 6, 12, 14, 13},
                {12, 9, 3, 7, 12, 14, 9, 2, 1, 6},
                {12, 12, 11, 1, 2, 1, 6, 13, 12, 11},
                {14, 8, 7, 12, 13, 12, 11, 4, 8, 7},
                {3, 4, 11, 6, 12, 10, 5, 12, 12, 11},
                {13, 8, 3, 3, 2, 7, 12, 12, 8, 7},
                {12, 14, 9, 3, 3, 1, 4, 12, 12, 11},
                {10, 3, 6, 9, 5, 10, 6, 12, 10, 7}}));
    assertTrue(
        Arrays.deepEquals(grid.getFaceStatus(Status.OUT_SOLUTION),
            new int[][]{
                {4, 12, 14, 12, 8, 3, 4, 14, 12, 8},
                {12, 8, 3, 4, 12, 15, 10, 3, 6, 12},
                {6, 12, 13, 12, 10, 7, 9, 3, 1, 2},
                {3, 6, 12, 8, 3, 1, 6, 13, 14, 9},
                {3, 3, 4, 14, 13, 14, 9, 2, 3, 4},
                {1, 7, 8, 3, 2, 3, 4, 11, 7, 8},
                {12, 11, 4, 9, 3, 5, 10, 3, 3, 4},
                {2, 7, 12, 12, 13, 8, 3, 3, 7, 8},
                {3, 1, 6, 12, 12, 14, 11, 3, 3, 4},
                {5, 12, 9, 6, 10, 5, 9, 3, 5, 8}}));
  }

  @Test
  public void testTenByTenTricky_2() {
    Grid grid = new Grid(10, 10);
    grid.setGridClues(
        new int[]{
            2, 2, -1, 1, -2, 3, -1, 3, 2, -1,
            -1, 3, -1, -1, 1, 2, -1, 2, -1, -1,
            -1, -1, 2, 2, 1, -1, -1, 2, 2, 2,
            3, 3, 2, -1, 2, 2, -1, 2, -1, 2,
            2, 1, 3, -1, 3, -1, 3, 1, 2, 2,
            -1, 2, 2, 1, 2, 0, -1, 2, -1, -1,
            2, 2, -1, -1, -1, -1, 3, 2, 2, -1,
            -1, -1, 1, -1, 2, -1, 2, -1, 2, 2,
            2, -1, 2, -1, -1, 3, -1, -1, -1, -1,
            -1, 2, 1, 3, -1, -1, -1, -1, -1, 3
        }
    );

    LoopSolver solver = new LoopSolver(grid);
    solver.solve();
    // grid.outputSvg("loopout.svg");
    assertTrue(
        Arrays.deepEquals(grid.getFaceStatus(Status.IN_SOLUTION),
            new int[][]{
                {9, 3, 7, 8, 4, 13, 12, 13, 12, 13},
                {12, 11, 3, 0, 4, 12, 12, 12, 12, 12},
                {8, 3, 5, 10, 4, 12, 12, 12, 12, 12},
                {14, 13, 10, 5, 12, 12, 12, 12, 14, 12},
                {3, 2, 7, 12, 14, 12, 14, 8, 3, 6},
                {11, 3, 3, 2, 3, 0, 3, 6, 9, 3},
                {3, 3, 3, 3, 7, 12, 11, 3, 6, 13},
                {11, 3, 1, 3, 3, 0, 3, 1, 3, 6},
                {3, 7, 12, 11, 5, 14, 13, 12, 11, 3},
                {11, 3, 2, 7, 8, 1, 4, 10, 3, 7}}));

    assertTrue(
        Arrays.deepEquals(grid.getFaceStatus(Status.OUT_SOLUTION),
            new int[][]{
                {6, 12, 8, 7, 11, 2, 3, 2, 3, 2},
                {3, 4, 12, 15, 11, 3, 3, 3, 3, 3},
                {7, 12, 10, 5, 11, 3, 3, 3, 3, 3},
                {1, 2, 5, 10, 3, 3, 3, 3, 1, 3},
                {12, 13, 8, 3, 1, 3, 1, 7, 12, 9},
                {4, 12, 12, 13, 12, 15, 12, 9, 6, 12},
                {12, 12, 12, 12, 8, 3, 4, 12, 9, 2},
                {4, 12, 14, 12, 12, 15, 12, 14, 12, 9},
                {12, 8, 3, 4, 10, 1, 2, 3, 4, 12},
                {4, 12, 13, 8, 7, 14, 11, 5, 12, 8}}));
  }
}