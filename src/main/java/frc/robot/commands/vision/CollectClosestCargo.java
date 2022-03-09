package frc.robot.commands.vision;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.intake.ExtendIntakeArm;
import frc.robot.commands.intake.RetractIntakeArm;
import frc.robot.commands.intake.RunFeederAndIndexer;
import frc.robot.commands.intake.StopFeederAndIndexer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Vision;


public class CollectClosestCargo extends SequentialCommandGroup {
	public CollectClosestCargo(Drivetrain drivetrain, Intake intake, Manipulator manipulator, Vision vision, boolean leaveArmExtended) {
		super(
			new TurnToClosestCargo(drivetrain, vision),
			new ParallelCommandGroup(
				new ExtendIntakeArm(intake, false),
				new RunFeederAndIndexer(intake, manipulator),
				new DriveStraightToCargo(drivetrain, vision)
			),
			leaveArmExtended ?
				new StopFeederAndIndexer(intake, manipulator):
				new SequentialCommandGroup(
					new StopFeederAndIndexer(intake, manipulator),
					new RetractIntakeArm(intake, false)
				)
		);
	}
}