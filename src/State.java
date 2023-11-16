public class State {
    private enum MyEnergy { LOW, MID, HIGH }
    private enum MyPosition { TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT}
    private enum MyDistanceToWall { LOW, MID, HIGH }
    private enum EnemyDistance { LOW, MID, HIGH }
    private enum EnemyBearing { FORWARD, FORWARDLEFT, FORWARDRIGHT, BACKWARD, BACKWARDLEFT, BACKWARDRIGHT }

    private MyEnergy myEnergy;
    private MyPosition myPosition;
    private MyDistanceToWall myDistanceToWall;
    private EnemyDistance enemyDistance;
    private EnemyBearing enemyBearing;

    // Constructor
    public State(double x, double y, double xLen, double yLen, double energy, double enemyDistance, double enemyBearing) {
        this.myEnergy = quantizeMyEnergy(energy);
        this.myPosition = quantizeMyPosition(x, y, xLen, yLen);
        this.myDistanceToWall = quantizeMyDistanceToWall(x, y, xLen, yLen);
        this.enemyDistance = quantizeEnemyDistance(enemyDistance);
        this.enemyBearing = quantizeEnemyBearing(enemyBearing);
    }

    // Get the size of states
    public static int getSize() {
        return MyEnergy.values().length
                * MyPosition.values().length
                * MyDistanceToWall.values().length
                * EnemyDistance.values().length
                * EnemyBearing.values().length;
    }

    // Convert state to index
    public int getStateIndex() {
        int index = 0;
        int factor = 1;

        index += myEnergy.ordinal() * factor;
        factor *= MyEnergy.values().length;

        index += myPosition.ordinal() * factor;
        factor *= MyPosition.values().length;

        index += myDistanceToWall.ordinal() * factor;
        factor *= MyDistanceToWall.values().length;

        index += enemyDistance.ordinal() * factor;
        factor *= EnemyDistance.values().length;

        index += enemyBearing.ordinal() * factor;
        return index;
    }

    private MyEnergy quantizeMyEnergy(double myEnergyValue) {
        if (myEnergyValue < 20) {
            return MyEnergy.LOW;
        } else if (myEnergyValue < 50) {
            return MyEnergy.MID;
        } else {
            return MyEnergy.HIGH;
        }
    }

    private MyPosition quantizeMyPosition(double x, double y, double xMax, double yMax) {
        double xMid = xMax / 2.0;
        double yMid = yMax / 2.0;

        if (x < xMid && y < yMid) {
            return MyPosition.BOTTOMLEFT;
        } else if (x >= xMid && y < yMid) {
            return MyPosition.BOTTOMRIGHT;
        } else if (x < xMid && y >= yMid) {
            return MyPosition.TOPLEFT;
        } else {
            return MyPosition.TOPRIGHT;
        }
    }

    private MyDistanceToWall quantizeMyDistanceToWall(double x, double y, double xMax, double yMax) {
        double minDistance = Math.min(Math.min(x, y), Math.min(xMax - x, yMax - y));
        if (minDistance < 100) {
            return MyDistanceToWall.LOW;
        } else if (minDistance < 200){
            return MyDistanceToWall.MID;
        } else {
            return MyDistanceToWall.LOW;
        }
    }

    private EnemyDistance quantizeEnemyDistance(double distanceValue) {
        if (distanceValue < 100) {
            return EnemyDistance.LOW;
        } else if (distanceValue < 300) {
            return EnemyDistance.MID;
        } else {
            return EnemyDistance.HIGH;
        }
    }

    private EnemyBearing quantizeEnemyBearing(double bearing) {
        if (bearing >= -30 && bearing < 30) {
            return EnemyBearing.FORWARD;
        } else if (bearing >= 30 && bearing < 90) {
            return EnemyBearing.FORWARDRIGHT;
        } else if (bearing >= 90 && bearing < 150) {
            return EnemyBearing.BACKWARDRIGHT;
        } else if (bearing >= 150 || bearing < -150) {
            return EnemyBearing.BACKWARD;
        } else if (bearing >= -150 && bearing < -90) {
            return EnemyBearing.BACKWARDLEFT;
        } else { // bearing >= -90 && bearing < -30
            return EnemyBearing.FORWARDLEFT;
        }
    }
}


