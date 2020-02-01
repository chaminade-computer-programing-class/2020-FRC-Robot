package frc.robot.commands.Tilter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class TilterStop extends CommandBase {
  /**
   * Creates a new TilterStop.
   */
  public TilterStop() {
    addRequirements(Robot.tilter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.tilter.move(0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}