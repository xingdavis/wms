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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.xs.wms.common.ExcelUtils;
import com.xs.wms.pojo.Delivery;
import com.xs.wms.pojo.Order;
import com.xs.wms.pojo.Stock_in;
import com.xs.wms.pojo.Stock_in_detail;
import com.xs.wms.pojo.SumStock;
import com.xs.wms.pojo.User;
import com.xs.wms.pojo.View_stock_in_detail;
import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;
import com.xs.wms.service.OrderService;
import com.xs.wms.service.StockInDetailService;
import com.xs.wms.service.StockInService;

@Controller
@RequestMapping("/stock_ins")
public class StockInController {
	private static Logger logger = Logger.getLogger(StockInController.class);
	@Resource
	private StockInService stockInService;

	@Resource
	private StockInDetailService stockInDetailService;
	@Resource
	OrderService orderService;

	/**
	 * 查单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/billpage/{orderId}", method = RequestMethod.GET)
	public String list(HttpServletRequest request,Model model, @PathVariable Integer orderId) {
		model.addAttribute("order_id", orderId);
		String flag = request.getParameter("flag");
		if (!StringUtils.isNullOrEmpty(flag))// 追加标记\是否客户查看模式
			model.addAttribute("flag", flag);
		return "stock/query_bill";
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public DataGrid getList(PageHelper page, Stock_in obj) {
		DataGrid dg = new DataGrid();
		List<Stock_in> list = stockInService.getDatagrid(page, obj.getCode(),
				"", "");
		dg.setTotal(Long.getLong(String.valueOf(list.size())));
		dg.setRows(list);
		return dg;
	}

	@ResponseBody
	@RequestMapping(value = "/bills/{orderId}", method = RequestMethod.GET)
	public DataGrid datagrid(PageHelper page, @PathVariable Integer orderId) {
		DataGrid dg = new DataGrid();
		List<Stock_in> list = stockInService.getListByOrder(orderId);
		dg.setTotal(Long.getLong(String.valueOf(list.size())));
		dg.setRows(list);
		return dg;
	}

	@RequestMapping(value = "/detailpage/{billId}", method = RequestMethod.GET)
	public String detailPage(Model model, @PathVariable Integer billId) {
		model.addAttribute("bill_id", billId);
		Stock_in obj = this.stockInService.get(billId);
		model.addAttribute("bill_model", obj);
		return "stock/view_detail";
	}

	/**
	 * 查看单明细
	 * 
	 * @param page
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/details/{billId}", method = RequestMethod.GET)
	public DataGrid details_datagrid(PageHelper page,
			@PathVariable Integer billId) {
		DataGrid dg = new DataGrid();
		// List<Stock_in_detail> list =
		// stockInDetailService.getDetailsByBillId(billId);
		List<View_stock_in_detail> list = this.stockInDetailService
				.getDetailView(billId.intValue());
		dg.setTotal(Long.getLong(String.valueOf(list.size())));
		dg.setRows(list);
		return dg;
	}

	/**
	 * 新增入仓单页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public String addPage(HttpServletRequest request, Model model) {
		String orderId = request.getParameter("orderId");
		// String flag = request.getParameter("flag");
		// if (!StringUtils.isNullOrEmpty(flag))//追加标记
		// model.addAttribute("flag", flag);
		model.addAttribute("order_id", orderId);
		return "stock/stock_in";
	}

	@RequestMapping(value = "/page/{id}", method = RequestMethod.GET)
	public String editPage(@PathVariable Integer id, Model model) {
		// Stock_in obj = this.stockInService.get(id);
		model.addAttribute("stock_in_id", id);
		return "stock/stock_in";
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Json get(@PathVariable Integer id) {
		Json j = new Json();
		try {
			Stock_in obj = this.stockInService.get(id);
			j.setSuccess(true);
			j.setObj(obj);
		} catch (Exception e) {
			logger.error(e);
			j.setMsg(e.getMessage());
		}
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
			stockInService.updateBill(id, "del", 0);
			j.setSuccess(true);
			j.setMsg("删除成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "/bills", method = RequestMethod.POST, consumes = "application/json")
	public Json addStockIn(HttpServletRequest request, @RequestBody Stock_in obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			int orderId = obj.getOrderId();
			int flag = obj.getFlag();
			List<Stock_in_detail> items = obj.getItems();
			if (items != null & items.size() > 0) {
				obj.setCrDate(new Date());
				obj.setUid(((User) request.getSession().getAttribute("USER"))
						.getId());
				obj.setFlag(0);
				if (orderId > 0) {// 写死绑定的OrderCode
					Order order = this.orderService.get(orderId);
					obj.setOrderCode(order.getCode());
				}
				stockInService.insert(obj);
				int billId = obj.getId();
				if (orderId > 0) {
					for (Stock_in_detail i : items) {
						if (i != null) {
							i.setBillId(billId);
							stockInDetailService.insert(i);
						}
					}
					String result = "";
					if (flag == 1)
						result = stockInService.updateBill(billId, "verify",
								flag);
					if (result.equals("")) {
						ok = true;
						j.setMsg("新增成功！");
					} else
						j.setMsg(result);
					// j.setObj(obj);//报错com.fasterxml.jackson.databind.JsonMappingException:
					// Infinite recursion (StackOverflowError)

				} else {
					j.setMsg("请选定订单！");
				}
			} else {
				j.setMsg("请添加货品明细！");
			}
		} catch (Exception e) {
			logger.error(e);
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "/bills/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public Json edit(HttpServletRequest request, @PathVariable Integer id,
			@RequestBody Stock_in obj) {
		Json j = new Json();
		boolean ok = false;
		try {
			Stock_in oObj = stockInService.get(id);
			if (oObj.getFlag() < 1) {
				int orderId = obj.getOrderId();
				obj.setUid(((User) request.getSession().getAttribute("USER"))
						.getId());
				List<Stock_in_detail> items = obj.getItems();
				if (items != null & items.size() > 0) {
					stockInService.update(obj);
					if (orderId > 0) {
						stockInDetailService.deleteAllByBillId(id);
						for (Stock_in_detail i : items) {
							if (i != null) {
								i.setBillId(id);
								stockInDetailService.insert(i);
							}
						}
						j.setObj(obj);// 报错com.fasterxml.jackson.databind.JsonMappingException:
						// Infinite recursion (StackOverflowError)
						ok = true;
						j.setMsg("修改成功！");

					} else {
						j.setMsg("请选定订单！");
					}

				} else {
					j.setMsg("请添加货品明细！");
				}
			} else
				j.setMsg("修改失败，请确保单据在未审批状态！");

		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "/bills/{flag}/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public Json verify(@PathVariable Integer id, @PathVariable Integer flag) {
		Json j = new Json();
		boolean ok = false;
		try {
			String result = stockInService.updateBill(id, "verify", flag);
			if (result.equals(""))
				ok = true;
			else
				j.setMsg(result);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}

	@RequestMapping(value = "/report/{id}")
	public void ExportBill(HttpServletRequest request,
			HttpServletResponse response, @PathVariable Integer id) {
		try {
			Stock_in stock_in = stockInService.get(id);
			ExcelUtils.exportStockInBill(request, response, stock_in,
					"sheetName", "fileName");
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/sumstockpage", method = RequestMethod.GET)
	public String sumStockPage(Model model) {
		return "stock/sum_list";
	}

	@ResponseBody
	@RequestMapping(value = "/sumstock", method = RequestMethod.GET)
	public DataGrid getSumStock(PageHelper page, String key) {
		DataGrid dg = new DataGrid();
		dg.setTotal(stockInService.getSumStockTotal(key));
		List<SumStock> list = stockInService.getSumStock(page, key);
		dg.setRows(list);
		return dg;
	}

	@ResponseBody
	@RequestMapping(value = "/bill/copy/{orderId}", method = RequestMethod.GET, consumes = "application/json")
	public Json CloneBill(HttpServletRequest request, @PathVariable int orderId) {
		Json j = new Json();
		boolean ok = false;
		try {
			int rtn = stockInService.CloneBill(orderId);
			if (rtn == 0) {
				ok = true;
				j.setMsg("复制成功！");
			} else
				j.setMsg("复制失败！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		j.setSuccess(ok);
		return j;
	}
}
