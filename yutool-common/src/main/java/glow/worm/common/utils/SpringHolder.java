package glow.worm.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author wangyulong
 * @time 2019/05/17 18:29:35
 * @description 上下文获取工具类, 注意:此种方式只适用发生ServletRequest时
 */
@Component
public class SpringHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;
	private static SpringHolder holder = null;

	public synchronized static SpringHolder init() {
		if (holder == null) {
			holder = new SpringHolder();
		}
		return holder;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringHolder.applicationContext = applicationContext;
	}

	public synchronized static ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			//这种仅适用于本地启动适用-无ServletRequest
			applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		}
		return applicationContext;
	}

	/**
	 * @author wangyulong
	 * @time 2019/05/17 18:32:48
	 * @param beanName
	 * @return
	 * @description 通过beanName获取bean
	 */
	public synchronized static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * @author wangyulong
	 * @time 2019/05/17 18:33:12
	 * @param requiredType
	 * @return
	 * @description 通过beanType获取bean
	 */
	public synchronized static <T> T getBean(Class<T> beanType) {
		return applicationContext.getBean(beanType);
	}

}