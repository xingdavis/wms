package com.xs.wms.testprj;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.Order_detail;
import com.xs.wms.pojo.User;
import com.xs.wms.service.OrderService;
import com.xs.wms.service.Order_detailService;
import com.xs.wms.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class) // 表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = { "file:src/main/resources/spring-mybatis.xml" })
public class MainTest {
	private static Logger logger = Logger.getLogger(MainTest.class);
	// private ApplicationContext ac = null;
	@Resource
	private UserService userService = null;
	@Resource
	private OrderService orderService = null;
	@Resource
	private Order_detailService order_detailService = null;
	// @Before
	// public void before() {
	// ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	// userService = (IUserService) ac.getBean("userService");
	// }

	// @Transactional
	@Test
	public void test1() {
		// User user = userService.getUserById(1);
		// System.out.println(user.getUserName());
		// logger.info("值："+user.getUserName());

		Order order = new Order();
		order.setClientId(0);
		order.setCode("2015122300005");
		order.setContactMan("contactMan");
		order.setContactTel("contactTel");
		order.setFlag(1);
		orderService.insert(order);
		/*try {
			orderService.insert(order);
		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
			logger.info("单号重复！");
		}*/
		int orderid = order.getId();
		logger.info("orderid=" + orderid);
		Order_detail item = new Order_detail();
		item.setCname("item1");
		item.setNum(2);
		item.setOrderId(orderid);
		item.setOrder(order);
		item.setVol(212.3200);
		item.setWeight(11.2);
		order_detailService.insert(item);
		// order.getOrder_details().add(item);
		// logger.info(items);
		// order.setOrder_details(items);
		// logger.info("itemid=" + item.getId());
		logger.info(item);
	}
}