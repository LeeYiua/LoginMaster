package web.servlet;

import user.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: YiHua Lee
 * @Version: 1.8.0_201       Java SE 8
 * @Application: IntelliJ IDEA
 * @CreateTime: 2020/5/23 01:45
 * @Description:
 */
@WebServlet("/web/servlet/successServlet")
public class SuccessServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取request域中共享的user对象
        User user = (User) request.getAttribute("username");
        if (user != null) {
            // 设置编码
            response.setContentType("text/html;charset=utf-8");
            // 输出
            response.getWriter().write("登录成功。"+ user.getUsername() +"，欢迎您！");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }
}
