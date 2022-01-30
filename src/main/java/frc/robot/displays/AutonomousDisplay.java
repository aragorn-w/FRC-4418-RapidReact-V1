package frc.robot.displays;


import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.robot.subsystems.Autonomous.AutonomousRoutine;


public class AutonomousDisplay {
    // ----------------------------------------------------------
    // Resources

    private SendableChooser<AutonomousRoutine> sendableAutoRoutineChooser = new SendableChooser<>();

    // ----------------------------------------------------------
    // Constructor (initializes the display the same time)
    
    public AutonomousDisplay(ShuffleboardTab HUDTab, int column, int row) {
        var autonomousLayout = HUDTab
			.getLayout("Autonomous", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 1, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(2, 1);
			
			// setting default options for sendable choosers also adds the label-value pair as an option
			sendableAutoRoutineChooser.setDefaultOption("Drive Straight Backwards", AutonomousRoutine.DRIVE_STRAIGHT_BACKWARDS);
			sendableAutoRoutineChooser.addOption("Drive Straight to Low Hub", AutonomousRoutine.DRIVE_STRAIGHT_TO_LOW_HUB);
			autonomousLayout
				.add("Autonomous Routine", sendableAutoRoutineChooser)
				.withWidget(BuiltInWidgets.kComboBoxChooser);
    }
}
