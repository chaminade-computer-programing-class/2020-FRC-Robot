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
//import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.commands.Base.DriveWithJoysticks;
import frc.robot.enums.BaseState;

public class Base extends SubsystemBase {
  /**
   * Creates a new Base.
   */
  
  //Creating the Talons
  private final TalonSRX leftFront, leftBack, rightFront, rightBack;
  public static final int KLeftFrontTalon = 1;
  public static final int KLeftBackTalon = 2;
  public static final int KRightFrontTalon = 3;
  public static final int KRightBackTalon = 4;
  //Sets the default state to medium
  private BaseState baseState = BaseState.MEDIUM;

  //Creating the Solenoids
  private final Solenoid shifterSolenoid;
  public static final int KShifterSolenoid = 0;

  //Variables
  public static final int TicksPerRotation = 4600; //conversion factor that we have to find
  public static final int FreeSpeed = (6380/3600) * TicksPerRotation; 
  public static final double LowGear = 62/8; // Numbers from the Quran, absolutely 100% true
  public static final double HighGear = 32/24; // Numbers from the Quran, absolutely 100% true

  public Base() {
    //instantiating the talons
    leftFront = new TalonSRX(KLeftFrontTalon);
    leftBack = new TalonSRX(KLeftBackTalon);
    rightFront = new TalonSRX(KRightFrontTalon);
    rightBack = new TalonSRX(KRightBackTalon);

    //slaving the talons
    leftBack.follow(leftFront);
    rightBack.follow(rightFront);
    
    //instantiating the solenoid
    shifterSolenoid = new Solenoid(KShifterSolenoid);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  //moves the base
  public void move(double leftSpeed, double rightSpeed) {
    if (baseState == BaseState.MEDIUM) {
      leftFront.set(ControlMode.PercentOutput, leftSpeed * 0.8);
      rightFront.set(ControlMode.PercentOutput, rightSpeed * 0.8);
    } else {
      leftFront.set(ControlMode.PercentOutput, leftSpeed);
      rightFront.set(ControlMode.PercentOutput, rightSpeed);
    }
  } 

  //shifts the base to the state we want to be in mechanically
  public void setBaseState(BaseState state) {
    baseState = state; 
    
    if(baseState == BaseState.HIGH || baseState == BaseState.MEDIUM) {
      shifterSolenoid.set(true);
    } else {
      shifterSolenoid.set(false);
    }
  }

  //Zeroes encoders
  public void zeroEncoders(){
    leftFront.getSensorCollection().setQuadraturePosition(0, 0);
    rightFront.getSensorCollection().setQuadraturePosition(0, 0);
    leftBack.getSensorCollection().setQuadraturePosition(0, 0);
    rightBack.getSensorCollection().setQuadraturePosition(0, 0);
  }

  //Getters
  //This specifically finds the speed at which we need to shift (documentation is in ryver)
  public int getShiftSpeed() {
    return (int)(FreeSpeed / (HighGear + LowGear));
  }

  public double getLeftSpeed(){
    return leftFront.getSelectedSensorVelocity(); //selected sensor (in raw sensor units) per 100ms
  }

  public double getRightSpeed(){
    return rightFront.getSelectedSensorVelocity(); //selected sensor (in raw sensor units) per 100ms
  }

  public double getLeftFrontEncoder() {
    return leftFront.getSelectedSensorPosition();
  }

  public double getRightFrontEncoder() {
    return rightFront.getSelectedSensorPosition();
  }

  public BaseState getBaseState() {
    return baseState;
  }
}
