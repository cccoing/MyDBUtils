package com.weng.mydbutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet resultSet;
	
	public MyQueryRunner(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 增删改
	 */
	public int update(String sql, Object ... params ) {
		int update = -1;
		try {
			// 初始化查询
			ps = initQuery(sql, params);
			// 执行语句
			update = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return update;
	}
	/**
	 * 查询
	 */
	public <T> T query(String sql, MyResultHander<T> hander, Object ... params ) {
		try {
			// 初始化查询
			ps = initQuery(sql, params);
			// 执行语句
			resultSet = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 获取所有列名
		List<String> columnNames = getColumns();
		return hander.hander(resultSet, columnNames);
	}
	/**
	 * 获取所有列名
	 */
	private List<String> getColumns() {
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
		return columnNames;
	}

	/**
	 * 初始化查询
	 */
	private PreparedStatement initQuery(String sql, Object... params) throws SQLException {
		conn = dataSource.getConnection();
		// sql预处理
		ps = conn.prepareStatement(sql);
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
		return ps;
	}
	
}
