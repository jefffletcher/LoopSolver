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
public class InSolutionEdgesIntoTwoTest {

  @Test
  public void testTwoInMiddle() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{-1, -1, -1, -1, 2, -1, -1, -1, -1});
    grid.getFace(0, 0).getEdge(DIR.E).setStatus(Status.IN_SOLUTION);
    grid.getFace(2, 2).getEdge(DIR.W).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new InSolutionEdgesIntoTwo()));
    assertArrayEquals(new int[]{1, 0, 2}, grid.getFaceStatus(Status.OUT_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new InSolutionEdgesIntoTwo()));
  }

  @Test
  public void testTwoOnEdge() {
    Grid grid = new Grid(3, 2);
    grid.setGridClues(new int[]{-1, -1, 2, -1, -1, -1});
    grid.getFace(0, 0).getEdge(DIR.W).setStatus(Status.IN_SOLUTION);
    grid.getFace(2, 1).getEdge(DIR.W).setStatus(Status.IN_SOLUTION);

    assertTrue(TestSolver.applySolution(grid, new InSolutionEdgesIntoTwo()));
    grid.outputSvg("loopout.svg");
    assertArrayEquals(new int[]{0, 2}, grid.getFaceStatus(Status.OUT_SOLUTION)[1]);

    assertFalse(TestSolver.applySolution(grid, new InSolutionEdgesIntoTwo()));

  }

  @Test
  public void testNotAppliedInCorner() {
    //TODO: Should this just apply?
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{-1, -1, -1, 2});
    grid.getFace(0, 1).getEdge(DIR.E).setStatus(Status.IN_SOLUTION);
    grid.getFace(1, 0).getEdge(DIR.S).setStatus(Status.IN_SOLUTION);

    assertFalse(TestSolver.applySolution(grid, new InSolutionEdgesIntoTwo()));
  }
}