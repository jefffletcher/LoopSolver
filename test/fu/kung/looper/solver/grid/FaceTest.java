package fu.kung.looper.solver.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FaceTest {

  @Test
  public void testGetOppositeFace() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1});
    Face centerFace = grid.getFace(1, 1);
    assertEquals(grid.getFace(0, 0), centerFace.getOppositeFace(grid, new Dot(1, 1)));
    assertEquals(grid.getFace(0, 2), centerFace.getOppositeFace(grid, new Dot(1, 2)));
    assertEquals(grid.getFace(2, 0), centerFace.getOppositeFace(grid, new Dot(2, 1)));
    assertEquals(grid.getFace(2, 2), centerFace.getOppositeFace(grid, new Dot(2, 2)));

    grid = new Grid(1, 1);
    grid.setGridClues(new int[]{-1});
    centerFace = grid.getFace(0, 0);
    for (Dot dot : centerFace.getGridDots()) {
      assertNull(centerFace.getOppositeFace(grid, dot));
    }
  }
}