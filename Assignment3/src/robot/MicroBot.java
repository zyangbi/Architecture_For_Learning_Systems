package robot;

import lut.LUT;
import robocode.*;
import state.*;
import utils.LogFile;

import java.io.File;

public class MicroBot extends AdvancedRobot {
    private static final int TOTAL_ROUNDS = 10000;
    private static final String logFileName = "original";
    private static final double ALPHA = 0.99; // alpha in Q learning update
    private static final double GAMMA = 0.95; // gamma in Q learning update
    private static final double EPSILON_INITIAL = 0.8;
    private static final double EPSILON_RATIO = 0.7; // percentage where epsilon gets to 0
    private static final double REWARD_BULLET_HIT = 0.4;
    private static final double REWARD_BULLET_MISSED = -0.2;
    private static final double REWARD_HIT_BY_BULLET = -0.2;
    private static final double REWARD_BEING_RAMMED = -0.2;
    private static final double REWARD_HIT_WALL = -0.3;
    private static final double REWARD_DEATH = -0.3;
    private static final double REWARD_WIN = 1.0;

    private static final LUT lut; // Q(s,a)
    private static int roundNumber;
    private static int numWin100R; // number of wins in 100 rounds
    private static double totalReward100R; // total reward in 100 rounds
    private static LogFile log;

    private State s; // s
    private State sPrime; // sPrime
    private Action a; // a
    private double reward; // r
    private double epsilon;
    private boolean firstScan;

    static {
        lut = new LUT(ALPHA, GAMMA);
        lut.initialiseLUT();
        roundNumber = 0;
        numWin100R = 0;
        totalReward100R = 0.0;
    }

    // Scan for enemy
    @Override
    public void run() {
        // Initialize at each round
        s = null;
        sPrime = null;
        reward = 0;
        epsilon = decayEpsilon();
        firstScan = true;

        // Initialize log at round 0
        if (roundNumber == 0) {
            log = new LogFile(getDataDirectory(), logFileName);
        }

        // Print log every 100 rounds
        if (roundNumber % 100 == 0) {
            printLog();
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
        sPrime = new State(getX(), getY(), getBattleFieldWidth(), getBattleFieldHeight(), getHeading(),
                getEnergy(), e.getDistance(), e.getBearing());

        // 4. Update Q(s,a) with s' and reward
        if (!firstScan) {
            lut.updateLUT(reward, s, a, sPrime);
            totalReward100R += reward;
            reward = 0;
        } else {
            firstScan = false;
        }

        // 5. s = s'
        s = sPrime;

        // 1. Choose a from s by epsilon-greedy policy
        a = lut.selectAction(s, epsilon);

        // 2. Take action a
        setTurnGunRight(getHeading() - getGunHeading() + e.getBearing()); // Turn the gun towards enemy
        a.setAction(this);
        execute();
    }

    // Update Q-value at the end of the game
    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        lut.updateLUT(reward, s, a, sPrime);
        totalReward100R += reward;
        reward = 0;
        roundNumber++;
    }

    // Update rewards when events happen
    @Override
    public void onBulletHit(BulletHitEvent event) {
        reward += REWARD_BULLET_HIT;
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        reward += REWARD_BULLET_MISSED;
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        reward += REWARD_HIT_BY_BULLET;
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        if (!event.isMyFault()) {
            reward += REWARD_BEING_RAMMED;
        }
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        reward += REWARD_HIT_WALL;
    }

    @Override
    public void onDeath(DeathEvent event) {
        reward += REWARD_DEATH; // My robot dies
    }

    @Override
    public void onWin(WinEvent event) {
        reward += REWARD_WIN;
        ++numWin100R;
    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {
        log.close();
        File file = new File(getDataDirectory(), "test.txt");
        lut.save(file, getBattleFieldWidth(), getBattleFieldHeight());
    }

    // Decay epsilon for first 80% rounds, and 0 epsilon for final 20% rounds
    private double decayEpsilon() {
        if (roundNumber < TOTAL_ROUNDS * EPSILON_RATIO) {
            return EPSILON_INITIAL * (1 - (double) roundNumber / (TOTAL_ROUNDS * EPSILON_RATIO));
        } else {
            return 0.0;
        }
    }

    private void printLog() {
        if (roundNumber == 0) {
            log.println("Round Number,Number of Wins,Total Reward");
        } else {
            log.println(roundNumber + "," + numWin100R + "," + totalReward100R);
        }
    }

}
