//*************************************************************************
// Copyright (c) 2005 by Yokogawa Electric Corporation.
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.yokogawa.theraris.logger.AppLogger;
import com.yokogawa.theraris.rtWeb.app.Exceptions.ConfigurationException;
import com.yokogawa.theraris.rtWeb.app.bean.BuiDefaultPosConfBean;
import com.yokogawa.theraris.rtWeb.app.bean.KeyValueConfBean;
import com.yokogawa.theraris.rtWeb.app.common.constants.CoreMessage;
import com.yokogawa.theraris.rtWeb.app.common.utils.CoreUtil;

/**
 * NEXTAScope全体の初期設定用サーブレット</br>
 *
 * このサーブレットは
 * <ul>
 * <li>ログの設定
 * <li>HTTPセッションの設定
 * <li>ユーザ認証の設定
 * <li>画面構成の設定
 * <li>NEXTASのセッション管理の設定
 * </ul>
 * を行う．</br>
 *
 * tomcat起動時にこのサーブレットのインスタンスも作成されて初期設定処理が 自動的に呼ばれる（ようにweb.xmlを設定すること）．
 *
 */
public class ConfiguratorServlet extends HttpServlet {
	static private boolean _isConfigured = false;
	private LogFileDelete m_logAutoDelete_ = null;
	private int m_nStartupDelay_ = 0;

	/** rtWebConf.xml読込用 */
	// 使用可否フラグ
	private static final String USE_FLG = "useFlg";
	// 使用可否フラグの使用可値
	private static final String USE_FLG_Y = "Y";
	// 手の位置マッピング指定箇所
	private static final String HANDPOS_NODE = "/rtWebSchemaConf/handPosList/dataKbn";
	// 手の位置ID
	private static final String HANDPOS_ID = "handPosId";
	// 手の位置連携側KEY
	private static final String HANDPOS_KEY = "handPosKey";
	// 部位マッピング箇所
	private static final String BUI_NODE = "/rtWebSchemaConf/buiList/dataKbn";
	// 部位ID
	private static final String BUI_ID = "buiId";
	// 部位連携側KEY
	private static final String BUI_KEY = "buiKey";
	// 初期デフォルト値設定箇所
	private static final String DEFAULT_NODE = "/rtWebSchemaConf/defaultList/dataKbn";
	// デフォルトID
	private static final String DEFAULT_ID = "defId";
	// デフォルト値
	private static final String DEFAULT_VAL = "defVal";
	// シェーマ画像の保存パス
	private static final String SCHEMA_PATH_NODE = "/rtWebSchemaConf/schemaImgPath/@path";
	// 「手の位置」毎のシェーマ画像設定
	private static final String SCHEMA_NAME_NODE = "/rtWebSchemaConf/schemaImgNameList/dataKbn";
	// 性別和名
	private static final String FILE_NAME = "fileName";
	// 部位ごとのデフォルト撮影範囲の座標設定指定箇所
	private static final String DEFAULT_POS_NODE = "/rtWebSchemaConf/defaultPosList/dataKbn";
	// 性別和名
	private static final String DEFAULT_START_X = "startX";
	// 性別和名
	private static final String DEFAULT_START_Y = "startY";
	// 性別和名
	private static final String DEFAULT_END_X = "endX";
	// 性別和名
	private static final String DEFAULT_END_Y = "endY";
	// XML読込時の文字エンコーディング
	private static final String XML_ENCODING = "MS932";






	public void init(ServletConfig config) throws ServletException {
		// 初期設定完了済みの場合は何もしない
		if (_isConfigured) {
			return;
		}

		try {
			initImpl(config);
			Configuration.getInstance().setIsSuccessConfiguration(true);
			AppLogger.getInstance().info("システムの初期設定を終了しました");
		} catch (ConfigurationException e) {
			// 例外1
			CoreUtil.logFatal("ConfigurationServlet#init",
					CoreMessage.CONFIG_FAILURE, CoreMessage.CONFIG_FAILURE_MSG,
					e);
			AppLogger.getInstance().info("全サービスの提供を不許可にしました");
			Configuration.getInstance().setIsSuccessConfiguration(false);
			throw new ServletException("全サービスの提供を不許可にしました", e);
		} catch (ServletException e) {

			System.err.println("全サービスの提供を不許可にしました");
			Configuration.getInstance().setIsSuccessConfiguration(false);
			throw e;

		} catch (Throwable e) {

			System.err.println("全サービスの提供を不許可にしました");
			Configuration.getInstance().setIsSuccessConfiguration(false);
			throw new ServletException("全サービスの提供を不許可にしました", e);

		}

		// 初期化済みフラグをtrueに
		_isConfigured = true;
	}

