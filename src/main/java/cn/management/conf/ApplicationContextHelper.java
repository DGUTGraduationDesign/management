package cn.management.conf;

import org.springframework.context.ApplicationContext;

/**
 * Spring工具栏 
 * @author ZhouJiaKai
 * @date 2018-03-07
 */
public class ApplicationContextHelper {  
	
	private static ApplicationContext applicationContext = null;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        if(ApplicationContextHelper.applicationContext == null){
        	ApplicationContextHelper.applicationContext  = applicationContext;
        }
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
    
}  