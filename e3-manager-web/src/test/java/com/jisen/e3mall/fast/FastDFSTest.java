package com.jisen.e3mall.fast;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.jisen.e3mall.common.utils.FastDFSClient;

/**
 * 测试fastDFS文件系统
 * @author Administrator
 *
 */
public class FastDFSTest {
	
	@Test
	public void testUpload() throws Exception{
		//创建一个配置文件,文件名任意.内容就是tracker服务器的地址.
		//加载全局对象加载配置文件.
		ClientGlobal.init("C:/Users/Administrator/workspace_yilifang/e3-manager-web/src/main/resources/conf/client.conf");
		//创建一个trackerclient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过trackclient获得一个trackServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个storageServer的引用,可以是null
		StorageServer storageServer = null;
		//创建一个storageclient,参数需要trackerServer和strorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用storageclient上传文件
		String[] strings = storageClient.upload_file("C:\\Users\\Administrator\\Desktop\\用力写.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testUploadByUtils() throws Exception{
		//传递一个配置文件,返回一个客户端
		FastDFSClient fastDFSClient = new FastDFSClient("C:/Users/Administrator/workspace_yilifang/e3-manager-web/src/main/resources/conf/client.conf");
		String string = fastDFSClient.uploadFile("C:\\Users\\Administrator\\Desktop\\参考书.PNG");
		System.out.println(string);
		
	}
}
