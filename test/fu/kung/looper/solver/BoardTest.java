package fu.kung.looper.solver;

import static org.junit.Assert.assertArrayEquals;

import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.solutions.DanglingEdge;
import fu.kung.looper.solver.solutions.EdgeCountSatisfied;
import org.junit.Test;

public class BoardTest {

  @Test
  public void testZeroClue() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{-1, -1, -1, -1, 0, -1, -1, -1, -1});
    LoopSolver solver = new LoopSolver(grid);

    solver.applySolution(new EdgeCountSatisfied());

    final int[][] walls = grid.getFaceStatus(Status.OUT_SOLUTION);
    assertArrayEquals(new int[]{0, 2, 0}, walls[0]);
    assertArrayEquals(new int[]{4, 15, 8}, walls[1]);
    assertArrayEquals(new int[]{0, 1, 0}, walls[2]);
  }

  @Test
  public void testDanglingEdge() {
    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{0, 1, 1, 2});
    LoopSolver solver = new LoopSolver(grid);

    solver.applySolution(new EdgeCountSatisfied());
    solver.applySolution(new DanglingEdge());

    final int[][] walls = grid.getFaceStatus(Status.OUT_SOLUTION);
    assertArrayEquals(new int[]{15, 13}, walls[0]);
    assertArrayEquals(new int[]{11, 0}, walls[1]);
  }
}