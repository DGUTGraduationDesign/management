package cn.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "cn.management.mapper")
@EnableTransactionManagement
@ServletComponentScan
public class ManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}
	
}
