## 案例：用户登录

### 用户登录案例需求

1. 编写login.html登录页面
2. 使用Druid数据库连接池技术,操作mysql，数据库名为Study，数据表名为users
3. 使用JdbcTemplate技术封装JDBC
4. 登录成功跳转到SuccessServlet展示：登录成功！用户名,欢迎您
5. 登录失败跳转到FailServlet展示：登录失败，用户名或密码错误



### 下载架包

1. [commons-logging-1.2.jar 下载](https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar)。说明：用来记录程序运行时的活动的日志记录。

   

2. [druid-1.0.9.jar下载地址](https://repo1.maven.org/maven2/com/alibaba/druid/1.0.9/druid-1.0.9.jar)。说明：Druid是一个数据库连接池，可以用来创建数据库连接池对象。

   

3. [mchange-commons-java-0.2.12.jar下载](https://repo1.maven.org/maven2/com/mchange/mchange-commons-java/0.2.12/mchange-commons-java-0.2.12.jar)。说明：C3P0数据库连接池依赖架包

   

4. [mysql-connector-java-5.1.37.jar下载](https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.37/mysql-connector-java-5.1.37.jar)。说明：MySQL驱动架包

   

5. Spring 框架部分架包：

   * [spring-beans-5.0.0.RELEASE.jar下载](https://repo1.maven.org/maven2/org/springframework/spring-beans/5.0.0.RELEASE/spring-beans-5.0.0.RELEASE.jar)

     

   * [spring-core-5.0.0.RELEASE.jar下载](https://repo1.maven.org/maven2/org/springframework/spring-core/5.0.0.RELEASE/spring-core-5.0.0.RELEASE.jar)

     

   * [spring-jdbc-5.0.0.RELEASE.jar下载](https://repo1.maven.org/maven2/org/springframework/spring-jdbc/5.0.0.RELEASE/spring-jdbc-5.0.0.RELEASE.jar)

     

   * [spring-tx-5.0.0.RELEASE.jar下载](https://repo1.maven.org/maven2/org/springframework/spring-tx/5.0.0.RELEASE/spring-tx-5.0.0.RELEASE.jar)

   

   1. `spring-core`和`spring-beans`模块提供了框架的基础结构部分，包含控制反转和依赖注入功能。
   2. `spring-jdbc`模块提供了JDBC抽象层，不需要再编写单调的JDBC代码，解析数据库提供商指定的错误编码。
   3. `spring-tx`模块为实现指定接口和所有的简单Java对象的类提供编程式和声明式的业务管理。



### 实现

1. 创建项目（RequestExample），导入架包到`web/WEB-INF/lib`目录下

   <img src='https://raw.githubusercontent.com/LeeYiua/FigureBed/master/img/May%202020/%202020%2005%2019%2014%2015%2029.png' alt='20200519141529'/>

   

2. 创建html页面：login.html

   ```html
   <!DOCTYPE html>
   <html lang="en">
   <head>
       <meta charset="UTF-8">
       <title>用户登录</title>
   </head>
   <body>
       <form action="" method="post">
           用户名:<label><input type="text" name="username"></label><br>
           密码:<label><input type="password" name="password"></label><br>
           <input type="submit" value="登录">
       </form>
   </body>
   </html>
   ```

   

3. 创建数据库连接池的配置文件：driud.properties

   ```properties
   # 数据库连接池类全名称
   driverClassName=com.mysql.jdbc.Driver
   # 连接数据库的URL——jdbc:mysql://MySQL地址/数据库名
   url=jdbc:mysql://127.0.0.1:3306/Study
   # 数据库用户名
   username=用户名
   # 数据库密码
   password=密码
   # 数据库连接池初始化连接数
   initialSize=5
   # 数据库连接池最大连接数
   maxActive=10
   # 最大等待时间（毫秒为单位）
   maxWait=3000
   ```

   

4. 创建数据表：

   ```mysql
   USE Study;
   CREATE TABLE users(
     -- 用户id为主键，主键值自增。
     id INT PRIMARY KEY AUTO_INCREMENT, 
     -- 用户名唯一，且非空。
     username VARCHAR(32) UNIQUE NOT NULL,
     -- 用户密码非空
     userPassword VARCHAR(32) NOT NULL
   );
   INSERT INTO users(username, userPassword) VALUES(LeeHua, 2018520), (Rainbow, 20201314);
   ```

   

5. 创建一个用户实体类，用该实体类数据类型来存储用户信息：user.domain.User.java

   ```java
   package user.domain;
   
   /** @Description: 用户实体类 */
   public class User {
       private int id;
       private String username;
       private String userPassword;
   
       public int getId() {
           return id;
       }
   
       public void setId(int id) {
           this.id = id;
       }
   
       public String getUsername() {
           return username;
       }
   
       public void setUsername(String username) {
           this.username = username;
       }
   
       public String getUserPassword() {
           return userPassword;
       }
   
       public void setUserPassword(String userPassword) {
           this.userPassword = userPassword;
       }
   
       @Override
       public String toString() {
           return "User{" +
                   "id=" + id +
                   ", username='" + username + '\'' +
                   ", userPassword='" + userPassword + '\'' +
                   '}';
       }
   }
   ```

   

6. 创建一个Jdbc工具类，用来获取数据库连接池对象：JdbcUtil.java

   ```java
   package user.util;
   
   import com.alibaba.druid.pool.DruidDataSourceFactory;
   
   import javax.sql.DataSource;
   import java.io.InputStream;
   import java.util.Properties;
   
   public class JdbcUtil {
       private static DataSource dataSource;
   
       static {
           try {
               // 获取加载配置文件的对象
               Properties properties = new Properties();
               // 获取类的类加载器
               ClassLoader classLoader = JdbcUtil.class.getClassLoader();
               // 获取druid-1.0.9.properties配置文件资源输入流
               InputStream resourceAsStream = classLoader.getResourceAsStream("druid.properties");
               // 加载配置文件
               properties.load(resourceAsStream);
               // 获取连接池对象
               dataSource = DruidDataSourceFactory.createDataSource(properties);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   
       public static DataSource getDataSource() {
           return dataSource;
       }
   }
   ```

   

7. 创建操作数据库users表中的类：UserDao.java

   ```java
   package user.dao;
   
   import org.springframework.dao.DataAccessException;
   import org.springframework.jdbc.core.BeanPropertyRowMapper;
   import org.springframework.jdbc.core.JdbcTemplate;
   import user.domain.User;
   import user.util.JdbcUtil;
   
   public class UserDao {
   
       /** 创建JdbcTemplate对象，需要传入数据库连接池对象。 */
       private JdbcTemplate template = new JdbcTemplate(JdbcUtil.getDataSource());
   
       /**
        * 登录方法
        * @param loginUser 只有用户名的密码
        * @return 用户全部数据
        */
       public User login(User loginUser) {
           try {
               // 定义预编译SQL语句
               String sql = "SELECT * FROM users WHERE username = ? AND userPassword = ?";
   
               // 查询结果，将结果封装为对象
               User user = template. queryForObject(
                       sql,
                       // 将查询到的结果封装为 User 自定义数据结构
                       new BeanPropertyRowMapper<User>(User.class),
                       // 传入预编译sql语句的参数
                       loginUser.getUsername(),
                       loginUser.getUserPassword()
               );
               return user;                            // 查询到有对应的用户数据，返回数据
           } catch (DataAccessException e) {
               e.printStackTrace();
               return null;                            // 查询不到对应的用户数据，返回null
           }
       }
   }
   ```

   

8. 创建三个servlet类：获取用户请求信息是否正确类、用户登录成功类、用户登录失败类

   1. 用户登录成功类：SuccessServlet.java

      ```java
      package web.servlet;
      
      import user.domain.User;
      
      import javax.servlet.ServletException;
      import javax.servlet.annotation.WebServlet;
      import javax.servlet.http.HttpServlet;
      import javax.servlet.http.HttpServletRequest;
      import javax.servlet.http.HttpServletResponse;
      import java.io.IOException;
      
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
      ```

      

   2. 用户登录失败类：FailServlet.java

      ```java
      package web.servlet;
      
      import javax.servlet.ServletException;
      import javax.servlet.annotation.WebServlet;
      import javax.servlet.http.HttpServlet;
      import javax.servlet.http.HttpServletRequest;
      import javax.servlet.http.HttpServletResponse;
      import java.io.IOException;
      
      @WebServlet("/web/servlet/failServlet")
      public class FailServlet extends HttpServlet {
          @Override
          protected void doGet(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
              // 设置编码
              response.setContentType("text/html;charset=utf-8");
              // 输出
              response.getWriter().write("登录失败，用户名或密码错误！");
          }
      
          @Override
          protected void doPost(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
              this.doGet(request, response);
          }
      }
      ```

      

   3. 获取用户请求信息是否正确类：LoginServlet.java

      ```java
      package web.servlet;
      
      import user.dao.UserDao;
      import user.domain.User;
      
      import javax.servlet.ServletException;
      import javax.servlet.annotation.WebServlet;
      import javax.servlet.http.HttpServlet;
      import javax.servlet.http.HttpServletRequest;
      import javax.servlet.http.HttpServletResponse;
      import java.io.IOException;
      
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
      ```

9. 编写login.html文件指定的servlet文件路径：

   ```html
   <form action="/RequestExample_war_exploded/web/servlet/loginServlet" method="post">
   ```

   修改该行即可。

   

10. 上面基本的实现已经完成，下面进行测试



### 测试

1. 启动服务器，浏览器访问：http://localhost:8080/RequestExample_war_exploded/login.html

2. 输入正确的用户名和密码：

   <img src='https://raw.githubusercontent.com/LeeYiua/FigureBed/master/img/May%202020/%202020%2005%2023%2002%2025%2051.png' alt='20200523022551'/>

   

3. 数据表中的数据如下：

   <img src='https://raw.githubusercontent.com/LeeYiua/FigureBed/master/img/May%202020/%202020%2005%2023%2002%2026%2051.png' alt='20200523022651'/>

   

4. 登录，跳转到页面：http://localhost:8080/RequestExample_war_exploded/web/servlet/loginServlet

   <img src='https://raw.githubusercontent.com/LeeYiua/FigureBed/master/img/May%202020/%202020%2005%2023%2002%2028%2010.png' alt='20200523022810'/>

   

5. 如果输入的账号密码，在数据库中没有一组与之对应的用户数据，那么会跑出错误，且页面也会跳转到：http://localhost:8080/RequestExample_war_exploded/web/servlet/loginServlet

   <img src='https://raw.githubusercontent.com/LeeYiua/FigureBed/master/img/May%202020/%202020%2005%2023%2002%2031%2010.png' alt='20200523023110'/>

   跳转后的页面：

   <img src='https://raw.githubusercontent.com/LeeYiua/FigureBed/master/img/May%202020/%202020%2005%2023%2002%2031%2059.png' alt='20200523023159'/>



## 参考文献

1. [commons-logging.jar的作用](https://blog.csdn.net/qq_32660307/article/details/80914448)
2. [C3P0基本使用教程](https://www.jianshu.com/p/51e6bd20e772)
3. [Spring 5.0.0框架介绍_中文版_第二章](https://noahsnail.com/2016/10/07/2016-10-07-Spring%205.0.0%E6%A1%86%E6%9E%B6%E4%BB%8B%E7%BB%8D_%E4%B8%AD%E6%96%87%E7%89%88_%E7%AC%AC%E4%BA%8C%E7%AB%A0/)
4. [Spring JDBC 的简单使用](https://www.stringbug.com/p/291506202005/)

