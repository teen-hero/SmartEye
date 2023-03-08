package com.yan.smarteye.ware.interceptor;


import com.alibaba.fastjson.JSON;
import com.yan.common.exception.BizCodeEnume;
import com.yan.common.to.EmployeeEntityTo;
import com.yan.common.utils.R;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginUserInterceptor implements HandlerInterceptor {

	public static ThreadLocal<EmployeeEntityTo> threadLocal = new ThreadLocal<>();

	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {



		// 获取session
		HttpSession session = request.getSession();
		if(session==null ){
			//没session也是没登陆，就去登录

			//response.sendRedirect("http://localhost:8001");
			String s = JSON.toJSONString(R.error(BizCodeEnume.LOGIN_FAIL.getCode(), BizCodeEnume.LOGIN_FAIL.getMsg()));
			response.getWriter().print(s);
			return false;
		}

		EmployeeEntityTo employeeEntityTo = (EmployeeEntityTo) session.getAttribute("loginkey");
		if(employeeEntityTo != null){
			threadLocal.set(employeeEntityTo);
			return true;
		}else{
			// 没登陆就去登录

			//response.sendRedirect("http://auth.smarteye.com/login.html");
			//response.sendRedirect("http://localhost:8001");
			String s = JSON.toJSONString(R.error(BizCodeEnume.LOGIN_FAIL.getCode(), BizCodeEnume.LOGIN_FAIL.getMsg()));
			response.getWriter().print(s);
			return false;
		}
	}
}
