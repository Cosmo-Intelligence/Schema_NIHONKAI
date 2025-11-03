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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.yokogawa.theraris.logger.AppLogger;

/**
 * DB接続オブジェクト生成クラス
 */
public class DbConnectionFactory
{
	// private members
	private static DbConnectionFactory _INSTANCE = new DbConnectionFactory();
//
	// private constructor
	private DbConnectionFactory()
	{
	}
//
	// public methods
	public static DbConnectionFactory getInstance()
	{
		return _INSTANCE;
	}

	/**
	 * 治療RISDBとの接続を返却する
	 * @return RTRISDBとのConnection
	 */
	public Connection getRTRISDBConnection() {
		// parameters
		Connection con = null;
		Class jdbcDriver = null;

		// begin log
		AppLogger.getInstance().log(AppLogger.FINE,"DbConnectionFactory.getRTRISDBConnection() - begin");

		try {
			jdbcDriver = Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(Configuration.getInstance().getRTRISDBConnection(), Configuration.getInstance().getRTRISDBUser(), Configuration.getInstance().getRTRISDBPassword());
			con.setAutoCommit(false);

			if (con != null) {
				AppLogger.getInstance().log(AppLogger.FINE,"DbConnectionFactory.getRTRISDBConnection()", "治療RISDBconnection[" + con.hashCode() + "]取得");
			}
		} catch (ClassNotFoundException e) {
			AppLogger.getInstance().fatal("DbConnectionFactory.getRTRISDBConnection()", "OracleDriverが見つかりませんでした", e);
		} catch (SQLException e) {
			AppLogger.getInstance().fatal("DbConnectionFactory.getRTRISDBConnection()", "治療RISDB接続に失敗しました", e);
		} catch (Exception e) {
			AppLogger.getInstance().fatal("DbConnectionFactory.getRTRISDBConnection()", "治療RISDB接続時にExceptionが発生しました", e);
		}

		// end log
		AppLogger.getInstance().log(AppLogger.FINE,"DbConnectionFactory.getRTRISDBConnection() - end");

		return con;
	}

	/**
	 * 治療RISDBのConnectionをcloseする
	 * @param con 治療RISDBのConnection
	 */
	public void returnRTRISDBConnection(Connection con) {
		if (con != null) {
			AppLogger.getInstance().log(AppLogger.FINE, "DbConnectionFactory.returnRTRISDBConnection()", "治療RISDBconnection[" + con.hashCode() + "]返却");

			try {
				if (!con.isClosed()) {
					con.close();
				}
				con = null;
			} catch(SQLException e) {
				AppLogger.getInstance().fatal( "DbConnectionFactory.returnRTRISDBConnection()", "治療RISDBのcloseに失敗しました", e);
			}
		}
	}

}
