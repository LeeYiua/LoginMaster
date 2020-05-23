package user.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import user.domain.User;
import user.util.JdbcUtil;

/**
 * @Author: YiHua Lee
 * @Version: 1.8.0_201       Java SE 8
 * @Application: IntelliJ IDEA
 * @CreateTime: 2020/5/19 15:16
 * @Description: 这是一个操作数据库users表中的类
 */
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

