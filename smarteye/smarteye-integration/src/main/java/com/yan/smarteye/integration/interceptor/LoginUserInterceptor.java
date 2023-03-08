package com.yan.smarteye.integration.interceptor;


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

		String uri = request.getRequestURI();
		// 这个请求直接放行 (发短信验证码注册时无需登录)
		boolean match1 = new AntPathMatcher().match("/integration/employee/login", uri);
		boolean match2 = new AntPathMatcher().match("/integration/employee/register", uri);
		if(match1 || match2){
			return true;
		}

		// 获取登录用户
//		Cookie[] cookies = request.getCookies();
//		String uuidkey = null;
//		if(cookies!=null){
//			for(Cookie cookie :cookies){
//				if("loginkey".equals(cookie.getName())){
//					uuidkey = cookie.getValue();
//					break;
//				}
//			}
//		}


		// 获取session
		HttpSession session = request.getSession();
		if(session==null){
			//没session也是没登陆，就去登录
//			session.setAttribute("msg","用户未登录");
			String s = JSON.toJSONString(R.error(BizCodeEnume.LOGIN_FAIL.getCode(), BizCodeEnume.LOGIN_FAIL.getMsg()));
			response.getWriter().print(s);
//			response.sendRedirect("http://localhost:8001");
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
