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
public class EdgeIntoAThreeTest {

  @Test
  public void testWithUndecided() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{-1, -1, -1, 3});
    Face face = grid.getFace(1, 0);
    face.getEdge(DIR.N).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new EdgeIntoAThree()));
    assertArrayEquals(new int[]{1, 6}, grid.getFaceStatus(Status.IN_SOLUTION)[1]);
    assertArrayEquals(new int[]{4, 8}, grid.getFaceStatus(Status.OUT_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new EdgeIntoAThree()));
  }

  @Test
  public void testSimpleCase() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{-1, -1, -1, 3});
    grid.getFace(0, 0).getEdge(DIR.S).setStatus(Status.IN_SOLUTION);
    grid.getFace(0, 0).getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new EdgeIntoAThree()));
    assertArrayEquals(new int[]{1, 6}, grid.getFaceStatus(Status.IN_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new EdgeIntoAThree()));
  }

  @Test
  public void testEdge() {
    Grid grid = new Grid(2, 1);
    grid.setGridClues(new int[]{-1, 3});
    grid.getFace(0, 0).getEdge(DIR.W).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new EdgeIntoAThree()));
    assertArrayEquals(new int[]{6}, grid.getFaceStatus(Status.IN_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new EdgeIntoAThree()));
  }
}