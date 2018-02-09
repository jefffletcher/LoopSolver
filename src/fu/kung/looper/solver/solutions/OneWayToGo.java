package fu.kung.looper.solver.solutions;

import static fu.kung.looper.solver.grid.Edge.Status;

import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;
import java.util.Set;

/**
 * If a Dot has only one IN_SOLUTION edge and one UNDECIDED edge, mark the UNDECIDED edge as
 * IN_SOLUTION.
 */
public class OneWayToGo extends Solution {

  @Override
  public boolean shouldRepeat() {
    return true;
  }

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    for (Dot dot : face.getGridDots()) {
      if (dot.getMatchingEdges(Status.IN_SOLUTION).size() == 1) {
        Set<Edge> undecidedEdges = dot.getMatchingEdges(Status.UNDECIDED);
        if (undecidedEdges.size() == 1) {
          mutations.add(new GridMutation(undecidedEdges.iterator().next()));
        }
      }
    }
  }
}
