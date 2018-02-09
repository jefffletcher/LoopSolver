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
 * If one IN_SOLUTION edge is going into a face of clue 1 then the two edges opposite where the edge
 * comes in should be marked OUT_SOLUTION.
 */
public class InSolutionEdgeInto extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 1 && face.getClue() != 2) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      if (dot.getSingleEdgeNotInFaceMatching(face, Status.IN_SOLUTION) != null
          && Sets.intersection(dot.getMatchingEdges(Status.UNDECIDED),
          face.getMatchingEdges(Status.UNDECIDED)).size() == 2) {
        if (face.getClue() == 1) {
          for (Edge edge : face.getEdgesOppositeDot(dot)) {
            mutations.add(new GridMutation(edge, Status.OUT_SOLUTION));
          }
        } else if (face.getClue() == 2) {
          if (face.getMatchingEdges(Status.IN_SOLUTION).size() == 0
              && face.getMatchingEdges(Status.UNDECIDED).size() == 4) {
            Dot oppositeDot = face.getDotOppositeDot(dot);
            if (oppositeDot.getMatchingEdges(Status.UNDECIDED).size() == 3) {
              Face oppositeFace = face.getOppositeFace(grid, oppositeDot);
              if (oppositeFace == null || (oppositeFace.getClue()
                  < oppositeFace.getMatchingEdges(Status.IN_SOLUTION).size())) {
                mutations.add(
                    new GridMutation(Sets.difference(
                        oppositeDot.getMatchingEdges(Status.UNDECIDED),
                        face.getEdgesUsingDot(oppositeDot)).iterator().next()));
              }
            }
          } else if (face.getMatchingEdges(Status.IN_SOLUTION).size() == 1) {
            for (Edge edge : face.getEdgesOppositeDot(dot)) {
              if (edge.getStatus() != Status.IN_SOLUTION) {
                mutations.add(new GridMutation(edge, Status.OUT_SOLUTION));
              }
            }
          }
        }
      }
      if (face.getClue() == 2
          && face.getMatchingEdges(Status.UNDECIDED).size() == 3
          && face.getMatchingEdges(Status.OUT_SOLUTION).size() == 1
          && dot.getMatchingEdges(Status.UNDECIDED).size() == 3
          && dot.getMatchingEdges(Status.IN_SOLUTION).size() == 1) {
        List<Edge> nonFaceEdges = Lists.newArrayList(
            Sets.difference(dot.getEdges(), face.getEdgesUsingDot(dot)));
        if (nonFaceEdges.get(0).getStatus() == Status.UNDECIDED) {
          mutations.add(new GridMutation(nonFaceEdges.get(0), Status.OUT_SOLUTION));
        } else {
          mutations.add(new GridMutation(nonFaceEdges.get(1), Status.OUT_SOLUTION));
        }
      }
    }
  }
}
