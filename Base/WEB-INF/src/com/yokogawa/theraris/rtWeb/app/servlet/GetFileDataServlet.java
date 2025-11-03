package com.yokogawa.theraris.rtWeb.app.servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yokogawa.theraris.logger.AppLogger;
import com.yokogawa.theraris.rtWeb.app.common.constants.ServletMessage;
import com.yokogawa.theraris.rtWeb.app.common.utils.CoreUtil;
import com.yokogawa.theraris.rtWeb.app.logic.RtWebServiceLogic;

public class GetFileDataServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		AppLogger.getInstance().log(AppLogger.FINE,"RestServlet.doGet() - begin");
		RtWebServiceLogic rtWebServiceLogic = new RtWebServiceLogic();
		try {
			rtWebServiceLogic.getRest(request,response);
		} catch (Throwable e) {
			CoreUtil.logFatal(
					request, "GetFileDataServlet#doGet()",
					ServletMessage.UNEXPECTED_EXCEPTION,
					ServletMessage.UNEXPECTED_EXCEPTION_MSG,
					e );
			throw new ServletException(
				ServletMessage.UNEXPECTED_EXCEPTION_MSG,
				e );
		} finally {
			AppLogger.getInstance().log(AppLogger.FINE,"GetFileDataServlet.doGet() - end");
		}
	}
}
