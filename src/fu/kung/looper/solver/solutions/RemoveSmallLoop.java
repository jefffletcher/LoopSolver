package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;
import java.util.Set;

/**
 * Get all UNDECIDED edges, and iteratively try marking each one as IN_SOLUTION. If it creates a
 * loop that doesn't contain all IN_SOLUTION edges, it must be OUT_SOLUTION.
 */
public class RemoveSmallLoop extends Solution {

  @Override
  public ImmutableSet<GridMutation> mutate(Grid grid) {
    ImmutableSet.Builder<GridMutation> mutations = ImmutableSet.builder();
    for (Edge edge : grid.getMatchingEdges(Status.UNDECIDED)) {
      // This is temporary, just for peeking into the future.
      edge.setStatus(Status.IN_SOLUTION);
      Set<Edge> loopEdges = grid.getEdgesInLoop(edge);

      if (loopEdges == null) {
        // loop not found, revert edge status and move on
        edge.setStatus(Status.UNDECIDED);
      } else {
        if (loopEdges.size() == grid.getMatchingEdges(Status.IN_SOLUTION).size()) {
          // found a loop that contains all IN_SOLUTION edges, leave it
          // TODO: What if we found a small loop that happens to have all the current IN_SOLUTION
          // edges, but there would be more IN_SOLUTION edges if we continued?
        } else {
          // found a loop that's smaller than the whole solution, mark edge as OUT_SOLUTION
          mutations.add(new GridMutation(edge, Status.OUT_SOLUTION));
        }
        break;
      }
    }
    return mutations.build();
  }
}
