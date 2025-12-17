package com.yokogawa.theraris.rtWeb.app.common.constants;
/**
 * パラメータ及び遷移パス文字列定義
 * @author cosmo
 *
 */
public class Parameters {

	/** 画面遷移用フィールド   */
	//登録用のjsp
	public static final String DISPATCHPATH_LOGIN = "/jsp/schema.jsp";
	//引用用のjsp
	public static final String DISPATCHPATH_QUOTE = "/jsp/quote.jsp";
	//mod 2025/12 yamagishi URLパラメータ変更対応 start
//	// 呼び出しパラメータオーダーNO
//	public static final String PARM_UID = "UID";
//	// 呼び出しパラメータ依頼医No
//	public static final String PARM_BUI = "BUI";
	// 呼び出しパラメータ患者ID
	public static final String PARM_KANJA_ID = "KANJAID";
//	// 呼び出しパラメータ依頼科コード
//	public static final String PARM_HANDPOS = "HANDPOS";
	// 呼び出しパラメータ検索用オーダーNO
	public static final String PARM_REF_FLAG = "REF";
	// 呼び出しパラメータRISID
	public static final String PARM_RIS_ID = "RISID";
	// 呼び出しパラメータユーザID
	public static final String PARM_USER_ID = "USRID";
	//mod 2025/12 yamagishi URLパラメータ変更対応 end

}
