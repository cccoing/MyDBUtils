package com.weng.mydbutils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public List<T> hander(ResultSet resultSet) {
		// 所有的列名
		ArrayList<String> columnNames = new ArrayList<String>();
		try {
			// 1.获取数据库的列数
			int columnCount = resultSet.getMetaData().getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				// 2.获取列名
				columnNames.add(resultSet.getMetaData().getColumnName(i + 1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 3.反射为javaBean赋值
		
		return (List<T>) columnsToBean(resultSet, columnNames);
	}

	/**
	 * 反射为javaBean赋值
	 * 
	 * @param columnNames 所有列名
	 *            
	 */
	private List<Object> columnsToBean(ResultSet resultSet, ArrayList<String> columnNames) {
		try {
			// 返回的beans
			List<Object> beans = new ArrayList<Object>();

			// beans -> 数据库列名与数据的键值对
			Map<Object, Map<String, Object>> map = new HashMap<Object, Map<String, Object>>();

			// 获取数据库列与值的键值对
			while (resultSet.next()) {
				Map<String, Object> dataKV = new HashMap<String, Object>(); 
				for (String columnName : columnNames) {
					dataKV.put(columnName, resultSet.getObject(columnName));
				}
				map.put(classType.newInstance(), dataKV);
			}
			// 反射获取bean的所有成员属性
			Set<Entry<Object, Map<String, Object>>> entrySet = map.entrySet();
			
			for (Entry<Object, Map<String, Object>> entry : entrySet) {
				// 获取bean对象
				Object instanceObj = entry.getKey();
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
			return beans;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
