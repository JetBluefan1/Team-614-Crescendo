// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.ManipulatorConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.drivetrain.vision.AlignScore;
import frc.robot.commands.drivetrain.vision.TurnToAngle;
import frc.robot.commands.drivetrain.ResetRobotHeading;
import frc.robot.commands.drivetrain.setXCommand;
import frc.robot.commands.manipulator.commandgroup.AutoFirstShot;
import frc.robot.commands.manipulator.commandgroup.AutoScore;
import frc.robot.commands.manipulator.commandgroup.IntakeNote;
import frc.robot.commands.manipulator.commandgroup.SimpleScoreNote;
import frc.robot.commands.manipulator.commandgroup.helpergroup.EmptyStomach;
import frc.robot.commands.manipulator.commandgroup.helpergroup.ResetWheels;
import frc.robot.commands.manipulator.commandgroup.helpergroup.ScoreReset;
import frc.robot.commands.manipulator.commandgroup.helpergroup.ShootPrep;
import frc.robot.commands.manipulator.feeder.IntakeFeed;
import frc.robot.commands.manipulator.feeder.ShooterFeed;
import frc.robot.commands.manipulator.intake.SimpleIntake;
import frc.robot.commands.manipulator.pivot.PivotDown;
import frc.robot.commands.manipulator.pivot.PivotPID;
import frc.robot.commands.manipulator.pivot.PivotUp;
import frc.robot.commands.manipulator.shooter.Shooter;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.PivotSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */

public class RobotContainer {
  // The robot's subsystems
  public final static DriveSubsystem swerveDrive = new DriveSubsystem();
  public final static IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public final static FeederSubsystem feederSubsystem = new FeederSubsystem();
  public final static ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  public final static PivotSubsystem pivotSubsystem = new PivotSubsystem();
  public final static LimelightSubsystem limeSubsystem = new LimelightSubsystem();
  static CommandXboxController m_driverController = new CommandXboxController(OIConstants.kDriverControllerPort);
  static CommandXboxController m_coDriverController = new CommandXboxController(OIConstants.kCoDriverControllerPort);
  private final SendableChooser<Command> autoChooser;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    DataLogManager.start();
    // Record both DS control and joystick data
    // DriverStation.startDataLog(DataLogManager.getLog());
    // (optional) Record only DS control data by uncommenting next line.
    // DriverStation.startDataLog(DataLogManager.getLog(), false);
  
    // Configure the button bindings
    configureButtonBindings();
    
    // Pathplanner Commands for use in auto. Name is what you type into pathplanner,
    // and the commands are "borrowed" from the controller
    NamedCommands.registerCommand("Score Close", new AutoScore(ManipulatorConstants.PIVOT_CLOSE_SCORE));
    NamedCommands.registerCommand("Score Far", new AutoScore(ManipulatorConstants.PIVOT_FAR_SCORE));
    NamedCommands.registerCommand("Score Amp", new AutoScore(ManipulatorConstants.PIVOT_AMP_GOAL));
    NamedCommands.registerCommand("Intake", new IntakeNote());
    NamedCommands.registerCommand("Firstshot", new AutoFirstShot());

    swerveDrive.setDefaultCommand(
        new RunCommand(
            () -> swerveDrive.drive(
                getDriverLeftY(),
                getDriverLeftX(),
                getDriverRightX(),
                true, true),
            swerveDrive));
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData(autoChooser);
  }

  public static CommandXboxController getDriverController() {
    return m_driverController;
  }

  public static CommandXboxController getCoDriverController() {
    return m_coDriverController;
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
   */

  public static double getLeftXWithDeadband() {
    return -MathUtil.applyDeadband(m_driverController.getLeftX(),
        OIConstants.kDriveDeadband);
  }

  public static double getDriverLeftX() {
    return .5 * Math.pow(getLeftXWithDeadband(), 5) + .5 * getLeftXWithDeadband();
  }

  public static double getLeftYWithDeadband() {
    return -MathUtil.applyDeadband(m_driverController.getLeftY(),
        OIConstants.kDriveDeadband);
  }

  public static double getDriverLeftY() {
    return .5 * Math.pow(getLeftYWithDeadband(), 5) + .5 * getLeftYWithDeadband();
  }

  public static double getRightXWithDeadband() {
    return -MathUtil.applyDeadband(m_driverController.getRightX(),
        OIConstants.kDriveDeadband);
  }

  public static double getDriverRightX() {
    return .5 * Math.pow(getRightXWithDeadband(), 5) + .5 * getRightXWithDeadband();
  }

  private void configureButtonBindings() {

    m_driverController.rightStick().whileTrue(new setXCommand());
    m_driverController.leftTrigger().whileTrue(new IntakeNote());
    m_driverController.rightTrigger().whileTrue(new EmptyStomach()).onFalse(new ResetWheels());
    m_driverController.y().onTrue(new PivotPID(ManipulatorConstants.PIVOT_MIN));
    m_driverController.a().whileTrue(new PivotDown(0.5, -0.1));
    m_driverController.start().whileTrue(new ResetRobotHeading());
    m_driverController.x().whileTrue(new TurnToAngle(90));
    m_driverController.b().whileTrue(new TurnToAngle(-90));

    m_coDriverController.rightStick().whileTrue(new setXCommand());
    m_coDriverController.rightTrigger().onTrue(new SimpleScoreNote(ManipulatorConstants.PIVOT_AMP_GOAL, 2000));
    m_coDriverController.rightBumper().onTrue(new SimpleScoreNote(ManipulatorConstants.PIVOT_FAR_SCORE, 5000));
    m_coDriverController.leftBumper().onTrue(new SimpleScoreNote(ManipulatorConstants.PIVOT_CLOSE_SCORE, 5000));
    m_coDriverController.y().whileTrue(new ScoreReset());
    m_coDriverController.b().whileTrue(new Shooter(ManipulatorConstants.SCORE_SIMPLE_RPM));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
