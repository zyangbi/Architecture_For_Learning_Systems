import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LUT implements LUTInterface {
    private Map<Integer, Double> lut;
    private final int stateSize = State.getSize();
    private final int actionSize = Action.values().length;
    private final Random rand = new Random();
    private final double alpha = 0.9;
    private final double gamma = 0.9;


//    public LUT (
//            int argNumInputs,
//            int[] argVariableFloor,
//            int[] argVariableCeiling) {
//
//    }

    @Override
    public void initialiseLUT() {
        int size = stateSize * actionSize;
        lut = new HashMap<>(size);
        for (int i = 0; i < size; ++i) {
            lut.put(i, rand.nextDouble());
        }
    }

    private double queryLUT(State state, Action action) {
        int index = StateActionToIndex(state, action);
        return lut.get(index);
    }

    public Action selectAction(State s, double e) {
        // random action with P = e
        if (Math.random() < e) {
            return selectRandomAction();
        }
        // greedy action with P = 1 - e
        else {
            return selectBestAction(s);
        }
    }



    // Update Q(s,a) in LUT
    public void updateLUT(double r, State s, Action a, State sPrime) {
        double Q = queryLUT(s, a);  // Q(s,a)

        Action aPrime = selectBestAction(sPrime);  // a' that maximize Q(s',a')
        double MaxQPrime= queryLUT(sPrime, aPrime);  // max Q(s',a')

        // Compute updated Q(s,a)
        // Q(s,a) = Q(s,a) + alpha * (r + gamma * max Q(s',a') - Q(s,a))
        Q += alpha * (r + gamma * MaxQPrime - Q);
        lut.put(StateActionToIndex(s, a), Q);
    }

    // Select a random action from the Action enum
    public Action selectRandomAction() {
        return Action.values()[rand.nextInt(actionSize)];
    }

    // Select the best action based on Q-values
    public Action selectBestAction(State state) {
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




    @Override
    public int indexFor(double[] X) {
        return 0;
    }

    private int StateActionToIndex(State state, Action action) {
        return state.stateToIndex() * actionSize + action.ordinal();
    }

}
