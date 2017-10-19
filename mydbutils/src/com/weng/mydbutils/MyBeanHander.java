package com.weng.mydbutils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyBeanHander<T> implements MyResultHander<T> {
	/**
	 * bean类型
	 */
	private Class classType;

	public MyBeanHander(Class<T> classType) {
		this.classType = classType;
	}
	
	@Override
	public T hander(ResultSet resultSet, List<String> columnNames) {
		
		return columnsToBean(resultSet, columnNames);
	}
	/**
	 * 数据转化为Bean
	 */
	private T columnsToBean(ResultSet resultSet, List<String> columnNames) {
		try {
			// 返回的bean
			T bean = (T) classType.newInstance();

			// 获取数据库列与值的键值对
			while (resultSet.next()) {
				for (String columnName : columnNames) {
					// 获取每一个字段
					Field filed = classType.getDeclaredField(columnName);
					// 暴力可见
					filed.setAccessible(true);
					// 为每一个字段赋值
					filed.set(bean, resultSet.getObject(columnName));
				}
			}
			// 返回bean
			return bean;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
