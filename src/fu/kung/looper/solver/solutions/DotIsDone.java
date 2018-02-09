package fu.kung.looper.solver.solutions;

import static fu.kung.looper.solver.grid.Edge.Status;

import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

/**
 * If there are 2 IN_SOLUTION edges, mark the rest as OUT_SOLUTION.
 */
public class DotIsDone extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    for (Dot dot : face.getGridDots()) {
      if (dot.getMatchingEdges(Status.IN_SOLUTION).size() == 2) {
        for (Edge edge : dot.getMatchingEdges(Status.UNDECIDED)) {
          mutations.add(new GridMutation(edge, Status.OUT_SOLUTION));
        }
      }
    }
  }
}
