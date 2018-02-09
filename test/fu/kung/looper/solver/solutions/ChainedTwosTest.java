package fu.kung.looper.solver.solutions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import fu.kung.looper.solver.TestSolver;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.Grid.DIR;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ChainedTwosTest {

  @Test
  public void testChain_twoToOne() {
    Grid grid = new Grid(2, 3);
    grid.setGridClues(new int[]{-1, 2, -1, 1, -1, -1});
    Face face = grid.getFace(0, 2);
    face.getEdge(DIR.N).setStatus(Status.IN_SOLUTION);
    face.getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new ChainedTwos()));
    assertArrayEquals(new int[]{10, 0, 0}, grid.getFaceStatus(Status.OUT_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new ChainedTwos()));
  }

  @Test
  public void testChain_twoToTwo() {
    Grid grid = new Grid(2, 4);
    grid.setGridClues(new int[]{-1, -1, 2, -1, -1, 2, -1, -1});
    grid.getFace(1, 0).getEdge(DIR.W).setStatus(Status.OUT_SOLUTION);
    grid.getFace(1, 0).getEdge(DIR.S).setStatus(Status.IN_SOLUTION);
    grid.getFace(0, 2).getEdge(DIR.N).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new ChainedTwos()));
    assertArrayEquals(new int[]{0, 0, 4, 8}, grid.getFaceStatus(Status.OUT_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new ChainedTwos()));
  }

  @Test
  public void testChain_twoToTwoOnEdge() {
    Grid grid = new Grid(2, 4);
    grid.setGridClues(new int[]{-1, -1, 2, -1, -1, 2, -1, -1});
    grid.getFace(1, 0).getEdge(DIR.W).setStatus(Status.OUT_SOLUTION);
    grid.getFace(1, 0).getEdge(DIR.S).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new ChainedTwos()));
    assertArrayEquals(new int[]{0, 0, 0, 1}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new ChainedTwos()));
  }
}