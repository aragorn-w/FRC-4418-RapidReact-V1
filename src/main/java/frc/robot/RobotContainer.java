package frc.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.Constants.AxisDominanceThresholds;
import frc.robot.Constants.Gamepad;
import frc.robot.Constants.X3D;
import frc.robot.commands.AutoDriveStraightForDistance;
import frc.robot.commands.DriveStraightWhileHeld;
import frc.robot.commands.IntakeDemo;
import frc.robot.commands.ManipulatorDemo;
import frc.robot.commands.ToggleIntake;
import frc.robot.commands.RunLauncherWhileHeld;
import frc.robot.commands.AutoDriveStraightForDistance.DriveStraightDirection;
import frc.robot.subsystems.Autonomous;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Manipulator;
import frc.robot.subsystems.Telemetry;


public class RobotContainer {
    // ----------------------------------------------------------
    // Resources

    // TODO: PERSISTENT CONFIG - enable robot's tuning tools or don't
	public boolean enableTuningTools = true;

    private boolean driverIsInArcadeMode = true;
	private boolean spotterIsInArcadeMode = false;

    private final Joystick
		X3D_LEFT = new Joystick(Constants.X3D.LEFT_JOYSTICK_ID),
		X3D_RIGHT = new Joystick(Constants.X3D.RIGHT_JOYSTICK_ID),
		GAMEPAD = new Joystick(Constants.Gamepad.JOYSTICK_ID);

	public DriverControls driverControls;
	public SpotterControls spotterControls;
    
    // ----------------------------------------------------------
    // Subsystems
	
	public final Drivetrain drivetrain = new Drivetrain();
	
	public final Intake intake = new Intake();
	public final Manipulator manipulator = new Manipulator();
	
	// public static Sensory sensory;
	public final Autonomous autonomous = new Autonomous();
	
	public final Telemetry telemetry = new Telemetry();

    // ----------------------------------------------------------
    // Runtime resources for Robot

    public Command getDefaultAutonomousCommand() {
        return new AutoDriveStraightForDistance(drivetrain, 60.0d, DriveStraightDirection.BACKWARDS);
    }

    public Command getIntakeDemo() {
        return new IntakeDemo(intake, telemetry);
    }

    public Command getManipulatorDemo() {
        return new ManipulatorDemo(manipulator, telemetry);
    }

    public Command getDriveStraightWhileHeldCommand() {
        return new DriveStraightWhileHeld(drivetrain);
    }

	public Command getRunLauncher() {
        return new RunLauncherWhileHeld(manipulator);
    }

    // ----------------------------------------------------------
    // Constructor and actions

    public RobotContainer() {
        driverControls = new DriverControls();
        driverControls.configButtonBindings();

		spotterControls = new SpotterControls();
        spotterControls.configureButtonBindings();
        
        telemetry.initializeTelemetry();
		if (enableTuningTools) {
			telemetry.initializeTuningTools();
		}
    }

    public boolean driverIsInArcade() { return driverIsInArcadeMode; }
	
	public void toggleDriverDriveMode() {
		driverIsInArcadeMode = !driverIsInArcadeMode;
	}

    public boolean spotterIsInArcade() { return spotterIsInArcadeMode; }
	
	public void toggleSpotterDriveMode() {
		spotterIsInArcadeMode = !spotterIsInArcadeMode;
	}

    public double gamepadJoystickMagnitude(boolean isLeftJoystick) {
		if (isLeftJoystick) {
			return Math.sqrt(
				Math.pow(GAMEPAD.getRawAxis(Constants.Gamepad.LEFT_X_AXIS), 2)
				+ Math.pow(GAMEPAD.getRawAxis(Constants.Gamepad.LEFT_Y_AXIS), 2));
		} else {
			return Math.sqrt(
				Math.pow(GAMEPAD.getRawAxis(Constants.Gamepad.RIGHT_X_AXIS), 2)
				+ Math.pow(GAMEPAD.getRawAxis(Constants.Gamepad.RIGHT_Y_AXIS), 2));
		}
	}

    public void teleopDrive() {
		if (spotterIsInArcade()
		&& (gamepadJoystickMagnitude(true) > AxisDominanceThresholds.ARCADE)) {
			drivetrain.arcadeDrive(
				spotterControls.getForwardArcadeDriveAxis(),
				spotterControls.getAngleArcadeDriveAxis());
		} else if (!spotterIsInArcade()
		&& (gamepadJoystickMagnitude(true) > AxisDominanceThresholds.TANK
		|| gamepadJoystickMagnitude(false) > AxisDominanceThresholds.TANK)) {
			drivetrain.tankDrive(
				spotterControls.getLeftTankDriveAxis(),
				spotterControls.getRightTankDriveAxis());
		}

		if (driverIsInArcade()) {
			drivetrain.arcadeDrive(
				driverControls.getForwardArcadeDriveAxis(),	// forward
				driverControls.getAngleArcadeDriveAxis());	// angle
		} else {
			drivetrain.tankDrive(
				driverControls.getLeftTankDriveAxis(),		// left
				driverControls.getRightTankDriveAxis());	// right
		}
	}

    // ----------------------------------------------------------
    // Driver controls inner class

