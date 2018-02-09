package fu.kung.looper.solver.grid;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import fu.kung.looper.solver.grid.Edge.Status;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dot {

  private final int x;
  private final int y;

  private Map<String, Edge> edgeMap = new HashMap<>();
  private Set<Face> faces; /* A NULL grid_face means infinite outside face */

  Dot(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public ImmutableSet<Edge> getMatchingEdges(Edge.Status... statuses) {
    List<Edge.Status> statusList = Arrays.asList(statuses);
    ImmutableSet.Builder<Edge> matchingEdges = ImmutableSet.builder();
    for (Edge edge : edgeMap.values()) {
      if (statusList.contains(edge.getStatus())) {
        matchingEdges.add(edge);
      }
    }
    return matchingEdges.build();
  }

  public Edge getSingleEdgeNotInFaceMatching(Face face, Edge.Status... statuses) {
    List<Edge.Status> statusList = Arrays.asList(statuses);
    Set<Edge> nonFaceEdges = Sets
        .difference(ImmutableSet.copyOf(edgeMap.values()), face.getEdges());
    int matchingCount = 0;
    Edge matchingEdge = null;
    for (Edge edge : nonFaceEdges) {
      if (edge.getStatus() != Status.OUT_SOLUTION) {
        matchingCount++;
      }
      if (statusList.contains(edge.getStatus())) {
        matchingEdge = edge;
      }
    }
    if (matchingCount == 1) {
      return matchingEdge;
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Dot dot = (Dot) o;
    return dot.getX() == getX() && dot.getY() == getY();
  }

  @Override
  public int hashCode() {
    return (getX() * 11) + (getY() * 19);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", x, y);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public ImmutableSet<Edge> getEdges() {
    return ImmutableSet.copyOf(edgeMap.values());
  }

  public void addEdge(Edge edge) {
    if (!edgeMap.containsKey(edge.getKey())) {
      edgeMap.put(edge.getKey(), edge);
    }
  }
}
