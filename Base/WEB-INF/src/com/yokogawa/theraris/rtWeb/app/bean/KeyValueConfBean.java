package com.yokogawa.theraris.rtWeb.app.bean;

/**
 * 入外区分用bean
 * @author cosmo
 *
 */
public class KeyValueConfBean {
	//入外区分コード
	private String m_Id = null;
	//入外区分名称
	private String m_Name = null;
	//使用区分
	private boolean m_useFlg = false;

	/**
	 * 入外区分IDを返却する
	 * @return 入外区分ID
	 */
	public String getId() {
		return m_Id;
	}

	/**
	 * 入外区分IDを設定する
	 * @param id 入外区分ID
	 */
	public void setId(String id) {
		m_Id = id;
	}

	/**
	 * 入外区分名を返却する
	 * @return 入外区分名
	 */
	public String getName() {
		return m_Name;
	}

	/**
	 * 入外区分名を設定する
	 * @param name 入外区分名
	 */
	public void setName(String name) {
		m_Name = name;
	}

	/**
	 * 入外区分使用可否を判定する
	 * @return この入外区分を使用する場合はtrue
	 */
	public boolean isUse() {
		return m_useFlg;
	}

	/**
	 * 入外区分使用可否を設定する
	 * @param flg 入外区分使用可の場合はtrue
	 */
	public void setUseFlg(boolean flg) {
		m_useFlg = flg;
	}

}
