package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

/**
 * If one UNDECIDED edge is going into a face and one and only one of that face's edges that also go
 * into the same dot could be in the solution, then the original edge is IN_SOLUTION.
 */
public class UndecidedEdgeInto extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() == -1) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      Edge incomingEdge = dot.getSingleEdgeNotInFaceMatching(face, Status.UNDECIDED);
      if (incomingEdge != null) {
        if (face.getMatchingEdgesUsingDot(dot, Status.UNDECIDED).size() == 2) {
          if (face.getClue() == face.getMatchingEdges(Status.IN_SOLUTION).size() + 1
              && face.getMatchingEdges(Status.UNDECIDED).size() == 2) {
            mutations.add(new GridMutation(incomingEdge));
          }
        }
      }
    }
  }
}
