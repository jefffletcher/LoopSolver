package fu.kung.looper.solver.solutions;

import com.google.common.collect.ImmutableSet;
import fu.kung.looper.solver.grid.Face;
import fu.kung.looper.solver.grid.Grid;
import fu.kung.looper.solver.grid.GridMutation;

public abstract class Solution {

  public boolean shouldRepeat() {
    return false;
  }

  public ImmutableSet<GridMutation> mutate(Grid grid) {
    ImmutableSet.Builder<GridMutation> mutations = ImmutableSet.builder();
    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getColumns(); j++) {
        Face face = grid.getFace(i, j);
        if (!face.isComplete()) {
          processFace(grid, face, mutations);
        }
      }
    }
    return mutations.build();
  }

  public void processFace(Grid grid, Face face, ImmutableSet.Builder<GridMutation> mutations) {
    return;
  }
}
