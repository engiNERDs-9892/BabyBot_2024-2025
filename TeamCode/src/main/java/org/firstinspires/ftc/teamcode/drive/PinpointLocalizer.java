package org.firstinspires.ftc.teamcode.drive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.util.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class PinpointLocalizer implements Localizer {

    private Encoder leftEncoder, rightEncoder, frontEncoder;

    private List<Integer> lastEncPositions, lastEncVels;
    GoBildaPinpointDriver odo;
    public PinpointLocalizer(HardwareMap hardwareMap, List<Integer> lastTrackingEncPositions, List<Integer> lastTrackingEncVels) {


        lastEncPositions = lastTrackingEncPositions;
        lastEncVels = lastTrackingEncVels;

        //leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftEncoder"));
        //rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightEncoder"));
        //frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "frontEncoder"));
        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();
        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)


    }


    @NonNull
    @Override
    public Pose2d getPoseEstimate() {
        Pose2D pose = odo.getPosition();
        return new Pose2d(pose.getX(DistanceUnit.MM), pose.getY(DistanceUnit.MM), pose.getHeading(AngleUnit.DEGREES));
    }

    @Override
    public void setPoseEstimate(@NonNull Pose2d pose2d) {

    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        Pose2D pose = odo.getVelocity();
        return new Pose2d(pose.getX(DistanceUnit.MM), pose.getY(DistanceUnit.MM), pose.getHeading(AngleUnit.DEGREES));
    }

    @Override
    public void update() {
        odo.update();
    }

    //@Override
    //public
}
