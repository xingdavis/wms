package com.xs.wms.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.Order_detail;
import com.xs.wms.pojo.Stock_in;
import com.xs.wms.pojo.Stock_in_detail;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.service.OrderService;
import com.xs.wms.service.Order_detailService;
import com.xs.wms.service.StockInDetailService;
import com.xs.wms.service.StockInService;

@Controller
@RequestMapping("/stock_in")
public class StockInController {
	
	@Resource
	private StockInService stockInService;

	@Resource
	private StockInDetailService stockInDetailService;

	
	/**
	 * 新增入仓单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String NewStockIn(Model model) {
		return "stock/stock_in";
	}

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	public Json addStockIn(@RequestBody Stock_in obj) {
		Json j = new Json();

		try {
			int orderId = obj.getOrderId();
			
			List<Stock_in_detail> items = obj.getItems();
			if (items != null & items.size() > 0) {
				stockInService.insert(obj);
				int billId = obj.getId();
				if (orderId > 0) {
					for (Stock_in_detail i : items) {
						if (i != null) {
							i.setBillId(billId);
							stockInDetailService.insert(i);
						}
					}
					j.setSuccess(true);
					j.setMsg("新增成功！");
					j.setObj(obj);

				} else {
					j.setMsg("请选定订单！");
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
