package com.xs.wms.controller;

import java.util.Date;
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
import com.xs.wms.pojo.Delivery;
import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.Stock_in;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;
import com.xs.wms.service.DeliveryService;

@Controller
@RequestMapping("/deliverys")
public class DeliveryController {
	private static Logger logger = Logger.getLogger(DeliveryController.class);

	@Resource
	private DeliveryService deliveryService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String ListPage(Model model) {
		return "delivery/list";
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String addPage(Model model) {
		return "delivery/delivery";
	}

	@RequestMapping(value = "/page/{id}", method = RequestMethod.GET)
	public String editPage(@PathVariable Integer id, Model model) {
		model.addAttribute("delivery_id", id);
		return "delivery/delivery";
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Json get(@PathVariable Integer id) {
		Json j = new Json();
		try {
			Delivery obj = this.deliveryService.getById(id);
			j.setSuccess(true);
			j.setObj(obj);
		} catch (Exception e) {
			logger.error(e);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	public Json addDelivery(HttpServletRequest request, @RequestBody Delivery obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			String code = obj.getCode();
			if (code != null & code != "") {
				obj.setCrDate(new Date());
				obj.setOp(((User) request.getSession().getAttribute("USER")).getId());
				obj.setFlag(0);
				deliveryService.add(obj);
				j.setMsg("新增成功！");
				ok = true;
			} else {
				j.setMsg("请输入订舱号！");
			}
		} catch (Exception e) {
			logger.error(e);
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public Json edit(HttpServletRequest request, @PathVariable Integer id, @RequestBody Delivery obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			Delivery oObj = deliveryService.getById(id);
			int flag = oObj.getFlag();
			String code = obj.getCode();
			if (code != null & code != "") {
				if (flag == 0) {
					obj.setOp(((User) request.getSession().getAttribute("USER")).getId());
					deliveryService.update(obj);
					ok = true;
					j.setObj(obj);
				} else
					j.setMsg("已出账单单据不允许修改！");
			} else
				j.setMsg("请输入订舱号！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	/**
	 * 删除
	 * 
	 * @param userId
	 * @param out
	 */
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Json delete(@PathVariable Integer id) {
		Json j = new Json();
		try {
			deliveryService.delete(id);
			j.setSuccess(true);
			j.setMsg("删除成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "/datagrid", method = RequestMethod.GET)
	public DataGrid datagrid(PageHelper page, Delivery obj) {
		DataGrid dg = new DataGrid();
		dg.setTotal(deliveryService.getDatagridTotal(obj));
		List<Delivery> list = deliveryService.datagrid(page, obj);
		dg.setRows(list);
		return dg;
	}

	@RequestMapping(value = "/report/{type}/{id}")
	public void ExportBill(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer type,
			@PathVariable Integer id) {
		try {
			Delivery delivery = deliveryService.getById(id);
			if (type == 2)
				ExcelUtils.exportDeliveryBill2(request, response, delivery, "delivery2", "东方派车单");
			else
				ExcelUtils.exportDeliveryBill(request, response, delivery, "delivery1", "信树派车单");
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
}
