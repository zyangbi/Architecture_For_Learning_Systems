import Utils.LogFile;
import robocode.*;

import java.io.IOException;

public class MicroBot extends AdvancedRobot {
    private State s; // s
    private State sPrime; // sPrime
    private Action a; // a
    private double reward; // r
    private double epsilon;
    private double xLen;
    private double yLen;
    private boolean firstScan;
    private static LUT Q; // Q(s,a)
    private static int roundNumber;
    private static int numWin100R; // number of wins in 100 rounds
    private static double totalReward100R; // total reward in 100 rounds
//    private static double deltaQ100R; // change of Q(s,a) in 100 rounds
//    private static LogFile log;
    private final double epsilonInitial = 0.8;
    private final int targetNumRounds = 8000;

    static {
        Q = new LUT();
        Q.initialiseLUT();
        roundNumber = 0;
        numWin100R = 0;
        totalReward100R = 0.0;
//        try {
//            log = new LogFile("log/log.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    // Scan for enemy
    @Override
    public void run() {
        // Initialize at each round
        s = null;
        sPrime = null;
        reward = 0;
        epsilon = decayEpsilon();
        xLen = getBattleFieldWidth();
        yLen = getBattleFieldHeight();
        firstScan = true;

        // Print metrics every 100 rounds
        if (roundNumber % 100 == 0) {
//            log.println("\n\n==== Metrics after " + roundNumber + " rounds ====");
//            log.println("Number of Wins in Last 100 Rounds: " + numWin100R);
//            log.println("Total Reward in Last 100 Rounds: " + totalReward100R);
//            log.println("===================================================\n\n");

            numWin100R = 0;
            totalReward100R = 0;
        }

        // Rotates the radar continuously
        turnRadarRight(Double.POSITIVE_INFINITY);
    }

    // Update state and Q-value upon scanning an enemy
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // 3. Get s' from scanning
        sPrime = new State();
        sPrime.setMyEnergy(State.quantizeMyEnergy(getEnergy()));
        sPrime.setDistance(State.quantizeDistance(e.getDistance()));
        sPrime.setEnemyEnergy(State.quantizeEnemyEnergy(e.getEnergy()));
        sPrime.setDistanceToWall(State.quantizeDistanceToWall(getX(), getY(), xLen, yLen));

        // 4. Update Q(s,a) with s' and reward
        if (!firstScan) {
            Q.updateLUT(reward, s, a, sPrime);
            totalReward100R += reward;
            reward = 0;
        } else {
            firstScan = false;
        }

        // 5. s = s'
        s = sPrime;

        // 1. Choose a from s by epsilon-greedy policy
        a = Q.selectAction(s, epsilon);

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
        totalReward100R += reward;
        reward = 0;
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
            reward -= 2.0;  // When my robot crashes with enemy
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        reward -= 2.5;
    }

    @Override
    public void onDeath(DeathEvent event) {
        reward -= 5.0; // My robot dies
    }

    @Override
    public void onWin(WinEvent event) {
        reward += 10.0;
        ++numWin100R;
    }

    private double decayEpsilon() {
        if (roundNumber < targetNumRounds) {
            return epsilonInitial * (1 - (double) roundNumber / targetNumRounds);
        } else {
            return 0.0;
        }
    }
}
