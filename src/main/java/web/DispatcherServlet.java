package web;

import entity.GetDispatcher;
import entity.ModelAndView;
import entity.PostDispatcher;
import entity.ViewEngine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/")
public class DispatcherServlet extends HttpServlet {
    private Map<String, GetDispatcher> getMappings = new HashMap<>();
    private Map<String, PostDispatcher> PostMappings = new HashMap<>();
    private ViewEngine viewEngine;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        //根据路径查找GetDispathcher
        GetDispatcher dispatcher = this.getMappings.get(path);
        if (dispatcher == null){
            //未找到返回404
            resp.sendError(404);
            return;
        }
        //调用Controller方法获得返回值
        ModelAndView mv = null;
        try {
            mv = dispatcher.invoke(req,resp);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //允许返回null
        if (mv == null){
            return;
        }
        //允许返回redirect：开头的view表示重定向
        if (mv.view.startsWith("redirect:")){
            resp.sendRedirect(mv.view.substring(9));
        }
        //将模板引擎渲染的内容写入响应
        PrintWriter printWriter = resp.getWriter();
        this.viewEngine.render(mv,printWriter);
        printWriter.flush();
    }
}
