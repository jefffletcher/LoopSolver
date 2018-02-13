package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Sets;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;
import java.util.Set;

/**
 * If an IN_SOLUTION edge enters a face with clue 3, then the edges meeting at the opposite corner
 * should be marked IN_SOLUTION.
 */
public class EdgeIntoAThree extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() == 3) {
      for (Dot dot : face.getGridDots()) {
        if (face.getMatchingEdgesUsingDot(dot, Status.UNDECIDED).size() == 2) {
          if (dot.getMatchingEdges(Status.IN_SOLUTION).size() == 1) {
            for (Edge edge : face.getEdgesOppositeDot(dot)) {
              mutations.add(new GridMutation(edge));
            }
            Set<Edge> undecidedEdges = Sets
                .difference(dot.getMatchingEdges(Status.UNDECIDED), face.getEdgesUsingDot(dot));
            if (undecidedEdges.size() == 1) {
              mutations.add(
                  new GridMutation(undecidedEdges.iterator().next(), Status.OUT_SOLUTION));
            }
          }
        }
      }
    }
  }
}
