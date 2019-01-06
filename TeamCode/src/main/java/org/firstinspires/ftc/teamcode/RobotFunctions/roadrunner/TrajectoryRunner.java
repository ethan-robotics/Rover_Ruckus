package org.firstinspires.ftc.teamcode.RobotFunctions.roadrunner;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.drive.TankDrive;
import com.acmerobotics.roadrunner.followers.TankPIDVAFollower;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.IOException;

public class TrajectoryRunner {
    TankDrive drive;
    Pose2d startingPose = new Pose2d(0, 0, 0);
    LinearOpMode opMode;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    TelemetryPacket packet = new TelemetryPacket();

    public TrajectoryRunner(TankDrive drive, Pose2d startingPose, LinearOpMode opMode){
        this.drive = drive;
        this.startingPose = startingPose;
        this.opMode = opMode;
    }

    public TrajectoryRunner(TankDrive drive, LinearOpMode opMode){
        this.drive = drive;
        this.opMode = opMode;
    }

    public void setStartingPose(Pose2d startingPose){this.startingPose = startingPose;}

    public void runTrajectory(String trajectory){
        Trajectory Trajectory;
        TankPIDVAFollower follower = new TankPIDVAFollower(
                drive,
                DriveConstants.TRANSLATIONAL_PID,
                DriveConstants.HEADING_PID,
                DriveConstants.kV,
                0,
                0);

        if(trajectory != null){
            try {
                Trajectory = AssetsTrajectoryLoader.load(trajectory);
            } catch (IOException e){
                throw new RuntimeException(e);
            }

            drive.setPoseEstimate(startingPose);

            follower.followTrajectory(Trajectory);
        } else {
            opMode.telemetry.addData("error:", "trajectory is null");
            opMode.telemetry.update();
        }

        while(follower.isFollowing() && opMode.opModeIsActive()){
            Pose2d currentPose = drive.getPoseEstimate();

            follower.update(currentPose);
            drive.updatePoseEstimate();
        }
    }


}
