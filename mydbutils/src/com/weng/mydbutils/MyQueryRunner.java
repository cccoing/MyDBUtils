package com.weng.mydbutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 自定义MyQueryRunner
 * @author apple
 *
 */
public class MyQueryRunner {
	/**
	 * 数据源
	 */
	private DataSource dataSource;
	
	public MyQueryRunner(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 查询
	 */
	public <T> T query(String sql, MyResultHander<T> hander, Object ... params ) {
		// 获取连接
		Connection conn = null;
		// 结果集
		ResultSet resultSet = null;
		try {
			conn = dataSource.getConnection();
			// sql预处理
			PreparedStatement ps = conn.prepareStatement(sql);
			// 获取sql占位符个数
			int sqlParamsCount = ps.getParameterMetaData().getParameterCount();
			// 判断
			if (sqlParamsCount != params.length) {
				throw new RuntimeException("参数个数不对");
			}
			// 遍历 设置参数
			for (int i = 0; i < sqlParamsCount; i++) {
				ps.setObject(i + 1, params[i]);
			}
			// 执行语句
			resultSet = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hander.hander(resultSet);
	}
}
