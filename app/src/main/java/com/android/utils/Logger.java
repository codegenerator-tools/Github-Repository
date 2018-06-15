/**
 *Revision History:
 *
 * VERSION   	DATE             	AUTHOR/MODIFIER      		  	COMMENT
 *---------------------------------------------------------------------------------------
 * 1.0          14-June-2018        Harsh Prashar  				 	Initial Create
 * 
 */


package com.android.utils;

import android.util.Log;

/**
 * Wrapper class for android LOG which takes care of logging
 * It contains logging modules as bit-fields
 * and can be set as per the user request
 */

public class Logger {

	// Module names are stored in an array ModuleNames[], and start with 0 based index
	// bit fields used to set debugging modules
	public static final byte MOD_NETWORK = 0;
	public static final byte MOD_DATA_HELPER = 1;
	public static final byte MOD_DATABASE = 2;
	public static final byte MOD_VIEW = 3;
	private static final byte MOD_EXCEPTION = 4;

	public static String mModuleNames[] = {

				"Network",
				"Data Helper",
				"Database",
				"View",
				"Exception"
	};

	private static int sLoggingModules = 0; // none is set initiallly, bitfield
											// 32 - bits

	public void setDebugModules(int debugModules) {

		sLoggingModules = debugModules;
	}

	public int getDebugModules() {

		return sLoggingModules;
	}

	public static void i(byte module, String logString) {
		
		Log.i(mModuleNames[module], logString);
	}
 
	public static void d(byte module, String logString) {

		Log.d(mModuleNames[module], logString);
	}

	public static void v(byte module, String logString) {

		Log.v(mModuleNames[module], logString);
	}

	public static void w(byte module, String logString) {

		Log.w(mModuleNames[module], logString);
	}

	public static void e(byte module, String logString) {

		Log.e(mModuleNames[module], logString);
	}

	public static void e(byte module, String logString, Exception e) {

		Log.e(mModuleNames[module], logString, e);
	}

	public static void dumpException(byte module, Exception exception) {

		Log.e(mModuleNames[module], exception.getLocalizedMessage());
	}

    public static void dumpException(byte module, String exception) {

        Log.e(mModuleNames[module], exception);
    }

	public static void dumpException(Exception exception) {

		Log.e(mModuleNames[MOD_EXCEPTION], exception + "");
	}
	
	public static void dumpException(String exception) {

		Log.e(mModuleNames[MOD_EXCEPTION], exception);
	}
}
