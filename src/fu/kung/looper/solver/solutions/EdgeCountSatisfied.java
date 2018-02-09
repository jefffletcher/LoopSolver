package fu.kung.looper.solver.solutions;

import static fu.kung.looper.solver.grid.Edge.Status;

import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

/**
 * Mark remaining edges OUT_SOLUTION if the face's IN_SOLUTION edge count equals the clue. If the
 * face has 3 IN_SOLUTION edges, mark the 4th edge OUT_SOLUTION.
 */
public class EdgeCountSatisfied extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    int matchingCount = face.getMatchingEdges(Status.IN_SOLUTION).size();
    if (matchingCount == face.getClue() || matchingCount == 3) {
      for (Edge edge : face.getMatchingEdges(Status.UNDECIDED)) {
        mutations.add(new GridMutation(edge, Status.OUT_SOLUTION));
      }
    }
  }
}
