package com.umpay.channelRouting.util;

import java.lang.reflect.Field;

/**
 * 类相关常用工具类 (单例模式需要先调用getClassUtil方法获取对象)
 * 
 * @author xuxiaojia
 */
public class ClassUtil {

	/**
	 * 获取当先正在运行的类的类加载器
	 * 
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}


	/**
	 * 获取类下的所有申明的字段
	 * 
	 * @param c
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Field[] getClassFields(Class c) {
		Field[] fields = c.getDeclaredFields();
		return fields;
	}

	/**
	 * 用反射获取字段的值
	 * 
	 * @param field
	 *            字段
	 * @param t
	 *            对象
	 * @return
	 */
	public static <T> Object getFieldValue(Field field, T t) {
		field.setAccessible(true);
		Object resu = null;
		try {
			resu = field.get(t);
		} catch (Exception e) {
			throw new RuntimeException(t.getClass() + "类的" + field.getName() + "字段取值失败");
		}
		return resu;
	}

	/**
	 * 用反射获取字段的值
	 * 
	 * @param fieldName
	 *            字段名
	 * @param t
	 *            对象
	 * @return
	 */
	public static <T> Object getFieldValue(String fieldName, T t) {
		try {
			Field field = t.getClass().getDeclaredField(fieldName);
			return getFieldValue(field, t);
		} catch (Exception e) {
			throw new RuntimeException(t.getClass() + "类的" + fieldName + "字段取值失败");
		}
	}

	/**
	 * 传入类的全路径名，生成该类的对象
	 * 
	 * @param fullClassName 类的全路径名
	 * @return
	 */
	public static Object createObjByClassName(String fullClassName) {
		try {
			return Class.forName(fullClassName).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("实例化" + fullClassName + "失败");
		}
	}
}
