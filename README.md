# MyDBUtils 使用方法
自定义DBUtils
1.导入数据库驱动
2.你要有数据源
3.导入MyDBUtils.jar
4.类似apache的DBUtils的使用方法
```java
public class MyDbutilsDemo {
	/**
	 * 增删改
	 * @throws Exception
	 */
	@Test
	public void showAdd() throws Exception {
		MyQueryRunner runner = new MyQueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into student values(?,?,?)";
		int query = runner.update(sql, null, "哈哈", "女");
		System.out.println(query);
	}
	
	/**
	 * MyBeanListHander
	 * @throws Exception
	 */
	@Test
	public void showListBean() throws Exception {
		MyQueryRunner runner = new MyQueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from student";
		List<Student> list = runner.query(sql , new MyBeanListHander<Student>(Student.class));
		System.out.println(list);
	}
	
	/**
	 * MyBeanHander
	 * @throws Exception
	 */
	@Test
	public void showBean() throws Exception {
		MyQueryRunner runner = new MyQueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from student where name = ?";
		Student stu = runner.query(sql , new MyBeanHander<Student>(Student.class), "如花");
		System.out.println(stu);
	}
	/**
	 * MyBeanHander
	 * @throws Exception
	 */
	@Test
	public void showScalarHandler() throws Exception {
		MyQueryRunner runner = new MyQueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from student";
		Long l = runner.query(sql , new MyScalarHandler());
		System.out.println(l);
	}
}
```
