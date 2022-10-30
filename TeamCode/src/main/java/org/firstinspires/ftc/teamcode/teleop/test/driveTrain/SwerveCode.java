package org.firstinspires.ftc.teamcode.teleop.test.driveTrain;

import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.ConstantsPKG.Constants;
import org.firstinspires.ftc.teamcode.common.Kinematics.Kinematics;
import org.firstinspires.ftc.teamcode.common.gps.GlobalPosSystem;
import org.firstinspires.ftc.teamcode.common.Button;

import org.firstinspires.ftc.teamcode.common.HardwareDrive;

@TeleOp(name="Swerve Code", group="Drive")
//@Disabled
public class SwerveCode extends OpMode{
    /* Declare OpMode members. */
    HardwareDrive robot = new HardwareDrive();
    GlobalPosSystem posSystem;
    Kinematics kinematics;
    private double[] posData = new double[4];

    private ElapsedTime runtime = new ElapsedTime();
    Constants constants = new Constants();

    ElapsedTime accelerationTimer = new ElapsedTime();
    boolean isAccelerateCycle = false;

    Button x = new Button();
    Button y = new Button();
    Button a = new Button();
    Button b = new Button();

    private int rotateR;
    private int rotateL;

    public enum State{
        DRIVE,
        RESET
    }
    State driveState = State.DRIVE;
    boolean isResetCycle = false;

    //for resetting the robot's wheels' orientation
    ElapsedTime resetTimer = new ElapsedTime();
    /** The relativeLayout field is used to aid in providing interesting visual feedback
     * in this sample application; you probably *don't* need this when you use a color sensor on your
     * robot. Note that you won't see anything change on the Driver Station, only on the Robot Controller. */
    View relativeLayout;

