public class State {
    public enum MyEnergy { LOW, MID, HIGH }
    public enum DistanceToEnemy { LOW, MID, HIGH }
    public enum EnemyEnergy { LOW, MID, HIGH }
    public enum DistanceToWall { LOW, HIGH }

    // Public attributes
    public MyEnergy myEnergy;
    public DistanceToEnemy distance;
    public EnemyEnergy enemyEnergy;
    public DistanceToWall distanceToWall;

    // Constructor
    public State(MyEnergy myEnergy, DistanceToEnemy distance,
                 EnemyEnergy enemyEnergy, DistanceToWall distanceToWall) {
        this.myEnergy = myEnergy;
        this.distance = distance;
        this.enemyEnergy = enemyEnergy;
        this.distanceToWall = distanceToWall;
    }

    // Get the size of states
    public static int getSize() {
        return MyEnergy.values().length * DistanceToEnemy.values().length
                * EnemyEnergy.values().length * DistanceToWall.values().length;
    }

    // Convert state to index
    public int stateToIndex() {
        int index = 0;
        int factor = 1;

        index += myEnergy.ordinal() * factor;
        factor *= MyEnergy.values().length;

        index += distance.ordinal() * factor;
        factor *= DistanceToEnemy.values().length;

        index += enemyEnergy.ordinal() * factor;
        factor *= EnemyEnergy.values().length;

        index += distanceToWall.ordinal() * factor;

        return index;
    }

    public static MyEnergy quantizeMyEnergy(double myEnergyValue) {
        if (myEnergyValue < 20) {
            return MyEnergy.LOW;
        } else if (myEnergyValue < 50) {
            return MyEnergy.MID;
        } else {
            return MyEnergy.HIGH;
        }
    }

    public static DistanceToEnemy quantizeDistance(double distanceValue) {
        if (distanceValue < 100) {
            return DistanceToEnemy.LOW;
        } else if (distanceValue < 300) {
            return DistanceToEnemy.MID;
        } else {
            return DistanceToEnemy.HIGH;
        }
    }

    public static EnemyEnergy quantizeEnemyEnergy(double enemyEnergyValue) {
        if (enemyEnergyValue < 20) {
            return EnemyEnergy.LOW;
        } else if (enemyEnergyValue < 50) {
            return EnemyEnergy.MID;
        } else {
            return EnemyEnergy.HIGH;
        }
    }

    public static DistanceToWall quantizeDistanceToWall(double x, double y, double xMax, double yMax) {
        double minDistance = Math.min(Math.min(x, y), Math.min(xMax - x, yMax - y));
        if (minDistance < 100) {
            return DistanceToWall.LOW;
        } else {
            return DistanceToWall.HIGH;
        }
    }

}


