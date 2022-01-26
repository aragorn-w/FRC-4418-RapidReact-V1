package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Telemetry;


public class IntakeDemo extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Intake it;
	private final Telemetry tt;

	// ----------------------------------------------------------
	// Constructor

	public IntakeDemo(Intake intake, Telemetry telemetry) {
		it = intake;
		tt = telemetry;
		addRequirements(it, tt);
	}

	// ----------------------------------------------------------
	// Scheduler actions

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		if (tt.rollerIntakeMotorToggleSwitch.getBoolean(false)) {
			it.setRollerMotorPercent(tt.rollerIntakeMotorPercentTextView.getDouble(Intake.ROLLER_MOTOR_DEFAULT_PERCENT_OUTPUT));
		} else {
			it.setRollerMotorPercent(0.d);
		}

		// if (tt.retractIntakeMotorToggleSwitch.getBoolean(false)) {
		// 	it.setRetractMotorPosition(tt.retractIntakeMotorPositionTextView.getDouble(it.RETRACT_MOTOR_DEFAULT_POSITION));
		// } else {
		// 	it.setRetractMotorPosition(0.d);
		// }
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		it.setRollerMotorPercent(0.d);
		// it.setRetractMotorPosition(0.d);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}