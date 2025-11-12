package com.yokogawa.theraris.rtWeb.app.dbaccess.rtris;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.yokogawa.theraris.logger.AppLogger;
import com.yokogawa.theraris.rtWeb.app.Exceptions.RTDBAccessException;
import com.yokogawa.theraris.rtWeb.app.bean.PhotoSchemaInfoBean;
import com.yokogawa.theraris.rtWeb.core.Configuration;

/**
 * PHOTO_SCHEMA_INFOへのアクセス用クラス
 * @author cosmo
 *
 */
public class PhotoSchemaInfoAccess {
	/**
	 * 取得処理
	 * @param con コネクション
	 * @param risId RISID
	 * @return 取得テーブルbeanリスト
	 * @throws SQLException 登録に失敗
	 */
	public PhotoSchemaInfoBean select(Connection con, String photeSchemaUid)
	throws RTDBAccessException {
		AppLogger.getInstance().log(AppLogger.FINE,"PHOTO_SCHEMA_INFOの値を取得します");
	if (con == null) {
		throw new RTDBAccessException("コネクションが取得できませんでした");
	}
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("SELECT ");
		sqlBuf.append(" PHOTO_SCHEMA_UID");
		sqlBuf.append(",KANJA_ID");
		sqlBuf.append(",RIS_ID");
		sqlBuf.append(",HAND_POSITION");
		sqlBuf.append(",SCHEMA_BUI");
		sqlBuf.append(",SCHEMA_COMMENT");
		sqlBuf.append(",START_POINT_X");
		sqlBuf.append(",START_POINT_Y");
		sqlBuf.append(",END_POINT_X");
		sqlBuf.append(",END_POINT_Y");
		sqlBuf.append(",SCHEMA_IMAGE_NAME");
		sqlBuf.append(",ENTRY_DATE");
		sqlBuf.append(",UPD_DATE");
		sqlBuf.append(" FROM ");
		sqlBuf.append(Configuration.getInstance().getRTRISDBUser());
		sqlBuf.append(".PHOTO_SCHEMA_INFO ");
		sqlBuf.append(" WHERE PHOTO_SCHEMA_UID = ?");

		PreparedStatement stmt = null;
		ResultSet rset = null;
		PhotoSchemaInfoBean photoSchemaInfoBean = new PhotoSchemaInfoBean();
		try {
			stmt = con.prepareStatement(sqlBuf.toString());
			stmt.setString(1, photeSchemaUid);
			rset = stmt.executeQuery();                // 結果取得
			if( rset != null ){
				if(rset.next()){
					photoSchemaInfoBean.setPhoteSchemaUid(rset.getString("PHOTO_SCHEMA_UID"));
					photoSchemaInfoBean.setKanjaID(rset.getString("KANJA_ID"));
					photoSchemaInfoBean.setRisId(rset.getString("RIS_ID"));
					photoSchemaInfoBean.setHandPosition(rset.getString("HAND_POSITION"));
					photoSchemaInfoBean.setSchemaBui(rset.getString("SCHEMA_BUI"));
					if(rset.getString("SCHEMA_COMMENT") !=null){
						photoSchemaInfoBean.setSchemaComment(rset.getString("SCHEMA_COMMENT"));
					}
					photoSchemaInfoBean.setStartPointX(rset.getBigDecimal("START_POINT_X"));
					photoSchemaInfoBean.setStartPointY(rset.getBigDecimal("START_POINT_Y"));
					photoSchemaInfoBean.setEndPointX(rset.getBigDecimal("END_POINT_X"));
					photoSchemaInfoBean.setEndPointY(rset.getBigDecimal("END_POINT_Y"));
					photoSchemaInfoBean.setSchemaImageName(rset.getString("SCHEMA_IMAGE_NAME"));
					photoSchemaInfoBean.setEntryDate(rset.getDate("ENTRY_DATE"));
					photoSchemaInfoBean.setUpdDate(rset.getDate("UPD_DATE"));
				}
			}
				AppLogger.getInstance().log(AppLogger.FINE,"[get PhotoSchemaInfoBean] " );
		} catch (SQLException e) {
			throw new RTDBAccessException(e);
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				AppLogger.getInstance().fatal("", e);
			}
		}
		return photoSchemaInfoBean;
	}
	/**
	 * データ挿入処理
	 * @param con コネクション
	 * @param bean テーブルbean
	 * @throws SQLException 登録に失敗
	 */
	public void insert(Connection con, PhotoSchemaInfoBean bean)
	throws RTDBAccessException {
		AppLogger.getInstance().log(AppLogger.FINE,"PHOTO_SCHEMA_INFOをINSERTします。");
		if (con == null) {
			throw new RTDBAccessException("コネクションが取得できませんでした");
		}

		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("INSERT INTO ");
		sqlBuf.append(Configuration.getInstance().getRTRISDBUser());
		sqlBuf.append(".PHOTO_SCHEMA_INFO (");
		sqlBuf.append(" PHOTO_SCHEMA_UID");
		sqlBuf.append(",KANJA_ID");
		sqlBuf.append(",RIS_ID");
		sqlBuf.append(",HAND_POSITION");
		sqlBuf.append(",SCHEMA_BUI");
		sqlBuf.append(",SCHEMA_COMMENT");
		sqlBuf.append(",START_POINT_X");
		sqlBuf.append(",START_POINT_Y");
		sqlBuf.append(",END_POINT_X");
		sqlBuf.append(",END_POINT_Y");
		sqlBuf.append(",SCHEMA_IMAGE_NAME");
		sqlBuf.append(",ENTRY_DATE");
		sqlBuf.append(")");
		sqlBuf.append(" VALUES (");
		sqlBuf.append("?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",?");
		sqlBuf.append(",SYSDATE");
		sqlBuf.append(")");

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sqlBuf.toString());
			stmt.setString(1, bean.getPhoteSchemaUid());
			stmt.setString(2, bean.getKanjaID());
			stmt.setString(3, bean.getRisId());
			stmt.setString(4, bean.getHandPosition());
			stmt.setString(5, bean.getSchemaBui());
			stmt.setCharacterStream(6, new StringReader(bean.getSchemaComment()));
			stmt.setBigDecimal(7, bean.getStartPointX());
			stmt.setBigDecimal(8, bean.getStartPointY());
			stmt.setBigDecimal(9, bean.getEndPointX());
			stmt.setBigDecimal(10, bean.getEndPointY());
			stmt.setString(11, bean.getSchemaImageName());
			stmt.executeUpdate();
			AppLogger.getInstance().log(AppLogger.FINE,"PHOTO_SCHEMA_INFOのINSERTが完了しました。");
		} catch (SQLException e) {
			throw new RTDBAccessException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				AppLogger.getInstance().fatal("", e);
			}
		}
	}
	/**
	 * データ更新処理
	 * @param con コネクション
	 * @param bean テーブルbean
	 * @throws SQLException 登録に失敗
	 */
	public void update(Connection con, PhotoSchemaInfoBean bean)
	throws RTDBAccessException {
		AppLogger.getInstance().log(AppLogger.FINE,"PHOTO_SCHEMA_INFOをUPDATEします。");
		if (con == null) {
			throw new RTDBAccessException("コネクションが取得できませんでした");
		}

		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("UPDATE ");
		sqlBuf.append(Configuration.getInstance().getRTRISDBUser());
		sqlBuf.append(".PHOTO_SCHEMA_INFO ");
		sqlBuf.append("SET KANJA_ID = ? ");
		sqlBuf.append("   ,RIS_ID = ? ");
		sqlBuf.append("   ,HAND_POSITION = ? ");
		sqlBuf.append("   ,SCHEMA_BUI = ? ");
		sqlBuf.append("   ,SCHEMA_COMMENT = ? ");
		sqlBuf.append("   ,START_POINT_X = ? ");
		sqlBuf.append("   ,START_POINT_Y = ? ");
		sqlBuf.append("   ,END_POINT_X = ? ");
		sqlBuf.append("   ,END_POINT_Y = ? ");
		sqlBuf.append("   ,SCHEMA_IMAGE_NAME = ? ");
		sqlBuf.append("   ,UPD_DATE = SYSDATE ");
		sqlBuf.append("WHERE PHOTO_SCHEMA_UID = ? ");
		sqlBuf.append(" ");

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sqlBuf.toString());
			stmt = con.prepareStatement(sqlBuf.toString());
			stmt.setString(1, bean.getKanjaID());
			stmt.setString(2, bean.getRisId());
			stmt.setString(3, bean.getHandPosition());
			stmt.setString(4, bean.getSchemaBui());
			stmt.setCharacterStream(5, new StringReader(bean.getSchemaComment()));
			stmt.setBigDecimal(6, bean.getStartPointX());
			stmt.setBigDecimal(7, bean.getStartPointY());
			stmt.setBigDecimal(8, bean.getEndPointX());
			stmt.setBigDecimal(9, bean.getEndPointY());
			stmt.setString(10, bean.getSchemaImageName());
			stmt.setString(11, bean.getPhoteSchemaUid());
			stmt.executeUpdate();
			AppLogger.getInstance().log(AppLogger.FINE,"PHOTO_SCHEMA_INFOのUPDATEが完了しました。");
		} catch (SQLException e) {
			throw new RTDBAccessException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				AppLogger.getInstance().fatal("", e);
			}
		}
	}
}
