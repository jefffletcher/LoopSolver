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
public class UndecidedEdgeIntoTest {

  @Test
  public void testOneClue() {
    Grid grid = new Grid(1, 2);
    grid.setGridClues(new int[]{-1, 1});
    Face face = grid.getFace(0, 1);
    face.getEdge(DIR.S).setStatus(Status.OUT_SOLUTION);
    face.getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new UndecidedEdgeInto()));
    assertArrayEquals(new int[]{1, 0}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);

    assertFalse(TestSolver.applySolution(grid, new UndecidedEdgeInto()));
  }

  @Test
  public void testTwoClue() {
    Grid grid = new Grid(1, 2);
    grid.setGridClues(new int[]{-1, 2});
    Face face = grid.getFace(0, 1);
    face.getEdge(DIR.S).setStatus(Status.OUT_SOLUTION);
    face.getEdge(DIR.E).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new UndecidedEdgeInto()));
    assertArrayEquals(new int[]{1, 4}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);
    assertArrayEquals(new int[]{0, 2}, grid.getFaceStatus(Status.OUT_SOLUTION)[0]);
    assertArrayEquals(new int[]{14, 9}, grid.getFaceStatus(Status.UNDECIDED)[0]);

    assertFalse(TestSolver.applySolution(grid, new UndecidedEdgeInto()));
  }

  @Test
  public void testThreeClue() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{-1, -1, -1, 3});
    grid.getFace(0, 0).getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);
    Face face = grid.getFace(1, 1);
    face.getEdge(DIR.S).setStatus(Status.IN_SOLUTION);
    face.getEdge(DIR.E).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new UndecidedEdgeInto()));

    assertArrayEquals(new int[]{2, 0}, grid.getFaceStatus(Status.IN_SOLUTION)[0]);
    assertArrayEquals(new int[]{1, 6}, grid.getFaceStatus(Status.IN_SOLUTION)[1]);
    assertArrayEquals(new int[]{4, 8}, grid.getFaceStatus(Status.OUT_SOLUTION)[0]);
    assertArrayEquals(new int[]{0, 0}, grid.getFaceStatus(Status.OUT_SOLUTION)[1]);
    assertArrayEquals(new int[]{9, 7}, grid.getFaceStatus(Status.UNDECIDED)[0]);
    assertArrayEquals(new int[]{14, 9}, grid.getFaceStatus(Status.UNDECIDED)[1]);

    assertFalse(TestSolver.applySolution(grid, new UndecidedEdgeInto()));
  }

  @Test
  public void testThreeNotApplied() {
    Grid grid = new Grid(1, 3);
    grid.setGridClues(new int[]{-1, 3, -1});

    assertFalse(TestSolver.applySolution(grid, new UndecidedEdgeInto()));
  }

  @Test
  public void testOneNotApplied() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{-1, -1, -1, 1, -1, -1, -1, -1, -1});

    assertFalse(TestSolver.applySolution(grid, new UndecidedEdgeInto()));
  }

  @Test
  public void testOddCase() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{1, -1, -1, -1, 2, 3, -1, -1, -1});
    Face face1 = grid.getFace(0, 1);
    face1.getEdge(DIR.N).setStatus(Status.OUT_SOLUTION);
    face1.getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);
    Face face2 = grid.getFace(0, 2);
    face2.getEdge(DIR.N).setStatus(Status.OUT_SOLUTION);
    face2.getEdge(DIR.E).setStatus(Status.OUT_SOLUTION);

    assertFalse(TestSolver.applySolution(grid, new UndecidedEdgeInto()));
  }
}