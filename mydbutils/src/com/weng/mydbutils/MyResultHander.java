package com.weng.mydbutils;

import java.sql.ResultSet;
/**
 * 结果接口
 * @author apple
 *
 * @param <T> 返回的类型
 */
public interface MyResultHander <T> {
	T hander(ResultSet resultSet);
}
