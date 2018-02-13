package fu.kung.looper.solver.solutions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import fu.kung.looper.solver.TestSolver;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.Grid.DIR;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UndecidedIntoOneTest {

  @Test
  public void testClueOne() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{1, -1, -1, 1});
    grid.getFace(0, 0).getEdge(DIR.N).setStatus(Status.OUT_SOLUTION);
    grid.getFace(0, 0).getEdge(DIR.W).setStatus(Status.OUT_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new UndecidedIntoOne()));
    assertArrayEquals(new int[]{0, 6}, grid.getFaceStatus(Status.OUT_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new UndecidedIntoOne()));
  }
  @Test
  public void testClueThree() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{3, -1, -1, 1});
    grid.getFace(0, 0).getEdge(DIR.N).setStatus(Status.IN_SOLUTION);
    grid.getFace(0, 0).getEdge(DIR.W).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new UndecidedIntoOne()));
    assertArrayEquals(new int[]{0, 6}, grid.getFaceStatus(Status.OUT_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new UndecidedIntoOne()));
  }

  @Test
  public void testClueThreeNotApplied() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{3, -1, -1, 1});
    grid.getFace(0, 0).getEdge(DIR.N).setStatus(Status.IN_SOLUTION);
    grid.getFace(0, 0).getEdge(DIR.S).setStatus(Status.IN_SOLUTION);

    assertFalse(TestSolver.applySolution(grid, new UndecidedIntoOne()));
  }

}