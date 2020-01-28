package frc.robot.subsystems;

import static frc.robot.Constants.KBaseMediumGear;
import static frc.robot.Constants.KBaseShifterSolenoid;
import static frc.robot.Constants.KLeftBackTalon;
import static frc.robot.Constants.KLeftFrontTalon;
import static frc.robot.Constants.KRightBackTalon;
import static frc.robot.Constants.KRightFrontTalon;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.enums.BaseState;

public class Base extends SubsystemBase {
  //Creating the Talons
  private final TalonSRX leftFront, leftBack, rightFront, rightBack;

  //Creating the Solenoids
  private final Solenoid shifter;

  //Sets the default state to medium
  private BaseState baseState = BaseState.MEDIUM;
  
  //Variables
  private static final int TicksPerRotation = 4600; //conversion factor that we have to find
  private static final int FreeSpeedRPM = 6380; // Free speed of the base motors in RPM (check in the Quran)
  private static final int FreeSpeed = (FreeSpeedRPM / 600) * TicksPerRotation; // Free speed of the base in ticks per 100 ms
  private static final double LowGear = 62 / 8; // Numbers from the Quran, absolutely 100% true
  private static final double HighGear = 32 / 24; // Numbers from the Quran, absolutely 100% true

  public Base() {
    //instantiating the talons
    leftFront = new TalonSRX(KLeftFrontTalon);
    leftBack = new TalonSRX(KLeftBackTalon);
    rightFront = new TalonSRX(KRightFrontTalon);
    rightBack = new TalonSRX(KRightBackTalon);

    // Slaving the talons
    leftBack.follow(leftFront);
    rightBack.follow(rightFront);
    
    // Instantiating the solenoid
    shifter = new Solenoid(KBaseShifterSolenoid);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Moves the base directly
   * 
   * @param leftSpeed   Speed to move the left side at
   * @param rightSpeed  Speed to move the right side at
   */
  public void move(double leftSpeed, double rightSpeed) {
    if (baseState == BaseState.MEDIUM) {
      leftFront.set(ControlMode.PercentOutput, leftSpeed * KBaseMediumGear);
      rightFront.set(ControlMode.PercentOutput, rightSpeed * KBaseMediumGear);
    } else {
      leftFront.set(ControlMode.PercentOutput, leftSpeed);
      rightFront.set(ControlMode.PercentOutput, rightSpeed);
    }
  }

  /**
   * Sets the base shift to high, medium, or low
   * 
   * @param state State to set the base to
   */
  public void setBaseState(BaseState state) {
    baseState = state;
    
    if (baseState == BaseState.HIGH || baseState == BaseState.MEDIUM) {
      shifter.set(true);
    } else {
      shifter.set(false);
    }
  }

  /**
   * Gets the base's state
   * 
   * @return State of the base
   */
  public BaseState getBaseState() {
    return baseState;
  }

  /**
   * Gets the encoder value of the left side
   * 
   * @return  Left encoder value
   */
  public double getLeftEncoder() {
    return (double)leftFront.getSelectedSensorPosition();
  }

  /**
   * Gets the encoder value of the right side
   * 
   * @return  Right encoder value
   */
  public double getRightEncoder() {
    return (double)rightFront.getSelectedSensorPosition();
  }

  /**
   * Zeroes the encoders on both sides
   */
  public void zeroEncoders() {
    leftFront.getSensorCollection().setQuadraturePosition(0, 0);
    rightFront.getSensorCollection().setQuadraturePosition(0, 0);
    leftBack.getSensorCollection().setQuadraturePosition(0, 0);
    rightBack.getSensorCollection().setQuadraturePosition(0, 0);
  }

  //Getters

  /**
   * Gets the speed at which we should shift from low gear to high gear. The relevant equation can be found here: https://www.chiefdelphi.com/t/frc-95-the-grasshoppers-2020-build-thread/368912/28
   * 
   * @return  Speed in ticks per 100 ms
   */
  public double getShiftSpeed() {
    return FreeSpeed / (HighGear + LowGear);
  }

  /**
   * Gets the speed of the left side
   * 
   * @return Speed in ticks per 100 ms
   */
  public double getLeftSpeed() {
    return (double)leftFront.getSelectedSensorVelocity(); //selected sensor (in raw sensor units) per 100ms
  }

  /**
   * Gets the speed of the right side
   * 
   * @return Speed in ticks per 100 ms
   */
  public double getRightSpeed() {
    return (double)rightFront.getSelectedSensorVelocity(); //selected sensor (in raw sensor units) per 100ms
  }
}
