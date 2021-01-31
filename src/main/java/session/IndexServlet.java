package session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从HttpSession获取当前用户名
        String user = (String) req.getSession().getAttribute("user");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("X-Powered-By","JavaEE Servlet");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write("<h1>Welcome,"+(user!=null?user:"Guest")+"</h1>");
        if (user==null){
            //未登录，显示登录连接
            printWriter.write("<p><a href=\"/signin\">Sign In</a></p>");
        }else {
            //已登录，显示登出连接
            printWriter.write("<p><a href=\"/signout\">Sign Out</a></p>");

        }
        printWriter.write("<p>language:"+parseLanguageFromCookie(req)+"</h1>");
        printWriter.flush();
    }



    private String parseLanguageFromCookie(HttpServletRequest req){
        //获取请求附带的所有Cookie
        Cookie[] cookies = req.getCookies();
        //如果获取到Cookie
        if (cookies!=null){
            for (Cookie cookie:cookies){
                //如果Cookie名称为lang
                if (cookie.getName().equals("lang")){
                    return cookie.getValue();
                }
            }

        }
        //返回默认值
        return "en";
    }
}
