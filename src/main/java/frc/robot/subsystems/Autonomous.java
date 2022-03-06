package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;


public class Autonomous extends SubsystemBase {
	// ----------------------------------------------------------
	// Publicly static resources


	private boolean usePremadeRoutine = Constants.Autonomous.kDefaultUsePremadeRoutine;

	private static double
		startDelaySeconds = Constants.Autonomous.kDefaultStartDelaySeconds,
		tarmacLeavingMeters = Constants.Autonomous.kDefaultTarmacLeavingMeters;


	// ----------------------------------------------------------
	// Public constants


	public enum AutonomousRoutine {
		// 'PICKUP_CARGO' just directly runs intake to collect the cargo that's assumed to be right behind us
		// 'RETRIEVE_CARGO' uses vision and the IMU to autonomously find the closest cargo and collect it 
		// LT = leave tarmac
		// LH = score low hub
		// PC = pickup cargo
		// RC = retrieve cargo
		WAIT_AND_LEAVE_TARMAC									(1),	// Wait LT
		WAIT_SCORE_LH_AND_LEAVE_TARMAC							(2),	// Wait LH LT
		SCORE_LH_AND_WAIT_AND_LEAVE_TARMAC						(3),	// LH Wait LT
		WAIT_AND_SCORE_LH_AND_PICKUP_CARGO_AND_SCORE_LH		(4),	// Wait LH PC LH
		WAIT_AND_SCORE_LH_AND_RETRIEVE_CARGO_AND_LEAVE_TARMAC	(5);	// Wait LH RC LT

		private final int value;

		private AutonomousRoutine(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}


	// ----------------------------------------------------------
	// Routine-parameter getters


	public boolean usingPremadeRoutine() {
		return usePremadeRoutine;
	}

	public static double getStartDelaySeconds() {
		return startDelaySeconds;
	}

	public static double getTarmacLeavingMeters() {
		return tarmacLeavingMeters;
	}


	// ----------------------------------------------------------
	// Routine-parameter setters


	public Autonomous setUsePremadeRoutine(boolean bool) {
		usePremadeRoutine = bool;
		return this;
	}

	public static void setStartDelaySeconds(double delaySeconds) {
		Constants.Autonomous.kDefaultStartDelaySeconds = delaySeconds;
		startDelaySeconds = delaySeconds;
		RobotContainer.instance.remakeAutoCommand();
	}

	public static void setTarmacLeavingMeters(double distance) {
		Constants.Autonomous.kDefaultTarmacLeavingMeters = distance;
		tarmacLeavingMeters = distance;
		RobotContainer.instance.remakeAutoCommand();
	}
}
