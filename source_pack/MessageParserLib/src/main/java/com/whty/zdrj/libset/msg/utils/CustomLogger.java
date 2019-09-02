package com.whty.zdrj.libset.msg.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CustomLogger {

	// private static CustomLogger customLogger;
	private static Map<String, CustomLogger> customLoggerList = new HashMap<String, CustomLogger>();
	private Object logger;
	private String loggerName;
	private static boolean isSlf4jLogAvaliable = true;
	// public final Logger logger1 =
	// LoggerFactory.getLogger(StandardTlvParserImpl.class);

	private CustomLogger() {
		// do nothing
	}

	// private CustomLogger(Class<?> className) {
	// try {
	// Class<?> c = Class.forName("org.slf4j.LoggerFactory");
	// Method getLoggerMethod = c.getMethod("getLogger", Class.class);
	// logger = getLoggerMethod.invoke(null, className);
	// } catch (Exception e) {
	// System.out.println("no slf4j logger found");
	// }
	// }

	private void setLoggerName(String name) {
		// System.out.println("set logger name:" + name);
		loggerName = name;
		try {
			Class<?> c = Class.forName("org.slf4j.LoggerFactory");
			Method getLoggerMethod = c.getMethod("getLogger", String.class);
			logger = getLoggerMethod.invoke(null, loggerName);
		} catch (Exception e) {
			System.out.println("no slf4j logger found");
			isSlf4jLogAvaliable = false;
		}
	}

	public static CustomLogger getLogger(String name) {
		// if (customLogger == null) {
		// System.out.println("create a new logger");
		CustomLogger customLogger = customLoggerList.get(name);
		if (customLogger == null) {
			customLogger = new CustomLogger();
			customLoggerList.put(name, customLogger);
		}
		// }
		if (isSlf4jLogAvaliable) {
			customLogger.setLoggerName(name);
		}
		return customLogger;
	}

	public static CustomLogger getLogger(Class<?> className) {
		// if (customLogger == null) {
		// CustomLogger customLogger = new CustomLogger();
		CustomLogger customLogger = customLoggerList.get(className.getName());
		if (customLogger == null) {
			customLogger = new CustomLogger();
			customLoggerList.put(className.getName(), customLogger);
		}
		// }
		if (isSlf4jLogAvaliable) {
			customLogger.setLoggerName(className.getName());
		}
		return customLogger;
	}

	public void trace(String msg) {
		if (logger != null) {
			try {
				Class<?> c = Class.forName("org.slf4j.Logger");
				Method traceMethod = c.getMethod("trace", String.class);
				traceMethod.invoke(logger, msg);
			} catch (Exception e) {
				System.out.println("invoke trace error");
			}
		}
	}

	public void info(String msg) {
		if (logger != null) {
			try {
				Class<?> c = Class.forName("org.slf4j.Logger");
				Method traceMethod = c.getMethod("info", String.class);
				traceMethod.invoke(logger, msg);
			} catch (Exception e) {
				System.out.println("invoke info error");
			}
		}
	}

	public void debug(String msg) {
		if (logger != null) {
			try {
				Class<?> c = Class.forName("org.slf4j.Logger");
				Method traceMethod = c.getMethod("debug", String.class);
				traceMethod.invoke(logger, msg);
			} catch (Exception e) {
				System.out.println("invoke debug error");
			}
		}
	}

	public void debug(String msg, Throwable exception) {
		if (logger != null) {
			try {
				Class<?> c = Class.forName("org.slf4j.Logger");
				Method traceMethod = c.getMethod("debug", String.class, Throwable.class);
				traceMethod.invoke(logger, msg, exception);
			} catch (Exception e) {
				System.out.println("invoke warn error");
			}
		}
	}

	public void warn(String msg) {
		if (logger != null) {
			try {
				Class<?> c = Class.forName("org.slf4j.Logger");
				Method traceMethod = c.getMethod("warn", String.class);
				traceMethod.invoke(logger, msg);
			} catch (Exception e) {
				System.out.println("invoke warn error");
			}
		}
	}

	public void warn(String msg, Throwable exception) {
		if (logger != null) {
			try {
				Class<?> c = Class.forName("org.slf4j.Logger");
				Method traceMethod = c.getMethod("warn", String.class, Throwable.class);
				traceMethod.invoke(logger, msg, exception);
			} catch (Exception e) {
				System.out.println("invoke warn error");
			}
		}
	}

	public void error(String msg) {
		if (logger != null) {
			try {
				Class<?> c = Class.forName("org.slf4j.Logger");
				Method traceMethod = c.getMethod("error", String.class);
				traceMethod.invoke(logger, msg);
			} catch (Exception e) {
				System.out.println("invoke error error");
			}
		}
	}

	public void error(String msg, Throwable exception) {
		if (logger != null) {
			try {
				Class<?> c = Class.forName("org.slf4j.Logger");
				Method traceMethod = c.getMethod("error", String.class, Throwable.class);
				traceMethod.invoke(logger, msg, exception);
			} catch (Exception e) {
				System.out.println("invoke error error");
			}
		}
	}
}
