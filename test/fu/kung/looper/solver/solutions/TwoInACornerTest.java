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
public class TwoInACornerTest {

  @Test
  public void testSimpleCase() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{-1, 2, 2, -1});

    assertTrue(TestSolver.applySolution(grid, new TwoInACorner()));
    int[][] results = grid.getFaceStatus(Status.IN_SOLUTION);
    assertArrayEquals(new int[]{9, 0}, results[0]);
    assertArrayEquals(new int[]{0, 6}, results[1]);

    assertFalse(TestSolver.applySolution(grid, new TwoInACorner()));
  }
}