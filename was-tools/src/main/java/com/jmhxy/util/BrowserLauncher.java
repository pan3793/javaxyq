package com.jmhxy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BrowserLauncher {
	private static int jvm;
	private static Object browser;
	private static boolean loadedWithoutErrors = true;
	private static Class mrjFileUtilsClass;
	private static Class mrjOSTypeClass;
	private static Class aeDescClass;
	private static Constructor aeTargetConstructor;
	private static Constructor appleEventConstructor;
	private static Constructor aeDescConstructor;
	private static Method findFolder;
	private static Method getFileCreator;
	private static Method getFileType;
	private static Method openURL;
	private static Method makeOSType;
	private static Method putParameter;
	private static Method sendNoReply;
	private static Object kSystemFolderType;
	private static Integer keyDirectObject;
	private static Integer kAutoGenerateReturnID;
	private static Integer kAnyTransactionID;
	private static Object linkage;
	private static final String JDirect_MacOSX = "/System/Library/Frameworks/Carbon.framework/Frameworks/HIToolbox.framework/HIToolbox";
	private static final int MRJ_2_0 = 0;
	private static final int MRJ_2_1 = 1;
	private static final int MRJ_3_0 = 3;
	private static final int MRJ_3_1 = 4;
	private static final int WINDOWS_NT = 5;
	private static final int WINDOWS_9x = 6;
	private static final int OTHER = -1;
	private static final String FINDER_TYPE = "FNDR";
	private static final String FINDER_CREATOR = "MACS";
	private static final String GURL_EVENT = "GURL";
	private static final String FIRST_WINDOWS_PARAMETER = "/c";
	private static final String SECOND_WINDOWS_PARAMETER = "start";
	private static final String THIRD_WINDOWS_PARAMETER = "\"\"";
	private static final String NETSCAPE_REMOTE_PARAMETER = "-remote";
	private static final String NETSCAPE_OPEN_PARAMETER_START = "'openURL(";
	private static final String NETSCAPE_OPEN_PARAMETER_END = ")'";
	private static String errorMessage;

	static {
		String osName = System.getProperty("os.name");

		if (osName.startsWith("Mac OS")) {
			String mrjVersion = System.getProperty("mrj.version");
			String majorMRJVersion = mrjVersion.substring(0, 3);
			try {
				double version = Double.valueOf(majorMRJVersion).doubleValue();

				if (version == 2.0D) {
					jvm = 0;
				} else if ((version >= 2.1D) && (version < 3.0D)) {
					jvm = 1;
				} else if (version == 3.0D) {
					jvm = 3;
				} else if (version >= 3.1D) {
					jvm = 4;
				} else {
					loadedWithoutErrors = false;
					errorMessage = "Unsupported MRJ version: " + version;
				}
			} catch (NumberFormatException nfe) {
				loadedWithoutErrors = false;
				errorMessage = "Invalid MRJ version: " + mrjVersion;
			}
		} else if (osName.startsWith("Windows")) {
			if ((osName.indexOf("9") != -1) || (osName.indexOf("Me") != -1))
				jvm = 6;
			else
				jvm = 5;
		} else {
			jvm = -1;
		}

		if (loadedWithoutErrors)
			loadedWithoutErrors = loadClasses();
	}

	private static boolean loadClasses() {
		switch (jvm) {
		case 0:
			return loadMRJ20Classes();
		case 1:
			try {
				mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
				mrjOSTypeClass = Class.forName("com.apple.mrj.MRJOSType");

				Field systemFolderField = mrjFileUtilsClass.getDeclaredField("kSystemFolderType");
				kSystemFolderType = systemFolderField.get(null);
				findFolder = mrjFileUtilsClass.getDeclaredMethod("findFolder", new Class[] { mrjOSTypeClass });
				getFileCreator = mrjFileUtilsClass.getDeclaredMethod("getFileCreator", new Class[] { File.class });
				getFileType = mrjFileUtilsClass.getDeclaredMethod("getFileType", new Class[] { File.class });
			} catch (ClassNotFoundException cnfe) {
				errorMessage = cnfe.getMessage();

				return false;
			} catch (NoSuchFieldException nsfe) {
				errorMessage = nsfe.getMessage();

				return false;
			} catch (NoSuchMethodException nsme) {
				errorMessage = nsme.getMessage();

				return false;
			} catch (SecurityException se) {
				errorMessage = se.getMessage();

				return false;
			} catch (IllegalAccessException iae) {
				errorMessage = iae.getMessage();

				return false;
			}

			break;
		case 3:
			try {
				Class linker = Class.forName("com.apple.mrj.jdirect.Linker");
				Constructor constructor = linker.getConstructor(new Class[] { Class.class });

				linkage = constructor.newInstance(new Object[] { BrowserLauncher.class });
			} catch (ClassNotFoundException cnfe) {
				errorMessage = cnfe.getMessage();

				return false;
			} catch (NoSuchMethodException nsme) {
				errorMessage = nsme.getMessage();

				return false;
			} catch (InvocationTargetException ite) {
				errorMessage = ite.getMessage();

				return false;
			} catch (InstantiationException ie) {
				errorMessage = ie.getMessage();

				return false;
			} catch (IllegalAccessException iae) {
				errorMessage = iae.getMessage();

				return false;
			}

			break;
		case 4:
			try {
				mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
				openURL = mrjFileUtilsClass.getDeclaredMethod("openURL", new Class[] { String.class });
			} catch (ClassNotFoundException cnfe) {
				errorMessage = cnfe.getMessage();

				return false;
			} catch (NoSuchMethodException nsme) {
				errorMessage = nsme.getMessage();

				return false;
			}

			break;
		case 2:
		}

		return true;
	}

	private static boolean loadMRJ20Classes() {
		try {
			Class aeTargetClass = Class.forName("com.apple.MacOS.AETarget");
			Class osUtilsClass = Class.forName("com.apple.MacOS.OSUtils");
			Class appleEventClass = Class.forName("com.apple.MacOS.AppleEvent");
			Class aeClass = Class.forName("com.apple.MacOS.ae");
			aeDescClass = Class.forName("com.apple.MacOS.AEDesc");

			aeTargetConstructor = aeTargetClass.getDeclaredConstructor(new Class[] { Integer.TYPE });

			appleEventConstructor = appleEventClass.getDeclaredConstructor(
					new Class[] { Integer.TYPE, Integer.TYPE, aeTargetClass, Integer.TYPE, Integer.TYPE });

			aeDescConstructor = aeDescClass.getDeclaredConstructor(new Class[] { String.class });

			makeOSType = osUtilsClass.getDeclaredMethod("makeOSType", new Class[] { String.class });
			putParameter = appleEventClass.getDeclaredMethod("putParameter", new Class[] { Integer.TYPE, aeDescClass });
			sendNoReply = appleEventClass.getDeclaredMethod("sendNoReply", new Class[0]);

			Field keyDirectObjectField = aeClass.getDeclaredField("keyDirectObject");
			keyDirectObject = (Integer) keyDirectObjectField.get(null);

			Field autoGenerateReturnIDField = appleEventClass.getDeclaredField("kAutoGenerateReturnID");
			kAutoGenerateReturnID = (Integer) autoGenerateReturnIDField.get(null);

			Field anyTransactionIDField = appleEventClass.getDeclaredField("kAnyTransactionID");
			kAnyTransactionID = (Integer) anyTransactionIDField.get(null);
		} catch (ClassNotFoundException cnfe) {
			errorMessage = cnfe.getMessage();

			return false;
		} catch (NoSuchMethodException nsme) {
			errorMessage = nsme.getMessage();

			return false;
		} catch (NoSuchFieldException nsfe) {
			errorMessage = nsfe.getMessage();

			return false;
		} catch (IllegalAccessException iae) {
			errorMessage = iae.getMessage();

			return false;
		}

		return true;
	}

	private static Object locateBrowser() {
		if (browser != null) {
			return browser;
		}

		switch (jvm) {
		case 0:
			try {
				Integer finderCreatorCode = (Integer) makeOSType.invoke(null, new Object[] { "MACS" });
				Object aeTarget = aeTargetConstructor.newInstance(new Object[] { finderCreatorCode });

				Integer gurlType = (Integer) makeOSType.invoke(null, new Object[] { "GURL" });
				return appleEventConstructor.newInstance(
						new Object[] { gurlType, gurlType, aeTarget, kAutoGenerateReturnID, kAnyTransactionID });
			} catch (IllegalAccessException iae) {
				browser = null;
				errorMessage = iae.getMessage();

				return browser;
			} catch (InstantiationException ie) {
				browser = null;
				errorMessage = ie.getMessage();

				return browser;
			} catch (InvocationTargetException ite) {
				browser = null;
				errorMessage = ite.getMessage();

				return browser;
			}

		case 1:
			File systemFolder;
			try {
				systemFolder = (File) findFolder.invoke(null, new Object[] { kSystemFolderType });
			} catch (IllegalArgumentException iare) {

				browser = null;
				errorMessage = iare.getMessage();

				return browser;
			} catch (IllegalAccessException iae) {
				browser = null;
				errorMessage = iae.getMessage();

				return browser;
			} catch (InvocationTargetException ite) {
				browser = null;
				errorMessage = ite.getTargetException().getClass() + ": " + ite.getTargetException().getMessage();

				return browser;
			}
			String[] systemFolderFiles = systemFolder.list();

			for (int i = 0; i < systemFolderFiles.length; i++) {
				try {
					File file = new File(systemFolder, systemFolderFiles[i]);

					if (file.isFile()) {
						Object fileType = getFileType.invoke(null, new Object[] { file });

						if ("FNDR".equals(fileType.toString())) {
							Object fileCreator = getFileCreator.invoke(null, new Object[] { file });

							if ("MACS".equals(fileCreator.toString())) {
								browser = file.toString();

								return browser;
							}
						}
					}
				} catch (IllegalArgumentException iare) {
					browser = browser;
					errorMessage = iare.getMessage();

					return null;
				} catch (IllegalAccessException iae) {
					browser = null;
					errorMessage = iae.getMessage();

					return browser;
				} catch (InvocationTargetException ite) {
					browser = null;
					errorMessage = ite.getTargetException().getClass() + ": " + ite.getTargetException().getMessage();

					return browser;
				}
			}

			browser = null;

			break;
		case 3:
		case 4:
			browser = "";

			break;
		case 5:
			browser = "cmd.exe";

			break;
		case 6:
			browser = "command.com";

			break;
		case -1:
		case 2:
		}

		browser = "netscape";

		return browser;
	}

	public static void openURL(String url) throws IOException {
		if (!loadedWithoutErrors) {
			throw new IOException("Exception in finding browser: " + errorMessage);
		}

		Object browser = locateBrowser();

		if (browser == null) {
			throw new IOException("Unable to locate browser: " + errorMessage);
		}
		Process process;
		switch (jvm) {
		case 0:
			Object aeDesc = null;
			try {
				aeDesc = aeDescConstructor.newInstance(new Object[] { url });
				putParameter.invoke(browser, new Object[] { keyDirectObject, aeDesc });
				sendNoReply.invoke(browser, new Object[0]);
			} catch (InvocationTargetException ite) {
				throw new IOException("InvocationTargetException while creating AEDesc: " + ite.getMessage());
			} catch (IllegalAccessException iae) {
				throw new IOException("IllegalAccessException while building AppleEvent: " + iae.getMessage());
			} catch (InstantiationException ie) {
				throw new IOException("InstantiationException while creating AEDesc: " + ie.getMessage());
			} finally {
				aeDesc = null;
				browser = null;
			}

			break;
		case 1:
			Runtime.getRuntime().exec(new String[] { (String) browser, url });

			break;
		case 3:
			int[] instance = new int[1];
			int result = ICStart(instance, 0);

			if (result == 0) {
				int[] selectionStart = new int[1];
				byte[] urlBytes = url.getBytes();
				int[] selectionEnd = { urlBytes.length };
				result = ICLaunchURL(instance[0], new byte[1], urlBytes, urlBytes.length, selectionStart, selectionEnd);

				if (result == 0) {
					ICStop(instance);
				} else
					throw new IOException("Unable to launch URL: " + result);
			} else {
				throw new IOException("Unable to create an Internet Config instance: " + result);
			}

			break;
		case 4:
			try {
				openURL.invoke(null, new Object[] { url });
			} catch (InvocationTargetException ite) {
				throw new IOException("InvocationTargetException while calling openURL: " + ite.getMessage());
			} catch (IllegalAccessException iae) {
				throw new IOException("IllegalAccessException while calling openURL: " + iae.getMessage());
			}

			break;
		case 5:
		case 6:
			String[] arguments;
			if (jvm == 6) {
				arguments = new String[] { (String) browser, "/c", "start" };
			} else {
				arguments = new String[] { (String) browser, "/c", "start", "\"\"" };
			}

			arguments[(arguments.length - 1)] = ('"' + url + '"');

			process = Runtime.getRuntime().exec(arguments);
			try {
				process.waitFor();
				process.exitValue();
			} catch (InterruptedException ie) {
				throw new IOException("InterruptedException while launching browser: " + ie.getMessage());
			}

			break;
		case -1:
			process = Runtime.getRuntime().exec(new String[] { (String) browser, "-remote", "'openURL(" + url + ")'" });
			try {
				int exitCode = process.waitFor();

				if (exitCode != 0)
					Runtime.getRuntime().exec(new String[] { (String) browser, url });
			} catch (InterruptedException ie) {
				throw new IOException("InterruptedException while launching browser: " + ie.getMessage());
			}

			break;
		case 2:
		default:
			Runtime.getRuntime().exec(new String[] { (String) browser, url });
		}
	}

	private static String getEnvironmentBrowser() {
		String browser = null;
		try {
			String[] echoParams = { "/bin/sh", "-c", "echo $BROWSER" };
			Process echoProcess = Runtime.getRuntime().exec(echoParams);
			InputStream echoStream = echoProcess.getInputStream();
			BufferedReader echoReader = new BufferedReader(new InputStreamReader(echoStream));
			browser = echoReader.readLine();
			echoReader.close();
		} catch (Throwable localThrowable) {
		}
		return browser;
	}

	private static native int ICStart(int[] paramArrayOfInt, int paramInt);

	private static native int ICStop(int[] paramArrayOfInt);

	private static native int ICLaunchURL(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2,
			int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}