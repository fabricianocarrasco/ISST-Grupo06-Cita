package es.upm.dit.isst.grupo06.cita.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

@WebServlet({ "/LoginServlet", "/" })
public class LoginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getServletContext().getRequestDispatcher( "/LoginView.jsp" ).forward( req, resp );
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter( "email" );
		String pass = req.getParameter( "password" );
		Subject currentUser = SecurityUtils.getSubject();
		if ( !currentUser.isAuthenticated() ) {
			UsernamePasswordToken token = new UsernamePasswordToken( email, pass );
			try {
				currentUser.login( token );
				if ( currentUser.hasRole( "admin" ) )
					resp.sendRedirect( req.getContextPath() + "/AdminServlet" );
				else if ( currentUser.hasRole( "professor" ) )
					resp.sendRedirect( req.getContextPath() + "/ProfessorServlet?email=" + currentUser.getPrincipal() );
				else
					resp.sendRedirect( req.getContextPath() + "/TFGServlet?email=" + currentUser.getPrincipal() );
			} catch ( Exception e ) {
				resp.sendRedirect( req.getContextPath() + "/LoginServlet" );
			}
		} else
			resp.sendRedirect( req.getContextPath() + "/LoginServlet" );
	}
}
