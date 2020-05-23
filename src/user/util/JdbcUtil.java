package user.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: YiHua Lee
 * @Version: 1.8.0_201       Java SE 8
 * @Application: IntelliJ IDEA
 * @CreateTime: 2020/5/19 15:23
 * @Description: JDBC工具类
 */
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