    @Override
    public void init() { //When "init" is clicked
        robot.init(hardwareMap);
        posSystem = new GlobalPosSystem(robot);
        kinematics = new Kinematics(posSystem);
        posSystem.grabKinematics(kinematics);

        telemetry.addData("Say", "Hello Driver");
        runtime.reset();

        robot.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void init_loop() { //Loop between "init" and "start"

    }

    @Override
    public void start() { //When "start" is pressed
    }

    @Override
    public void loop() { //Loop between "start" and "stop"
        UpdatePlayer1();
        UpdatePlayer2();
        UpdateButton();
        UpdateTelemetry();
    }

    void UpdatePlayer1(){
       // DriveTrainBasePower();
        if (driveState == State.DRIVE){
            DriveTrainPowerEncoder();
            isResetCycle = false;
        } else if (driveState == State.RESET){
            reset();
        }

    }

    void UpdatePlayer2(){
    }

    void UpdateTelemetry(){
        telemetry.addData("X", gamepad1.left_stick_x);
        telemetry.addData("Y", -gamepad1.left_stick_y);
        telemetry.addData("R", gamepad1.right_stick_x);
        //  telemetry.addData("Touch Sensor", robot.digitalTouch.getState());

        for(int i = 0; i < 4; i++){
            posData[i] = posSystem.getPositionArr()[i];
        }
        telemetry.addData("Xpos", posData[0]);
        telemetry.addData("Ypos", posData[1]);
        telemetry.addData("W", posData[2]);
        telemetry.addData("R", posData[3]);

        telemetry.addData("topL clicks", robot.topL.getCurrentPosition());
        telemetry.addData("botL clicks", robot.botL.getCurrentPosition());
        telemetry.addData("topR clicks", robot.topR.getCurrentPosition());
        telemetry.addData("botR clicks", robot.botR.getCurrentPosition());

        telemetry.addData("rotateR target", rotateR);
        telemetry.addData("rotateL target", rotateL);
        telemetry.addData("isBusy", robot.wheelsAreBusy());
        telemetry.addData("reset cycle?", isResetCycle);
        telemetry.update();
    }

    void UpdateButton(){
        x.update(gamepad1.x);
        y.update(gamepad1.y);
        a.update(gamepad1.a);
        b.update(gamepad1.b);

        if (x.getState() == Button.State.TAP){
            driveState = State.RESET;
        } else if (y.getState() == Button.State.TAP){
            driveState = State.DRIVE;
        }
    }

    void DriveTrainBasePower(){
        int powerBotL = 1;
        int powerTopL = 1;

        if (gamepad1.dpad_up){
            robot.botL.setPower(powerBotL);
            robot.topL.setPower(powerTopL);
        }
        else{
            robot.botL.setPower(0);
            robot.topL.setPower(0);
        }
    }

    void DriveTrainPowerEncoder(){
        posSystem.calculatePos();
        if (noMovementRequests()){
            isAccelerateCycle = false;
        }

        int posBotL = robot.botL.getCurrentPosition();
        int posTopL = robot.topL.getCurrentPosition();
        int posBotR = robot.botR.getCurrentPosition();
        int posTopR = robot.topR.getCurrentPosition();

        double alpha = 0.5;
        double beta = 1 - alpha;

        int distanceTopL = (int) (gamepad1.left_stick_y * 100 * beta);
        int distanceBotL = (int) (-gamepad1.left_stick_y * 100 * beta);
        int distanceTopR = distanceTopL;
        int distanceBotR = distanceBotL;

        int rotationalTopL = (int) (gamepad1.left_stick_x * 100 * alpha);
        int rotationalBotL = (int) (gamepad1.left_stick_x * 100 * alpha);
        int rotationalTopR = rotationalTopL;
        int rotationalBotR = rotationalBotL;

        robot.botL.setTargetPosition(posBotL + distanceBotL + rotationalBotL);
        robot.topL.setTargetPosition(posTopL + distanceTopL + rotationalTopL);
        robot.botR.setTargetPosition(posBotR + distanceBotR + rotationalBotR);
        robot.topR.setTargetPosition(posTopR + distanceTopR + rotationalTopR);

        robot.botL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.topL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.botR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.topR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.botL.setPower(gamepad1.left_stick_y * beta + gamepad1.left_stick_x * alpha);
        robot.topL.setPower(gamepad1.left_stick_y * beta + gamepad1.left_stick_x * alpha);
        robot.botR.setPower(gamepad1.left_stick_y * beta + gamepad1.left_stick_x * alpha);
        robot.topR.setPower(gamepad1.left_stick_y * beta + gamepad1.left_stick_x * alpha);
    }

    public double accelerator(double power){
        if (power == 0) return 0.0;

        if (!isAccelerateCycle){
            accelerationTimer.reset();
            isAccelerateCycle = true;
        }
        double accelerationFactor = (Math.tanh(accelerationTimer.milliseconds() - 1.5) / 2.5) + 0.6;
        power *= accelerationFactor;

        if (power > 1) power = 1;
        else if (power < -1) power = -1;

        return power;
    }

    private void reset(){
        int topR = robot.topR.getCurrentPosition();
        int botR = robot.botR.getCurrentPosition();
        int topL = robot.topL.getCurrentPosition();
        int botL = robot.botL.getCurrentPosition();

        rotateR = (topR + botR) / 2;
        rotateL = (topL + botL) / 2;

        int topLTarget = (int)((topL - rotateL) % constants.CLICKS_PER_PURPLE_REV);
        int botLTarget = (int)((botL - rotateL) % constants.CLICKS_PER_PURPLE_REV);
        int topRTarget = (int)((topR - rotateR) % constants.CLICKS_PER_PURPLE_REV);
        int botRTarget = (int)((botR - rotateR) % constants.CLICKS_PER_PURPLE_REV);

        if (!isResetCycle){
            isResetCycle = true;

            robot.topL.setTargetPosition(topLTarget);
            robot.botL.setTargetPosition(botLTarget);
            robot.topR.setTargetPosition(topRTarget);
            robot.botR.setTargetPosition(botRTarget);

            robot.botL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.topL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.botR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.topR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.botL.setPower(0.3);
            robot.topL.setPower(0.3);
            robot.botR.setPower(0.3);
            robot.topR.setPower(0.3);
        }

        else{
            robot.botL.setPower(0.3);
            robot.topL.setPower(0.3);
            robot.botR.setPower(0.3);
            robot.topR.setPower(0.3);

            if (robot.topL.getCurrentPosition() == topLTarget && robot.botL.getCurrentPosition() == botLTarget && robot.topR.getCurrentPosition() == topRTarget && robot.botR.getCurrentPosition() == botRTarget){
                robot.topL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.botL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.topR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.botR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                robot.topL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.botL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.topR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.botR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                robot.botL.setPower(0);
                robot.topL.setPower(0);
                robot.botR.setPower(0);
                robot.topR.setPower(0);
            }
        }

        //make sure to reset the encoder position afterwards without messing stuff up like before.
    }


    public boolean noMovementRequests(){
        return (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 && gamepad1.right_stick_x == 0 && gamepad1.right_stick_y == 0);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}