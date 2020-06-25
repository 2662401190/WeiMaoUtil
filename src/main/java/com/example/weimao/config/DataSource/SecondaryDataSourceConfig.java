package com.example.weimao.config.DataSource;

/**
 * @author WeiMao
 * @create 2020-06-26 0:06
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactorySecondary",
        transactionManagerRef = "transactionManagerSecondary"
//        dao层
//        basePackages={"com.xxx.springboot.dao.postgresql"}
)
public class SecondaryDataSourceConfig {

        @Autowired
        @Qualifier("secondaryDataSource")
        private DataSource secondaryDataSource;

        @Autowired
        private JpaProperties jpaProperties;


        @Bean
        public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
                return entityManagerFactoryBean(builder).getObject().createEntityManager();
        }
        @Bean(name="transactionManagerSecondary")
        public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder){
                return builder
                        .dataSource(secondaryDataSource)
                        .properties(getProperties())
//                        实体类的位置
//                        .packages("com.xxx.springboot.domain.postgresql")
                        .persistenceUnit("SecondaryPersistentUnit")
                        .build();
        }

        public Map<String, String> getProperties(){
                Map<String, String> map = new HashMap<String, String>();
                map.put("format_sql", "true");
                map.put("max_fetch_depth", "1");
                //map.put("dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
                return map;
        }

        @Bean(name="transactionManagerSecondary")
        public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder){
                return new JpaTransactionManager(entityManagerFactoryBean(builder).getObject());
        }


}
