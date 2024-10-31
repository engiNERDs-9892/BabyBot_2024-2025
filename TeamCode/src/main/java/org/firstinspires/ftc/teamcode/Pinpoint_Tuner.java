package org.firstinspires.ftc.teamcode;

import android.icu.text.UFormat;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

@TeleOp
public class Pinpoint_Tuner extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        double rotationPower = 0.1;

        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("motorFL");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("motorBL");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("motorFR");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("motorBR");

        GoBildaPinpointDriver odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        /////////////////////////////////////////////////////////////////////////////////
        //////////////        ADJUST HUB DIRECTIONS TO SET HEADINGS        //////////////
        /////////////////////////////////////////////////////////////////////////////////
        //Ignore comment above
        //Copied from gobilda sample code please adjust values
        odo.setOffsets(0, 0);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            //Update the pinpoint odometry controller
            odo.update();

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                odo.resetPosAndIMU();
            }

            Pose2D position = odo.getPosition();
            Pose2D velocity = odo.getVelocity();

            double frontLeftPower = - rotationPower;
            double backLeftPower = - rotationPower;
            double frontRightPower = rotationPower;
            double backRightPower = rotationPower;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            double dxdtheta = velocity.getX(DistanceUnit.MM) / velocity.getHeading(AngleUnit.RADIANS);
            double dydtheta = velocity.getY(DistanceUnit.MM) / velocity.getHeading(AngleUnit.RADIANS);

            double radius = Math.sqrt(dxdtheta * dxdtheta + dydtheta * dydtheta);

            //Why does Math.atan2(y, x)?
            double angle = Math.atan2(dxdtheta / radius, -dydtheta / radius) - position.getHeading(AngleUnit.RADIANS);

            //In normal people coords so I can actually comprehend them
            double predicted_x = radius * Math.cos(angle);
            double predicted_y = radius * Math.sin(angle);

            telemetry.addData("Position",
                    "X: %.2f Y: %.2f H: %.2f",
                    position.getX(DistanceUnit.MM),
                    position.getY(DistanceUnit.MM),
                    position.getHeading(AngleUnit.RADIANS)
            );
            telemetry.addData("Velocity",
                    "X: %.2f Y: %.2f H: %.2f",
                    velocity.getX(DistanceUnit.MM),
                    velocity.getY(DistanceUnit.MM),
                    velocity.getHeading(AngleUnit.RADIANS)
            );
            telemetry.addData("Rotation Power", "%f", rotationPower);
            telemetry.addData("Intermediate Variables", "dx: %.2f dy %.2f rad: %.2f ang: %.2f", dxdtheta, dydtheta, radius, angle);
            telemetry.addData("Predicted Offsets", "X: %.2f Y: %.2f", predicted_x, predicted_y);

            telemetry.update();
        }
    }
}