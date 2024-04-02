// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain.vision;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.VisionConstants;
import frc.robot.RobotContainer;

/** Creates a new AlignScore. */
public class AlignToAngle extends Command {

  private double angle, target, turn;

  public AlignToAngle(double target) {
    addRequirements(RobotContainer.swerveDrive);
    this.target = target;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (!RobotContainer.isAllianceRed()) {
      target = -target;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    angle = RobotContainer.swerveDrive.getDisplacementToTarget(target);
    turn = -angle / 180.0;

    if (Math.abs(angle) > VisionConstants.ALIGN_THRESHOLD) {
      RobotContainer.swerveDrive.drive(
          RobotContainer.getDriverLeftY(), RobotContainer.getDriverLeftX(), turn, true, true);
    } else {
      RobotContainer.swerveDrive.drive(
          RobotContainer.getDriverLeftY(),
          RobotContainer.getDriverLeftX(),
          RobotContainer.getDriverRightX(),
          true,
          true);
    }
  }

  // insert code to adjust robot angle here

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (!RobotContainer.isAllianceRed()) {
      target = -target;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
