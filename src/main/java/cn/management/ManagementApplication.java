package cn.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.management.conf.ApplicationContextHelper;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(basePackages = "cn.management.mapper")
@EnableTransactionManagement
@ServletComponentScan
@EnableSwagger2
public class ManagementApplication {

	public static void main(String[] args) {
		 ApplicationContext app = SpringApplication.run(ManagementApplication.class, args);
		 //ApplicationContextHelper.setApplicationContext(app);
	}
	
}
