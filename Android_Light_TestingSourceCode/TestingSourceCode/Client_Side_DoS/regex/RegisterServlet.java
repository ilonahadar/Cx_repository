package com.cx.demo.nonsecure.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cx.demo.nonsecure.commons.*;

public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 7890L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doGet(req,resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		CxDemoProperties props = CxDemoProperties.getInstance(getServletContext().getRealPath("/"));
		CxDemoDAL dal = CxDemoDAL.getInstance(props);

		HttpSession session = req.getSession(true);

		String password = req.getParameter("password");
		String password2 = req.getParameter("password2");

		if (!password.equals(password2)) {
			req.setAttribute("error", props.getLabel("ERROR_CONFIRM_FAILED"));
			getServletConfig().getServletContext().getRequestDispatcher("/login.jsp").forward(req,resp);
			return;
		}

		// Get login params
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		String surname = req.getParameter("surname");
		String country = req.getParameter("country");

		//Check if id is available
		if (dal.isIdAvailable(id)) {
			// Timing RegEx evaluation to illustrate ReDos
			long startTime = System.currentTimeMillis();
			boolean goodId = id.matches("^[a-zA-Z]+([a-zA-Z0-9]*)*$");
			if (!goodId) {
				// Id does not match guidelines - Starts with a letter
				req.setAttribute("error", props.getLabel("ERROR_BAD_ID") + " (" + (System.currentTimeMillis() - startTime) / 1000 + "sec)");
			}
			else {
				CxDemoUser user = new CxDemoUser(id, name, surname, country);
				try {
					dal.registerUser(user, password);
					// Registration passed
					session.setAttribute("user", user);
					getServletConfig().getServletContext().getRequestDispatcher("/user.jsp").forward(req,resp);
					return;
				}
				catch (CxDemoException ex) {
					// Registration failed
					req.setAttribute("error", props.getLabel("ERROR_REG_TO_DB_FAILED"));
				}
			}
		}
		else {
			req.setAttribute("error", props.getLabel("ERROR_ID_UNAVAILABLE"));
		}
		// All registration failures are redirected back to the login page.
		getServletConfig().getServletContext().getRequestDispatcher("/login.jsp").forward(req,resp);
	}

}
