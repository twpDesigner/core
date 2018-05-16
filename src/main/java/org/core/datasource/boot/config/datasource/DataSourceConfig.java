package org.core.datasource.boot.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.core.datasource.boot.config.DynamicDataSource;
import org.core.datasource.boot.config.aop.DynamicDataSourceAspect;
import org.core.datasource.boot.config.property.DynamicDSPropertyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDSPropertyConfig.class)
@ImportAutoConfiguration({
        DynamicDataSourceAspect.class
})
public class DataSourceConfig{

    @Autowired
    DynamicDSPropertyConfig dynamicDSPropertyConfig;


    @Primary
    @Bean(name = "defaultDataSource")
    @ConfigurationProperties(prefix = "spring.datasource") // application.properteis中对应属性的前缀
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * @return
     */
    @Bean(name = "dynamicDS")
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        log.info("dynamicDSPropertyConfig:",dynamicDSPropertyConfig);

        Map<Object,Object> dynamicDSObject = dynamicDSPropertyConfig.getDynamicDSObject();

//        dynamicDSPropertyConfig.getDynamicDS().entrySet().stream().filter(
//                s->s.getValue().isMaster()
//        ).findFirst().ifPresent(
//                s->dynamicDataSource.setDefaultTargetDataSource(dynamicDSObject.get(s.getKey()))
//        );
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource());
        dynamicDataSource.setTargetDataSources(dynamicDSObject);
        return dynamicDataSource;
    }
}
