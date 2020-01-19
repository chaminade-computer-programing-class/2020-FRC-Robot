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
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.enums.SolenoidState;
import frc.robot.enums.StorageStage;
import edu.wpi.first.wpilibj.DigitalInput;

public class Storage extends SubsystemBase {
  /**
   * Creates a new Storage.
   */
  private final TalonSRX Stage1;
  private final TalonSRX Stage2;
  private Solenoid Shifter;
  public static final int KStage1Talon = 9;
  public static final int KStage2Talon = 10;
  public static final int KSolenoid = 11;
  public static double KStorageSpeed = 1;

  public static DigitalInput BallSensor1;
  public static DigitalInput BallSensor2;
  public static final int KBallSensor1 = 12; 
  public static final int KBallSensor2 = 13; 
 
  public static int numberOfBalls = 0;

  public Storage() {
    Shifter = new Solenoid(KSolenoid);
    Stage1 = new TalonSRX(KStage1Talon);
    Stage2 = new TalonSRX(KStage2Talon);
    BallSensor1 = new DigitalInput(KBallSensor1);
    BallSensor2 = new DigitalInput(KBallSensor2);
  }

  public void move(double speed, StorageStage stage) {
    if (stage == StorageStage.STAGE1) {
      Stage1.set(ControlMode.PercentOutput, speed);
    }
    if (stage == StorageStage.STAGE2) {
      Stage2.set(ControlMode.PercentOutput, speed);
    }
    if (stage == StorageStage.BOTH) {
      Stage1.set(ControlMode.PercentOutput, speed);
      Stage2.set(ControlMode.PercentOutput, speed);
    }
  }
  public void setShifter(SolenoidState state){
    //if (state == SolenoidState ACTIVE) {
      Shifter.set(state == SolenoidState.ACTIVE);
    //}
  }

   public int ballCount(boolean isIntaking){
     numberOfBalls +=1;
     return numberOfBalls;
   }
  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
