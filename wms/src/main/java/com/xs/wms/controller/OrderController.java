package com.xs.wms.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xs.wms.common.ExcelUtils;

import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.Order_detail;


import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;

import com.xs.wms.service.OrderService;
import com.xs.wms.service.Order_detailService;


@Controller
@RequestMapping("/orders")
public class OrderController {
	private static Logger logger = Logger.getLogger(OrderController.class);
	@Resource
	private OrderService orderService;
	
	@Resource
	private Order_detailService order_detailService;

	/**
	 * 获取入仓单
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Json get(@PathVariable Integer id) {
		Json j = new Json();
		try {
			Order obj = orderService.get(id);
			j.setSuccess(true);
			j.setObj(obj);
		} catch (Exception e) {
			logger.error(e);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 查询我的订单datagrid
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public DataGrid datagrid(PageHelper page, Order order) {
		DataGrid dg = new DataGrid();
		dg.setTotal(orderService.getDatagridTotal(order));
		List<Order> list = orderService.datagridOrder(page, order);
		dg.setRows(list);
		return dg;
	}

	/**
	 * 查询我的订单datagrid
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public DataGrid myDatagrid(PageHelper page, String clientId, String code) {
		DataGrid dg = new DataGrid();
		dg.setTotal(orderService.getDatagridTotal(clientId, code));
		List<Order> list = orderService.datagridOrder(page, clientId, code);
		dg.setRows(list);
		return dg;
	}

	/**
	 * 客户查订单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public String myPage(Model model) {
		return "order/myorders";
	}

	/**
	 * 客户查订单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		return "order/list";
	}

	/**
	 * 客户新增订单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String NewOrder(Model model) {
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
							if (i != null) {
								i.setOrderId(order_id);
								order_detailService.insert(i);
							}
						}
						j.setSuccess(true);
						j.setMsg("用户新增成功！");
						j.setObj(orderService.get(order_id));
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

	@RequestMapping(value = "/report/{id}")
	public void ExportOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
		try {
			Order order = orderService.get(id);
			ExcelUtils.exportOrder(request, response, order, "sheetName", "fileName");
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
}
