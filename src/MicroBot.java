import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.ScannedRobotEvent;

public class MicroBot extends AdvancedRobot {
    private enum OperationalMode {SCAN, ACTION};

    private OperationalMode operationalMode;


    private State s, sPrime; // s and s'
    private Action a, aPrime; // a and a'
    private double reward; // reward

    private double enemyBearing;

    private double xMid;
    private double yMid;
    private LUT lut;
    private final double epsilon = 0.2;


    // Constructor
    public MicroBot() {
        xMid = getBattleFieldWidth() / 2.0;
        yMid = getBattleFieldHeight() / 2.0;
    }

    @Override
    public void run() {
        while (true) {
            switch (operationalMode) {
                case SCAN:
                    reward = 0;
                    turnRadarLeft(90);
                    break;

                case ACTION:
                    // 1. Choose a from s by epsilon-greedy policy
                    a = lut.selectAction(s, epsilon);

                    // 2. Take action a
                    switch (a) {
                        case LEFT:
                            setTurnLeft(90);
                            break;
                        case RIGHT:
                            setTurnRight(90);
                            break;
                        case FORWARD:
                            setAhead(100);
                            break;
                        case BACK:
                            setBack(100);
                            break;
                        case FIRE:
                            fire(1);
                            break;
                    }
                    setTurnGunRight(getHeading() - getGunHeading() + enemyBearing);
                    execute();
                    // After ACTION finishes, return to SCAN mode
                    operationalMode = OperationalMode.SCAN;
                    break;
            }
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // 3. Observe s' from scanning
        sPrime.myEnergy = State.quantizeMyEnergy(getEnergy());
        sPrime.distance2Enemy = State.quantizeDistance(e.getDistance());
        sPrime.enemyEnergy = State.quantizeEnemyEnergy(e.getEnergy());
        sPrime.distanceToCentre = State.quantizeDistanceToCentre(getX(), getY(), xMid, yMid);
        enemyBearing = e.getBearing();
        operationalMode = OperationalMode.ACTION;

        // 4. Update Q(s,a)
        lut.updateLUT(reward, s, a, sPrime);

        // 5. s = s'
        s = sPrime;
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        reward += event.getBullet().getPower();
    }


}
