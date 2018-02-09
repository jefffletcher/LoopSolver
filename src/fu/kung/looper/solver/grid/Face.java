package fu.kung.looper.solver.grid;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Grid.DIR;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Face {

  private final int x;
  private final int y;
  private int clue = -1;
  private boolean complete = false;

  private List<Edge> edges = new ArrayList<>();
  private List<Dot> dots = new ArrayList<>();

  public Face(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public ImmutableSet<Edge> getMatchingEdges(Edge.Status... statuses) {
    List<Edge.Status> statusList = Arrays.asList(statuses);
    ImmutableSet.Builder<Edge> matchingEdges = ImmutableSet.builder();
    for (Edge edge : edges) {
      if (statusList.contains(edge.getStatus())) {
        matchingEdges.add(edge);
      }
    }
    return matchingEdges.build();
  }

  public Edge getEdge(DIR dir) {
    switch (dir) {
      case N:
        return getEdgeWithKey(new Edge(new Dot(x, y), new Dot(x, y + 1)).getKey());
      case S:
        return getEdgeWithKey(
            new Edge(new Dot(x + 1, y), new Dot(x + 1, y + 1)).getKey());
      case E:
        return getEdgeWithKey(
            new Edge(new Dot(x, y + 1), new Dot(x + 1, y + 1)).getKey());
      case W:
        return getEdgeWithKey(new Edge(new Dot(x, y), new Dot(x + 1, y)).getKey());
    }
    throw new IllegalArgumentException(
        String.format("Couldn't find edge with DIR %s for face %s", dir, this));
  }

  public Edge getEdgeWithKey(String key) {
    for (Edge edge : edges) {
      if (edge.getKey().equals(key)) {
        return edge;
      }
    }
    throw new IllegalArgumentException(
        String.format("Couldn't find edge with key %s for face %s", key, this));
  }

  public ImmutableSet<Edge> getEdgesUsingDot(Dot dot) {
    ImmutableSet.Builder<Edge> results = ImmutableSet.builder();
    for (Edge edge : edges) {
      if (dot.equals(edge.getDot1()) || dot.equals(edge.getDot2())) {
        results.add(edge);
      }
    }
    return results.build();
  }

  public ImmutableSet<Edge> getMatchingEdgesUsingDot(Dot dot, Edge.Status... statuses) {
    List<Edge.Status> statusList = Arrays.asList(statuses);
    ImmutableSet.Builder<Edge> matchingEdges = ImmutableSet.builder();
    for (Edge edge : edges) {
      if (statusList.contains(edge.getStatus())
          && (dot.equals(edge.getDot1()) || dot.equals(edge.getDot2()))) {
        matchingEdges.add(edge);
      }
    }
    return matchingEdges.build();
  }

  public boolean faceEdgesUsingDotMatch(Dot dot, Status status) {
    return Sets.intersection(getEdgesUsingDot(dot), dot.getMatchingEdges(status)).size() == 2;
  }

  public ImmutableSet<Edge> getEdgesOppositeDot(Dot dot) {
    Dot oppositeDot = getDotOppositeDot(dot);
    int adj1X = oppositeDot.getX();
    int adj1Y = dot.getY();
    int adj2X = dot.getX();
    int adj2Y = oppositeDot.getY();
    ImmutableSet.Builder<Edge> results = ImmutableSet.builder();
    results.add(getEdgeWithKey(new Edge(oppositeDot, new Dot(adj1X, adj1Y)).getKey()));
    results.add(getEdgeWithKey(new Edge(oppositeDot, new Dot(adj2X, adj2Y)).getKey()));
    return results.build();
  }

  /**
   * It's a funky calculation to figure out the opposite corner.
   * <pre>
   * (4,3)             (4,4)
   *      +---+---+---+
   *      |           |
   *      +           +
   *      |   (4,3)   |
   *      +           +
   *      |           |
   *      +---+---+---+
   * (5,3)             (5,4)
   * </pre>
   */
  public Dot getDotOppositeDot(Dot startingDot) {
    int dx = startingDot.getX() - x;
    int dy = startingDot.getY() - y;
    int oppositeX;
    int oppositeY;
    if (dx == 0) {
      oppositeX = startingDot.getX() + 1;
    } else {
      oppositeX = startingDot.getX() - 1;
    }
    if (dy == 0) {
      oppositeY = startingDot.getY() + 1;
    } else {
      oppositeY = startingDot.getY() - 1;
    }
    Dot matchingDot = new Dot(oppositeX, oppositeY);
    for (Dot dot : dots) {
      if (dot.equals(matchingDot)) {
        return dot;
      }
    }
    throw new IllegalStateException("Shouldn't be possible to not find an opposite Dot.");
  }

  /**
   * <pre>
   * (4,3)             (4,4)
   *      +---+---+---+
   *      |           |
   *      +           +
   *      |   (4,3)   |
   *      +           +
   *      |           |
   *      +---+---+---+
   * (5,3)             (5,4)
   * </pre>
   */
  public Face getOppositeFace(Grid grid, Dot sharedDot) {
    int dx = sharedDot.getX() - x;
    int dy = sharedDot.getY() - y;
    int oppositeX;
    int oppositeY;
    if (dx == 0) {
      oppositeX = x - 1;
    } else {
      oppositeX = x + 1;
    }
    if (dy == 0) {
      oppositeY = y - 1;
    } else {
      oppositeY = y + 1;
    }
    return grid.getFace(oppositeX, oppositeY);
  }

  /**
   * Returns true if the provided edge is one of this face's edges.
   */
  public boolean isAnEdge(Edge edge) {
    try {
      getEdgeWithKey(edge.getKey());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)[%d]%n\tedges: %s%n\tdots: %s%n", x, y, clue, edges, dots);
  }

  public void addGridDot(Dot dot) {
    dots.add(dot);
  }

  public ImmutableList<Dot> getGridDots() {
    return ImmutableList.copyOf(dots);
  }

  public void setClue(int clue) {
    this.clue = clue;
  }

  public int getClue() {
    return clue;
  }

  public void addGridEdge(Edge edge) {
    edges.add(edge);
  }

  public ImmutableSet<Edge> getEdges() {
    return ImmutableSet.copyOf(edges);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }
}
