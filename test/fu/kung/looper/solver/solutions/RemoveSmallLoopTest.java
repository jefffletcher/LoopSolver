package fu.kung.looper.solver.solutions;

import static org.junit.Assert.assertArrayEquals;
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
public class RemoveSmallLoopTest {

  @Test
  public void testSimpleCase() {
    Grid grid = new Grid(1, 3);
    grid.setGridClues(new int[]{-1, -1, -1});
    Face faceLeft = grid.getFace(0, 0);
    faceLeft.getEdge(DIR.N).setStatus(Status.IN_SOLUTION);
    faceLeft.getEdge(DIR.W).setStatus(Status.IN_SOLUTION);
    faceLeft.getEdge(DIR.S).setStatus(Status.IN_SOLUTION);
    Face faceRight = grid.getFace(0, 2);
    faceRight.getEdge(DIR.N).setStatus(Status.IN_SOLUTION);
    faceRight.getEdge(DIR.E).setStatus(Status.IN_SOLUTION);
    faceRight.getEdge(DIR.S).setStatus(Status.IN_SOLUTION);

    assertArrayEquals(new int[]{11, 0, 7}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertTrue(TestSolver.applySolution(grid, new RemoveSmallLoop()));
    assertArrayEquals(new int[]{11, 0, 7}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertArrayEquals(new int[]{0, 7, 8}, grid.getFaceStatus(Status.UNDECIDED)[0]);
  }
}