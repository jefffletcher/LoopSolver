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

/**
 * If a face has clue of 2, has one pair of edges with no other edges coming into the shared dot,
 * and one edge ends in a dot (not the shared one) with only one incoming edge, mark that incoming
 * edge with IN_SOLUTION.
 */
public class TwoInACorner extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() == 2) {
      for (Dot dot : face.getGridDots()) {
        if (dot.getEdges().size() == 2) {
          Set<Edge> joiningEdges = face.getMatchingEdgesUsingDot(dot, Status.UNDECIDED);
          if (joiningEdges.size() == 2) {
            for (Edge edge : joiningEdges) {
              Dot otherDot = edge.getOtherDot(dot);
              Set<Edge> otherDotEdges =
                  Sets.newHashSet(otherDot.getMatchingEdges(Status.UNDECIDED));
              otherDotEdges.removeAll(face.getEdgesUsingDot(otherDot));
              if (otherDotEdges.size() == 1) {
                mutations.add(new GridMutation(otherDotEdges.iterator().next()));
              }
            }
          }
        }
      }
    }
  }
}
