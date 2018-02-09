package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Sets;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

public class OneWithALongEdge extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 1) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      ImmutableSet<Edge> dotEdges = dot.getMatchingEdges(Status.UNDECIDED);
      if (Sets.intersection(
          dot.getMatchingEdges(Status.UNDECIDED),
          face.getMatchingEdges(Status.UNDECIDED)).size() == 2) {

        if (dot.getMatchingEdges(Status.OUT_SOLUTION).size() == dot.getEdges().size() - 2) {
          for (Edge edge : dotEdges) {
            mutations.add(new GridMutation(edge, Status.OUT_SOLUTION));
          }
          return;
        }
      }
    }
  }
}
