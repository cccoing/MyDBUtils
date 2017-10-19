package com.weng.mydbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 * select * from table
 * @author apple
 *
 */
public class MyScalarHandler implements MyResultHander<Long> {

	@Override
	public Long hander(ResultSet resultSet, List<String> columns) {
		try {
			if (resultSet.next()) {
				return (Long) resultSet.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
