package autofunctions;

import com.qualcomm.robotcore.util.ElapsedTime;

import global.TerraBot;
import globalfunctions.Constants;
import util.CodeSeg;

public class RobotFunctions {
    private TerraBot bot;
    public ElapsedTime timer = new ElapsedTime();
    public void init(TerraBot t) {
        bot = t;
    }
    public CodeSeg intake(final double pow) {
        return new CodeSeg() {
            @Override
            public void run() {
                bot.intake(pow);
            }
        };
    }
    public CodeSeg startOuttake() {
        return new CodeSeg() {
            @Override
            public void run() {
                bot.outtaking = true;
                bot.resetOuttake();
            }
        };
    }
    public CodeSeg stopOuttake() {
        return new CodeSeg() {
            @Override
            public void run() {
                bot.outtaking = false;
            }
        };
    }
    public CodeSeg turnArm (final double pos) {
        return new CodeSeg() {
            @Override
            public void run() {
                bot.startWobbleGoalWithEncoders(pos, 1);
            }
        };
    }
    public CodeSeg stopArm () {
        return new CodeSeg() {
            @Override
            public void run() {
                bot.stopWobbleGoal();
            }
        };
    }
    public CodeSeg wgMoveFront() {
        return moveWgTo(Constants.WG_LOWER_LIMIT);
    }
    public CodeSeg wgMoveBack() {
        return moveWgTo(Constants.WG_UPPER_LIMIT);
    }
    public CodeSeg closeClaw() {
        return new CodeSeg() {
            @Override
            public void run() {
                bot.setClawPos(1);
            }
        };
    }
    public CodeSeg openClaw() {
        return new CodeSeg() {
            @Override
            public void run() {
                bot.setClawPos(0);
            }
        };
    }
    public CodeSeg moveWgTo(final double deg) {
        return new CodeSeg() {
            @Override
            public void run() {
                bot.moveArmWithEnc(deg, 1);
            }
        };
    }
    public CodeSeg resetHeadingUsingGyro(){
        return new CodeSeg() {
            @Override
            public void run() {
                bot.resetHeadingUsingGyro();
            }
        };
    }

    public CodeSeg resetPosUsingDisSensors(){
        return new CodeSeg() {
            @Override
            public void run() {
                bot.resetPosUsingDisSensors();
            }
        };
    }
    public CodeSeg resetAll(){
        return new CodeSeg() {
            @Override
            public void run() {
                bot.resetHeadingUsingGyro();
                bot.resetPosUsingDisSensors();
            }
        };
    }

    public CodeSeg setShotMode(final int i){
        return new CodeSeg() {
            @Override
            public void run() {
                bot.autoAimer.shotMode = i;
            }
        };
    }
    public CodeSeg nextShotMode(){
        return new CodeSeg() {
            @Override
            public void run() {
                bot.autoAimer.nextShotMode();
            }
        };
    }

    public CodeSeg readyShooter(){
        return new CodeSeg() {
            @Override
            public void run() {
                bot.outtaking = true;
                bot.resetOuttake();
                bot.rh.setPower(-0.3);
                bot.rh2.setPower(-1);
                bot.rp.setPosition(bot.pushControl.getPos(1));
                pause(0.3);
                bot.rh2.setPower(0);
                bot.rh.setPower(0);

            }
        };
    }

    public CodeSeg shootIntoGoal(final int numRings){
        return new CodeSeg() {
            @Override
            public void run() {
                bot.rh.setPower(-0.5);
                for(int i = 0; i < numRings; i++) {
                    pause(0.5);
                }
                bot.rp.setPosition(bot.pushControl.getPos(0));
                bot.rh.setPower(0);
            }
        };
    }

    public void pause(final double secs){
        timer.reset();
        while (timer.seconds() < secs){}
    }




}
