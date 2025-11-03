package com.yokogawa.theraris.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
/**
 * ログ出力設定ファイル
 *
 * @author cosmo
 * */
public class AppLogger {

	 static final Logger logger = LogManager.getLogger();

	 //custom level
	 public static final Level SECURITY = Level.forName("SECURITY", 50);
	 public static final Level CONFIG = Level.forName("CONFIG", 430);
	 public static final Level FINE = Level.forName("FINE", 460);

	 public static Logger getInstance() {
		 return LogManager.getLogger(new Throwable().getStackTrace()[1].getClassName());
	 }

	 public static String getLogFilePath() {
		 LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		 Configuration config = ctx.getConfiguration();
		 RollingFileAppender append = config.getAppender("File");
		 return append.getFileName();
	 }
}
