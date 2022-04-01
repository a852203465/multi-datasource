package cn.darkjrong.multimp.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 辅助数据源配置
 *
 * @author Rong.Jia
 * @date 2022/03/30
 */
@Configuration
@MapperScan(basePackages = "cn.darkjrong.multimp.mapper.secondary",
        sqlSessionFactoryRef = "secondarySqlSessionFactory",
        sqlSessionTemplateRef = "secondarySqlSessionTemplate")
public class SecondaryDatasourceConfig {

    @Autowired
    private MybatisPlusProperties mybatisPlusProperties;

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryDataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }

    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
        final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setGlobalConfig(mybatisPlusProperties.getGlobalConfig());
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/secondary/*.xml"));
        sessionFactory.setTypeAliasesPackage("cn.darkjrong.multimp.entity.secondary");
        return sessionFactory.getObject();
    }

    @Primary
    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate secondarySqlSessionTemplate(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
