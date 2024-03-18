package frc.robot.commands.manipulator.pivot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.Constants.ManipulatorConstants;

public class PivotAdjust extends Command {
  /** Creates a new TiltPIDCommand. */
  private double distanceFromAprilTag;
  private double setpoint;

  public PivotAdjust() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.pivotSubsystem);
    addRequirements(RobotContainer.limeSubsystem);
    distanceFromAprilTag = RobotContainer.limeSubsystem.estimateDistance();
    setpoint = RobotContainer.limeSubsystem.interpolateAngle(distanceFromAprilTag);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.pivotSubsystem.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    RobotContainer.pivotSubsystem.setGoal(setpoint);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return RobotContainer.pivotSubsystem.atGoal(setpoint, ManipulatorConstants.PIVOT_SHOOTER_THRESHOLD);
  }
}