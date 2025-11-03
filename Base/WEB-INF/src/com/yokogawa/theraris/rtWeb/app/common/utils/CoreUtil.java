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
package com.yokogawa.theraris.rtWeb.app.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Node;

import com.yokogawa.theraris.logger.AppLogger;

public class CoreUtil
{
	/**
	 * 与えられたノードから任意のXPath式で得られるノードの値を取得する
	 *
	 * @param node 任意のDOMのノード
	 * @param xpath XPath式
	 * @return XPath式を評価して得られた値を返す．String.trim()によって
	 * 除去できる空白や，XPath式に一致するノードが無ければ長さ0の文字列を返す．
	 * @throws TransformerException
	 */
	public static String getNodeValue(Node node, String xpath)
		throws TransformerException {
		Node _node = XPathAPI.selectSingleNode(node, xpath);
		if (_node == null) {
			return "";
		}
		if (_node.getNodeValue() == null
			|| _node.getNodeValue().trim().length() == 0) {
			return "";
		}
		return _node.getNodeValue().trim();
	}
//
	/**
	 * テキストの暗黙定義エンティティへの置換</br>
	 * テキスト中の&amp;, &lt;, &gt;, &apos;, &quot; を
	 * XMLの暗黙定義エンティティに置換します．
	 *
	 * @param text XML要素のテキスト値
	 * @return 置換後の文字列
	 */
	static public String escapeXML(String text) {
		// 2019.01.31 Add MERX / K.Mitomi Start For 3S1559
		if (text == null) return "";
		// 2019.01.31 Add MERX / K.Mitomi End For 3S1559
		StringBuffer buf = new StringBuffer(text.length());
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == '&') {
				buf.append("&amp;");
			} else if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else if (ch == '\'') {
				buf.append("&apos;");
			} else if (ch == '"') {
				buf.append("&quot;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}
//
	/**
	 * テキストの暗黙定義エンティティへの置換</br>
	 * テキスト中の&amp;, &lt;, &gt;, &apos;, &quot を
	 * HTMLの暗黙定義エンティティに置換します．
	 *
	 * @param text XML要素のテキスト値
	 * @return 置換後の文字列
	 */
	static public String escapeHTML( String text )
	{
		// 2019.01.31 Add MERX / K.Mitomi Start For 3S1559
		if (text == null) return "";
		// 2019.01.31 Add MERX / K.Mitomi End For 3S1559
		StringBuffer buf = new StringBuffer( text.length() );
		for( int i = 0; i < text.length(); i++ ){
			char ch = text.charAt( i );
			if( ch == '&' ){
				buf.append( "&amp;" );
			}
			else if( ch == '<'){
				buf.append( "&lt;" );
			}
			else if( ch == '>' ){
				buf.append( "&gt;" );
			}
			else {
				buf.append( ch );
			}
		}
		return buf.toString();
	}
//
	/**
	 * SQL文用のエスケープを行う</br>
	 * テキストリテラルの開始と終了を意味する一重引用符をエスケープします．
	 * パラメータの文字列にテキストリテラルの開始と終了の一重引用符を
	 * 含めてはいけません．
	 *
	 * @param str SQL文のテキストリテラルとして使う文字列
	 * @return エスケープされた文字列
	 */
	static public String escapeSQL(final String str) {
		int length = str.length();
		StringBuffer buf = new StringBuffer(length);
		char c;
		for (int i = 0; i < length; i++) {
			c = str.charAt(i);
			if (c == '\'') {
				buf.append('\'');
			}
			buf.append(c);
		}
		return buf.toString();
	}
//
	/**
	 * SQL文用のエスケープを行う</br>
	 * テキストリテラルの開始と終了を意味する一重引用符をエスケープします．
	 * パラメータの文字列にテキストリテラルの開始と終了の一重引用符を
	 * 含めてはいけません．
	 *
	 * @param str SQL文のテキストリテラルとして使う文字列
	 * @return エスケープされた文字列
	 */
	static public String escapeGarbledValue(final String str) {
		String retStr = "";

		if( str != null ){
			retStr = str.replace('\u301c', '\uff5e');
		}

		return retStr;
	}
	//
/**
	 * nullを空文字に変換する
	 * @param str 変換元文字列
	 * @return null-->"" null以外-->そのまま
	 */
	public static String escapeNull(String str) {
		if (str == null) {
			return "";
		} else {
			return str;
		}
	}
//
	/**
	 * 指定文字列を指定サイズで出力する(数字の文字列表現の出力等)<br>
	 * 右詰で余りは指定文字で埋められる<br>
	 * 文字列が大きい場合は左側から消去する<br>
	 * 例)toPaddingLeft("1", 4, '0')→"0001"<br>
	 * @param nums 文字列変換する数字
	 * @param length 出力する文字列長
	 * @param ch 埋める文字
	 * @return String 変換した文字列
	 */
	public static String toPaddingLeft(String nums, int length, char ch) {
		if (nums.length() > length) {
			return nums.substring(nums.length() - length, nums.length());
		}else {
			while (nums.length() < length) {
				nums = ch + nums;
			}
			return nums;
		}
	}
//
	/**
	 * 指定文字列を指定サイズで出力する(文字情報の出力等)<br>
	 * 左詰で余りは指定文字で埋められる<br>
	 * 文字列が大きい場合は右側から消去する<br>
	 * 例)toPaddingRight("1", 4, '0')→"4000"<br>
	 * @param s 対象文字列
	 * @param length 出力する文字長
	 * @param ch 埋める文字
	 * @return String 変換した文字列
	 */
	public static String toPaddingRight(String s, int length, char ch) {
		if (s.length() > length) {
			return s.substring(0, length);
		}else {
			while (s.length() < length) {
				s = s + ch;
			}
			return s;
		}
	}

	/**
	 * int型の数値を文字列に変換し指定サイズで出力する(文字情報の出力等)<br>
	 * 右詰で余りは指定文字で埋められる<br>
	 * int型の数値が大きい場合はそのまま返却する<br>
	 * 例)toPaddingLeft(1, 4, '0')→"0001"<br>
	 * @param i 対象数値
	 * @param length 出力する文字列長
	 * @param ch 埋める文字
	 * @return 変換した文字列
	 */
	public static String toPaddingLeft(int i, int length, char ch) {
		String intStr = Integer.toString(i);
		if (intStr.length() > length) {
			return intStr;
		}else {
			while (intStr.length() < length) {
				intStr = ch + intStr;
			}
			return intStr;
		}
	}
//
	/**
	 * FATALレベルのログを出力する．HTTPリクエスト処理を受け付けるような場面で使う．
	 *
	 * @param request HTTPリクエスト
	 * @param methodName エラーが処理をしたメソッド名
	 * @param errorCode エラーコード
	 * @param errorMessage エラーメッセージ
	 * @param exception 例外（null値でもかまわない）
	 */
	public static void logFatal(
		HttpServletRequest request,
		String methodName,
		int errorCode,
		String errorMessage,
		Throwable exception) {
		AppLogger.getInstance().fatal(
			request.getRemoteAddr()
				+ ": "
				+ request.getRequestURI()
				+ " ["
				+ errorCode
				+ "] "
				+ errorMessage
				+ ": "
				+ methodName,
			exception);
	}
//
	/**
	 * FATALレベルのログを出力する．
	 *
	 * @param methodName エラーが処理をしたメソッド名
	 * @param errorCode エラーコード
	 * @param errorMessage エラーメッセージ
	 * @param exception 例外（null値でもかまわない）
	 */
	public static void logFatal(
		String methodName,
		int errorCode,
		String errorMessage,
		Throwable exception) {
		AppLogger.getInstance().fatal(
			"[" + errorCode + "] " + errorMessage + ":" + methodName,
			exception);
	}
	//2014.07.18 Add Y.Kobayashi@MERX Start For A2917
//
	/**
	 * FATALレベルのログを出力する．HTTPリクエスト処理を受け付けるような場面で使う．
	 *
	 * @param request HTTPリクエスト
	 * @param methodName エラーが処理をしたメソッド名
	 * @param errorCode エラーコード
	 * @param errorMessage エラーメッセージ
	 * @param exception 例外（null値でもかまわない）
	 */
	public static void logFatal(
		HttpServletRequest request,
		String methodName,
		String errorMessage,
		Throwable exception )
	{
		AppLogger.getInstance().fatal(
			request.getRemoteAddr()
				+ ": "
				+ request.getRequestURI()
				+ " "
				+ errorMessage
				+ ": "
				+ methodName,
			exception);
	}
	//2014.07.18 Add Y.Kobayashi@MERX End For A2917
//
	/**
	 * WARNINGレベルのログを出力する．HTTPリクエストを処理するような場面で利用する．
	 *
	 * @param request HTTPリクエスト
	 * @param methodName エラーを処理したメソッド名
	 * @param errorCode エラーコード
	 * @param errorMessage エラーメッセージ
	 * @param exception 例外（null値でもかまわない）
	 */
	public static void logWarning(
		HttpServletRequest request,
		String methodName,
		int errorCode,
		String errorMessage,
		Throwable exception) {
		AppLogger.getInstance().warn(
			request.getRemoteAddr()
				+ ": "
				+ request.getRequestURI()
				+ " ["
				+ errorCode
				+ "] "
				+ errorMessage
				+ ": "
				+ methodName,
			exception);
	}
}
