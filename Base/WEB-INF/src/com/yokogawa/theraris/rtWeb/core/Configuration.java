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
import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.yokogawa.theraris.rtWeb.app.bean.BuiDefaultPosConfBean;
import com.yokogawa.theraris.rtWeb.app.bean.KeyValueConfBean;

/**
 * システム全体で利用される設定値を保持します
 */
public class Configuration
{
	// private members
	static private Configuration _INSTANCE = new Configuration();
	private boolean m_boolIsConfigured_ = false;

	/**
	 * アプリケーション基本情報
	 */
	// rpWebHomeのフォルダ・フルパス
	private File m_appRootDir_;
	// 読み込む対象の設定ファイル（通常はrtWebConf.xml）
	private File m_appConfigDir_;
	// 初期化成否フラグ
	private boolean m_isSuccessConfiguration_ = true;

	/**
	 * 特注処理関連
	 */
	// JOB_ID（特注等サイト単位での追加処理の整合性のため）
	private String m_strJobID_ = "";

	/**
	 * DB接続
	 */
	// RTRISDB接続
	private String m_strRTRISDBConnection_ = "";
	// RTRISDBユーザ
	private String m_strRTRISDBUser_ = "";
	// RTRISDBパスワード
	private String m_strRTRISDBPassword_ = "";
	// RTRIS最大取得レコード数
	private String m_strRTRISDBMaxResults_ = "500";

	///
	private String schemaImgPath = "";
	/**
	 * 手の位置のマッピング表
	 * ID順にソートされる
	 */
	private TreeMap<String, KeyValueConfBean> handPosMap = new TreeMap<String, KeyValueConfBean>();
	/**
	 * 部位のマッピング表
	 * ID順にソートされる
	 */
	private TreeMap<String, KeyValueConfBean> buiMap = new TreeMap<String, KeyValueConfBean>();

	/**
	 * 初期デフォルト値のマッピング表
	 * put順を保持する
	 */
	private LinkedHashMap<String, KeyValueConfBean> defaultMap = new LinkedHashMap<String, KeyValueConfBean>();

	/**
	 * 初期デフォルト値のマッピング表
	 * put順を保持する
	 */
	private LinkedHashMap<String, KeyValueConfBean> schemaImgNameMap = new LinkedHashMap<String, KeyValueConfBean>();

	/**
	 * 初期デフォルト値のマッピング表
	 * put順を保持する
	 */
	private LinkedHashMap<String, BuiDefaultPosConfBean> defaultPosConfBeanMap = new LinkedHashMap<String, BuiDefaultPosConfBean>();

	//
	static public Configuration getInstance()
	{
		return _INSTANCE;
	}
//
	private Configuration()
	{
	}
//
	public void setIsConfigured()
	{
		m_boolIsConfigured_ = true;
	}
//
	public boolean isConfigured()
	{
		return m_boolIsConfigured_;
	}
//
	public void setApplicationRootDir( File appRootDir )
	{
		m_appRootDir_ = appRootDir;
	}
//
	public File getApplicationRootDir()
	{
		return m_appRootDir_;
	}
//
	public void setApplicationConfigDir( File appConfigDir )
	{
		m_appConfigDir_ = appConfigDir;
	}
//
	public File getApplicationConfigDir()
	{
		return m_appConfigDir_;
	}
//
	synchronized public boolean isSuccessConfiguration()
	{
		return m_isSuccessConfiguration_;
	}
//
	synchronized public void setIsSuccessConfiguration( boolean isSuccessConfiguration )
	{
		m_isSuccessConfiguration_ = isSuccessConfiguration;
	}
//
	public void setJobID( String jobID )
	{
		if( jobID != null ){
			m_strJobID_ = jobID;
		}
	}
//
	public String getJobID()
	{
		return m_strJobID_;
	}
	public void setRTRISDBConnection( String dbCon )
	{
		if( dbCon != null ){
			m_strRTRISDBConnection_ = dbCon;
		}
	}
//
	public String getRTRISDBConnection()
	{
		return m_strRTRISDBConnection_;
	}
//
	public void setRTRISDBUser( String dbUser )
	{
		if( dbUser != null ){
			m_strRTRISDBUser_ = dbUser;
		}
	}
//
	public String getRTRISDBUser()
	{
		return m_strRTRISDBUser_;
	}
//
	public void setRTRISDBPassword( String dbPwd )
	{
		if( dbPwd != null ){
			m_strRTRISDBPassword_ = dbPwd;
		}
	}
//
	public String getRTRISDBPassword()
	{
		return m_strRTRISDBPassword_;
	}
//
	public void setRTRISMaxResults( String max )
	{
		if( max != null ){
			m_strRTRISDBMaxResults_ = max;
		}
	}
//
	public String getRTRISMaxResults()
	{
		return m_strRTRISDBMaxResults_;
	}
	/**
	 * 手の位置マッピングを設定する<br>
	 * 設定されたタイトル区分は手の位置ID順にソートされる
	 * @param key 手の位置ID
	 * @param keyValueConfBean 手の位置オブジェクト
	 */
	public synchronized void setHandPosMap(String key, KeyValueConfBean keyValueConfBean) {
		if (key != null && keyValueConfBean != null) {
			handPosMap.put(key, keyValueConfBean);
		}
	}

