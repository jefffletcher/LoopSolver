package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;
import java.util.ArrayList;
import java.util.Set;

public class ChainedTwos extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 2 || face.getMatchingEdges(Status.UNDECIDED).size() != 4) {
      return;
    }

    for (Dot dot : face.getGridDots()) {
      Edge incomingEdge = dot.getSingleEdgeNotInFaceMatching(face, Status.IN_SOLUTION);
      if (incomingEdge != null) {
        DiagonalFace dFace = getNextNonTwoFaceOppositeDot(grid, face, dot);
        // System.out.printf("dFace.face: %s%n", dFace.otherFace);
        // System.out.printf("dFace.dot: %s%n", dFace.otherDot);

        if (dFace.otherFace.getClue() == 1) {
          for (Edge edge : dFace.otherFace.getEdgesOppositeDot(dFace.otherDot)) {
            mutations.add(new GridMutation(edge, Status.OUT_SOLUTION));
          }
        } else if (dFace.otherFace.getClue() == 2) {
          ensureOnlyOneOfTwoEdgesInSolution(
              dFace.otherFace.getEdgesUsingDot(dFace.otherDot), mutations);
          ensureOnlyOneOfTwoEdgesInSolution(
              dFace.otherFace.getEdgesOppositeDot(dFace.otherDot), mutations);

          // At the end of the chain, if the face has clue 2 and there's only one way to go
          // mark it IN_SOLUTION
          Dot farAwayDot = dFace.otherFace.getDotOppositeDot(dFace.otherDot);
          // System.out.printf("farAwayDot: %s%n", farAwayDot);
          if (farAwayDot
              .getSingleEdgeNotInFaceMatching(dFace.otherFace, Status.IN_SOLUTION) == null) {
            Edge edge =
                farAwayDot.getSingleEdgeNotInFaceMatching(dFace.otherFace, Status.UNDECIDED);
            // System.out.printf("edge: %s%n", edge);
            if (edge != null) {
              mutations.add(new GridMutation(edge));
            }
          }
        }
        //TODO if (dFace.otherFace.getClue() == 3)
      }
    }
  }

  private void ensureOnlyOneOfTwoEdgesInSolution(
      Set<Edge> edges, Builder<GridMutation> mutations) {
    ArrayList<Edge> edgeList = Lists.newArrayList(edges);
    if (edgeList.get(0).getStatus() == Status.IN_SOLUTION
        && edgeList.get(1).getStatus() == Status.UNDECIDED) {
      mutations.add(new GridMutation(edgeList.get(1), Status.OUT_SOLUTION));
    } else if (edgeList.get(0).getStatus() == Status.UNDECIDED
        && edgeList.get(1).getStatus() == Status.IN_SOLUTION) {
      mutations.add(new GridMutation(edgeList.get(0), Status.OUT_SOLUTION));
    }
  }

  private DiagonalFace getNextNonTwoFaceOppositeDot(
      Grid grid, Face startingFace, Dot startingDot) {
    Dot oppositeDot = startingFace.getDotOppositeDot(startingDot);
    Face oppositeFace = startingFace.getOppositeFace(grid, oppositeDot);
    if (oppositeFace == null) {
      return new DiagonalFace(startingFace, startingDot);
    } else if (oppositeFace.getClue() == 2
        && oppositeFace.getMatchingEdges(Status.UNDECIDED).size() == 4) {
      return getNextNonTwoFaceOppositeDot(grid, oppositeFace, oppositeDot);
    }
    return new DiagonalFace(oppositeFace, oppositeDot);
  }

  class DiagonalFace {

    Face otherFace;
    Dot otherDot;

    DiagonalFace(Face face, Dot dot) {
      this.otherFace = face;
      this.otherDot = dot;
    }
  }
}
