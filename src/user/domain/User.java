package user.domain;

/**
 * @Author: YiHua Lee
 * @Version: 1.8.0_201       Java SE 8
 * @Application: IntelliJ IDEA
 * @CreateTime: 2020/5/19 15:12
 * @Description: 用户实体类：用户信息存储实体类（自定义数据类型）
 */
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

