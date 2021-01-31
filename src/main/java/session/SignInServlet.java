package session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = "/signin")
public class SignInServlet extends HttpServlet {
    //模拟一个数据库
    private Map<String,String> users = Map.of("bob","bob123","alice","alice123","tom","tomcat");
    //GET请求时显示登录页
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write("<h1>Sign In</h1>");
        printWriter.write("<form action=\"/signin\" method=\"post\">");
        printWriter.write("<p>Username: <input name=\"username\"></p>");
        printWriter.write("<p>Password: <input name=\"password\" type=\"password\"></p>");
        printWriter.write("<p><button type=\"submit\">Sign In</button> <a href=\"/\">Cancel</a></p>");
        printWriter.write("</form>");
        printWriter.flush();
    }
    //POST请求时处理用户登录

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("username");
        String password = req.getParameter("password");
        String expectedPassword = users.get(name.toLowerCase());
        if (expectedPassword!=null&&expectedPassword.equals(password)){
            //登录成功
            req.getSession().setAttribute("user",name);
            resp.sendRedirect("/index");
        }else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
