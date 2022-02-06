package frc.robot.displays;


import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;


public class MotorTestingDisplay {
    // ----------------------------------------------------------
	// Resources

	// TODO: P3 If using event listeners, make motor testing resources private

    public NetworkTableEntry motorTestingModeToggleSwitch;

	public NetworkTableEntry indexerToggleSwitch;
	public NetworkTableEntry indexerOutputPercentTextView;

	public NetworkTableEntry launcherToggleSwitch;
	public NetworkTableEntry launcherOutputPercentTextView;

	public NetworkTableEntry feederToggleSwitch;
	public NetworkTableEntry feederOutputPercentTextView;

	public NetworkTableEntry retractorToggleSwitch;
	public NetworkTableEntry retractorPositionTextView;

    // ----------------------------------------------------------
	// Constructor (initializes the display the same time)

    public MotorTestingDisplay(ShuffleboardTab diagnosticsTab, int column, int row) {
        var motorTestingLayout = diagnosticsTab
			.getLayout("Motor Testing", BuiltInLayouts.kGrid)
			// vertical stack so we can do (motor testing toggle-switch) and ([intake], [manipulator])
			.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "HIDDEN"))
			.withPosition(column, row)
			.withSize(7, 3);

			// Enable/disable motor testing
			motorTestingModeToggleSwitch = motorTestingLayout
				.add("CLICK ME! Red = enabled", false)
				.withWidget(BuiltInWidgets.kToggleButton)
				.getEntry();

			// put into the 2nd slot of motorTestingLayout's vertical stack
			var horizontalStack = motorTestingLayout
				.getLayout("Horizontal Stack", BuiltInLayouts.kGrid)
				.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

				// ----------------------------------------------------------
				// Testing the intake motors

				var intakeLayout = horizontalStack
					.getLayout("Intake", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

					// Intake retractor motor

					var retractorLayout = intakeLayout
						.getLayout("Retractor", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						// I have no fucking clue why the textView entry is always added before (to the 2nd row) the toggleSwitch but it's good enough
						retractorToggleSwitch = retractorLayout
							.add(" ", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						retractorPositionTextView = retractorLayout
							.add("Position", Intake.DEFAULT_RETRACTOR_POSITION)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
							.getEntry();

					// intake feeder motor
					
					var feederLayout = intakeLayout
						.getLayout("Feeder", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						feederToggleSwitch = feederLayout
							.add(" ", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						feederOutputPercentTextView = feederLayout
							.add("Percentage", Intake.DEFAULT_FEEDER_OUTPUT_PERCENT)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
							.getEntry();

				// ----------------------------------------------------------
				// Testing the conveyor-shooter motors

				var manipulatorLayout = horizontalStack
					.getLayout("Manipulator", BuiltInLayouts.kGrid)
					.withProperties(Map.of("Number of columns", 2, "Number of rows", 1, "Label position", "TOP"));

					var indexerLayout = manipulatorLayout
						.getLayout("Indexer", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						indexerToggleSwitch = indexerLayout
							.add(" ", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						indexerOutputPercentTextView = indexerLayout
							.add("Percentage", Manipulator.DEFAULT_INDEXER_MOTOR_OUTPUT_PERCENT)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
							.getEntry();

					// Manipulator launcher motor
					
					var launcherLayout = manipulatorLayout
						.getLayout("Launcher", BuiltInLayouts.kGrid)
						.withProperties(Map.of("Number of columns", 1, "Number of rows", 2, "Label position", "TOP"));

						launcherToggleSwitch = launcherLayout
							.add(" ", false)
							.withWidget(BuiltInWidgets.kToggleSwitch)
							.getEntry();
						launcherOutputPercentTextView = launcherLayout
							.add("Percentage", Manipulator.DEFAULT_LAUNCHER_MOTOR_OUTPUT_PERCENT)
							.withWidget(BuiltInWidgets.kNumberSlider)
							.withProperties(Map.of("Min", 0.d, "Max", 1.0d, "Block increment", 0.05d))
							.getEntry();
    }
}
