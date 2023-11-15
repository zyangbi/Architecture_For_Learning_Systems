public class State {
    public enum MyEnergy { LOW, MID, HIGH }
    public enum Distance2Enemy { LOW, MID, HIGH }
    public enum EnemyEnergy { LOW, MID, HIGH }
    public enum DistanceToCentre { LOW, MID, HIGH }

    // Public attributes
    public MyEnergy myEnergy;
    public Distance2Enemy distance2Enemy;
    public EnemyEnergy enemyEnergy;
    public DistanceToCentre distanceToCentre;

    // Constructor
    public State(MyEnergy myEnergy, Distance2Enemy distance2Enemy,
                 EnemyEnergy enemyEnergy, DistanceToCentre distanceToCentre) {
        this.myEnergy = myEnergy;
        this.distance2Enemy = distance2Enemy;
        this.enemyEnergy = enemyEnergy;
        this.distanceToCentre = distanceToCentre;
    }

    // Get the size of states
    public static int getSize() {
        return MyEnergy.values().length * Distance2Enemy.values().length
                * EnemyEnergy.values().length * DistanceToCentre.values().length;
    }

    // Convert state to index
    public int stateToIndex() {
        int index = 0;
        int factor = 1;

        index += myEnergy.ordinal() * factor;
        factor *= MyEnergy.values().length;

        index += distance2Enemy.ordinal() * factor;
        factor *= Distance2Enemy.values().length;

        index += enemyEnergy.ordinal() * factor;
        factor *= EnemyEnergy.values().length;

        index += distanceToCentre.ordinal() * factor;

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

    public static Distance2Enemy quantizeDistance(double distanceValue) {
        if (distanceValue < 100) {
            return Distance2Enemy.LOW;
        } else if (distanceValue < 300) {
            return Distance2Enemy.MID;
        } else {
            return Distance2Enemy.HIGH;
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

    public static DistanceToCentre quantizeDistanceToCentre(double myX, double myY, double xMid, double yMid) {
        double deltaX = xMid - myX;
        double deltaY = yMid - myY;
        double distanceToCentre = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distanceToCentre < 100) {
            return DistanceToCentre.LOW;
        } else if (distanceToCentre < 500) {
            return DistanceToCentre.MID;
        } else {
            return DistanceToCentre.HIGH;
        }
    }


}


