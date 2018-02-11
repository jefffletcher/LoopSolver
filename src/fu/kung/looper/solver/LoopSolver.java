package fu.kung.looper.solver;

import com.google.common.collect.Lists;
import fu.kung.looper.solver.grid.Dot;
import fu.kung.looper.solver.grid.Edge;
import fu.kung.looper.solver.grid.Edge.Status;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;
import fu.kung.looper.solver.solutions.ChainedTwos;
import fu.kung.looper.solver.solutions.CornerThree;
import fu.kung.looper.solver.solutions.CornerTwo;
import fu.kung.looper.solver.solutions.DanglingEdge;
import fu.kung.looper.solver.solutions.DotIsDone;
import fu.kung.looper.solver.solutions.EdgeCountSatisfied;
import fu.kung.looper.solver.solutions.EdgeIntoAThree;
import fu.kung.looper.solver.solutions.InSolutionEdgeInto;
import fu.kung.looper.solver.solutions.InSolutionEdgeIntoTwo;
import fu.kung.looper.solver.solutions.InSolutionEdgesIntoTwo;
import fu.kung.looper.solver.solutions.MarkInitialFaces;
import fu.kung.looper.solver.solutions.OneWayToGo;
import fu.kung.looper.solver.solutions.OneWithALongEdge;
import fu.kung.looper.solver.solutions.RemoveSmallLoop;
import fu.kung.looper.solver.solutions.Solution;
import fu.kung.looper.solver.solutions.TwoInACorner;
import fu.kung.looper.solver.solutions.UndecidedEdgeInto;
import fu.kung.looper.solver.solutions.UndecidedIntoOne;
import fu.kung.looper.solver.solutions.UndecidedIntoTwo;
import fu.kung.looper.solver.solutions.UseAllRemainingEdges;
import java.util.List;
import java.util.Set;

public class LoopSolver {

  private final List<Solution> cleanupSolutions = Lists.newArrayList(
      new DanglingEdge(), new UseAllRemainingEdges(), new OneWayToGo(), new EdgeCountSatisfied(),
      new DotIsDone(), new EdgeIntoAThree(), new TwoInACorner(), new UndecidedEdgeInto(),
      new InSolutionEdgeInto(), new ChainedTwos(), new CornerThree(), new CornerTwo(),
      new InSolutionEdgesIntoTwo(), new UndecidedIntoOne(), new UndecidedIntoTwo(),
      new OneWithALongEdge(), new InSolutionEdgeIntoTwo());

  private final Grid grid;

  public LoopSolver(Grid grid) {
    this.grid = grid;
  }

  public void solve() {
    // This can be done once at the beginning
    applySolution(new MarkInitialFaces());

    // for (int i = 0; i < 30; i++) {
    //   runCleanupSolutions();
    //   grid.outputSvg("loopout.svg");
    // }
    // applySolution(new OneWithALongEdge());
    // runCleanupSolutions();
    // runCleanupSolutions();
    // runCleanupSolutions();
    // runCleanupSolutions();
    // runCleanupSolutions();

    boolean done = false;
    while (!done) {
      while (runCleanupSolutions()) {
        grid.outputSvg("loopout.svg");
      }

      if (!grid.isSolved()) {
        // Stuck, try an expensive thing
        if (!applySolution(new RemoveSmallLoop())) {
          done = true;
        }
        grid.outputSvg("loopout.svg");
      } else {
        System.out.println("Voila!");
        done = true;
      }
    }
  }

  public boolean runCleanupSolutions() {
    boolean gridWasModified = false;
    for (Solution solution : cleanupSolutions) {
      if (applySolution(solution)) {
        gridWasModified = true;
      }
    }
    return gridWasModified;
  }

  public boolean applySolution(Solution solution) {
    // System.out.printf("Running solution %s%n", solution);
    Set<GridMutation> mutations = solution.mutate(grid);
    if (mutations == null) {
      throw new IllegalArgumentException("WTF? null mutations.");
    }

    boolean gridWasMutated = applyMutations(mutations, solution);

    if (solution.shouldRepeat()) {
      while (applyMutations(solution.mutate(grid), solution)) {
      }
    }

    return gridWasMutated;
  }

