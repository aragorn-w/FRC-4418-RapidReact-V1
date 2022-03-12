package frc.robot.commands.autonomous;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.drivetrain.DriveStraightForDistance;
import frc.robot.commands.drivetrain.DriveStraightWhileHeld.DriveStraightDirection;
import frc.robot.commands.manipulator.LaunchBalls;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Manipulator;


public class Wait_LH_LT extends SequentialCommandGroup {
	public Wait_LH_LT(Drivetrain drivetrain, Manipulator manipulator) {
		super(
			new WaitFor(),
			new LaunchBalls(manipulator),
			new DriveStraightForDistance(drivetrain, DriveStraightDirection.FORWARDS)
		);
	}
}