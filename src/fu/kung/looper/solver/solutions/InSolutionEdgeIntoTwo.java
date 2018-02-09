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

public class InSolutionEdgeIntoTwo extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 2) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      if (face.faceEdgesUsingDotMatch(dot, Status.UNDECIDED)) {
        if (dot.getMatchingEdges(Status.UNDECIDED).size() == 3
            && dot.getMatchingEdges(Status.IN_SOLUTION).size() == 1) {
          // There are 2 non-face edges coming in that are one UNDECIDED and one IN_SOLUTION

          if (face.getMatchingEdges(Status.IN_SOLUTION).size() == 1
              && face.getMatchingEdges(Status.OUT_SOLUTION).size() == 1) {
            // 2 in-face edges are UNDECIDED, one is OUT, and one is IN
            Set<Edge> edge = Sets
                .difference(dot.getMatchingEdges(Status.UNDECIDED), face.getEdgesUsingDot(dot));
            if (edge.size() != 1) {
              throw new IllegalStateException("There can be only one!");
            }
            mutations.add(new GridMutation(edge.iterator().next(), Status.OUT_SOLUTION));
          }
        }
      }
    }
  }
}
