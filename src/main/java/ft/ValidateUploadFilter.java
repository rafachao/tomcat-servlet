package ft;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebFilter("/upload/*")
public class ValidateUploadFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        //获取客户端传入的签名方法和签名
        String digest = req.getHeader("Signature-Method");
        String signature = req.getHeader("Signature");
        if (digest == null || digest.isEmpty() || signature == null || signature.isEmpty()){
            sendErrorPage(resp,"Missing signature");
            return;
        }
        //读取Request的Body并验证签名
        MessageDigest md = getMessageDigest(digest);
        byte[] buffer = new byte[1024];
        InputStream inputStream = req.getInputStream();
        for (;;){
            int len = inputStream.read(buffer);
            System.out.println(len);
            if (len == -1){
                break;
            }
        }
        InputStream input = new DigestInputStream(req.getInputStream(),md);

        for (;;){

            int len = input.read(buffer);
            if (len == -1){
                break;
            }
        }
        byte[] linshi = md.digest();

        String actual = toHexString(linshi);
        if (!signature.equals(actual)){
            sendErrorPage(resp,"Invalid signature");
            return;
        }
        //验证成功后继续处理
        filterChain.doFilter(servletRequest,servletResponse);

    }

    //将byte转换为hexString
    private String toHexString(byte[] digest){
        StringBuilder sb = new StringBuilder();
        for (byte b :digest){
            sb.append(String.format("%02x",b));
        }
        System.out.println();
        System.out.println("signature: "+sb.toString());
        return sb.toString();
    }


    //根据名称创建MessageDigest
    private MessageDigest getMessageDigest(String name) throws ServletException {
        try {
            return MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException e) {
            throw new ServletException(e);
        }
    }

    private void sendErrorPage(HttpServletResponse response,String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter pw = response.getWriter();
        pw.write("<html><body><h1>");
        pw.write(errorMessage);
        pw.write("</h1></body></html>");
        pw.flush();
    }
}
