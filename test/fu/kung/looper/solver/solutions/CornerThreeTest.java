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
public class CornerThreeTest {

  @Test
  public void testOutsideCorner() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{3, -1, -1, -1});

    assertTrue(TestSolver.applySolution(grid, new CornerThree()));
    assertArrayEquals(new int[]{9, 0}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new CornerThree()));
  }

  @Test
  public void testInsideCorner() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{-1, -1, -1, -1, 3, -1, -1, -1, -1});
    Face face = grid.getFace(2, 2);
    face.getEdge(DIR.N).setStatus(Status.OUT_SOLUTION);
    face.getEdge(DIR.W).setStatus(Status.OUT_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new CornerThree()));
    assertArrayEquals(new int[]{0, 6, 8}, grid.getFaceStatus(Status.IN_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new CornerThree()));
  }

  @Test
  public void testNotApplied() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{-1, -1, -1, -1, 3, -1, -1, -1, -1});
    Face face = grid.getFace(2, 2);
    face.getEdge(DIR.N).setStatus(Status.IN_SOLUTION);
    face.getEdge(DIR.W).setStatus(Status.OUT_SOLUTION);

    assertFalse(TestSolver.applySolution(grid, new CornerThree()));
  }

}