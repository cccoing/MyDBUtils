package com.weng.mydbutils;

import java.sql.ResultSet;
import java.util.List;
/**
 * 结果接口
 * @author apple
 *
 * @param <T> 返回的类型
 */
public interface MyResultHander <T> {
	T hander(ResultSet resultSet, List<String> columnNames);
}