    public class DriverControls {
		// ----------------------------------------------------------
		// Constants

		public static final int
			// Tank drive axis
			LEFT_TANK_DRIVE_AXIS_ID = X3D.PITCH_AXIS,
			RIGHT_TANK_DRIVE_AXIS_ID = X3D.PITCH_AXIS,

			// Arcade drive axis
			ARCADE_DRIVE_FORWARD_AXIS_ID = X3D.PITCH_AXIS,
			ARCADE_DRIVE_ANGLE_AXIS_ID = X3D.YAW_AXIS,

			TOGGLE_ARCADE_DRIVE_BUTTON_ID = X3D.BUTTON_5_ID,	// does not toggle drive mode for spotter
			DRIVE_STRAIGHT_BUTTON_ID = X3D.GRIP_BUTTON_ID,
			TOGGLE_INTAKE_BUTTON_ID = X3D.BUTTON_3_ID,
			RUN_LAUNCHER_BUTTON_ID = X3D.TRIGGER_BUTTON_ID;

        // ----------------------------------------------------------
		// Resources

        public JoystickButton
            driveStraightButton = new JoystickButton(X3D_LEFT, DRIVE_STRAIGHT_BUTTON_ID),
			toggleIntakeButton = new JoystickButton(X3D_LEFT, TOGGLE_INTAKE_BUTTON_ID),
			runLaunchButton = new JoystickButton(X3D_LEFT, RUN_LAUNCHER_BUTTON_ID);
    
        // ----------------------------------------------------------
		// Actions

        public void configButtonBindings() {
            driveStraightButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
			toggleIntakeButton.toggleWhenPressed(new ToggleIntake(intake));
        }

        // Tank drive axes
		public double getLeftTankDriveAxis() {
			return X3D_LEFT.getRawAxis(LEFT_TANK_DRIVE_AXIS_ID);
		}
		public double getRightTankDriveAxis() {
			return X3D_RIGHT.getRawAxis(RIGHT_TANK_DRIVE_AXIS_ID);
		}

		// Arcade drive axes
		public double getForwardArcadeDriveAxis() {
			return X3D_LEFT.getRawAxis(ARCADE_DRIVE_FORWARD_AXIS_ID);
		}

		public double getAngleArcadeDriveAxis() {
			return X3D_LEFT.getRawAxis(ARCADE_DRIVE_ANGLE_AXIS_ID);
		}
    }

    // ----------------------------------------------------------
    // Spotter controls inner class

    public class SpotterControls {
		// ----------------------------------------------------------
		// Constants

		public static final int
			// Tank drive axis
			LEFT_TANK_DRIVE_AXIS_ID = Gamepad.LEFT_Y_AXIS,
			RIGHT_TANK_DRIVE_AXIS_ID = Gamepad.RIGHT_Y_AXIS,

			// Arcade drive axis
			ARCADE_DRIVE_FORWARD_AXIS_ID = Gamepad.LEFT_Y_AXIS,
			ARCADE_DRIVE_ANGLE_AXIS_ID = Gamepad.LEFT_X_AXIS,
			
			// Drive mode function buttons
			DRIVE_STRAIGHT_POV_ANGLE = Gamepad.ANGLE_UP_POV,
			TOGGLE_ARCADE_DRIVE_BUTTON_ID = Gamepad.LEFT_JOYSTICK_BUTTON_ID;	// does not toggle drive mode for driver

			// Manipulator buttons
			// TOGGLE_INTAKE_BUTTON_ID = Gamepad.BUTTON_3_ID,
			// RUN_LAUNCHER_BUTTON_ID = Gamepad.TRIGGER_BUTTON_ID;
		
		// ----------------------------------------------------------
		// Resources
		
		public POVButton
			driveStraightButton = new POVButton(GAMEPAD, DRIVE_STRAIGHT_POV_ANGLE);
		
		// public JoystickButton
		// 	intakeButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.INTAKE_BUTTON_ID),
		// 	feederButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.FEEDER_BUTTON_ID),
		// 	extendClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.EXTEND_CLIMBER_BUTTON_ID),
		// 	lowerClimberButton = new JoystickButton(GAMEPAD, Constants.SpotterControlIDs.LOWER_CLIMBER_BUTTON_ID);

		// ----------------------------------------------------------
		// Actions

		public void configureButtonBindings() {
			spotterControls.driveStraightButton.whenHeld(new DriveStraightWhileHeld(drivetrain));
		}

		// Tank drive axes
		public double getLeftTankDriveAxis() {
			return GAMEPAD.getRawAxis(LEFT_TANK_DRIVE_AXIS_ID);
		}
		public double getRightTankDriveAxis() {
			return GAMEPAD.getRawAxis(RIGHT_TANK_DRIVE_AXIS_ID);
		}

		// Arcade drive axes
		public double getForwardArcadeDriveAxis() {
			return GAMEPAD.getRawAxis(ARCADE_DRIVE_FORWARD_AXIS_ID);
		}
		public double getAngleArcadeDriveAxis() {
			return GAMEPAD.getRawAxis(ARCADE_DRIVE_ANGLE_AXIS_ID);
		}
	}
}
