import robocode.*;

public class MicroBot extends AdvancedRobot {
    private static LUT Q; // Q(s,a)
    private static int roundNumber;
    private State s; // s
    private State sPrime; // sPrime
    private Action a; // a
    private double reward; // r
    private double xLen;
    private double yLen;
    private final double epsilonInitial = 0.8;
    private final int targetNumRounds = 8000;

    static {
        Q = new LUT();
        Q.initialiseLUT();
        roundNumber = 0;
    }

    // Scan for enemy
    @Override
    public void run() {
        // Initialize Q table at first round
        if (getTime() == 0) {
            Q.initialiseLUT();
            roundNumber = 0;
        }
        // Initialize at each round
        s = null;
        sPrime = null;
        a = null;
        reward = 0;
        xLen = getBattleFieldWidth();
        yLen = getBattleFieldHeight();

        // Rotates the radar continuously
        turnRadarRight(Double.POSITIVE_INFINITY);
    }

    // Update state and Q-value upon scanning an enemy
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // 3. Get s' from scanning
        sPrime.myEnergy = State.quantizeMyEnergy(getEnergy());
        sPrime.distance = State.quantizeDistance(e.getDistance());
        sPrime.enemyEnergy = State.quantizeEnemyEnergy(e.getEnergy());
        sPrime.distanceToWall = State.quantizeDistanceToWall(getX(), getY(), xLen, yLen);

        // 4. Update Q(s,a) with s' and reward
        Q.updateLUT(reward, s, a, sPrime);
        reward = 0;

        // 5. s = s'
        s = sPrime;

        // 1. Choose a from s by epsilon-greedy policy
        a = Q.selectAction(s, decayEpsilon());

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

    // Update Q-value at the end of the game
    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        Q.updateLUT(reward, s, a, sPrime);
        roundNumber++;
    }

    // Update rewards when events happen
    @Override
    public void onBulletHit(BulletHitEvent event) {
        reward += 3.0;
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        reward -= 1.0;
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        reward -= 2.0;
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        if (event.isMyFault()) {
            reward -= 1.5;  // When my robot crashes with enemy
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        reward -= 2.5;
    }

    @Override
    public void onDeath(DeathEvent event) {
        reward -= 3.0; // My robot dies
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        reward += 4.0; // Enemy robot dies
    }

    private double decayEpsilon() {
        if (roundNumber < targetNumRounds) {
            return epsilonInitial * (1 - (double) roundNumber / targetNumRounds);
        } else {
            return 0.0;
        }
    }
}
