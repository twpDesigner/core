package org.core.datasource.boot.config.property;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "boot")
public class DynamicDSPropertyConfig {
    private Map<String,DataSourceModel> dynamicDS = new HashMap<>();
    private Map<Object,Object> dynamicDSObject=new HashMap<>();

    public Map<Object,Object> getDynamicDSObject() {
        if (!dynamicDS.isEmpty()){
            dynamicDS.forEach((str,model)->{
                if (dynamicDS.isEmpty()) return;
                dynamicDSObject.put(
                        str,
                        new HikariDataSource(){
                            {
                                setDataSource(
                                        DataSourceBuilder
                                                .create()
                                                .url(model.getUrl())
                                                .driverClassName(model.getDriverClassName())
                                                .password(model.getPassword())
                                                .username(model.getUsername())
                                                .build()
                                );
                                setMaximumPoolSize(100);
                                setMinimumIdle(5);
                            }
                        }
                        /*
                        DataSourceBuilder
                                .create()
                                .url(model.getUrl())
                                .driverClassName(model.getDriverClassName())
                                .password(model.getPassword())
                                .username(model.getUsername())
                                .type(com.zaxxer.hikari.HikariDataSource.class)
                                .build()
                         */
                );
            });
        }
        return dynamicDSObject;
    }
}
