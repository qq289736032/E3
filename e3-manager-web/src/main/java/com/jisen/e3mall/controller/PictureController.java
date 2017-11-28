package com.jisen.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jisen.e3mall.common.utils.FastDFSClient;
import com.jisen.e3mall.common.utils.JsonUtils;

/**
 * 图片上传处理,Controller
 * 
 * @author Administrator
 */
@Controller
public class PictureController {
	/*
	 * /pic/upload 响应的是json数据
	 */
	@Value("${IMAGE_SEVER_URL}")
	private String IMAGE_SEVER_URL;

	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		try {

			// 把图片上传到服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			// 取文件扩展名
			String filename = uploadFile.getOriginalFilename();
			String extName = filename.substring(filename.lastIndexOf(".") + 1);
			// 得到一个图片地址和文件名
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			// 补充为完整的url
			url = IMAGE_SEVER_URL + url;
			// 封装到map中返回
			Map result = new HashMap<>();

			result.put("error", 0);
			result.put("url", url);
			
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("mesage", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
	}
}
