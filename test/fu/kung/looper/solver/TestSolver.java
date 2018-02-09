package fu.kung.looper.solver;

import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;
import fu.kung.looper.solver.solutions.Solution;
import java.util.Set;

public class TestSolver {

  // TODO: These two methods are straight from LoopSolver, although those aren't static
  public static boolean applySolution(Grid grid, Solution solution) {
    Set<GridMutation> mutations = solution.mutate(grid);

    boolean gridWasMutated = applyMutations(mutations);

    if (solution.shouldRepeat()) {
      while (applyMutations(solution.mutate(grid))) {
      }
    }

    return gridWasMutated;
  }

  public static boolean applyMutations(Set<GridMutation> mutations) {
    boolean gridWasMutated = false;
    for (GridMutation mutation : mutations) {
      if (mutation.getNewStatus() != mutation.getOriginalStatus()) {
        mutation.getEdge().setStatus(mutation.getNewStatus());
        gridWasMutated = true;
      }
    }

    return gridWasMutated;
  }
}
