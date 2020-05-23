package user.test;

import org.junit.Test;
import user.dao.UserDao;
import user.domain.User;

/**
 * @Author: YiHua Lee
 * @Version: 1.8.0_201       Java SE 8
 * @Application: IntelliJ IDEA
 * @CreateTime: 2020/5/19 15:57
 * @Description:
 */
public class UserDaoTest {
    @Test
    public void testLogin() {
        // 创建用户数据存储对象（自定义数据结构）
        User loginUser = new User();
        // 设置用户名
        loginUser.setUsername("LeeHua");
        // 设置密码
        loginUser.setUserPassword("2018520");

        // 创建数据库接口对象
        UserDao userDao = new UserDao();
        // 传入数据存储对象，获取对应的用户数据
        User user = userDao.login(loginUser);
        // 输出用户数据（如果不存在该用户数据 --> 报错）
        System.out.println(user);
    }
}

