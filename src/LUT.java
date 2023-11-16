import Interface.LUTInterface;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LUT implements LUTInterface {
    private Map<Integer, Double> lut;
    private final int stateSize = State.getSize();
    private final int actionSize = Action.values().length;
    private final double alpha = 0.95;
    private final double gamma = 0.9;

    public LUT() {
        this.lut = new HashMap<>();
    }

    @Override
    public void initialiseLUT() {
        lut.clear();
        for (int i = 0; i < stateSize * actionSize; ++i) {
            lut.put(i, Math.random());
        }
    }

    // Query Q with s, a
    private double queryLUT(State state, Action action) {
        if (state == null || action == null) {
            throw new IllegalArgumentException();
        }

        int index = StateActionToIndex(state, action);
        return lut.get(index);
    }

    // Update Q(s,a) in LUT
    public void updateLUT(double r, State s, Action a, State sPrime) {
        if (s == null || a == null || sPrime == null) {
            throw new IllegalArgumentException();
        }

        double Q = queryLUT(s, a);  // Q(s,a)
        Action aPrime = selectBestAction(sPrime);  // a' that maximize Q(s',a')
        double MaxQPrime= queryLUT(sPrime, aPrime);  // max Q(s',a')

        // Q(s,a) = Q(s,a) + alpha * (r + gamma * max Q(s',a') - Q(s,a))
        Q += alpha * (r + gamma * MaxQPrime - Q);
        lut.put(StateActionToIndex(s, a), Q);
    }

    // Select an action by greedy-epsilon policy
    public Action selectAction(State state, double e) {
        // random action with P = e
        if (state == null || Math.random() < e) {
            return selectRandomAction();
        }
        // greedy action with P = 1 - e
        else {
            return selectBestAction(state);
        }
    }

    // Select a random action from the Action enum
    public Action selectRandomAction() {
        return Action.values()[(int) (Math.random() * actionSize)];
    }

    // Select the best action based on Q-values
    public Action selectBestAction(State state) {
        if (state == null) {
            throw new IllegalArgumentException();
        }

        double bestQ = -Double.MAX_VALUE;
        Action bestAction = null;
        for (Action action : Action.values()) {
            double Q = queryLUT(state, action);
            if (Q > bestQ) {
                bestQ = Q;
                bestAction = action;
            }
        }
        return bestAction;
    }

    private int StateActionToIndex(State state, Action action) {
        if (state == null || action == null) {
            throw new IllegalArgumentException();
        }
        return state.stateToIndex() * actionSize + action.ordinal();
    }

    @Override
    public int indexFor(double[] X) {
        return 0;
    }

    @Override
    public double outputFor(double[] X) {
        return 0;
    }

    @Override
    public double train(double[] X, double argValue) {
        return 0;
    }

    @Override
    public void save(File argFile) {

    }

    @Override
    public void load(String argFileName) throws IOException {

    }
}
