package web.servlet;

import user.dao.UserDao;
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
 * @CreateTime: 2020/5/23 00:53
 * @Description:
 */
@WebServlet("/web/servlet/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置编码
        request.setCharacterEncoding("utf-8");
        // 获取请求参数
        String username = request.getParameter("username");
        String userPassword = request.getParameter("userPassword");
        // 封装User对象（将获取到的请求参数封装到自定义数据类型中）
        User loginUser = new User();
        loginUser.setUsername(username);
        loginUser.setUserPassword(userPassword);
        // 创建UserDao，来操作数据库users表
        UserDao userDao = new UserDao();
        // 查询users表中是否存在请求的username和userPassword的一组数据
        User user = userDao.login(loginUser);
        if (user == null) {
            // 不存在这组数据，登录失败。
            // 将 request, response 传给 FailServlet.java
            request.getRequestDispatcher("/web/servlet/failServlet").forward(request, response);
//            System.out.println(request.getParameter("username") + "\t" + request.getParameter("userPassword"));
        } else {
            // 存在这组数据，登录成功
            // 设置请求属性 'user':user
            request.setAttribute("username", loginUser);
            // 将 request, response 传给 SuccessServlet.java
            request.getRequestDispatcher("/web/servlet/successServlet").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }
}
