package com.example.weimao.config.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WeiMao
 * @create 2020-06-25 23:43
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="transactionManagerPrimary",
        transactionManagerRef="transactionManagerPrimary"
//        basePackages={"com.xxx.springboot.dao.postgresql"}
)
public class PrimaryDataSourceConfig {

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource dataSource;

    @Autowired
    private JpaProperties jpaProperties;


    private EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .properties(getProperties())
                .packages("com.example.weimao")
                .persistenceUnit("primaryPersistentUnit")
                .build().getObject()
                .createEntityManager();
    }
    public Map<String, String> getProperties(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("format_sql", "true");
        map.put("max_fetch_depth", "1");
        return map;
    }

    @Primary
    @Bean(name="transactionManagerPrimary")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder){
        return new JpaTransactionManager(builder.dataSource(dataSource)
                .properties(getProperties())
                //  实体类层
//                .packages("com.example.weimao")
                .persistenceUnit("primaryPersistentUnit")
                .build().getObject());
    }

}
