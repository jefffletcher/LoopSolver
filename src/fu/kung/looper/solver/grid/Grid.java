package fu.kung.looper.solver.grid;

import com.google.common.collect.ImmutableSet;
import fu.kung.looper.solver.grid.Edge.Status;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grid {

  int rows;
  int columns;

  private Map<Point, Face> faceMap = new HashMap<>();
  private Map<String, Edge> edgeMap = new HashMap<>();
  private Map<Point, Dot> dotMap = new HashMap<>();

  public enum DIR {
    N(1), S(2), E(4), W(8);
    public final int bit;

    DIR(int bit) {
      this.bit = bit;
    }
  }

  public Grid(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;

    // Create faces, add points to faces
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        Face face = new Face(i, j);
        Dot dotNW = getGridDot(i, j);
        face.addGridDot(dotNW);
        Dot dotSW = getGridDot(i + 1, j);
        face.addGridDot(dotSW);
        Dot dotSE = getGridDot(i + 1, j + 1);
        face.addGridDot(dotSE);
        Dot dotNE = getGridDot(i, j + 1);
        face.addGridDot(dotNE);
        faceMap.put(new Point(i, j), face);

        Edge topEdge = getGridEdge(dotNW, dotNE);
        Edge bottomEdge = getGridEdge(dotSW, dotSE);
        Edge leftEdge = getGridEdge(dotNW, dotSW);
        Edge rightEdge = getGridEdge(dotNE, dotSE);
        face.addGridEdge(topEdge);
        face.addGridEdge(rightEdge);
        face.addGridEdge(bottomEdge);
        face.addGridEdge(leftEdge);

        dotNW.addEdge(topEdge);
        dotNW.addEdge(leftEdge);
        dotSW.addEdge(bottomEdge);
        dotSW.addEdge(leftEdge);
        dotSE.addEdge(bottomEdge);
        dotSE.addEdge(rightEdge);
        dotNE.addEdge(topEdge);
        dotNE.addEdge(rightEdge);

        if (faceMap.containsKey(new Point(i - 1, j))) {
          topEdge.addFaces(face, faceMap.get(new Point(i - 1, j)));
        } else {
          topEdge.addFaces(face, null);
        }
        if (faceMap.containsKey(new Point(i, j - 1))) {
          leftEdge.addFaces(face, faceMap.get(new Point(i, j - 1)));
        } else {
          leftEdge.addFaces(face, null);
        }
        if (j == (columns - 1)) {
          rightEdge.addFaces(face, null);
        }
        if (i == (rows - 1)) {
          bottomEdge.addFaces(face, null);
        }
      }
    }
  }

  private Edge getGridEdge(Dot dot1, Dot dot2) {
    // Build the edge, so it figures out which Dot is first
    Edge edge = new Edge(dot1, dot2);
    String key = edge.getKey();
    if (!edgeMap.containsKey(key)) {
      edgeMap.put(key, edge);
    }
    return edgeMap.get(key);
  }

  private Dot getGridDot(int x, int y) {
    Point key = new Point(x, y);
    if (!dotMap.containsKey(key)) {
      Dot newDot = new Dot(x, y);
      dotMap.put(key, newDot);
    }
    return dotMap.get(key);
  }

  public boolean isSolved() {
    if (getMatchingEdges(Status.UNDECIDED).size() > 0) {
      return false;
    }
    Set<Edge> solvedEdges = getMatchingEdges(Status.IN_SOLUTION);
    int loopEdgeCount = getEdgesInLoop(solvedEdges.iterator().next()).size();
    return (solvedEdges.size() == loopEdgeCount);
  }

  // Returning null means there is no loop
  public Set<Edge> getEdgesInLoop(Edge initialEdge) {
    Set<Edge> results = new HashSet<>();

    boolean done = false;
    Edge currentEdge = initialEdge;
    Dot currentDot = initialEdge.getDot1();
    while (!done) {
      results.add(currentEdge);
      // System.out.printf("results: %s%n", results);
      ImmutableSet<Edge> inSolutionEdges = currentDot.getMatchingEdges(Status.IN_SOLUTION);
      if (inSolutionEdges.size() != 2) {
        return null;
      }
      Iterator<Edge> edgeIter = inSolutionEdges.iterator();
      Edge edge1 = edgeIter.next();
      Edge edge2 = edgeIter.next();
      Edge nextEdge;
      if (edge1.equals(currentEdge)) {
        nextEdge = edge2;
      } else {
        nextEdge = edge1;
      }

      if (nextEdge.equals(initialEdge)) {
        return results;
      }

      if (nextEdge.getDot1().equals(currentDot)) {
        currentDot = nextEdge.getDot2();
      } else {
        currentDot = nextEdge.getDot1();
      }

      currentEdge = nextEdge;
    }

    return results;
  }

  public void setGridClues(int[] clues) {
    int index = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (clues[index] >= 0) {
          faceMap.get(new Point(i, j)).setClue(clues[index]);
        }
        index++;
      }
    }
  }

  public boolean isValidPoint(int a, int b) {
    return ((a >= 0) && (a < rows) && (b >= 0) && (b < columns));
  }

  public int[][] getFaceStatus(Status status) {
    int[][] result = new int[rows][columns];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {

        Face face = faceMap.get(new Point(i, j));
        int value = 0;
        for (DIR dir : DIR.values()) {
          if (face.getEdge(dir).getStatus() == status) {
            value += dir.bit;
          }
        }
        result[i][j] = value;
      }
    }

    return result;
  }

  public ImmutableSet<Edge> getMatchingEdges(Edge.Status... statuses) {
    List<Status> statusList = Arrays.asList(statuses);
    ImmutableSet.Builder<Edge> matchingEdges = ImmutableSet.builder();
    for (Edge edge : edgeMap.values()) {
      if (statusList.contains(edge.getStatus())) {
        matchingEdges.add(edge);
      }
    }
    return matchingEdges.build();
  }

  public void dumpInfo() {
    System.out.printf("Number of edges: %d%n", edgeMap.size());
    for (Edge edge : edgeMap.values()) {
      System.out.println(edge);
    }
  }

  public void outputSvg(String filename) {
    String tempDir = System.getProperty("java.io.tmpdir");
    String fullFilename = String.format("%s/%s", tempDir, filename);

    StringBuilder svg = new StringBuilder();
    svg.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
    svg.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"\n");
    svg.append("\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n");
    svg.append("\n");
    svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\"\n");
    svg.append("xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n\n");

    // GridFaces
    svg.append("<g>\n");
    for (Face face : faceMap.values()) {
      svg.append("<polygon points=\"");
      boolean first = true;
      int totalX = 0; // Averaging (x, y) for the face to find center
      int totalY = 0;
      for (Dot dot : face.getGridDots()) {
        svg.append(
            String.format("%s%d,%d", first ? "" : " ", scale(dot.getY()), scale(dot.getX())));
        first = false;
        totalX += scale(dot.getX());
        totalY += scale(dot.getY());
      }
      svg.append("\" style=\"fill: white; fill-opacity: 0.2; stroke: white\" />\n");

      // Write clue
      if (face.getClue() >= 0) {
        svg.append(String
            .format("<text x=\"%d\" y=\"%d\" fill=\"black\">%d</text>\n", totalY / 4, totalX / 4,
                face.getClue()));
      }
    }
    svg.append("</g>\n");

    // GridEdges
    svg.append("<g>\n");
    for (Edge edge : edgeMap.values()) {
      // TODO: Vary edge color based on whether it's undecided, part of the solution or not part
      // of the solution
      String color = "blue";
      switch (edge.getStatus()) {
        case UNDECIDED:
          color = "orange";
          break;
        case IN_SOLUTION:
          color = "black";
          break;
        case OUT_SOLUTION:
          color = "white";
          break;
      }
      svg.append(String.format(
          "<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\" stroke-width=\"3\" />\n",
          scale(edge.getDot1().getY()), scale(edge.getDot1().getX()),
          scale(edge.getDot2().getY()), scale(edge.getDot2().getX()), color)
      );
    }
    svg.append("</g>\n");

    // GridDots
    svg.append("<g>\n");
    for (Dot dot : dotMap.values()) {
      svg.append(
          String.format("<ellipse cx=\"%d\" cy=\"%d\" rx=\"%d\" ry=\"%d\" fill=\"black\" />\n",
              scale(dot.getY()), scale(dot.getX()), 4, 4));
    }
    svg.append("</g>\n");

    svg.append("</svg>\n");
    writeOutput(fullFilename, svg.toString());
  }

  int scale(int value) {
    return (value * 50) + 50;
  }

  private static void writeOutput(String filename, String data) {
    try {
      Files.write(Paths.get(filename), data.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }

  public Face getFace(int i, int j) {
    return faceMap.get(new Point(i, j));
  }
}
