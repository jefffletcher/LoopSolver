package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

public class CornerThree extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 3) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      ImmutableSet<Edge> edges = face.getMatchingEdgesUsingDot(dot, Status.UNDECIDED);
      if (edges.size() == 2
          && (dot.getEdges().size() == dot.getMatchingEdges(Status.OUT_SOLUTION).size() + 2)) {
        for (Edge edge : edges) {
          mutations.add(new GridMutation(edge));
        }
      }
    }
  }
}
