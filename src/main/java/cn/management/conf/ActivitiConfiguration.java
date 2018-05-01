package cn.management.conf;

import java.io.IOException;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.activiti.spring.boot.JpaProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 自定义Activiti配置，解决通用Mapper引入JPA包导致activiti自动配置异常
 * 详情查看AbstractProcessEngineAutoConfiguration的实现类
 * @author ZhouJiaKai
 * @date 2018-03-05
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(JpaProcessEngineAutoConfiguration.class)
public class ActivitiConfiguration {
    @Configuration
    @EnableConfigurationProperties(ActivitiProperties.class)
    public static class SysConfiguration extends AbstractProcessEngineAutoConfiguration {
        //@Bean以方法名命名
        @Bean
        @ConditionalOnMissingBean
        public PlatformTransactionManager transactionManager(@Qualifier("dataSource")DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        @ConditionalOnMissingBean
        public SpringProcessEngineConfiguration springProcessEngineConfiguration(
                @Qualifier("dataSource") DataSource dataSource,
                PlatformTransactionManager transactionManager,
                SpringAsyncExecutor springAsyncExecutor) throws IOException {
            SpringProcessEngineConfiguration configuration = this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
            return configuration;
        }

        @Bean
        public MultipartConfigElement multipartConfigElement() {
            MultipartConfigFactory factory = new MultipartConfigFactory();
            factory.setMaxFileSize(1024L * 1024L * 500L);
            return factory.createMultipartConfig();
        }
    }
}
