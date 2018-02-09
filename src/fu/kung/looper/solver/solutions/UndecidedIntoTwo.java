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

public class UndecidedIntoTwo extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 2 || face.getMatchingEdges(Status.UNDECIDED).size() != 4) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      if (dot.getMatchingEdges(Status.UNDECIDED).size() == 4) {
        Face oppositeFace = face.getOppositeFace(grid, dot);
        if (oppositeFace.getClue() == 2) {
          if ((oppositeFace.getMatchingEdges(Status.IN_SOLUTION).size() == 1)
              && (oppositeFace.getMatchingEdges(Status.OUT_SOLUTION).size() == 1)) {
            List<Edge> oppositeNonFaceEdges = Lists.newArrayList(Sets.difference(
                face.getDotOppositeDot(dot).getEdges(),
                face.getEdgesUsingDot(face.getDotOppositeDot(dot))));
            if (oppositeNonFaceEdges.size() == 1) {
              mutations.add(new GridMutation(oppositeNonFaceEdges.get(0)));
            } else { // size() is 2
              if (oppositeNonFaceEdges.get(0).getStatus() == Status.OUT_SOLUTION
                  && oppositeNonFaceEdges.get(1).getStatus() == Status.UNDECIDED) {
                mutations.add(new GridMutation(oppositeNonFaceEdges.get(1), Status.IN_SOLUTION));
              } else if (oppositeNonFaceEdges.get(0).getStatus() == Status.UNDECIDED
                  && oppositeNonFaceEdges.get(1).getStatus() == Status.OUT_SOLUTION) {
                mutations.add(new GridMutation(oppositeNonFaceEdges.get(0), Status.IN_SOLUTION));
              }
            }
          }
        }
      }
    }
  }
}
