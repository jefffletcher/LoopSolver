package fu.kung.looper.solver.grid;

import com.google.common.collect.ImmutableSet;

public class Edge {

  private Dot dot1;
  private Dot dot2;
  /* Use NULL for infinite outside face. */
  private Face face1;
  private Face face2;
  private Status status;
  private Face[] faces;

  public enum Status {
    UNDECIDED,
    IN_SOLUTION,
    OUT_SOLUTION
  }

  Edge(Dot dot1, Dot dot2) {
    // One dot will always be closer to 0 (assuming they're on a grid). Set that
    // closest one to dot1, so equals works well.
    int dot1Sum = dot1.getX() + dot1.getY();
    int dot2Sum = dot2.getX() + dot2.getY();
    if (dot1Sum < dot2Sum) {
      this.dot1 = dot1;
      this.dot2 = dot2;
    } else if (dot1Sum > dot2Sum) {
      this.dot1 = dot2;
      this.dot2 = dot1;
    } else {
      throw new IllegalArgumentException(String.format(
          "Creating a Edge for 2 GridDots whose points add up to the same value. %s and %s",
          dot1, dot2));
    }

    this.status = Status.UNDECIDED;
  }

  public void addFaces(Face face1, Face face2) {
    if (this.face1 != null) {
      throw new IllegalStateException("Faces have already been set for this edge.");
    }
    this.face1 = face1;
    this.face2 = face2;
  }

  public Face getOtherFace(Face face) {
    if (face.equals(face1)) {
      return face2;
    }
    return face1;
  }

  public ImmutableSet<Face> getFaces() {
    ImmutableSet.Builder<Face> faces = ImmutableSet.builder();
    if (face1 != null) {
      faces.add(face1);
    }
    if (face2 != null) {
      faces.add(face2);
    }
    return faces.build();
  }

  public Dot getOtherDot(Dot dot) {
    if (dot.equals(dot1)) {
      return dot2;
    }
    return dot1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Edge edge = (Edge) o;
    return edge.dot1.getX() == dot1.getX()
        && edge.dot1.getY() == dot1.getY()
        && edge.dot2.getX() == dot2.getX()
        && edge.dot2.getY() == dot2.getY();
  }

  @Override
  public int hashCode() {
    return (dot1.getX() * 11) + (dot1.getY() * 13) + (dot2.getX() * 17) + (dot2.getY() * 19);
  }

  @Override
  public String toString() {
    return String
        .format("(%d, %d)->(%d, %d)[%d]: %s", dot1.getX(), dot1.getY(), dot2.getX(), dot2.getY(),
            hashCode(), status);
  }

  public String getKey() {
    return String.format("%s--%s", dot1, dot2);
  }

  public Dot getDot1() {
    return dot1;
  }

  public Dot getDot2() {
    return dot2;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    // System.out.printf("setting status[%s] for %s%n", status, this);
    this.status = status;
  }
}