  public boolean applyMutations(Set<GridMutation> mutations, Solution solution) {
    boolean gridWasMutated = false;
    for (GridMutation mutation : mutations) {
      if (mutation.getNewStatus() != mutation.getOriginalStatus()) {
        Edge mutatedEdge = mutation.getEdge();
        mutatedEdge.setStatus(mutation.getNewStatus());

        // if (mutatedEdge.equals(new Edge(new Dot(1, 9), new Dot(1, 10)))) {
        //   System.out.printf("Problem here: %s%n%s%n", solution, mutatedEdge);
        // }

        for (Face face : mutation.getEdge().getFaces()) {
          if (face.getClue() > -1
              && face.getMatchingEdges(Status.IN_SOLUTION).size() > face.getClue()) {
            System.out.printf("While running solution %s%n", solution);
            grid.outputSvg("loopout.svg");
            throw new IllegalStateException(String.format("Too many IN_SOLUTION%n%s%n", face));
          }
          if (face.getMatchingEdges(Status.UNDECIDED).size() == 0) {
            face.setComplete(true);
          }
        }
        gridWasMutated = true;
      }
    }

    return gridWasMutated;
  }

  public static void main(String[] args) {
//     Grid grid = getZeroInTheMiddleGrid();
//     Grid grid = getSevenBySevenEasy();
    Grid grid = getTenByTenTricky();
//     Grid grid = getDisplayTestGrid();
    // Grid grid = getSimpleGrid();

    LoopSolver solver = new LoopSolver(grid);
    solver.solve();

    // grid.dumpInfo();
    // grid.outputSvg("loopout.svg");
  }

  private static Grid getSimpleGrid() {
    // Grid grid = new Grid(1, 3);
    // grid.setGridClues(new int[]{0, 1, 2});

    Grid grid = new Grid(2, 2);
    grid.setGridClues(new int[]{0, 1, 1, 2});
    return grid;
  }

  private static Grid getZeroInTheMiddleGrid() {
    Grid grid = new Grid(3, 3);
    grid.setGridClues(new int[]{-1, -1, -1, -1, 0, -1, -1, -1, -1});
    return grid;
  }

  private static Grid getDisplayTestGrid() {
    Grid grid = new Grid(4, 7);
    grid.setGridClues(
        new int[]{0, 1, 2, 3, 4, 5, 6,
            1, 2, 3, 4, 5, 6, 0,
            2, 3, 4, 5, 6, 0, 1,
            3, 4, 5, 6, 0, 1, 2});
    return grid;
  }

  private static Grid getTenByTenTricky() {
    Grid grid = new Grid(10, 10);
    grid.setGridClues(
        new int[]{
            3, -1, 1, 2, -1, -1, 3, 1, -1, 3,
            -1, -1, -1, 3, -1, -1, -1, 2, 2, -1,
            -1, 2, 1, 2, 2, 1, -1, -1, 3, -1,
            2, 2, -1, 3, -1, 3, -1, 1, -1, 2,
            -1, 2, -1, -1, 1, -1, -1, -1, 2, -1,
            3, 1, 3, -1, -1, 2, 3, 1, 1, -1,
            2, -1, -1, 2, -1, -1, 2, -1, -1, -1,
            -1, -1, -1, -1, 1, -1, -1, -1, -1, 3,
            2, 3, -1, 2, 2, -1, 1, -1, -1, 3,
            2, 2, 2, 2, -1, 2, 2, -1, -1, 3
        }
    );
    return grid;
  }

  private static Grid getSevenBySevenEasy() {
    Grid grid = new Grid(7, 7);
    grid.setGridClues(
        new int[]{0, 2, -1, 3, 3, -1, 3,
            -1, 2, 1, -1, -1, 3, -1,
            -1, -1, 2, -1, -1, 2, 1,
            3, -1, 2, -1, -1, 2, -1,
            -1, -1, -1, 3, -1, -1, -1,
            3, -1, -1, 3, 1, -1, -1,
            -1, 1, 1, 3, 1, 1, -1});
    return grid;
  }
}
