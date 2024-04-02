// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.manipulator.commandgroup;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.Constants.ManipulatorConstants;
import frc.robot.commands.manipulator.shooter.Blow;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SimpleScoreTrap extends ParallelDeadlineGroup {
  /** Creates a new SimpleScoreTrap. */
  public SimpleScoreTrap() {
    // Add the deadline command in the super() call. Add other commands using
    // addCommands().
    super(new SimpleScoreNote(ManipulatorConstants.PIVOT_TRAP_SCORE,
          ManipulatorConstants.TRAP_SPEED, ManipulatorConstants.SHOOTER_THRESHOLD));
    addCommands(new Blow());
  }
}
