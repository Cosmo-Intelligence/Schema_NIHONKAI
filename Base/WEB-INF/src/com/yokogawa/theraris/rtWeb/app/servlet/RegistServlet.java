package com.yokogawa.theraris.rtWeb.app.servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yokogawa.theraris.logger.AppLogger;
import com.yokogawa.theraris.rtWeb.app.common.constants.Parameters;
import com.yokogawa.theraris.rtWeb.app.common.constants.ServletMessage;
import com.yokogawa.theraris.rtWeb.app.common.utils.CoreUtil;
import com.yokogawa.theraris.rtWeb.app.logic.RtWebServiceLogic;

public class RegistServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		// start log
		AppLogger.getInstance().log(AppLogger.FINE,"RegistServlet.doPost() - begin");
		RtWebServiceLogic rtWebServiceLogic = new RtWebServiceLogic();
		try {
			rtWebServiceLogic.check(request, response);
			//ÉfÅ[É^ì«Ç›çûÇ›
			rtWebServiceLogic.reload(request, response);
			request.getRequestDispatcher(Parameters.DISPATCHPATH_LOGIN).forward(request, response);
		} catch (Throwable e) {
			CoreUtil.logFatal(
					request, "RegistServlet#doPost()",
					ServletMessage.UNEXPECTED_EXCEPTION,
					ServletMessage.UNEXPECTED_EXCEPTION_MSG,
					e );
				throw new ServletException(
					ServletMessage.UNEXPECTED_EXCEPTION_MSG,
					e );
		} finally {
			AppLogger.getInstance().log(AppLogger.FINE,"RegistServlet.doPost() - end");
		}
	}
}
