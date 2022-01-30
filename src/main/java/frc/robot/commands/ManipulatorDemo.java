package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Manipulator;
import frc.robot.huds.MotorTesting;
import frc.robot.subsystems.HUD;


public class ManipulatorDemo extends CommandBase {
	// ----------------------------------------------------------
	// Resources

	private final Manipulator ms;
	private final MotorTesting mt;

	// ----------------------------------------------------------
	// Constructor

	public ManipulatorDemo(Manipulator manipulator, HUD hud) {
		ms = manipulator;
		mt = hud.motorTesting;

		addRequirements(ms);
	}

	// ----------------------------------------------------------
	// Scheduler methods

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
		if (mt.motorTestingModeToggleSwitch.getBoolean(false)) {
			if (mt.manipulatorIndexerToggleSwitch.getBoolean(false)) {
				ms.setLowMotorPercent(mt.manipulatorIndexerOutputPercentTextView.getDouble(Manipulator.LOWER_MOTOR_DEFAULT_PERCENT_OUTPUT));
			} else {
				ms.setLowMotorPercent(0.d);
			}

			if (mt.manipulatorLauncherToggleSwitch.getBoolean(false)) {
				ms.setHighMotorPercent(mt.manipulatorLauncherOutputPercentTextView.getDouble(Manipulator.HIGHER_MOTOR_DEFAULT_PERCENT_OUTPUT));
			} else {
				ms.setHighMotorPercent(0.d);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		ms.setLowMotorPercent(0.d);
		ms.setHighMotorPercent(0.d);
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
