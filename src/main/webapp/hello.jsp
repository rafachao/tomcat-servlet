<html>
<head>
    <title>Hello World biubiubiu - JSP</title>
<head>
<body>
    <%--JSP COMMENT --%>
    <h1>Hello World!</h1>
    <p>
    <%
        out.println("Your IP address is ");
    %>
    <span style="color:red">
        <%= request.getRemoteAddr() %>
    </span>
    </p>
</body>
</html>