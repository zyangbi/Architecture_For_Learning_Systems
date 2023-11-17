import robocode.AdvancedRobot;

public enum Action {
    FORWARD,
    BACKWARD,
    LEFTFORWARD,
    LEFTBAKCWARD,
    RIGHTFORWARD,
    RIGHTBACKWARD,
    FIRE;

    public void setAction(AdvancedRobot robot) {
        switch (this) {
            case FORWARD:
                robot.setAhead(100);
                break;
            case LEFTFORWARD:
                robot.setTurnLeft(90);
                robot.setAhead(100);
                break;
            case RIGHTFORWARD:
                robot.setTurnRight(90);
                robot.setAhead(100);
                break;
            case BACKWARD:
                robot.setBack(100);
                break;
            case LEFTBAKCWARD:
                robot.setTurnLeft(90);
                robot.setBack(100);
                break;
            case RIGHTBACKWARD:
                robot.setTurnRight(90);
                robot.setBack(100);
                break;
            case FIRE:
                robot.fire(3);
                break;
        }
    }
}
