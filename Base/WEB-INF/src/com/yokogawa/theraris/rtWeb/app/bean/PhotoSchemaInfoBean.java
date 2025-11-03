package com.yokogawa.theraris.rtWeb.app.bean;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * シェーマ画像テーブルbean
 * @author cosmo
 *
 */
public class PhotoSchemaInfoBean {
	/** シェーマUID */
	private String photeSchemaUid="";
	/** 患者ID */
	private String kanjaID="";
	/** RISID */
	private String risId="";
	/** 手の位置 */
	private String handPosition="";
	/** 部位区分 */
	private String schemaBui="";
	/** コメント */
	private String schemaComment="";
	/** 撮影範囲　始点X座標 */
	private BigDecimal startPointX;
	/** 撮影範囲　始点Y座標 */
	private BigDecimal startPointY;
	/** 撮影範囲　終点X座標 */
	private BigDecimal endPointX;
	/** 撮影範囲　終点Y座標 */
	private BigDecimal endPointY;
	/** シェーマ画像ファイル名 */
	private String schemaImageName;
	/** 登録日時 */
	private Date entryDate;
	/** 更新日時 */
	private Date updDate;
	public String getPhoteSchemaUid() {
		return photeSchemaUid;
	}
	public void setPhoteSchemaUid(String photeSchemaUid) {
		this.photeSchemaUid = photeSchemaUid;
	}
	public String getKanjaID() {
		return kanjaID;
	}
	public void setKanjaID(String kanjaID) {
		this.kanjaID = kanjaID;
	}
	public String getRisId() {
		return risId;
	}
	public void setRisId(String risId) {
		this.risId = risId;
	}
	public String getHandPosition() {
		return handPosition;
	}
	public void setHandPosition(String handPosition) {
		this.handPosition = handPosition;
	}
	public String getSchemaBui() {
		return schemaBui;
	}
	public void setSchemaBui(String schemaBui) {
		this.schemaBui = schemaBui;
	}
	public String getSchemaComment() {
		return schemaComment;
	}
	public void setSchemaComment(String schemaComment) {
		this.schemaComment = schemaComment;
	}
	public BigDecimal getStartPointX() {
		return startPointX;
	}
	public void setStartPointX(BigDecimal startPointX) {
		this.startPointX = startPointX;
	}
	public BigDecimal getStartPointY() {
		return startPointY;
	}
	public void setStartPointY(BigDecimal startPointY) {
		this.startPointY = startPointY;
	}
	public BigDecimal getEndPointX() {
		return endPointX;
	}
	public void setEndPointX(BigDecimal endPointX) {
		this.endPointX = endPointX;
	}
	public BigDecimal getEndPointY() {
		return endPointY;
	}
	public void setEndPointY(BigDecimal endPointY) {
		this.endPointY = endPointY;
	}
	public String getSchemaImageName() {
		return schemaImageName;
	}
	public void setSchemaImageName(String schemaImageName) {
		this.schemaImageName = schemaImageName;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public Date getUpdDate() {
		return updDate;
	}
	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

}
