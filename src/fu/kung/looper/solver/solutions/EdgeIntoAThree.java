package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet.Builder;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

/**
 * If an IN_SOLUTION edge enters a face with clue 3, then the edges meeting at the opposite corner
 * should be marked IN_SOLUTION.
 */
public class EdgeIntoAThree extends Solution {

  //TODO: If there is one incoming IN_SOLUTION edge, the other non-face edge coming into the same
  // dot is OUT_SOLUTION.

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() == 3) {
      for (Dot dot : face.getGridDots()) {
        // Edge incomingEdge = dot.getSingleEdgeNotInFaceMatching(face, Status.IN_SOLUTION);
        Edge incomingEdge = getIncomingEdge(face, dot);
        if (incomingEdge != null) {
          for (Edge edge : face.getEdgesOppositeDot(dot)) {
            mutations.add(new GridMutation(edge));
          }
        }
      }
    }
  }

  // TODO: Replace this with implementation in Dot
  private Edge getIncomingEdge(Face face, Dot dot) {
    // count of edges that are not edges on this face and that are marked IN_SOLUTION
    int count = 0;
    // The one edge coming into this dot that is marked IN_SOLUTION
    Edge incomingEdge = null;

    for (Edge edge : dot.getEdges()) {
      if (edge.getStatus() == Status.IN_SOLUTION) {
        if (!face.isAnEdge(edge)) {
          count++;
          incomingEdge = edge;
        }
      }
    }

    if (count == 1) {
      return incomingEdge;
    }

    return null;
  }
}