	public void destroy() {

		AppLogger.getInstance().info("システムのshutdown処理を開始しました...");

		try {
			// ログ削除threadのdestroy
			if (m_logAutoDelete_ != null) {
				m_logAutoDelete_.interrupt();
				m_logAutoDelete_.join();
			}
		} catch (Exception e) {
		}

		AppLogger.getInstance().info("システムのshutdown処理を終了しました");
	}

	/**
	 * 初期化処理
	 *
	 * @param config
	 *            サーブレットの設定オブジェクト
	 * @throws ServletException
	 * @throws ConfigurationException
	 */
	private void initImpl(ServletConfig config) throws ServletException,
			ConfigurationException {
		// parameters
		ServletContext context = null;
		String rootPath = null;
		String configDirPath = null;
		String configFileName = null;
		Document rootNode = null;

		// ServletContext取得
		context = config.getServletContext();
		// ServletContextからdocbaseの絶対パスを取得
		rootPath = context.getRealPath("/");
		// web.xmlの設定を取得する
		if (config != null) {
			// 設定ファイルが存在するパスを取得
			configDirPath = config
					.getInitParameter("rtWebConfigurationDirectory");
			// 設定ファイル名を取得
			configFileName = config
					.getInitParameter("rtWebConfigurationFileName");
		}

		// 設定ファイル内の定義をチェック
		if (configDirPath == null) {
			throw new ServletException("web.xmlにrtWebディレクトリ設定が見つかりません");
		}
		if (configFileName == null) {
			throw new ServletException("web.xmlにrtWebファイル名設定が見つかりません");
		}

		// rtWebConf.xmlのFileオブジェクトを生成する
		File configDir = new File(rootPath, configDirPath);
		File rtWebConfig = new File(configDir, configFileName);

		// rtWebSchemaConf.xmlの存在チェック
		if (!rtWebConfig.exists()) {
			throw new ServletException("rtWebSchemaの設定ファイルが存在しません");
		}

		// Configurationにディレクトリパス情報を設定する
		Configuration.getInstance().setApplicationRootDir(new File(rootPath));
		Configuration.getInstance().setApplicationConfigDir(configDir);
		AppLogger.getInstance().info(
				"ApplicationRootDir = " + new File(rootPath).getAbsolutePath());
		AppLogger.getInstance().info(
				"ApplicationConfigDir = " + configDir.getAbsolutePath());

		// rtWebConf.xmlをparseして、XML規約違反の有無を判定
		try {
			rootNode = parseConfigurationFile(rtWebConfig);
		} catch (ConfigurationException e) {
			throw new ServletException("rtWebSchema設定ファイルにXML規約違反が見つかりました", e);
		}

		// //////////////////////////////////////
		//
		// 以下、各種設定を順番に読み込む
		//
		// //////////////////////////////////////

		// JOB_IDの読み込み
		// （特注等サイト単位での処理を加える場合を考慮）
		readJobID(rootNode);

		// 起動遅延時間の読み込み
		initStartupDelay(rootNode);

		// DB接続設定の読み込み
		initTheraRISConnections(rootNode);

		// 手の位置マッピングの初期設定
		initHandPosList(rootNode);

		// 部位マッピングの初期設定
		initBuiMap(rootNode);

		// 初期デフォルト値設定の初期設定
		initDefaultMap(rootNode);

		// シェーマ画像の保存パス値設定の初期設定
		initSchemaImgNameList(rootNode);

		// 「手の位置」毎のシェーマ画像設定
		readSchemaImgPath(rootNode);

		// keyValueConfBean
		initDefaultPosList(rootNode);

		// 治療RISDBへの接続確認
		// 接続に失敗したら、設定された起動遅延時間だけ待つループに入る
		// 5回待ってもダメな場合はExceptionを投げる
		for (int i = 0; i < 5; i++) {
			Connection con = DbConnectionFactory.getInstance()
					.getRTRISDBConnection();
			if (con == null) {
				try {
					// 起動遅延(Oracleの起動に時間がかかるケースがあるため、Tomcat側で待つ)
					AppLogger.getInstance().log(AppLogger.FINE,
							"Sleeping " + m_nStartupDelay_ + "[miliSec] ...");
					Thread.sleep(m_nStartupDelay_);
				} catch (Exception e) {
				}
			} else {
				if (con != null) {
					DbConnectionFactory.getInstance().returnRTRISDBConnection(
							con);
				}
				break;
			}
		}

		//log自動削除設定
		initLogAutoDelete(rootNode);
	}

