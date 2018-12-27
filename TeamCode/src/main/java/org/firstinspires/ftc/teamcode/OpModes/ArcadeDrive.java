package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RobotFunctions.Calculators;
import org.firstinspires.ftc.teamcode.RobotFunctions.TankHardware;

import static org.firstinspires.ftc.teamcode.RobotFunctions.dashboardConstants.Misc.turnSpeed;

@TeleOp
public class ArcadeDrive extends OpMode {
    TankHardware robot = new TankHardware();
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket packet = new TelemetryPacket();
    Calculators calc = new Calculators();

    public void init(){
        robot.init(hardwareMap);
        robot.driveTrain.setBrake();
    }

    public void loop(){
        robot.driveTrain.arcadeDrive(gamepad1.left_stick_x * turnSpeed, gamepad1.left_stick_y, gamepad1.left_bumper);
        robot.intake.pivot.setPower(gamepad1.right_stick_y / 4);
        if(gamepad1.left_bumper){robot.intake.flaps.setPower(1);}
        else if (gamepad1.right_bumper){robot.intake.flaps.setPower(-1);}
        telemetry.addData("left dist", calc.Encoder2Inches(robot.driveTrain.bl.getCurrentPosition()));
        packet.put("back left pow", robot.driveTrain.bl.getPower());
        packet.put("back right pow", robot.driveTrain.br.getPower());
        dashboard.sendTelemetryPacket(packet);
    }
}
