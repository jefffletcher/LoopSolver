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
  public void testSimpleCase() {
    Grid grid = new Grid(1, 2);
    grid.setGridClues(new int[]{-1, 3});
    Face face = grid.getFace(0, 0);
    face.getEdge(DIR.N).setStatus(Status.IN_SOLUTION);

    assertArrayEquals(new int[]{1, 0}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertTrue(TestSolver.applySolution(grid, new EdgeIntoAThree()));
    assertArrayEquals(new int[]{1, 6}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new EdgeIntoAThree()));
  }
}