	/**
	 * 手の位置マッピングMapを返却する
	 * @return タイトル区分IDをキーに、昇順にマッピングされたMap
	 */
	public TreeMap<String, KeyValueConfBean> getHandPosMap() {
		TreeMap<String, KeyValueConfBean> map = new TreeMap<String, KeyValueConfBean>();
		map.putAll(handPosMap);

		return map;
	}
	/**
	 * 部位マッピングを設定する<br>
	 * 設定されたタイトル区分は手の位置ID順にソートされる
	 * @param key 手の位置ID
	 * @param keyValueConfBean 手の位置オブジェクト
	 */
	public synchronized void setBuiMap(String key, KeyValueConfBean keyValueConfBean) {
		if (key != null && keyValueConfBean != null) {
			buiMap.put(key, keyValueConfBean);
		}
	}

	/**
	 * 部位マッピングMapを返却する
	 * @return タイトル区分IDをキーに、昇順にマッピングされたMap
	 */
	public TreeMap<String, KeyValueConfBean> getBuiMap() {
		TreeMap<String, KeyValueConfBean> map = new TreeMap<String, KeyValueConfBean>();
		map.putAll(buiMap);

		return map;
	}
	/**
	 * デフォルトを設定する<br>
	 * 設定された入外区分は入外区分ID順にソートされる
	 * @param nyugaiId 入外区分ID
	 * @param nyugaiKbn 入外区分オブジェクト
	 */
	public synchronized void setDefaultMap(String key, KeyValueConfBean keyValueConfBean) {
		if (key != null && keyValueConfBean != null) {
			defaultMap.put(key, keyValueConfBean);
		}
	}

	/**
	 * 入外区分Mapを返却する
	 * @return 入外区分IDをキーに、昇順にマッピングされたMap
	 */
	public TreeMap<String, KeyValueConfBean> getDefaultMap() {
		TreeMap<String, KeyValueConfBean> map = new TreeMap<String, KeyValueConfBean>();
		map.putAll(defaultMap);

		return map;
	}
	/**
	 * デフォルトを設定する<br>
	 * 設定された入外区分は入外区分ID順にソートされる
	 * @param nyugaiId 入外区分ID
	 * @param nyugaiKbn 入外区分オブジェクト
	 */
	public synchronized void setSchemaImgNameMap(String key, KeyValueConfBean keyValueConfBean) {
		if (key != null && keyValueConfBean != null) {
			schemaImgNameMap.put(key, keyValueConfBean);
		}
	}

	/**
	 * 入外区分Mapを返却する
	 * @return 入外区分IDをキーに、昇順にマッピングされたMap
	 */
	public TreeMap<String, KeyValueConfBean> getSchemaImgNameMap() {
		TreeMap<String, KeyValueConfBean> map = new TreeMap<String, KeyValueConfBean>();
		map.putAll(schemaImgNameMap);

		return map;
	}
	/**
	 * デフォルトを設定する<br>
	 * 設定された入外区分は入外区分ID順にソートされる
	 * @param nyugaiId 入外区分ID
	 * @param nyugaiKbn 入外区分オブジェクト
	 */
	public synchronized void setDefaultPosConfBeanMap(String key, BuiDefaultPosConfBean buiDefaultPosConfBean) {
		if (key != null && buiDefaultPosConfBean != null) {
			defaultPosConfBeanMap.put(key, buiDefaultPosConfBean);
		}
	}

	/**
	 * 入外区分Mapを返却する
	 * @return 入外区分IDをキーに、昇順にマッピングされたMap
	 */
	public TreeMap<String, BuiDefaultPosConfBean> getDefaultPosConfBeanMap() {
		TreeMap<String, BuiDefaultPosConfBean> map = new TreeMap<String, BuiDefaultPosConfBean>();
		map.putAll(defaultPosConfBeanMap);

		return map;
	}
	public String getSchemaImgPath() {
		return schemaImgPath;
	}
	public void setSchemaImgPath(String schemaImgPath) {
		this.schemaImgPath = schemaImgPath;
	}

}
