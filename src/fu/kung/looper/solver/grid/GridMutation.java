package fu.kung.looper.solver.grid;

import fu.kung.looper.solver.grid.Edge.Status;

public class GridMutation {

  // Currently only support Edge mutations.
  private final Edge edge;
  private final Status originalStatus;
  private final Status newStatus;

  public GridMutation(Edge edge, Status newStatus) {
    this.edge = edge;
    this.originalStatus = edge.getStatus();
    this.newStatus = newStatus;
  }

  public GridMutation(Edge edge) {
    this(edge, Status.IN_SOLUTION);
  }

  public Edge getEdge() {
    return edge;
  }

  public Status getOriginalStatus() {
    return originalStatus;
  }

  public Status getNewStatus() {
    return newStatus;
  }
}
