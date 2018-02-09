package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;
import java.util.List;

/**
 * If two IN_SOLUTION edges are going into a face of clue 2 then the two edges going into those same
 * dots should be marked OUT_SOLUTION.
 */
public class InSolutionEdgesIntoTwo extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 2 || face.getMatchingEdges(Status.UNDECIDED).size() != 4) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      List<Edge> edges =
          Lists.newArrayList(Sets.difference(dot.getEdges(), face.getEdgesUsingDot(dot)));
      List<Edge> oppositeEdges =
          Lists.newArrayList(
              Sets.difference(
                  face.getDotOppositeDot(dot).getEdges(),
                  face.getEdgesUsingDot(face.getDotOppositeDot(dot))));
      if (matchesCriteria(edges) && matchesCriteria(oppositeEdges)) {
        if (edges.size() == 2) {
          if (edges.get(0).getStatus() == Status.UNDECIDED) {
            mutations.add(new GridMutation(edges.get(0), Status.OUT_SOLUTION));
          } else {
            mutations.add(new GridMutation(edges.get(1), Status.OUT_SOLUTION));
          }
        }
        if (oppositeEdges.size() == 2) {
          if (oppositeEdges.get(0).getStatus() == Status.UNDECIDED) {
            mutations.add(new GridMutation(oppositeEdges.get(0), Status.OUT_SOLUTION));
          } else {
            mutations.add(new GridMutation(oppositeEdges.get(1), Status.OUT_SOLUTION));
          }
        }
      }
    }
  }

  private boolean matchesCriteria(List<Edge> edges) {
    if (edges.size() == 1) {
      return edges.get(0).getStatus() == Status.IN_SOLUTION;
    }
    if (edges.get(0).getStatus() == Status.IN_SOLUTION
        && edges.get(1).getStatus() == Status.UNDECIDED) {
      return true;
    }
    if (edges.get(0).getStatus() == Status.UNDECIDED
        && edges.get(1).getStatus() == Status.IN_SOLUTION) {
      return true;
    }

    return false;
  }
}
