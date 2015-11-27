package com.xs.wms.pojo;

import java.util.List;


public class Menu {
    private Integer id;

    private String mname;

    private Integer pid;

    private Integer orderid;

    private String icon;

    private String url;

    private Boolean disable;
    
    private List<Menu> subMenu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname == null ? null : mname.trim();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
    
    public List<Menu> getSubMenu() {
		return subMenu;
	}
    public void setSubMenu(List<Menu> subMenu) {
		this.subMenu = subMenu;
	}
    
    /**
	 * 子节点个数
	 */
	private Integer countChildrens;
	public Integer getCountChildrens() {
		return countChildrens;
	}
	public void setCountChildrens(Integer countChildrens) {
		this.countChildrens = countChildrens;
	}
}