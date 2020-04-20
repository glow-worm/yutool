package glow.worm.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

/**
 * @author wangyulong 
 * @time 2019/04/11 14:35:39
 * @description BeanCopier - [BeanCopier只拷贝名称和类型都相同的属性]
 */
public class BeanCopierUtil {
	// 使用多线程安全的Map缓存BeanCopier，由于读操作远大于写，所以性能影响可以忽略
	private static Map<String, BeanCopier> COPIER_MAP = new ConcurrentHashMap<>();
	private static final Object lock = new Object();

	/**
	 * @author wangyulong
	 * @time 2019/04/11 15:31:22
	 * @param source
	 * @param target
	 * @description bean拷贝
	 */
	public static void copyProperties(Object source, Object target) {
		BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), false);
		copier.copy(source, target, null);
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 17:33:38
	 * @param source
	 * @param targetClass
	 * @return
	 * @description bean拷贝
	 */
	public static <T> T copyProperties(Object source, Class<T> targetClass) {
		T t = getTObject(targetClass);
		copyProperties(source, t);
		return t;
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/11 17:34:06
	 * @param source
	 * @param target
	 * @description bean拷贝, 使用默认转换器转换包装类型, 支持的类型如下:
	 * 				boolean <=> Boolean, byte <=> Byte, char <=> Character, short <=> Short,
	 * 				int <=> Integer, long <=> Long, float <=> Float, double <=> Double
	 */
	public static void copyPropertiesUseTypeConverter(Object source, Object target) {
		BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), true);
		copier.copy(source, target, new TypeConverter());
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 17:34:06
	 * @param source
	 * @param targetClass
	 * @description bean拷贝, 使用默认转换器转换包装类型, 支持的类型如下:
	 * 				boolean <=> Boolean, byte <=> Byte, char <=> Character, short <=> Short,
	 * 				int <=> Integer, long <=> Long, float <=> Float, double <=> Double
	 */
	public static <T> T copyPropertiesUseTypeConverter(Object source, Class<T> targetClass) {
		T t = getTObject(targetClass);
		BeanCopier copier = getBeanCopier(source.getClass(), targetClass, true);
		copier.copy(source, t, new TypeConverter());
		return t;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 17:34:06
	 * @param source
	 * @param target
	 * @param converter
	 * @description bean拷贝, 使用自定义转换器
	 */
	public static void copyProperties(Object source, Object target, Converter customConverter) {
		BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), true);
		copier.copy(source, target, customConverter);
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 17:34:31
	 * @param source
	 * @param targetClass
	 * @param converter
	 * @return
	 * @description bean拷贝, 使用转换器
	 */
	public static <T> T copyProperties(Object source, Class<T> targetClass, Converter customConverter) {
		T t = getTObject(targetClass);
		BeanCopier copier = getBeanCopier(source.getClass(), targetClass, true);
		copier.copy(source, t, customConverter);
		return t;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 19:47:09
	 * @param sourceList
	 * @param targetClass
	 * @return
	 * @description list拷贝
	 */
	public static <T> List<T> copyPropertiesToList(List<?> sourceList, Class<T> targetClass) {
		if (sourceList == null || sourceList.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> list = new ArrayList<T>(sourceList.size());
		for (Object obj : sourceList) {
			T t = getTObject(targetClass);
			copyProperties(obj, t);
			list.add(t);
		}
		return list;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 17:34:42
	 * @param sourceClass
	 * @param targetClass
	 * @param userConverter
	 * @return
	 * @description 获取BeanCopier
	 */
	private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass, Boolean userConverter) {
		String beanKey = generateKey(sourceClass, targetClass);
		BeanCopier copier = COPIER_MAP.get(beanKey);
		if (copier == null) {
			synchronized (lock) {
				copier = COPIER_MAP.get(beanKey);
				if (copier == null) {
					copier = BeanCopier.create(sourceClass, targetClass, userConverter);
					// putIfAbsent实现原子操作
					COPIER_MAP.putIfAbsent(beanKey, copier);
				}
			}
		}
		return copier;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 15:30:47
	 * @param sourceClass
	 * @param targetClass
	 * @return
	 * @description 封装beanKey
	 */
	private static String generateKey(Class<?> sourceClass, Class<?> targetClass) {
		return sourceClass.toString() + targetClass.toString();
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 17:35:02
	 * @param targetClass
	 * @return
	 * @description 获取泛型对象
	 */
	private static <T> T getTObject(Class<T> targetClass) {
		T t = null;
		try {
			t = targetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
		}
		return t;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/11 17:35:24
	 * @param text
	 * @param targetClass
	 * @param errMsg
	 * @return
	 * @description 异常信息格式化
	 */
	private static String format(String text, Class<?> targetClass, String errMsg) {
		return String.format(text, targetClass, errMsg);
	}
	
	/**
	 * @author  wangyulong
	 * @time    2019/04/12 11:40:09
	 * @description 包装类型转换器, 支持的类型如下:
	 * 				boolean <=> Boolean, byte <=> Byte, char <=> Character, short <=> Short,
	 * 				int <=> Integer, long <=> Long, float <=> Float, double <=> Double
	 */
	private static class TypeConverter implements Converter {

	    @SuppressWarnings("rawtypes")
	    @Override
	    public Object convert(Object source, Class targetClass, Object context) {
	    	if (!source.getClass().equals(targetClass)) {
				if (source instanceof Integer) {
					return (int) source;
				}else if (targetClass.equals(Integer.class)) {
					return (Integer) source;
				}else if (source instanceof Byte) {
					return (byte) source;
				}else if (targetClass.equals(Byte.class)) {
					return (Byte) source;
				}else if (source instanceof Character) {
					return (char) source;
				}else if (targetClass.equals(Character.class)) {
					return (Character) source;
				}else if (source instanceof Boolean) {
					return (boolean) source;
				}else if (targetClass.equals(Boolean.class)) {
					return (Boolean) source;
				}else if (source instanceof Short) {
					return (short) source;
				}else if (targetClass.equals(Short.class)) {
					return (Short) source;
				}else if (source instanceof Long) {
					return (long) source;
				}else if (targetClass.equals(Long.class)) {
					return (Long) source;
				}else if (source instanceof Float) {
					return (float) source;
				}else if (targetClass.equals(Float.class)) {
					return (Float) source;
				}else if (source instanceof Double) {
					return (double) source;
				}else if (targetClass.equals(Double.class)) {
					return (Double) source;
				}else {
					return null;
				}
			}
	    	return source;
	    }
	}
}

