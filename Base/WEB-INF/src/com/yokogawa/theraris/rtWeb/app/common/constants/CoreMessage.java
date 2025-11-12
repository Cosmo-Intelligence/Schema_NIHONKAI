package com.yokogawa.theraris.rtWeb.app.common.constants;
/**
 * コンフィグ時のエラーメッセージ
 * @author cosmo
 *
 */
final public class CoreMessage {
	final static public int CONFIG_FAILURE = 60013;
	final static public String CONFIG_FAILURE_MSG = "システムの初期設定に失敗しました";

	final static public int ILLEGAL_CONFIG_FILE_DESCRIPTION = 60014;
	final static public String ILLEGAL_CONFIG_FILE_DESCRIPTION_MSG =
		"設定ファイルの記述に問題があります";

	final static public int ILLEGAL_XML_PARSER_BEHAVIOR = 60015;
	final static public String ILLEGAL_XML_PARSER_BEHAVIOR_MSG =
		"XMLパーサの動作に問題があります";

	final static public int CONFIG_IO_ERROR = 60016;
	final static public String CONFIG_IO_ERROR_MSG = "入出力に障害が発生しました";

	final static public int LOGGING_ELEMENT_NOT_FOUND = 60017;
	final static public String LOGGING_ELEMENT_NOT_FOUND_MSG = "ロギングの設定見つかりません";

	final static public int LOGGING_FUNC_CONFIG_FAILURE = 60019;
	final static public String LOGGING_FUNC_CONFIG_FAILURE_MSG = "ロギング機能の設定に失敗";


	final static public int XML_PROCESSING_FAILURE_ON_LOGGING_CONFIG = 60018;
	final static public String XML_PROCESSING_FAILURE_ON_LOGGING_CONFIG_MSG =
		"ロギング機能の設定中にXML処理に失敗";

	final static public int UAA_INSTANCE_CANNOT_CREATE = 60029;
	final static public String UAA_INSTANCE_CANNOT_CREATE_MSG =
		"インスタンス化に失敗しました";

	final static public int UAA_CONFIGURATION_FAILURE = 60031;
	final static public String UAA_CONFIGURATION_FAILURE_MSG = "初期設定処理に失敗";
}
