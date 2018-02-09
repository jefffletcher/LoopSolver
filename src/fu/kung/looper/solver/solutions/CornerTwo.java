package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

public class CornerTwo extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 2 || face.getMatchingEdges(Status.UNDECIDED).size() != 4) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      ImmutableSet<Edge> edges = face.getMatchingEdgesUsingDot(dot, Status.UNDECIDED);
      if (edges.size() == 2
          && (dot.getEdges().size() == dot.getMatchingEdges(Status.OUT_SOLUTION).size() + 2)) {
        // dot is effectively the middle dot on an edge of length 2
        Dot oppositeDot = face.getDotOppositeDot(dot);
        Face oppositeFace = face.getOppositeFace(grid, oppositeDot);
        if (oppositeFace != null) {
          if ((oppositeDot.getMatchingEdges(Status.IN_SOLUTION).size() == 1)
              || (oppositeFace.getClue() == 3)) {
            for (Edge edge : edges) {
              mutations.add(new GridMutation(edge));
            }
          }
        }
      }
    }
  }
}
