package com.yokogawa.theraris.rtWeb.app.Exceptions;


import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * フレームワークやアプリケーションが作成する例外の基本クラス．</br>
 * アプリケーションはこの例外クラスを拡張してユーザ定義例外を作成する．</br>
 *
 * <pre>
 * 例えば，この例外を拡張したConfigurationExceptionというサブクラスの例外を
 *
 *	 throw new ConfigurationException( <message_string>, <error-code> );
 *
 * のように投げた場合，
 *
 *	 ConfigurationException: <exception-id>: <error-code>: <message-string>
 *	  at Main.test2(Main.java:31)
 *	  at Main.test(Main.java:27)
 * 	  at Main.main(Main.java:19)
 *
 * といった感じで，スタックのトレースが表示されます．
 * <exception-id> とは，例外インスタンスに一意な識別子を割り当てた値です．
 * 入れ子の例外がある場合には，さらにその例外のスタックトレースも行われます．
 * </pre>
 */

public class ApplicationException extends Exception {

	static final public int NO_ERROR_CODE = -1;

	private int 	 m_errorCode = 0;
	private boolean  m_isLogged = false;
	private String	  m_id		 = null;
	private Throwable m_cause	 = null;

	/**
	 * メッセージのデフォルト値は， Exception caused in application である．
	 */
	public ApplicationException(){
		super( "Exception caused in application" );
		m_id = createID();
		m_errorCode = NO_ERROR_CODE;
	}

	/**
	 * @param message 例外のメッセージ
	 */
	public ApplicationException( String message ){
		super( message );
		m_id = createID();
		m_errorCode = NO_ERROR_CODE;
	}

	/**
	 * JavaAPI等から受け取った例外をラッピングする
	 *
	 * @param message 例外のメッセージ
	 * @param cause 入れ子となる例外インスタンス
	 */
	public ApplicationException( String message, Throwable cause ){
		super( message );
		m_id = createID();
		m_cause = cause;
		m_errorCode = NO_ERROR_CODE;
	}

	/**
	 * エラーコード付
	 *
	 * @param errorCode エラーコード
	 * @param message 例外のメッセージ
	 */
	public ApplicationException( int errorCode, String message ){
		super( message );
		m_id = createID();
		m_errorCode = errorCode;
	}

	/**
	 * JavaAPI等から受け取った例外をラッピングする
	 * エラーコードを指定する
	 *
	 * @param errorCode エラーコード値
	 * @param message 例外のメッセージ
	 * @param cause 入れ子となる例外インスタンス
	 */
	public ApplicationException( int errorCode, String message, Throwable cause ){
		super( message );
		m_id = createID();
		m_cause = cause;
		m_errorCode = errorCode;
	}

	public Throwable initCause( Throwable cause ){
		m_cause = cause;
		return m_cause;
	}

	public Throwable getCause(){
		return m_cause;
	}

	public void setLogged( boolean logged ){
		m_isLogged = logged;
	}

	public boolean isLogged(){
		return m_isLogged;
	}

	public String getId(){
		return m_id;
	}

	public int getErrorCode(){
		return m_errorCode;
	}

	public String getMessage() {
		StringBuffer buf = new StringBuffer();
		buf.append( m_id ).append( ": ");
		if( m_errorCode != NO_ERROR_CODE ){
			buf.append( m_errorCode ).append( ": " );
		}
		if( super.getMessage() != null ){
			buf.append( super.getMessage() );
		}
		return buf.toString();
	}

	public void printStackTrace() {
		this.printStackTrace( System.err );
	}

	public void printStackTrace( PrintStream s ){
		super.printStackTrace( s );
		if( this.getCause() == null ){
			return;
		}
		s.print( "Caused by: " );
		this.getCause().printStackTrace( s );
	}

	public void printStackTrace( PrintWriter w ){
		super.printStackTrace( w );
		if( this.getCause() == null ){
			return;
		}
		w.print( "Caused by: " );
		this.getCause().printStackTrace( w );
	}

	/**
	 * オブジェクトのHashCodeと現在時刻を加算した値を識別子とする
	 * @return 16進数の識別子
	 */
	private String createID(){
		return Long.toHexString( System.identityHashCode( this ) + System.currentTimeMillis() );
	}
}
