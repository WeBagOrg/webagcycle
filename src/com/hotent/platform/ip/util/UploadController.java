package com.hotent.platform.ip.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hotent.core.web.controller.BaseController;

@Controller
@RequestMapping("/utils/")
public class UploadController extends BaseController{
	/**
	 * 上传文件
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("uploadify")
	public String uploadify(HttpServletRequest request, HttpServletResponse response){
		
		String responseStr="";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  

	    Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();  
	    // 文件保存路径
	    String ctxPath=request.getSession().getServletContext().getRealPath("/")+File.separator+"uploadFiles"; 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String ymd = sdf.format(new Date());
		ctxPath += File.separator + ymd + File.separator;
		// 创建文件夹
	    File file = new File(ctxPath);  
	    if (!file.exists()) {  
	        file.mkdirs();  
	    }  
	    String fileName = null;  
	    for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {  
	    	// 上传文件 
	    	MultipartFile mf = entity.getValue();  
	    	fileName = mf.getOriginalFilename();
	    	String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			// 重命名文件
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
	     	File uploadFile = new File(ctxPath + newFileName);  
	     	try {
		    	FileCopyUtils.copy(mf.getBytes(), uploadFile);
				responseStr="上传成功";
		    } catch (IOException e) {
				responseStr="上传失败";
				e.printStackTrace();
		    }  
	  	} 
	    return responseStr;
	}
}