import java.lang.reflect.Field;

public class State {
    private enum MyEnergy { LOW, MID, HIGH }
    private enum MyRow {TOP, MID, BOTTOM}
    private enum MyColumn {LEFT, MID, RIGHT}
    private enum MyHeading { NORTH, SOUTH, WEST, EAST}
    private enum EnemyDistance { LOW, MID, HIGH }
    private enum EnemyBearing { FORWARD, FORWARDLEFT, FORWARDRIGHT, BACKWARD, BACKWARDLEFT, BACKWARDRIGHT }

    private MyEnergy myEnergy;
    private MyRow myRow; // seperate y axis into 3 rows
    private MyColumn myColumn; // seperate x axis into 3 columns
    private MyHeading myHeading;
    private EnemyDistance enemyDistance;
    private EnemyBearing enemyBearing; // position of enemy relative to my heading

    // Constructor
    public State(double x, double y, double xLen, double yLen, double heading, double energy,
                 double enemyDistance, double enemyBearing) {
        this.myEnergy = quantizeMyEnergy(energy);
        this.myRow = quantizeMyRow(y, yLen);
        this.myColumn = quantizeMyColumn(x, xLen);
        this.myHeading = quantizeMyHeading(heading);
        this.enemyDistance = quantizeEnemyDistance(enemyDistance);
        this.enemyBearing = quantizeEnemyBearing(enemyBearing);
    }

    // Get the size of states
    public static int getSize() {
        return MyEnergy.values().length
                * MyRow.values().length
                * MyColumn.values().length
                * MyHeading.values().length
                * EnemyDistance.values().length
                * EnemyBearing.values().length;
    }

    // Convert state to index
    public int getStateIndex() {
        int index = 0;
        int factor = 1;

        index += myEnergy.ordinal() * factor;
        factor *= MyEnergy.values().length;

        index += myRow.ordinal() * factor;
        factor *= MyRow.values().length;

        index += myColumn.ordinal() * factor;
        factor *= MyColumn.values().length;

        index += myHeading.ordinal() * factor;
        factor *= MyHeading.values().length;

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

    private MyRow quantizeMyRow(double y, double yMax) {
        if (y < yMax * 0.15) {
            return MyRow.BOTTOM;
        } else if (y < yMax * 0.85) {
            return MyRow.MID;
        } else {
            return MyRow.TOP;
        }
    }

    private MyColumn quantizeMyColumn(double x, double xMax) {
        if (x < xMax * 0.15) {
            return MyColumn.LEFT;
        } else if (x < xMax * 0.85) {
            return MyColumn.MID;
        } else {
            return MyColumn.RIGHT;
        }
    }

    private MyHeading quantizeMyHeading(double heading) {
        if (heading >= 45 && heading < 135) {
            return MyHeading.EAST;
        } else if (heading >= 135 && heading < 225) {
            return MyHeading.SOUTH;
        } else if (heading >= 225 && heading < 315) {
            return MyHeading.WEST;
        } else {
            return MyHeading.NORTH;
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


