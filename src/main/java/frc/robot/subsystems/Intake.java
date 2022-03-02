package frc.robot.subsystems;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Intake extends SubsystemBase {
	// ----------------------------------------------------------
	// Resources


	private final DigitalInput m_whiskerSensor = new DigitalInput(Constants.Intake.kWhiskerSensorDIOPort);

	private final WPI_TalonSRX m_feederMotor = new WPI_TalonSRX(Constants.Intake.CAN_ID.kFeeder);
	private final WPI_TalonFX m_retractorMotor = new WPI_TalonFX(Constants.Intake.CAN_ID.kRetractor);

	// the origin (0 ticks, or 0 degrees) is supposed to be the retractor's retracted angle when using this offset
	private int retractorTicksOriginOffset = 1_000_000;


	// ----------------------------------------------------------
	// Constructor
	

	public Intake() {
		// ----------------------------------------------------------
		// Feeder motor configuration

		m_feederMotor.configFactoryDefault();
		m_feederMotor.configOpenloopRamp(Constants.Intake.kFeederRampTime);
		m_feederMotor.setInverted(true);
		
		// ----------------------------------------------------------
		// Retractor motor configuration

		m_retractorMotor.configFactoryDefault();
		m_retractorMotor.configOpenloopRamp(Constants.Intake.kRetractorOpenLoopRampSeconds);
		m_retractorMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, Constants.Intake.kRetractorPidIdx, Constants.Intake.kTimeoutMs);
		// m_retractorMotor.config_kF(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGainsV2.kF);
		m_retractorMotor.config_kP(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGainsV2.kP);
		// m_retractorMotor.config_kI(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGainsV2.kI);
        // m_retractorMotor.config_kD(Constants.Intake.kRetractorSlotIdx, Constants.Intake.kRetractorPositionGainsV2.kD);
	}


	// ----------------------------------------------------------
	// Scheduler methods


	@Override
	public void periodic() {
		// TODO: Remove these prints once useless
		SmartDashboard.putNumber("Raw ticks", getRawRetractorTicks());
		SmartDashboard.putNumber("Offset ticks", getRetractorTicks());
		SmartDashboard.putNumber("Origin offset", retractorTicksOriginOffset);

		updateRetractorOrigin();
	}
	

	// ----------------------------------------------------------
	// Ball-intake whisker sensor


	public boolean whiskerSensorIsActive() {
		return m_whiskerSensor.get();
	}


	// ----------------------------------------------------------
	// Retractor motor

	
	public Intake updateRetractorOrigin() {
		int newMostRetractedPosition = getRawRetractorTicks();
		if (newMostRetractedPosition < retractorTicksOriginOffset) {
			retractorTicksOriginOffset = newMostRetractedPosition;
		}
		return this;
	}

	public Intake brakeRetractor() {
		m_retractorMotor.setNeutralMode(NeutralMode.Brake);
		return this;
	}

	public Intake coastRetractor() {
		m_retractorMotor.setNeutralMode(NeutralMode.Coast);
		return this;
	}

	// 'raw' because there's no origin offset
	public int getRawRetractorTicks() {
		return (int) m_retractorMotor.getSelectedSensorPosition();
	}

	public int getRetractorTicks() {
		return getRawRetractorTicks() - retractorTicksOriginOffset;
	}

	public double getRetractorDegree() {
		return (double) getRetractorTicks() / Constants.Intake.kRetractorDegreesToTicks;
	}

	// offset + safety buffer --> positionDegrees = 0 means going to the retracted position
	public Intake setRetractorDegree(double positionDegrees) {
		setRetractorTicks((int) (positionDegrees * Constants.Intake.kRetractorDegreesToTicks));
		return this;
	}

	// offset + safety buffer --> positionTicks = 0 means going to the retracted position
	public Intake setRetractorTicks(int positionTicks) {
		m_retractorMotor.set(ControlMode.Position, positionTicks + retractorTicksOriginOffset + Constants.Intake.kRetractorOriginOffsetBufferMargin);
		return this;
	}

	public Intake retractIntakeArm() {
		brakeRetractor();
		setRetractorTicks(Constants.Intake.kRetractedIntakeRetractorTicks);
		return this;
	}

	// true means it is satisfiably close to the retracted-arm degree, false means it is not
	// false DOES NOT NECESSARILY MEAN that the intake arm is extended
	public boolean intakeArmIsRetracted() {
		return Math.abs(getRetractorTicks() - Constants.Intake.kRetractedIntakeRetractorTicks) <= Constants.Intake.kRetractorDegreeTolerance;
	}

	public Intake extendIntakeArm() {
		brakeRetractor();
		setRetractorTicks(Constants.Intake.kExtendedIntakeRetractorTicks);
		return this;
	}

	// true means it is satisfiably close to the extended-arm degree, false means it is not
	// false DOES NOT NECESSARILY MEAN that the intake arm is retracted
	public boolean intakeArmIsExtended() {
		return Math.abs(getRetractorTicks() - Constants.Intake.kExtendedIntakeRetractorTicks) <= Constants.Intake.kRetractorDegreeTolerance;
	}


	// ----------------------------------------------------------
	// Feeder motor


	// -1 to 1
	public double getFeederPercent() {
		return m_feederMotor.get();
	}

	// -1 to 1
	public Intake setFeederPercent(double percentOutput) {
		m_feederMotor.set(ControlMode.PercentOutput, percentOutput);
		return this;
	}

	public Intake runReverseFeeder() {
		setFeederPercent(Constants.Intake.kDefaultReverseFeederPercent);
		return this;
	}

	public Intake runFeeder() {
		setFeederPercent(Constants.Intake.kDefaultFeederPercent);
		return this;
	}

	public Intake stopFeeder() {
		setFeederPercent(0.);
		return this;
	}
}