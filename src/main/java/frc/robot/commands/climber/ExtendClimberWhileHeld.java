package frc.robot.commands.climber;


import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Climber;


public class ExtendClimberWhileHeld extends CommandBase {
	private final Climber m_climber;

	public ExtendClimberWhileHeld(Climber climber) {
		m_climber = climber;
	}

	@Override
	public void initialize() {
		m_climber.setWinchToExtendPercent();
	}

	@Override
	public void end(boolean interrupted) {
		m_climber.stopWinchMotor();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
