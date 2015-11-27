package com.xs.wms.controller;

import com.xs.wms.pojo.easyui.DataGrid;
import com.xs.wms.pojo.easyui.Json;
import com.xs.wms.pojo.easyui.PageHelper;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.xs.wms.pojo.Menu;
import com.xs.wms.service.MenuService;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "/menu")
public class MenuController extends BaseController {
    @Resource
    private MenuService menuService;

    /**
     * 跳转到菜单表格页面
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        return "menu/list";
    }

    /**
     * 跳转到资源管理页面
     * @return
     */
    @RequestMapping(value = "/listtree", method = RequestMethod.GET)
    public String listTree(Model model) {
        return "menu/list_tree";
    }

    /**
     * 菜单信息-列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/datagrid", method = RequestMethod.POST)
    public DataGrid datagrid(PageHelper page, Menu menu) {
        DataGrid dg = new DataGrid();
        dg.setTotal(menuService.getDatagridTotal(menu));

        List<Menu> menuList = menuService.datagridMenu(page);
        dg.setRows(menuList);

        return dg;
    }

    /**
     * 菜单列表-树
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/treegrid", method = RequestMethod.POST)
    public void treegrid(HttpServletResponse response,PageHelper page) {
        List<Menu> menuList = menuService.getAll(page);
        String json = createTreeJson(menuList);
        this.write(response, json);
    }

    /**
     * 保存（新增，修改）
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Json add(Menu menu) {
        Json j = new Json();

        try {
            menuService.saveMenu(menu);
            j.setSuccess(true);
            j.setMsg("保存成功！");
            j.setObj(menu);
        } catch (Exception e) {
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 获取当前菜单的所有子菜单
     * @param menuId
     * @param response
     */
    @RequestMapping(value = "/sub")
    public void getSub(@RequestParam
    Integer menuId, HttpServletResponse response) {
        List<Menu> subMenu = menuService.listSubMenuByParentid(menuId);
        JSONArray arr = JSONArray.fromObject(subMenu);
        PrintWriter out;

        try {
            response.setCharacterEncoding("utf-8");
            out = response.getWriter();

            String json = arr.toString();
            out.write(json);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除
     * @param out
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public Json deleteUser(Menu menu) {
        Json j = new Json();

        try {
            menuService.deleteMenuById(menu.getId());
            j.setSuccess(true);
            j.setMsg("删除成功！");
        } catch (Exception e) {
            j.setMsg(e.getMessage());
        }

        return j;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    /**
      * 创建一颗树，以json字符串形式返回
      * @param list 原始数据列表
      * @return 树
      */
    private String createTreeJson(List<Menu> list) {
        JSONArray rootArray = new JSONArray();

        for (Menu menu : list) {
            if (menu.getPid() == 0 ) { //有父节点

                JSONObject rootObj = createBranch(list, menu);
                rootArray.add(rootObj);
            }
        }

        return rootArray.toString();
    }

    /**
     * 递归创建分支节点Json对象
     * @param list 创建树的原始数据
     * @param currentNode 当前节点
     * @return 当前节点与该节点的子节点json对象
     */
    private JSONObject createBranch(List<Menu> list, Menu currentNode) {
        /*
         * 将javabean对象解析成为JSON对象
         */
        JSONObject currentObj = JSONObject.fromObject(currentNode);
        JSONArray childArray = new JSONArray();

        /*
         * 循环遍历原始数据列表，判断列表中某对象的父id值是否等于当前节点的id值，
         * 如果相等，进入递归创建新节点的子节点，直至无子节点时，返回节点，并将该
         * 节点放入当前节点的子节点列表中
         */
        for (Menu newNode : list) {
            if ((newNode.getPid() != 0) &&
                    (newNode.getPid().compareTo(currentNode.getId()) == 0)) {
                JSONObject childObj = createBranch(list, newNode);
                childArray.add(childObj);
            }
        }

        /*
         * 判断当前子节点数组是否为空，不为空将子节点数组加入children字段中
         */
        if (!childArray.isEmpty()) {
            currentObj.put("children", childArray);
        }

        return currentObj;
    }
}
