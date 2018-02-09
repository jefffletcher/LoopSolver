package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.Grid.DIR;
import fu.kung.looper.solver.grid.GridMutation;
import java.util.ArrayList;
import java.util.List;

/**
 * If two faces with clues 3 are adjacent, mark the edge between them as IN_SOLUTION. If two faces
 * with clues 3 are diagonally adjacent, mark the outer corners of both as IN_SOLUTION. If a face is
 * in the corner with a clue of 3, mark its outer edges with IN_SOLUTION. If a face is in the corner
 * with a clue of 2, mark the outer edges of the adjacent faces with IN_SOLUTION.
 */
public class MarkInitialFaces extends Solution {

  @Override
  public void processFace(Grid grid, Face face, Builder<GridMutation> mutations) {
    if (face.getClue() != 3) {
      return;
    }

    // Mark adjacent 3's
    for (Edge edge : face.getEdges()) {
      Face otherFace = edge.getOtherFace(face);
      if ((otherFace != null) && (otherFace.getClue() == 3)) {
        // TODO: As well as marking this edge IN_SOLUTION, the two edges on either
        // side should be marked OUT_SOLUTION
        mutations.add(new GridMutation(edge));
      }
    }

    // Mark diagonal 3's
    for (DiagonalFace diagonalFace : getDiagonalFaces(grid, face)) {
      for (Edge edge : diagonalFace.faceEdgesInSolution) {
        mutations.add(new GridMutation(edge));
      }
      for (Edge edge : diagonalFace.otherFaceEdgesInSolution) {
        mutations.add(new GridMutation(edge));
      }
    }
  }

  private List<DiagonalFace> getDiagonalFaces(Grid grid, Face face) {
    List<DiagonalFace> results = new ArrayList<>();
    Face faceNW = getValidGridFace(grid, face, -1, -1);
    if (faceNW != null) {
      DiagonalFace dFace = new DiagonalFace();
      dFace.otherFace = faceNW;
      dFace.faceEdgesInSolution = Lists.newArrayList(face.getEdge(DIR.S), face.getEdge(DIR.E));
      dFace.otherFaceEdgesInSolution = Lists
          .newArrayList(dFace.otherFace.getEdge(DIR.N), dFace.otherFace.getEdge(DIR.W));
      results.add(dFace);
    }
    Face faceNE = getValidGridFace(grid, face, -1, 1);
    if (getValidGridFace(grid, face, -1, 1) != null) {
      DiagonalFace dFace = new DiagonalFace();
      dFace.otherFace = faceNE;
      dFace.faceEdgesInSolution = Lists.newArrayList(face.getEdge(DIR.S), face.getEdge(DIR.W));
      dFace.otherFaceEdgesInSolution = Lists
          .newArrayList(dFace.otherFace.getEdge(DIR.N), dFace.otherFace.getEdge(DIR.E));
      results.add(dFace);
    }
    return results;
  }

  Face getValidGridFace(Grid grid, Face startingFace, int dx, int dy) {
    if (grid.isValidPoint(startingFace.getX() + dx, startingFace.getY() + dy)) {
      Face dFace = grid.getFace(startingFace.getX() + dx, startingFace.getY() + dy);
      if (dFace.getClue() == 3) {
        return dFace;
      }
      if (dFace.getClue() == 2) {
        int newDx = dx > 0 ? dx + 1 : dx - 1;
        int newDy = dy > 0 ? dy + 1 : dy - 1;
        return getValidGridFace(grid, startingFace, newDx, newDy);
      }
    }

    return null;
  }

  class DiagonalFace {

    Face otherFace;
    List<Edge> faceEdgesInSolution;
    List<Edge> otherFaceEdgesInSolution;
  }
}
