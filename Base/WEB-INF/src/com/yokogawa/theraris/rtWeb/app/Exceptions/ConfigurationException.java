//*************************************************************************
// Copyright (c) 2002 by Yokogawa Electric Corporation.
//
// $RCSfile$
// $Date$
// $Revision$
// $Author$
// Description:
// 更新履歴
//
//*************************************************************************
package com.yokogawa.theraris.rtWeb.app.Exceptions;


/**
 * @author hironori
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
public class ConfigurationException extends ApplicationException {

	public ConfigurationException( int errorCode, String message ){
		super( errorCode, message );
	}

	public ConfigurationException( String message, Throwable exp ){
		super( message, exp );
	}

	public ConfigurationException( int errorCode, String message, Throwable exp ){
		super( errorCode, message, exp );
	}
}
