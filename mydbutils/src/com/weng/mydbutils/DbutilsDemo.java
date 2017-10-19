package com.weng.mydbutils;

import java.util.List;

import org.junit.Test;

import com.weng.utils.DataSourceUtils;
/**
 * 测试
 * @author apple
 *
 */
public class DbutilsDemo {
	@Test
	public void showAll() throws Exception {
		MyQueryRunner runner = new MyQueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from student where name = ?";
		List<Student> list = runner.query(sql , new MyBeanListHander<Student>(Student.class), "如花");
		System.out.println(list);
	}
}
