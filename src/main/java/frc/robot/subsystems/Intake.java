/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.commands.Base.DriveWithJoysticks;

public class Intake extends SubsystemBase {
  /**
   * Creates a new Intake.
   */
  private final TalonSRX Intake;
  public static final int KIntakeTalon = 1;
  public static double KIntakeSpeed = 1; 
  
  public Intake() {
    Intake = new TalonSRX(KIntakeTalon);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void move(double IntakeSpeed){
    Intake.set(ControlMode.PercentOutput, IntakeSpeed);
  }
}