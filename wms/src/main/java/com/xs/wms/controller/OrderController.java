package com.xs.wms.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.Order_detail;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.service.ClientService;
import com.xs.wms.service.OrderService;
import com.xs.wms.service.Order_detailService;

@Controller
@RequestMapping("/orders")
public class OrderController {

	@Resource
	private OrderService orderService;

	@Resource
	private Order_detailService order_detailService;

	/**
	 * 提交订单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/myorder", method = RequestMethod.GET)
	public String indexPage(Model model) {
		return "order/client_order";
	}

	@ResponseBody
	@RequestMapping(value = "/myorder", method = RequestMethod.POST, consumes = "application/json")
	public Json addOrder(@RequestBody Order order) {
		Json j = new Json();

		try {
			int client = order.getClientId();
			List<Order_detail> items = order.getOrder_details();
			if (items != null & items.size() > 0) {
				if (client > 0) {
					if (!orderService.existClientCode(order.getCode(), client)) {
						orderService.insert(order);
						int order_id = order.getId();
						for (Order_detail i : items) {
							i.setOrderId(order_id);
							order_detailService.insert(i);
						}
						j.setSuccess(true);
						j.setMsg("用户新增成功！");
						j.setObj(order);
					} else {
						j.setMsg("订单号重复，请改用其它！");
					}
				} else {
					j.setMsg("请选定客户！");
				}
			} else {
				j.setMsg("请添加货品明细！");
			}
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

}
