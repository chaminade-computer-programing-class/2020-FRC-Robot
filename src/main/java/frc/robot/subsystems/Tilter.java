package frc.robot.subsystems;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.*;

public class Tilter extends SubsystemBase {
    private final CANSparkMax tilterMotor; 
    private final CANEncoder tilterEncoder;
    private final PIDController tilterPID;
    
    private static final double ticksPerRev = 2048; 
    private static final double gearRatio = 300;
    private static final double degreesPerTick = KDegreesPerRevolution / (ticksPerRev * gearRatio);

    public Tilter() {
        tilterMotor = new CANSparkMax(KTilterSparkMax, CANSparkMaxLowLevel.MotorType.kBrushless); 
        tilterEncoder = new CANEncoder(tilterMotor);
        tilterPID = new PIDController(0.0001, 0, 0);

        tilterPID.enableContinuousInput(0, 40);
        tilterPID.disableContinuousInput();
        tilterPID.setTolerance(1,.001); //possible position and velocity tolerance values

        tilterPID.reset();
    }

    /**
     * Moves the tilter by a given speed
     * 
     * @param speed The speed to move the tilter at
     */
    public void move(double speed) {
        tilterMotor.set(speed);
    }

    /**
     * Sets the setpoint for the tiler PID
     * 
     * @param setpoint  The setpoint
     */
    public void setSetpoint(double setpoint) {
        tilterPID.setSetpoint(setpoint);
    }

    /**
     * Gets the setpoint of the tilter PID
     * 
     * @return  The setpoint
     */
    public double getSetpoint() {
        return tilterPID.getSetpoint();
    }

    public double getTilterAngle() {
        return tilterEncoder.getPosition() * degreesPerTick; // Function that gets the encoder value from the motor object
        
    }

    public void calculate() {
        move(tilterPID.calculate(getTilterAngle()));
    }
 
    public boolean atSetpoint() {
        return tilterPID.atSetpoint();
    }
}
