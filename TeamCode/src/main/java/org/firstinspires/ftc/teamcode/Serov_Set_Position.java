package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Serov_Set_Position extends LinearOpMode {
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
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad2.a) {
                double pos = servo0.getPosition();
                servo0.setPosition(pos+.05);
                servo1.setPosition(pos+.05);
                servo2.setPosition(pos+.05);
                servo3.setPosition(pos+.05);
                servo4.setPosition(pos+.05);
                servo5.setPosition(pos+.05);
                telemetry.update();
            }

            if (gamepad2.b) {
                double pos = servo0.getPosition();
                servo0.setPosition(pos-.05);
                servo1.setPosition(pos-.05);
                servo2.setPosition(pos-.05);
                servo3.setPosition(pos-.05);
                servo4.setPosition(pos-.05);
                servo5.setPosition(pos-.05);
            }
            telemetry.addData("Current Position", servo0.getPosition());
            telemetry.addData("Add .05 to Position", "Press A");
            telemetry.addData("Subtract .05 to position", "Press B");
        }
    }
}