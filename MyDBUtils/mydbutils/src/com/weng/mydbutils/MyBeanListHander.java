package com.weng.mydbutils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 自定义beanListHander  获取传入对象的集合
 * 
 * @author apple       
 *
 */
public class MyBeanListHander<T> implements MyResultHander<List<T>> {
	/**
	 * bean类型
	 */
	private Class classType;

	public MyBeanListHander(Class<T> classType) {
		this.classType = classType;
	}

	@Override
	public List<T> hander(ResultSet resultSet, List<String> columnNames) {
		// 反射为javaBean赋值
		return columnsToBeanList(resultSet, columnNames);
	}

	/**
	 * 数据转化为bean集合
	 * 
	 * @param columnNames 所有列名
	 *            
	 */
	private List<T> columnsToBeanList(ResultSet resultSet, List<String> columnNames) {
		try {
			// 返回的beans
			List<T> beans = new ArrayList<T>();

			// beans -> 数据库列名与数据的键值对
			Map<T, Map<String, Object>> map = new HashMap<T, Map<String, Object>>();

			// 获取数据库列与值的键值对
			while (resultSet.next()) {
				Map<String, Object> dataKV = new HashMap<String, Object>(); 
				for (String columnName : columnNames) {
					dataKV.put(columnName, resultSet.getObject(columnName));
				}
				map.put((T) classType.newInstance(), dataKV);
			}
			// 反射获取bean的所有成员属性
			Set<Entry<T, Map<String, Object>>> entrySet = map.entrySet();
			
			for (Entry<T, Map<String, Object>> entry : entrySet) {
				// 获取bean对象
				T instanceObj = entry.getKey();
				Map<String, Object> dataKV = entry.getValue();
				// 遍历数据库列与值的键值对
				Set<String> keySet = dataKV.keySet();
				for (String columnName : keySet) {
					// 获取每一个字段
					Field filed = classType.getDeclaredField(columnName);
					// 暴力可见
					filed.setAccessible(true);
					// 为每一个字段赋值
					filed.set(instanceObj, dataKV.get(columnName));
				}
				// 加入beans
				beans.add(instanceObj);
			}
			// 返回beans
			return (List<T>) beans;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
