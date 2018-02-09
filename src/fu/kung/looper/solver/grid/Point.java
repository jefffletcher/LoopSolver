package fu.kung.looper.solver.grid;

public class Point {

  final int x;
  final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Point p = (Point) o;
    return p.x == x && p.y == y;
  }

  @Override
  public int hashCode() {
    return (x / 11) + (y / 17);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", x, y);
  }
}
