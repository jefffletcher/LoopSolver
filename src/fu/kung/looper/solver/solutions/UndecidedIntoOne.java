package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

public class UndecidedIntoOne extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 1) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      if (dot.getMatchingEdges(Status.UNDECIDED).size() == 4) {
        Face oppositeFace = face.getOppositeFace(grid, dot);
        if (oppositeFace.getClue() == 3) {
          if (oppositeFace.getMatchingEdges(Status.IN_SOLUTION).size() == 2) {
            for (Edge edge : face.getEdgesUsingDot(face.getDotOppositeDot(dot))) {
              mutations.add(new GridMutation(edge, Status.OUT_SOLUTION));
            }
          }
        }
      }
    }
  }
}
