package entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.dynalink.linker.LinkerServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostDispatcher {
    Object instance;//Controller实例
    Method method;//Controller方法
    Class<?>[] parameterClasses;//方法参数类型
    ObjectMapper objectMapper;//JSON映射


    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, InvocationTargetException, IllegalAccessException {
        Object[] arguments = new Object[parameterClasses.length];
        for (int i = 0;i<parameterClasses.length;i++){
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass ==HttpServletRequest.class){
                arguments[i] = request;
            }else if (parameterClass == HttpServletResponse.class){
                arguments[i] = response;
            }else if (parameterClass == HttpSession.class){
                arguments[i] = request.getSession();
            }else {
                //读取JSON并解析为JavaBean
                BufferedReader reader = request.getReader();
                arguments[i] = this.objectMapper.readValue(reader,parameterClass);
            }
        }
        return (ModelAndView)this.method.invoke(instance,arguments);
    }

}
