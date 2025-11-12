package com.yokogawa.theraris.rtWeb.app.bean;

import java.math.BigDecimal;

/**
 * 入外区分用bean
 * @author cosmo
 *
 */
public class BuiDefaultPosConfBean {
	//入外区分コード
	private String id = null;
	//入外区分名称
	private BigDecimal startX = null;
	private BigDecimal startY = null;
	private BigDecimal endX = null;
	private BigDecimal endY = null;
	//使用区分
	private boolean useFlg = false;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getStartX() {
		return startX;
	}
	public void setStartX(BigDecimal startX) {
		this.startX = startX;
	}
	public BigDecimal getStartY() {
		return startY;
	}
	public void setStartY(BigDecimal startY) {
		this.startY = startY;
	}
	public BigDecimal getEndX() {
		return endX;
	}
	public void setEndX(BigDecimal endX) {
		this.endX = endX;
	}
	public BigDecimal getEndY() {
		return endY;
	}
	public void setEndY(BigDecimal endY) {
		this.endY = endY;
	}
	public boolean isUseFlg() {
		return useFlg;
	}
	public void setUseFlg(boolean useFlg) {
		this.useFlg = useFlg;
	}

}
