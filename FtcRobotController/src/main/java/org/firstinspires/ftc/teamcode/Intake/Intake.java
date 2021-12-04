
package org.firstinspires.ftc.teamcode.Intake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    DcMotor intakeMotor;

    //Toggle
    Boolean prevInput;
    Boolean itToggle;

    public Intake(HardwareMap hardwareMap){
        intakeMotor = hardwareMap.dcMotor.get("motorIntake");
        prevInput = false;
        itToggle = false;
    }


    public void intakeToggle(Boolean inp) {
        if(inp == true && prevInput == false) {
            itToggle = !itToggle;
            prevInput = true;
        }
        prevInput = inp;
        if (itToggle == true) {
            intakeMotor.setPower(1);
        } else {
            intakeMotor.setPower(0);
        }
    }


    public void intakeHold(Boolean inp) {
        if (inp == true) {
            intakeMotor.setPower(1);
        } else {
            intakeMotor.setPower(0);
        }
    }

}


