package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class Serov_Set extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our servos
        Servo servo0;
        Servo servo1;
        Servo servo2;
        Servo servo3;
        Servo servo4;
        Servo servo5;



        // Hardware Map
        // Make sure your ID's match your configuration
        servo0 = hardwareMap.servo.get("servo0");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        servo3 = hardwareMap.servo.get("servo3");
        servo4 = hardwareMap.servo.get("servo4");
        servo5 = hardwareMap.servo.get("servo5");


        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Set Servo Position", "A=0");
        telemetry.addData("Set Servo Position", "B=.25");
        telemetry.addData("Set Servo Position", "Y=.5");
        telemetry.addData("Set Servo Position", "X=.75");

        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad2.a) {
                servo0.setPosition(0);
                servo1.setPosition(0);
                servo2.setPosition(0);
                servo3.setPosition(0);
                servo4.setPosition(0);
                servo5.setPosition(0);
            }

            if (gamepad2.b) {
                servo0.setPosition(.25);
                servo1.setPosition(.25);
                servo2.setPosition(.25);
                servo3.setPosition(.25);
                servo4.setPosition(.25);
                servo5.setPosition(.25);
            }
            if (gamepad2.y) {
                servo0.setPosition(0.5);
                servo2.setPosition(.5);
                servo3.setPosition(.5);
                servo4.setPosition(0.5);
                servo5.setPosition(0.5);
            }
            if (gamepad2.x) {
                servo0.setPosition(.75);
                servo1.setPosition(0.75);
                servo2.setPosition(0.75);
                servo3.setPosition(0.75);
                servo4.setPosition(0.75);
                servo5.setPosition(0.75);
            }
        }
    }
}