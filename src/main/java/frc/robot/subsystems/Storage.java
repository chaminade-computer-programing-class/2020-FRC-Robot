package frc.robot.subsystems;

import static frc.robot.Constants.KBallSensor1;
import static frc.robot.Constants.KBallSensor2;
import static frc.robot.Constants.KStage1Talon;
import static frc.robot.Constants.KStage2Talon;
import static frc.robot.Constants.KStorageShifterSolenoid;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.enums.SolenoidState;
import frc.robot.enums.StorageStage;

public class Storage extends SubsystemBase {
  //Create the talons
  private final VictorSPX stage1;
  private final VictorSPX stage2;

  //Create the solenoid
  private final Solenoid shifter;

  //Create the sensors
  private final DigitalInput ballSensor1;
  private final DigitalInput ballSensor2;

  //Variables, enums, etc.
  private int ballCount = 0;
  private boolean isIntaking;
  private SolenoidState shifterState = SolenoidState.DEFAULT;
  private boolean ballSensor1LastState = false; // Keeps track of the last state of the 1st ball sensor
  private boolean ballSensor2LastState = false; // Keeps track of the last state of the 2nd ball sensor
  private double stage1Speed = 0; // Keeps track of the speed of stage 1
  private double stage2Speed = 0; // Keeps track of the speed of stage 2

  public Storage() {
    //Instantiate everything
    stage1 = new VictorSPX(KStage1Talon);
    stage2 = new VictorSPX(KStage2Talon);
    shifter = new Solenoid(KStorageShifterSolenoid);
    ballSensor1 = new DigitalInput(KBallSensor1);
    ballSensor2 = new DigitalInput(KBallSensor2);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateBallCount();
  }

  /**
   * Moves the given conveyor belt(s) directly
   * 
   * @param speed Speed to move the conveyor belt(s) at
   * @param stage The stage(s) of the storage we want to move
   */
  public void move(double speed, StorageStage stage) {
    if (stage == StorageStage.STAGE1) {
      stage1Speed = speed;
      stage1.set(ControlMode.PercentOutput, speed);
    }

    if (stage == StorageStage.STAGE2) {
      stage2Speed = speed;
      stage2.set(ControlMode.PercentOutput, speed);
    }

    if (stage == StorageStage.BOTH) {
      stage1Speed = speed;
      stage1.set(ControlMode.PercentOutput, speed);
      // Make sure stage 2 is engaged before running the motor
      if (shifterState == SolenoidState.ACTIVE) {
        stage2Speed = speed;
        stage2.set(ControlMode.PercentOutput, speed);
      }
    }
  }

  /**
   * Moves both conveyor belts directly
   * 
   * @param speed Speed to move the conveyor belts at
   */
  public void move(double speed) {
    move(speed, StorageStage.BOTH);
  }

  /**
   * Set the state of the dogshifter to engage/disengage stage 2
   * 
   * @param state Change the state of the solenoid on the dogshifter
   */
  public void setShifterState(SolenoidState state){
    shifterState = state;
    shifter.set(state == SolenoidState.ACTIVE);
  }

  /**
   * Gets the state of the shifter
   * 
   * @return  The shifter's state
   */
  public SolenoidState getShifterState() {
    return shifterState;
  }

  /**
   * Tracks the number of balls in the storage
   */
  private void updateBallCount() {
    boolean ballSensor1State = ballSensor1.get();
    boolean ballSensor2State = ballSensor2.get();

    // Handles counting balls entering/exiting from the first stage
    if (stage1Speed > 0) {
      // Counts ball as in on the rising edge
      if (ballSensor1State && !ballSensor1LastState) {
          ballCount++;
      }
    } else {
      // Counts ball as out on the falling edge
      if (!ballSensor1State && ballSensor1LastState) {
        ballCount--;
      }
    }

    // Handles counting balls entering/exiting from the second stage
    if (stage2Speed > 0) {
      // Counts ball as out on the falling edge
      if (!ballSensor2State && ballSensor2LastState) {
        ballCount--;
      }
    } else {
      // Counts ball as in on the rising edge
      if (ballSensor2State && !ballSensor2LastState) {
        ballCount++;
      }
    }

    ballSensor1LastState = ballSensor1State;
    ballSensor2LastState = ballSensor2State;
  }
}