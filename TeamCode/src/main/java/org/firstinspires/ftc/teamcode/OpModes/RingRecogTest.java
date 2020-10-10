package org.firstinspires.ftc.teamcode.OpModes;

import android.hardware.camera2.CameraCharacteristics;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.autonomous.AutoDot;
import org.firstinspires.ftc.teamcode.bots.DummyBot;
import org.firstinspires.ftc.teamcode.skills.RingDetector;
import org.firstinspires.ftc.teamcode.tfrec.Detector;
import org.firstinspires.ftc.teamcode.tfrec.classification.Classifier;
import java.util.List;

// Control Hub ADB Terminal Command for Reference
// adb.exe connect 192.168.43.1:5555

@TeleOp(name="Ring Rec", group="Robot15173")
//@Disabled
public class RingRecogTest extends LinearOpMode {

    // Declare OpMode members.
    private RingDetector rf = null;
    private AutoDot wobdot = new AutoDot();

    @Override
    public void runOpMode() {
        try {
            try {
                rf = new RingDetector(this.hardwareMap, telemetry);
            }
            catch (Exception ex){
                telemetry.addData("Error", String.format("Unable to initialize Detector. %s", ex.getMessage()));
                sleep(5000);
                return;
            }

            telemetry.addData("Detector", "Ready");
            telemetry.update();

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // detect ring stack
            wobdot = rf.detectRing(1, telemetry, this);

            // run until the end of the match (driver presses STOP)
            while (opModeIsActive()) {
                telemetry.addData("Zone", rf.returnZone());
                telemetry.addData("X-Coord", wobdot.getX());
                telemetry.addData("Y-Coord", wobdot.getY());
                telemetry.update();
            }
        }
        catch (Exception ex){
            telemetry.addData("Init Error", ex.getMessage());
            telemetry.update();
        }
        finally {
            if (rf != null){
                rf.stopDetection();
            }
        }
    }
}
