// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.Constants.IntakeConstants;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Pivot Command uses the PivotSubsystem in order
 * to set a specific value to the Pivot for the intake
 * of the robot
 * -
 * 
 * @param pivotSpeed,RobotContainer.pivotSubsystem takes in the speed that the
 *                                                 pivot is supposed to be set
 *                                                 to
 */

public class PivotUp extends Command {

  private double climbSpeed;

  /** Creates a new Pivot. */
  public PivotUp(double climbSpeed) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.climbSubsystem);
    this.climbSpeed = climbSpeed;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (Math.abs(RobotContainer.climbSubsystem.getClimbMotorHeight()) > 2 ) {
      RobotContainer.climbSubsystem.set(-climbSpeed);

      SmartDashboard.putNumber("Encoder Position in Command",
      RobotContainer.climbSubsystem.getClimbMotorHeight());
      
    
    } else {
      
      RobotContainer.climbSubsystem.set(IntakeConstants.MOTOR_ZERO_SPEED);

      

    }


  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.climbSubsystem.set(IntakeConstants.MOTOR_ZERO_SPEED);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
