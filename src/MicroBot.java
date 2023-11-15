import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.ScannedRobotEvent;

public class MicroBot extends AdvancedRobot {
    private LUT lut;
    private State s, sPrime; // s and s'
    private Action a, aPrime; // a and a'
    private double reward;
    private final double epsilon = 0.2;
    private double xLen;
    private double yLen;


    // Scan for the enemy
    @Override
    public void run() {
        // Initialization
        lut = new LUT();
        xLen = getBattleFieldWidth();
        yLen = getBattleFieldHeight();

        // Rotates the radar continuously
        turnRadarRight(Double.POSITIVE_INFINITY);
    }

    // When an enemy is observed, Q-learning on the new state
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // 3. Get s' from scanning
        sPrime.myEnergy = State.quantizeMyEnergy(getEnergy());
        sPrime.distance = State.quantizeDistance(e.getDistance());
        sPrime.enemyEnergy = State.quantizeEnemyEnergy(e.getEnergy());
        sPrime.distanceToWall = State.quantizeDistanceToWall(getX(), getY(), xLen, yLen);

        // 4. Update Q(s,a) with s'
        lut.updateLUT(reward, s, a, sPrime);

        // 5. s = s'
        s = sPrime;

        // 1. Choose a from s by epsilon-greedy policy
        a = lut.selectAction(s, epsilon);

        // 2. Take action a
        setTurnGunRight(getHeading() - getGunHeading() + e.getBearing()); // Turn the gun towards enemy
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
        execute();
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        reward += event.getBullet().getPower();
    }


}
