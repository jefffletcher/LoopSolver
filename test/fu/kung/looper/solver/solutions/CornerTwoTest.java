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
public class CornerTwoTest {

  @Test
  public void testThreeOnOppositeCorner() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{-1, 3, 2, -1});

    assertTrue(TestSolver.applySolution(grid, new CornerTwo()));
    assertArrayEquals(new int[]{10, 0}, grid.getFaceStatus(Status.IN_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new CornerTwo()));
  }

  @Test
  public void testTwoOnEdge() {
    Grid grid = new Grid(1, 3);
    grid.setGridClues(new int[]{-1, 2, -1});
    grid.getFace(0, 2).getEdge(DIR.N).setStatus(Status.OUT_SOLUTION);
    grid.getFace(0, 2).getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);

    assertFalse(TestSolver.applySolution(grid, new CornerTwo()));
  }

  @Test
  public void testInSolutionEdgeOnOppositeCorner() {
    Grid grid = new Grid(2, 3);
    grid.setGridClues(new int[]{-1, 2, -1, -1, -1, -1});
    grid.getFace(0, 0).getEdge(DIR.N).setStatus(Status.OUT_SOLUTION);
    grid.getFace(0, 0).getEdge(DIR.W).setStatus(Status.OUT_SOLUTION);
    grid.getFace(1, 1).getEdge(DIR.E).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new CornerTwo()));
    assertArrayEquals(new int[]{4, 9, 0}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new CornerTwo()));
  }
}