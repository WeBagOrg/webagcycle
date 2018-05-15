package com.hotent.platform.controller.ip;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hotent.core.api.org.model.ISysUser;
import com.hotent.core.api.util.ContextUtil;
import com.hotent.core.log.SysAuditThreadLocalHolder;
import com.hotent.core.util.FileUtil;
import com.hotent.core.util.ImageUtil;
import com.hotent.core.util.StringUtil;
import com.hotent.core.util.UniqueIdUtil;
import com.hotent.core.web.controller.BaseController;
import com.hotent.core.web.util.RequestUtil;
import com.hotent.platform.model.system.GlobalType;
import com.hotent.platform.model.system.SysFile;
import com.hotent.platform.model.system.SysUser;
import com.hotent.platform.service.system.GlobalTypeService;
import com.hotent.platform.service.system.SysFileService;
import com.hotent.platform.service.system.SysTypeKeyService;
import com.hotent.platform.service.system.SysUserService;
import com.hotent.platform.service.util.ServiceUtil;

@Controller
@RequestMapping("/platform/ip/utils/")
public class UploadController extends BaseController {
	@Resource
	private SysUserService sysUserService;
	@Resource
	private GlobalTypeService globalTypeService;
	@Resource
	private SysTypeKeyService sysTypeKeyService;
	@Resource
	private SysFileService sysFileService;

	/**
	 * 上传文件
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping("uploadify")
	@ResponseBody
	public void uploadify(MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
	    	ISysUser curUser = ContextUtil.getCurrentUser();
	        Long userId = curUser.getUserId();
			long typeId = RequestUtil.getLong(request, "typeId");
			String uploadType = RequestUtil.getString(request, "uploadType");
			String fileFormates = RequestUtil
					.getString(request, "fileFormates");
			boolean mark = true;
			SysUser appUser = null;
			if (userId > 0L) {
				appUser = (SysUser) this.sysUserService.getById(Long
						.valueOf(userId));
			}

			String attachPath = ServiceUtil.getBasePath();

			GlobalType globalType = null;
			if (typeId > 0L)
				globalType = (GlobalType) this.globalTypeService.getById(Long
						.valueOf(typeId));

			Map<String, MultipartFile> files = request.getFileMap();
			Iterator it = files.values().iterator();

			while (it.hasNext()) {
				Long fileId = Long.valueOf(UniqueIdUtil.genId());
				MultipartFile f = (MultipartFile) it.next();
				String oriFileName = f.getOriginalFilename();
				String extName = FileUtil.getFileExt(oriFileName);

				if ((StringUtil.isNotEmpty(fileFormates))
						&& (!(fileFormates.contains("*." + extName)))) {
					mark = false;
				}

				if (mark) {
					String fileName = fileId + "." + extName;

					String filePath = "";
					if ("pictureShow".equals(uploadType))
						filePath = createFilePath(attachPath + File.separator
								+ appUser.getAccount() + File.separator
								+ "pictureShow", fileName);
					else
						filePath = createFilePath(attachPath + File.separator
								+ appUser.getAccount(), fileName);

					FileUtil.writeByte(filePath, f.getBytes());

					SysFile sysFile = new SysFile();
					sysFile.setFileId(fileId);

					sysFile.setFileName(oriFileName.substring(0,
							oriFileName.lastIndexOf(46)));

					Calendar cal = Calendar.getInstance();
					Integer year = Integer.valueOf(cal.get(1));
					Integer month = Integer.valueOf(cal.get(2) + 1);

					sysFile.setFilePath(filePath.replace(attachPath
							+ File.separator, ""));

					if (globalType != null) {
						sysFile.setTypeId(globalType.getTypeId());
						sysFile.setFileType(globalType.getTypeName());
					} else {
						sysFile.setTypeId(this.sysTypeKeyService.getByKey(
								"FILE_TYPE").getTypeId());
						sysFile.setFileType("-");
					}

					sysFile.setCreatetime(new Date());

					sysFile.setExt(extName);

					sysFile.setTotalBytes(Long.valueOf(f.getSize()));

					sysFile.setNote(FileUtil.getSize(f.getSize()));

					if (appUser != null) {
						sysFile.setCreatorId(appUser.getUserId());
						sysFile.setCreator(appUser.getUsername());
					} else {
						sysFile.setCreator(SysFile.FILE_UPLOAD_UNKNOWN);
					}

					sysFile.setDelFlag(SysFile.FILE_NOT_DEL);
					this.sysFileService.add(sysFile);

					if ("pictureShow".equals(uploadType)) {
						int width = Integer.parseInt(this.configproperties
								.getProperty("compression.width"));
						String filePrex = filePath.substring(0,
								filePath.lastIndexOf("."));
						filePath = filePrex + "_small"
								+ filePath.substring(filePrex.length());
						String reutrnStr = ImageUtil.doCompressByByte(
								f.getBytes(), filePath, width, 40, 1F, true);
						this.logger.info("压缩后的文件：" + reutrnStr);
					}

					writer.println("{\"success\":\"true\",\"fileId\":\""
							+ fileId + "\",\"fileName\":\"" + oriFileName
							+ "\"}");
					try {
						List sysFiles;
						if (SysAuditThreadLocalHolder.getParamerter("sysFiles") == null) {
							sysFiles = new ArrayList();
							SysAuditThreadLocalHolder.putParamerter("sysFiles",
									sysFiles);
						} else {
							sysFiles = (List) SysAuditThreadLocalHolder
									.getParamerter("sysFiles");
						}
						sysFiles.add(sysFile);
					} catch (Exception e) {
						e.printStackTrace();
						this.logger.error(e.getMessage());
					}
				} else {
					this.logger.error("文件格式不符合要求！");
					writer.println("{\"success\":\"false\"}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			writer.println("{\"success\":\"false\"}");
		}
	}

	private String createFilePath(String tempPath, String fileName) {
		File one = new File(tempPath);
		Calendar cal = Calendar.getInstance();
		Integer year = Integer.valueOf(cal.get(1));
		Integer month = Integer.valueOf(cal.get(2) + 1);
		one = new File(tempPath + "/" + year + "/" + month);
		if (!(one.exists()))
			one.mkdirs();

		return one.getPath() + File.separator + fileName;
	}
}