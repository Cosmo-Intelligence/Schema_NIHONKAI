//*************************************************************************
// Copyright (c) 2003 by Yokogawa Electric Corporation.
//
// $RCSfile$
// $Date$
// $Revision$
// $Author$
// Description:
// 更新履歴
//
//*************************************************************************
package com.yokogawa.theraris.rtWeb.core;

import java.io.File;
import java.util.Calendar;

import com.yokogawa.theraris.logger.AppLogger;

/**
 * システム全体で利用される設定値を保持します
 */
public class LogFileDelete extends Thread
{
	// private members
	private static LogFileDelete m_objINSTANCE_= null;
	private boolean m_boolInitFlg_ = false;
	private boolean m_boolRunFlg_ = false;
	private int m_nLogStorageMonths_ = 12;
	private File m_fLogDir_ = null;
	private String m_strLogFilePrefix_ = "";
//
	private LogFileDelete()
	{
	}
//
	public static synchronized LogFileDelete getInstance()
	{
		if( m_objINSTANCE_ == null ){
			m_objINSTANCE_ = new LogFileDelete();
		}

		return m_objINSTANCE_;
	}
//
	public boolean init( String logFile, int logStorageMonths	)
	{
		if( logFile != null && logStorageMonths > 0 ){

			File filePath = new File(logFile);
			String logFilePrefix = filePath.getName().substring(0, filePath.getName().indexOf("."));
			File fLogDir = new File(filePath.getParent());
			if( fLogDir.exists() && fLogDir.canWrite() && logFilePrefix.length() > 0 ){
				m_fLogDir_ = fLogDir;
				m_nLogStorageMonths_ = logStorageMonths;
				m_strLogFilePrefix_ = logFilePrefix;

				m_boolInitFlg_ = true;
			}
		}

		return m_boolInitFlg_;
	}
//
	private void deleteOldLogFiles()
	{
		// paremeters
		int nLogFileNum = 0;
		File [] logFileList = null;
		StringBuffer strBufOldDate = new StringBuffer("");
		String strOldestFileToKeep = m_strLogFilePrefix_;
		Calendar curCal = Calendar.getInstance();
		Calendar oldCal = Calendar.getInstance();

		if( m_boolInitFlg_ != false ){
			AppLogger.getInstance().log(AppLogger.FINE,"LogFileDelete.deleteOldLogFiles - begin");

			// get current date
			int nCurYear = curCal.get(Calendar.YEAR);
			int nCurMonth = curCal.get(Calendar.MONTH) + 1;
			int nCurDay = curCal.get(Calendar.DAY_OF_MONTH);

			// create old date
			int nOldYear = nCurYear;
			int nOldMonth = 0;

			int nRewindOffSet = nCurMonth - m_nLogStorageMonths_%12;
			int nRewindYears = m_nLogStorageMonths_/12;

			if( nRewindOffSet <= 0 ){
				nOldMonth = 12 + nRewindOffSet;
				nOldYear -= (nRewindYears + 1);
			}
			else{
				nOldMonth = nCurMonth - nRewindOffSet;
				nOldYear -= nRewindYears;
			}

			strBufOldDate.append(nOldYear);
			if( nOldMonth < 10 ){
				strBufOldDate.append("0");
			}
			strBufOldDate.append(nOldMonth);
			if( nCurDay < 10 ){
				strBufOldDate.append("0");
			}
			strBufOldDate.append(nCurDay);

			AppLogger.getInstance().info("[BOUNDARY LOGFILE DATE] " + strBufOldDate.toString() );

			// create boundary log file name
			strOldestFileToKeep += strBufOldDate.toString();

			// get log file list
			logFileList = m_fLogDir_.listFiles();
			nLogFileNum = logFileList.length;

			// check log files and delete old files
			for( int i=0; i<nLogFileNum; i++ ){
				String possibleLogFileName = logFileList[i].getName();

				if (possibleLogFileName.length() < strOldestFileToKeep.length()){
					continue;
				}

				if( possibleLogFileName.indexOf(m_strLogFilePrefix_) >= 0 ){
//					AppLogger.getInstance().info("[CHECK LOG FILE] " + possibleLogFileName );

					if(possibleLogFileName.substring( 0, m_strLogFilePrefix_.length() + 8 ).compareTo(strOldestFileToKeep) < 0 ){
						AppLogger.getInstance().info("[DELETE LOG FILE] " + possibleLogFileName );
						logFileList[i].delete();
					}
				}
			}

			AppLogger.getInstance().log(AppLogger.FINE,"LogFileDelete.deleteOldLogFiles - end");
		}
	}
//
	public void run()
	{
		try{
			if( m_boolRunFlg_ != true ){
				m_boolRunFlg_ = true;
				AppLogger.getInstance().log(AppLogger.CONFIG, "Run LogAutoDelete  = [LogDirectory:" + m_fLogDir_.getAbsolutePath() + "] [Storage Months:" + m_nLogStorageMonths_ + "]" );

				while( true ){
					deleteOldLogFiles();
					Thread.sleep(86400000);// awake every 24 hour(24[hour]*60[min]*60[sec]*1000[milsec])
//					Thread.sleep(300000);// awake every 24 hour(24[hour]*60[min]*60[sec]*1000[milsec])
				}
			}
		}
		catch( InterruptedException e ){
//			AppLogger.getInstance().fatal( "<LogFileDelete.run()>", "[InterruptedException] Exception when runnnig LogFileDelete thread", e );
		}
	}
}