	/**
	 * rtWeb設定ファイルをparseする
	 *
	 * @param path
	 *            rtWeb設定ファイルのパス
	 * @return rtWeb設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             XML規約違反
	 */
	private Document parseConfigurationFile(File path)
			throws ConfigurationException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			return builder.parse(path);
		} catch (SAXException e) {
			throw new ConfigurationException(
					CoreMessage.ILLEGAL_CONFIG_FILE_DESCRIPTION,
					CoreMessage.ILLEGAL_CONFIG_FILE_DESCRIPTION_MSG, e);
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException(
					CoreMessage.ILLEGAL_XML_PARSER_BEHAVIOR,
					CoreMessage.ILLEGAL_XML_PARSER_BEHAVIOR_MSG, e);
		} catch (IOException e) {
			throw new ConfigurationException(CoreMessage.CONFIG_IO_ERROR,
					CoreMessage.CONFIG_IO_ERROR_MSG, e);
		}
	}

//	/**
//	 * Loggerの初期化
//	 *
//	 * @param rootNode
//	 *            設定ファイルのノード情報
//	 * @throws ConfigurationException
//	 *             ログ設定の定義違反またはLoggerの初期化失敗
//	 */
//	private void initLogger(Node rootNode) throws ConfigurationException {
//		// parameters
//		Logger logger = Logger.getInstance();
//
//		try {
//			Node loggingRootNode = XPathAPI.selectSingleNode(rootNode,
//					"/rtWebConf/logging");
//			if (loggingRootNode == null
//					|| loggingRootNode.getNodeType() != Node.ELEMENT_NODE) {
//				throw new ConfigurationException(
//						CoreMessage.LOGGING_ELEMENT_NOT_FOUND,
//						CoreMessage.LOGGING_ELEMENT_NOT_FOUND_MSG);
//			}
//
//			logger.init((Element) loggingRootNode);
//
//		} catch (TransformerException e) {
//			throw new ConfigurationException(
//					CoreMessage.XML_PROCESSING_FAILURE_ON_LOGGING_CONFIG,
//					CoreMessage.XML_PROCESSING_FAILURE_ON_LOGGING_CONFIG_MSG, e);
//		} catch (LoggerInitializeException e) {
//			throw new ConfigurationException(
//					CoreMessage.LOGGING_FUNC_CONFIG_FAILURE,
//					CoreMessage.LOGGING_FUNC_CONFIG_FAILURE_MSG, e);
//		}
//	}

	/**
	 * JOB_ID(rtWebConf/JOB_ID)の取得
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             JOB_IDの定義違反
	 */
	private void readJobID(Node rootNode) throws ConfigurationException {
		// begin log
		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.readJobID - begin");

		try {
			String strJobID = CoreUtil.getNodeValue(rootNode,
					"/rtWebSchemaConf/@JOB_ID");

			if (strJobID.length() < 1) {
				throw new ConfigurationException(60077,
						"JOB_IDには長さ1以上の文字列を指定してください");
			}

			// 取得したJOB_IDをConfigurationインスタンスに設定
			Configuration.getInstance().setJobID(strJobID);

		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "JOB IDの読込に失敗しました", e);
		} catch (NumberFormatException e) {
			throw new ConfigurationException(60077, "JOB IDの読込に失敗しました", e);
		} finally {
			AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.readJobID - end");
		}
	}

	/**
	 * 起動時の遅延時間設定（Oracleより遅れて起動しないと初期化が行なえないため）
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             遅延時間設定の定義違反
	 */
	private void initStartupDelay(Node rootNode) throws ConfigurationException {

		AppLogger.getInstance().log(AppLogger.FINE,
				"ConfigurationServlet.initStartupDelay - begin");

		try {
			m_nStartupDelay_ = Integer.parseInt(CoreUtil.getNodeValue(rootNode,
					"/rtWebSchemaConf/startupDelay/@miliSec"));

			if (m_nStartupDelay_ < 60000 || m_nStartupDelay_ > 600000) {
				throw new ConfigurationException(60077,
						"起動時遅延時間は、60000-600000[miliSec]の間で設定してください");
			}
		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "起動時遅延時間設定の読込に失敗しました", e);
		} catch (NumberFormatException e) {
			throw new ConfigurationException(60077, "起動時遅延時間設定の読込に失敗しました", e);
		} finally {
			AppLogger.getInstance().log(AppLogger.FINE,
					"ConfigurationServlet.initStartupDelay - end");
		}
	}

	/**
	 * DBへの接続設定
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             DB接続設定の定義違反
	 */
	private void initTheraRISConnections(Document rootNode)
			throws ConfigurationException {

		// RTRISDBのJDBCURL
		String strRTRISDBConnection = "";
		// RRISDBのJDBCURL
		String strRRISDBConnection = "";

		// RTRISDBのDBUser
		String strRTRISDBUser = "";
		// RRISDBのDBUser
		String strRRISDBUser = "";

		// RTRISDBのPassword
		String strRTRISDBPassword = "";
		// RRISDBのPassword
		String strRRISDBPassword = "";

		// begin log
		AppLogger.getInstance().log(AppLogger.FINE,
				"ConfigurationServlet.initTheraRISConnection - begin");

		/** JDBCURLの取得 */
		// RTRISDBのJDBCURL取得
		try {
			strRTRISDBConnection = CoreUtil.getNodeValue(rootNode,
					"/rtWebSchemaConf/DBINFO/RTRISDB/Connection/text()");

			AppLogger.getInstance().log(AppLogger.CONFIG,
					"RTRISDBConnection = " + strRTRISDBConnection);

			// Configurationに設定
			Configuration.getInstance().setRTRISDBConnection(
					strRTRISDBConnection);

		} catch (TransformerException e) {
			throw new ConfigurationException(60073, "治療RIS DB接続設定の読込に失敗しました", e);
		}


		/** DBUserの取得 */
		// RTRISのDBUser
		try {
			strRTRISDBUser = CoreUtil.getNodeValue(rootNode,
					"/rtWebSchemaConf/DBINFO/RTRISDB/User/text()");

			AppLogger.getInstance().log(AppLogger.CONFIG, "RTRISDBUser = " + strRTRISDBUser);
			Configuration.getInstance().setRTRISDBUser(strRTRISDBUser);
		} catch (TransformerException e) {
			throw new ConfigurationException(60074,
					"治療RIS DB接続ユーザ設定の読込に失敗しました", e);
		}
		/** DBPasswordの取得 */
		// RTRISのDBPassword
		try {
			strRTRISDBPassword = CoreUtil.getNodeValue(rootNode,
					"/rtWebSchemaConf/DBINFO/RTRISDB/Password/text()");

			AppLogger.getInstance().log(AppLogger.CONFIG,
					"RTRISDBPassword = " + strRTRISDBPassword);
			Configuration.getInstance().setRTRISDBPassword(strRTRISDBPassword);
		} catch (TransformerException e) {
			throw new ConfigurationException(60075,
					"治療RIS DB接続ユーザパスワード設定の読込に失敗しました", e);
		}

		// 最大取得件数
		try {
			int maxResults = Integer.parseInt(CoreUtil.getNodeValue(rootNode,
					"/rtWebSchemaConf/DBINFO/MaxResults/text()"));

			if (maxResults < 1) {
				throw new ConfigurationException(60075,
						"検索結果の最大取得件数は、1件以上に設定してください");
			}

			AppLogger.getInstance().log(AppLogger.CONFIG, "MaxResults = " + maxResults);
			Configuration.getInstance().setRTRISMaxResults(
					Integer.toString(maxResults));
		} catch (TransformerException e) {
			throw new ConfigurationException(60075, "検索最大取得件数設定の読込に失敗しました", e);
		} catch (NumberFormatException e) {
			throw new ConfigurationException(60075, "検索最大取得件数設定の読込に失敗しました", e);
		}
		AppLogger.getInstance().log(AppLogger.FINE,
				"ConfigurationServlet.initTheraRISConnection - end");
	}

	/**
	 * ログの自動削除設定
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             ログの自動削除設定の定義違反
	 */
	private void initLogAutoDelete(Document rootNode)
			throws ConfigurationException {
		// begin log
		AppLogger.getInstance().log(AppLogger.FINE,
				"ConfigurationServlet.initLogAutoDelete - begin");

		try {
			String strAutoDeleteFlg = CoreUtil.getNodeValue(rootNode,
					"/rtWebSchemaConf/logging/appender/autoDelete/@active")
					.toUpperCase();

			int nLogStorageMonths = Integer
					.parseInt(
							CoreUtil.getNodeValue(rootNode,
									"/rtWebSchemaConf/logging/appender/autoDelete/logStorageMonths/text()"),
							10);

			if (nLogStorageMonths < 1 || nLogStorageMonths > 36) {
				throw new ConfigurationException(60077,
						"ログファイルの保存月数は、1-36ヶ月の期間で設定してください");
			}

			String strLogFileDir = AppLogger.getLogFilePath();

			if (strAutoDeleteFlg.compareToIgnoreCase("Y") == 0) {
				AppLogger.getInstance().log(AppLogger.CONFIG, "LogAutoDelete is [ON]");

				m_logAutoDelete_ = LogFileDelete.getInstance();
				if (m_logAutoDelete_.init(strLogFileDir, nLogStorageMonths)) {
					m_logAutoDelete_.start();
				}
			} else {
				AppLogger.getInstance().log(AppLogger.CONFIG, "LogAutoDelete is [OFF]");
			}
		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "ログ自動削除設定の読込に失敗しました", e);
		} catch (NumberFormatException e) {
			throw new ConfigurationException(60077, "ログ自動削除設定の読込に失敗しました", e);
		} catch (Exception e) {
			throw new ConfigurationException(60077, "ログ自動削除設定に失敗しました", e);
		}

		// end log
		AppLogger.getInstance().log(AppLogger.FINE,
				"ConfigurationServlet.initLogAutoDelete - end");
	}
	/**
	 * 手の位置マッピング設定
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             入外区分の定義違反
	 */
	private void initHandPosList(Node rootNode) throws ConfigurationException {
		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initHandPosList - begin");

		try {
			NodeList handPosNodeList = XPathAPI.selectNodeList(rootNode,
					HANDPOS_NODE);

			for (int i = 0; i < handPosNodeList.getLength(); i++) {
				// 手の位置マッピングのエレメントを取得
				Element handPosElement = (Element) (handPosNodeList.item(i));
				// 入外区分ID取得
				String handPosId = new String(
						(handPosElement.getAttribute(HANDPOS_ID))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 入外区分名取得
				String handPosKey = new String(
						(handPosElement.getAttribute(HANDPOS_KEY))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 使用可否フラグ取得
				String handPosFlg = new String(
						(handPosElement.getAttribute(USE_FLG))
								.getBytes(XML_ENCODING),
						XML_ENCODING);

				if (handPosKey != null && handPosKey.length() > 0) {
					KeyValueConfBean keyValueConfBean = new KeyValueConfBean();
					// 入外区分にID、表示名、使用可否フラグを設定
					keyValueConfBean.setId(handPosId);
					keyValueConfBean.setName(handPosKey);
					if (USE_FLG_Y.equals(handPosFlg)) {
						// useFlgが"Y"だった場合使用フラグをtrueに
						keyValueConfBean.setUseFlg(true);
					} else {
						keyValueConfBean.setUseFlg(false);
					}

					AppLogger.getInstance().log(AppLogger.CONFIG,
							"nyugaiKbnSet [" + HANDPOS_ID + ":" + handPosId + " "
									+ HANDPOS_KEY + ":" + handPosKey + " "
									+ USE_FLG + ":" + handPosFlg + "]");
					if (USE_FLG_Y.equals(handPosFlg)) {
						// useFlgが"Y"だった場合性別IDと性別和名をConfigurationに設定
					Configuration.getInstance().setHandPosMap(handPosKey,
							keyValueConfBean);
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException(60077, "手の位置マッピングの読み込みに失敗しました", e);
		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "手の位置マッピングの読み込みに失敗しました", e);
		}

		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initNyugaiKbn - end");
	}
	/**
	 * 文言タイトル設定
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             文言タイトルの定義違反
	 */
	private void initBuiMap(Node rootNode) throws ConfigurationException {
		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initBuiMap - begin");

		try {
			NodeList buiNodeList = XPathAPI.selectNodeList(rootNode,
					BUI_NODE);

			for (int i = 0; i < buiNodeList.getLength(); i++) {
				// 入外区分のエレメントを取得
				Element buiElement = (Element) (buiNodeList.item(i));
				// 入外区分ID取得
				String buiID = new String(
						(buiElement.getAttribute(BUI_ID))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 入外区分名取得
				String buiKey = new String(
						(buiElement.getAttribute(BUI_KEY))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 使用可否フラグ取得
				String buieUseFlg = new String(
						(buiElement.getAttribute(USE_FLG))
								.getBytes(XML_ENCODING),
						XML_ENCODING);

				if (buiKey != null && buiKey.length() > 0) {
					KeyValueConfBean keyValueConfBean = new KeyValueConfBean();
					// 入外区分にID、表示名、使用可否フラグを設定
					keyValueConfBean.setId(buiID);
					keyValueConfBean.setName(buiKey);
					if (USE_FLG_Y.equals(buieUseFlg)) {
						// useFlgが"Y"だった場合使用フラグをtrueに
						keyValueConfBean.setUseFlg(true);
					} else {
						keyValueConfBean.setUseFlg(false);
					}

					AppLogger.getInstance().log(AppLogger.CONFIG,
							"titleKbnSet [" + BUI_ID + ":" + buiID + " "
									+ BUI_KEY + ":" + buiKey + " "
									+ USE_FLG + ":" + buieUseFlg + "]");
					if (USE_FLG_Y.equals(buieUseFlg)) {
						// useFlgが"Y"だった場合性別IDと性別和名をConfigurationに設定
					Configuration.getInstance().setBuiMap(buiKey,
							keyValueConfBean);
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException(60077, "部位マッピングの読み込みに失敗しました", e);
		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "部位マッピングの読み込みに失敗しました", e);
		}

		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initBuiMap - end");
	}

	private void initDefaultMap(Node rootNode) throws ConfigurationException {
		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initDefaultMap - begin");

		try {
			NodeList defaultNodeList = XPathAPI.selectNodeList(rootNode, DEFAULT_NODE);

			for (int i = 0; i < defaultNodeList.getLength(); i++) {
				// 入外区分のエレメントを取得
				Element defaultElement = (Element) (defaultNodeList.item(i));
				// 入外区分ID取得
				String defaultID = new String(
						(defaultElement.getAttribute(DEFAULT_ID))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 入外区分名取得
				String defaultName = new String(
						(defaultElement.getAttribute(DEFAULT_VAL))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 使用可否フラグ取得
				String defaultUseFlg = new String(
						(defaultElement.getAttribute(USE_FLG))
								.getBytes(XML_ENCODING),
						XML_ENCODING);

				if (defaultID != null && defaultID.length() > 0) {
					KeyValueConfBean keyValueConfBean = new KeyValueConfBean();
					keyValueConfBean.setId(defaultID);
					keyValueConfBean.setName(defaultName);
					if (USE_FLG_Y.equals(defaultUseFlg)) {
						// useFlgが"Y"だった場合使用フラグをtrueに
						keyValueConfBean.setUseFlg(true);
					} else {
						keyValueConfBean.setUseFlg(false);
					}
					AppLogger.getInstance().log(AppLogger.CONFIG,
							"sexNameSet [" + DEFAULT_ID + ":" + defaultID + " "
									+ DEFAULT_VAL + ":" + defaultName + " " + USE_FLG
									+ ":" + defaultUseFlg + "]");
					if (USE_FLG_Y.equals(defaultUseFlg)) {
						// useFlgが"Y"だった場合性別IDと性別和名をConfigurationに設定
						Configuration.getInstance().setDefaultMap(defaultID, keyValueConfBean);
					}

				}
			}

		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException(60077, "初期デフォルト値の読み込みに失敗しました", e);
		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "初期デフォルト値の読み込みに失敗しました", e);
		}

		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initSex - end");
	}

	/**
	 * JOB_ID(rtWebConf/JOB_ID)の取得
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             JOB_IDの定義違反
	 */
	private void readSchemaImgPath(Node rootNode) throws ConfigurationException {
		// begin log
		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.readSchemaImgPath - begin");

		try {
			String schemaImgPath = CoreUtil.getNodeValue(rootNode,
					SCHEMA_PATH_NODE);

			if (schemaImgPath.length() < 1) {
				throw new ConfigurationException(60077,
						"schemaImgPathには長さ1以上の文字列を指定してください");
			}

			// 取得したJOB_IDをConfigurationインスタンスに設定
			Configuration.getInstance().setSchemaImgPath(schemaImgPath);

		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "JOB IDの読込に失敗しました", e);
		} catch (NumberFormatException e) {
			throw new ConfigurationException(60077, "JOB IDの読込に失敗しました", e);
		} finally {
			AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.readJobID - end");
		}
	}
	/**
	 * 手の位置マッピング設定
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             入外区分の定義違反
	 */
	private void initSchemaImgNameList(Node rootNode) throws ConfigurationException {
		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initHandPosList - begin");

		try {
			NodeList schemaImgNameList = XPathAPI.selectNodeList(rootNode,
					SCHEMA_NAME_NODE);

			for (int i = 0; i < schemaImgNameList.getLength(); i++) {
				// 手の位置マッピングのエレメントを取得
				Element schemaImgNameElement = (Element) (schemaImgNameList.item(i));
				// 入外区分ID取得
				String schemaImgId = new String(
						(schemaImgNameElement.getAttribute(HANDPOS_ID))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 入外区分名取得
				String schemaImgName = new String(
						(schemaImgNameElement.getAttribute(FILE_NAME))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 使用可否フラグ取得
				String schemaImgNameUseFlg = new String(
						(schemaImgNameElement.getAttribute(USE_FLG))
								.getBytes(XML_ENCODING),
						XML_ENCODING);

				if (schemaImgName != null && schemaImgName.length() > 0) {
					KeyValueConfBean keyValueConfBean = new KeyValueConfBean();
					// 入外区分にID、表示名、使用可否フラグを設定
					keyValueConfBean.setId(schemaImgId);
					keyValueConfBean.setName(schemaImgName);
					if (USE_FLG_Y.equals(schemaImgNameUseFlg)) {
						// useFlgが"Y"だった場合使用フラグをtrueに
						keyValueConfBean.setUseFlg(true);
					} else {
						keyValueConfBean.setUseFlg(false);
					}

					AppLogger.getInstance().log(AppLogger.CONFIG,
							"nyugaiKbnSet [" + HANDPOS_ID + ":" + schemaImgId + " "
									+ FILE_NAME + ":" + schemaImgName + " "
									+ USE_FLG + ":" + schemaImgNameUseFlg + "]");

					if (USE_FLG_Y.equals(schemaImgNameUseFlg)) {
					// Configurationに設定
					Configuration.getInstance().setSchemaImgNameMap(schemaImgId,
							keyValueConfBean);
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException(60077, "シェーマ画像設定の読み込みに失敗しました", e);
		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "シェーマ画像設定の読み込みに失敗しました", e);
		}

		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initNyugaiKbn - end");
	}
	/**
	 * 手の位置マッピング設定
	 *
	 * @param rootNode
	 *            設定ファイルのノード情報
	 * @throws ConfigurationException
	 *             入外区分の定義違反
	 */
	private void initDefaultPosList(Node rootNode) throws ConfigurationException {
		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initHandPosList - begin");

		try {
			NodeList defaultPosList = XPathAPI.selectNodeList(rootNode,
					DEFAULT_POS_NODE);

			for (int i = 0; i < defaultPosList.getLength(); i++) {
				// 手の位置マッピングのエレメントを取得
				Element defaultPosElement = (Element) (defaultPosList.item(i));
				// 入外区分ID取得
				String defaultPosListId = new String(
						(defaultPosElement.getAttribute(BUI_ID))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 入外区分名取得
				String defaultPosListStartX = new String(
						(defaultPosElement.getAttribute(DEFAULT_START_X))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 入外区分名取得
				String defaultPosListStartY = new String(
						(defaultPosElement.getAttribute(DEFAULT_START_Y))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 入外区分名取得
				String defaultPosListEndX = new String(
						(defaultPosElement.getAttribute(DEFAULT_END_X))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 入外区分名取得
				String defaultPosListEndY = new String(
						(defaultPosElement.getAttribute(DEFAULT_END_Y))
								.getBytes(XML_ENCODING),
						XML_ENCODING);
				// 使用可否フラグ取得
				String defaultPosListFlg = new String(
						(defaultPosElement.getAttribute(USE_FLG))
								.getBytes(XML_ENCODING),
						XML_ENCODING);

				if ((defaultPosListStartX != null && defaultPosListStartX.length() > 0)
						&&(defaultPosListStartY != null && defaultPosListStartY.length() > 0)
						&&(defaultPosListEndX != null && defaultPosListStartY.length() > 0)
						&&(defaultPosListEndY != null && defaultPosListEndY.length() > 0)
						) {
					BuiDefaultPosConfBean buiDefaultPosConfBean = new BuiDefaultPosConfBean();
					// 入外区分にID、表示名、使用可否フラグを設定
					buiDefaultPosConfBean.setStartX(new BigDecimal(defaultPosListStartX));
					buiDefaultPosConfBean.setStartY(new BigDecimal(defaultPosListStartY));
					buiDefaultPosConfBean.setEndX(new BigDecimal(defaultPosListEndX));
					buiDefaultPosConfBean.setEndY(new BigDecimal(defaultPosListEndY));
					if (USE_FLG_Y.equals(defaultPosListFlg)) {
						// useFlgが"Y"だった場合使用フラグをtrueに
						buiDefaultPosConfBean.setUseFlg(true);
					} else {
						buiDefaultPosConfBean.setUseFlg(false);
					}

					AppLogger.getInstance().log(AppLogger.CONFIG,
							"nyugaiKbnSet [" + BUI_ID + ":" + defaultPosListId + " "
									+ DEFAULT_START_X + ":" + defaultPosListStartX + " "
									+ DEFAULT_START_Y + ":" + defaultPosListStartY + " "
									+ DEFAULT_END_X + ":" + defaultPosListEndX + " "
									+ DEFAULT_END_Y + ":" + defaultPosListEndY + " "
									+ USE_FLG + ":" + defaultPosListFlg + "]");
					if (USE_FLG_Y.equals(defaultPosListFlg)) {

					// Configurationに設定
					Configuration.getInstance().setDefaultPosConfBeanMap(defaultPosListId,
							buiDefaultPosConfBean);
					}
				}
			}

		} catch (UnsupportedEncodingException e) {
			throw new ConfigurationException(60077, "手の位置マッピングの読み込みに失敗しました", e);
		} catch (TransformerException e) {
			throw new ConfigurationException(60077, "手の位置マッピングの読み込みに失敗しました", e);
		}

		AppLogger.getInstance().log(AppLogger.FINE,"ConfigurationServlet.initNyugaiKbn - end");
	}
}
