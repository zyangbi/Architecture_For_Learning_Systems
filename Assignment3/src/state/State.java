package state;

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
    private double xLen;
    private double yLen;


    // Constructor
    public State(double x, double y, double xLen, double yLen, double heading, double energy,
                 double enemyDistance, double enemyBearing) {
        this.xLen = xLen;
        this.yLen = yLen;
        this.myEnergy = quantizeMyEnergy(energy);
        this.myRow = quantizeMyRow(y);
        this.myColumn = quantizeMyColumn(x);
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

    public double[] dequantizeState(double xMax, double yMax) {
        return new double[]{
                dequantizeMyEnergy(),
                dequantizeMyRow(),
                dequantizeMyColumn(),
                dequantizeMyHeading(),
                dequantizeEnemyDistance(),
                dequantizeEnemyBearing()
        };
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

    private double dequantizeMyEnergy() {
        switch (myEnergy) {
            case LOW:
                return 10;
            case MID:
                return 35;
            case HIGH:
                return 75;
            default:
                return 0;
        }
    }

    private MyRow quantizeMyRow(double y) {
        if (y < yLen * 0.15) {
            return MyRow.BOTTOM;
        } else if (y < yLen * 0.85) {
            return MyRow.MID;
        } else {
            return MyRow.TOP;
        }
    }

    private double dequantizeMyRow() {
        switch (myRow) {
            case BOTTOM:
                return yLen * 0.075;
            case MID:
                return yLen * 0.5;
            case TOP:
                return yLen * 0.925;
            default:
                return 0;
        }
    }

    private MyColumn quantizeMyColumn(double x) {
        if (x < xLen * 0.15) {
            return MyColumn.LEFT;
        } else if (x < xLen * 0.85) {
            return MyColumn.MID;
        } else {
            return MyColumn.RIGHT;
        }
    }

    private double dequantizeMyColumn() {
        switch (myColumn) {
            case LEFT:
                return xLen * 0.075;
            case MID:
                return xLen * 0.5;
            case RIGHT:
                return xLen * 0.925;
            default:
                return 0;
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

    private double dequantizeMyHeading() {
        switch (myHeading) {
            case NORTH:
                return 0;
            case EAST:
                return 90;
            case SOUTH:
                return 180;
            case WEST:
                return 270;
            default:
                return 0;
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

    private double dequantizeEnemyDistance() {
        switch (enemyDistance) {
            case LOW:
                return 50;
            case MID:
                return 200;
            case HIGH:
                return 400;
            default:
                return 0;
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

    private double dequantizeEnemyBearing() {
        switch (enemyBearing) {
            case FORWARD:
                return 0;
            case FORWARDRIGHT:
                return 60;
            case BACKWARDRIGHT:
                return 120;
            case BACKWARD:
                return 180;
            case BACKWARDLEFT:
                return -120;
            case FORWARDLEFT:
                return -60;
            default:
                return 0;
        }
    }
}
