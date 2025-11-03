package com.yokogawa.theraris.rtWeb.app.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yokogawa.theraris.logger.AppLogger;
import com.yokogawa.theraris.rtWeb.app.common.constants.Parameters;
import com.yokogawa.theraris.rtWeb.app.common.constants.ServletMessage;
import com.yokogawa.theraris.rtWeb.app.common.utils.CoreUtil;
import com.yokogawa.theraris.rtWeb.app.logic.RtWebServiceLogic;

/**
 * 初期アクセスフィルタクラス
 * @author cosmo
 *
 */
public class WebSchemaFilter implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// start log
		AppLogger.getInstance().log(AppLogger.FINE,"WebTreatmentFilter.doFilter() - begin");

		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		//データの取得
		RtWebServiceLogic rtWebServiceLogic = new RtWebServiceLogic();
		try{
			rtWebServiceLogic.search(req,res);
			request.getRequestDispatcher(Parameters.DISPATCHPATH_LOGIN).forward(req, res);
			return;
		} catch (Throwable e) {
			CoreUtil.logFatal(
					req, "AuthenticationFilter#doFilter()",
					ServletMessage.UNEXPECTED_EXCEPTION,
					ServletMessage.UNEXPECTED_EXCEPTION_MSG,
					e );
				throw new ServletException(
					ServletMessage.UNEXPECTED_EXCEPTION_MSG,
					e );
		} finally {
			AppLogger.getInstance().log(AppLogger.FINE,"WebTreatmentFilter.doFilter() - end");
		}
	}
	public void destroy() {
	}

}
