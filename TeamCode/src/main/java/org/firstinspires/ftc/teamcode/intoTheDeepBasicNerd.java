package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.sequencesegment.TrajectorySegment;


@Autonomous(group = "drive")
public class intoTheDeepBasicNerd extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        telemetry.addLine("Initializing trajectory");
        telemetry.update();
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d())
                .splineTo(new Vector2d(12, 24), Math.toRadians(135))
                .build();
        Trajectory traj2 = drive.trajectoryBuilder(traj1.end())
                .lineToSplineHeading(new Pose2d(60, 12, Math.toRadians(270)))
                .addDisplacementMarker(()->{telemetry.addLine("2.2");telemetry.update();})
                .splineToSplineHeading(new Pose2d(60, -12, Math.toRadians(270)), Math.toRadians(270))
                .build();

        Trajectory trajE = drive.trajectoryBuilder(traj2.end(), true)
                .back(36)
                .splineTo(new Vector2d(6, 0), Math.toRadians(180))
                .build();
        telemetry.addLine("Done initializing trajectory");
        telemetry.addData("Pose: ", drive.getPoseEstimate());
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;
        telemetry.addLine("Trajectory 1");
        telemetry.update();
        drive.followTrajectory(traj1);
        sleep(2000);
        telemetry.addLine("Trajectory 2");
        telemetry.update();
        drive.followTrajectory(traj2);
        sleep(3000);
        telemetry.addLine("Trajectory 3");
        telemetry.update();
        drive.followTrajectory(trajE);
    }
}
