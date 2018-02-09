package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;
import java.util.Set;

/**
 * Removes edges that aren't connected on one end. Will keep iterating until no changes are made.
 */
public class DanglingEdge extends Solution {

  @Override
  public boolean shouldRepeat() {
    return true;
  }

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    for (Dot dot : face.getGridDots()) {
      Set<Edge> matchingEdges = dot.getMatchingEdges(Status.UNDECIDED, Status.IN_SOLUTION);
      if (matchingEdges.size() == 1) {
        matchingEdges.stream().forEach(
            e -> mutations.add(new GridMutation(e, Status.OUT_SOLUTION)));
      }
    }
  }
}
