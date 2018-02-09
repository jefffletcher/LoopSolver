package fu.kung.looper.solver.solutions;

import static fu.kung.looper.solver.grid.Edge.Status;

import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

/**
 * If the count of remaining edges equals the clue, mark remaining edges as IN_SOLUTION.
 */
public class UseAllRemainingEdges extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getMatchingEdges(Status.UNDECIDED, Status.IN_SOLUTION).size() == face.getClue()) {
      for (Edge edge : face.getMatchingEdges(Status.UNDECIDED)) {
        mutations.add(new GridMutation(edge));
      }
    }
  }
}
