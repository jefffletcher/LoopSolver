package fu.kung.looper.solver.solutions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import fu.kung.looper.solver.TestSolver;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Grid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MarkInitialFacesTest {

  @Test
  public void testThreeTwoThreeDiagonal() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{3, -1, -1, -1, 2, -1, -1, -1, 3});

    assertTrue(TestSolver.applySolution(grid, new MarkInitialFaces()));
    int[][] results = grid.getFaceStatus(Status.IN_SOLUTION);
    assertArrayEquals(new int[]{9, 0, 0}, results[0]);
    assertArrayEquals(new int[]{0, 0, 0}, results[1]);
    assertArrayEquals(new int[]{0, 0, 6}, results[2]);

    assertFalse(TestSolver.applySolution(grid, new MarkInitialFaces()));
  }

  @Test
  public void testThreeThreeDiagonal() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{3, -1, -1, 3});

    assertTrue(TestSolver.applySolution(grid, new MarkInitialFaces()));
    int[][] results = grid.getFaceStatus(Status.IN_SOLUTION);
    assertArrayEquals(new int[]{9, 0}, results[0]);
    assertArrayEquals(new int[]{0, 6}, results[1]);

    assertFalse(TestSolver.applySolution(grid, new MarkInitialFaces()));
  }
}