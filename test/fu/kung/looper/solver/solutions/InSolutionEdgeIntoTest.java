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
public class InSolutionEdgeIntoTest {

  @Test
  public void testOneClue() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{-1, -1, -1, 1});
    grid.getFace(0, 0).getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);
    grid.getFace(1, 0).getEdge(DIR.N).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new InSolutionEdgeInto()));
    assertArrayEquals(new int[]{0, 6}, grid.getFaceStatus(Status.OUT_SOLUTION)[1]);
    assertArrayEquals(new int[]{1, 0}, grid.getFaceStatus(Status.IN_SOLUTION)[1]);
    assertArrayEquals(new int[]{14, 9}, grid.getFaceStatus(Status.UNDECIDED)[1]);

    assertFalse(TestSolver.applySolution(grid, new InSolutionEdgeInto()));
  }

  @Test
  public void testOneNotApplied() {
    Grid grid = new Grid(2, 3);
    grid.setGridClues(new int[]{3, 1, -1, 3, 1, -1});
    grid.getFace(0, 0).getEdge(DIR.S).setStatus(Status.IN_SOLUTION);
    assertFalse(TestSolver.applySolution(grid, new InSolutionEdgeInto()));
  }

  @Test
  public void testTwoClue() {
    Grid grid = new Grid(1, 2);
    grid.setGridClues(new int[]{-1, 2});
    Face face = grid.getFace(0, 0);
    face.getEdge(DIR.N).setStatus(Status.IN_SOLUTION);
    Face face2 = grid.getFace(0, 1);
    face2.getEdge(DIR.E).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new InSolutionEdgeInto()));
    assertArrayEquals(new int[]{0, 2}, grid.getFaceStatus(Status.OUT_SOLUTION)[0]);
    assertArrayEquals(new int[]{1, 4}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new InSolutionEdgeInto()));
  }

  @Test
  public void testTwoWithThreeUndecided() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{-1, -1, -1, 2});
    Face face = grid.getFace(0, 0);
    face.getEdge(DIR.S).setStatus(Status.IN_SOLUTION);
    Face face2 = grid.getFace(1, 1);
    face2.getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new InSolutionEdgeInto()));
    assertArrayEquals(new int[]{4, 8}, grid.getFaceStatus(Status.OUT_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new InSolutionEdgeInto()));
  }

  @Test
  public void testMultipleTwo() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{2, 3, 2, 2});
    Face face = grid.getFace(0, 0);
    face.getEdge(DIR.W).setStatus(Status.IN_SOLUTION);
    Face face2 = grid.getFace(1, 1);
    face2.getEdge(DIR.S).setStatus(Status.IN_SOLUTION);

    assertFalse(TestSolver.applySolution(grid, new InSolutionEdgeInto()));
  }
}