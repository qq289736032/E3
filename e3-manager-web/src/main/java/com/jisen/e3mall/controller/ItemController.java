package com.jisen.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisen.e3mall.common.pojo.EasyUIDataGridResult;
import com.jisen.e3mall.common.utils.E3Result;
import com.jisen.e3mall.pojo.TbItem;
import com.jisen.e3mall.service.ItemService;

/**
 * 商品管理controller
 * 
 * @author Administrator
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	@ResponseBody // 返回页面的是json数据,自动处理json返回格式
	public TbItem getTbItemByID(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}

	// 分页查询
	// 请求:http://localhost:8080/item/list?page=1&rows=30
	// 返回:商品ID(id) 商品标题(title) 叶子类目(cid) 卖点(sellPoint)
	// 价格(price)===datagrid的json数据
	// 库存数量(num) 条形码(barcode) 状态(status) 创建日期(created)更新日期 (updated)
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		EasyUIDataGridResult itemList = itemService.getItemList(page, rows);
		return itemList;
	}

	/**
	 * 商品添加功能
	 *  barcode 123456789 cid 163 desc 很好很好<br />
	 * image
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPuA6AVPRVAABUZFrUPH8275.jpg
	 * itemParams num 34 price 1230000 priceView 12300.00 sellPoint 很好 title 笔记本
	 */
	@RequestMapping(value = "item/save", method = RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item, String desc) {
		E3Result result = itemService.addItem(item, desc);
		return result;
	}

	/**http://localhost:8080/rest/item/update
	 * 商品修改功能 
	 * barcode 12345678 
	 * cid 560 
	 * desc 很好的手机 
	 * id 150259284773962 
	 * image 
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPvnKAX3J4AAJn3r1ulnc409.jpg,
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPvnKADUxWAADON5gZA4c470.jpg,
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPvnKAFyCEAACFQgzdDhI110.jpg,
	 * http://192.168.25.133/group1/M00/00/00/wKgZhVmPvnKAPCD4AABagtBmQTA499.jpg,
	 * http://192.168.25.133 /group1/M00/00/00/wKgZhVmPvnKAHq5WAADlhPSxOOg388.jpg itemParamId
	 * itemParams [] 
	 * num 354 
	 * price 299900 
	 * priceView 299.90 
	 * sellPoint 8月14日10:00抢购！骁龙835 旗舰处理器， 6GB 大内存，5.15”四曲面机身！变焦双摄拍人更美！小米5X，变焦双摄，拍人更美！ 
	 * title小米6 全网通 6GB+128GB 陶瓷黑尊享版 移动联通电信4G手机 双卡双待
	 */
	//根据商品id修改商品updateItem
	@RequestMapping(value = "rest/item/update", method = RequestMethod.POST)
	@ResponseBody
	public E3Result update(TbItem item, String desc) {
		E3Result result = itemService.updateItem(item, desc);
		return result;
	}
	
	//商品描述回显http://localhost:8080/rest/item/query/item/desc/536563
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result getItemDescById(@PathVariable Long itemId){
		E3Result result = itemService.getDescById(itemId);
		return result;
		
	}
}
