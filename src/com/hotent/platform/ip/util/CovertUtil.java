package com.hotent.platform.ip.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.hotent.core.util.AppConfigUtil;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

public class CovertUtil {

	public static final String PDF2SWF; // PDF转swf程序的调用方式

	static {
		if (isLinux()) {
			PDF2SWF = "pdf2swf";
		} else {
			PDF2SWF = AppConfigUtil.get("pdf2swf.url");
		}
	}

	/***
	 * 判断当前系统是否是Linux系统，如果非Linux系统默认认为是windows
	 * 
	 * @return
	 */
	public static Boolean isLinux() {
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			return true;
		}
		return false;
	}

	/***
	 * PDF转SWF
	 * 
	 * @param src
	 *            源PDF文件的路径
	 * @param dst
	 *            转码后swf文件的保存路径
	 * @param pn
	 *            待处理的页码
	 * @return
	 */
	public static boolean convertSinglePagePDF(String src, String dst, int pn) {
		File srcPdf = new File(src);
		if (!srcPdf.exists()) {
			return false;
		}
		File targetSwfDir = new File(dst).getParentFile();
		if (targetSwfDir.exists()) {
			// 文件存在
			if (targetSwfDir.isFile()) {
				// 是标准文件（而不是目录）
				return false;
			}
		} else {
			// 文件不存在，尝试新建
			if (!targetSwfDir.mkdirs()) {
				// 新建目标目录失败
				return false;
			}
		}
		String command = PDF2SWF + " " + src + " " + dst + " -f -T 9 -G -p " + pn;
		Process process;
		// 错误消息
		// final StringBuilder errorMessage = new StringBuilder();
		try {
			System.out
					.println("Start converting File: " + src
							+ ", page range is: " + pn
							+ ", Swfs will saved as :" + dst);
			process = Runtime.getRuntime().exec(command);
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			// 新建线程, 处理进程的标准输出
			new Thread() {
				public void run() {
					String message = "";
					try {
						while ((message = br.readLine()) != null) {
							System.out.println(message);
						}
					} catch (IOException e) {
					}
				}
			}.start();
			// 处理错误流
			final BufferedReader brError = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			// 新建线程, 处理进程的错误流
			new Thread() {
				public void run() {
					String message = "";
					try {
						while ((message = brError.readLine()) != null) {
							System.out.println(message);
							// errorMessage.append(message);
						}
					} catch (IOException e) {
					}
				}
			}.start();
		} catch (IOException e) {
			return false;
		}

		// 等待进程执行完毕
		try {
			process.waitFor();
			process.destroy();
			System.out.println("Successed in converting File: " + src
					+ ", the converted page(s) is: " + pn);
			return true;
		} catch (Exception ex) {
			process.destroy();
			return false;
		}
	}

	/***
	 * 获取pdf文件的总页数
	 * 
	 * @param pdfPath
	 *            pdf文件的路径
	 * @return pdf文件的总页数
	 */
	public static int getTotalPage(String pdfPath) {
		PdfReader pdfReader;
		int n;
		try {
			@SuppressWarnings("deprecation")
			RandomAccessFileOrArray file = new RandomAccessFileOrArray(pdfPath,
					false, false);
			pdfReader = new PdfReader(file, null);
			n = pdfReader.getNumberOfPages();
			pdfReader.close();
			return n;
		} catch (IOException ex) {
			return 0;
		}
	}

	public static int office2PDF(String sourceFile, String destFile) {
		Process pro=null;
		OpenOfficeConnection connection=null;
		try {
			File inputFile = new File(sourceFile);
			if (!inputFile.exists()) {
				return -1;// 找不到源文件, 则返回-1
			}
			
			// 如果目标路径不存在, 则新建该路径
			File outputFile = new File(destFile);
			if (!outputFile.getParentFile().exists()) {
				outputFile.getParentFile().mkdirs();
			}
			String OpenOffice_HOME = AppConfigUtil.get("openoffice.url");//"C:/Program Files/OpenOffice 4";
			// 这里是OpenOffice的安装目录, 在我的项目中,为了便于拓展接口,没有直接写成这个样子,但是这样是绝对没问题的
			// 如果从文件中读取的URL地址最后一个字符不是 '\'，则添加'\'
			if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {
				OpenOffice_HOME += "\\";
			}
			// 启动OpenOffice的服务
			String command = OpenOffice_HOME + "program/soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
			pro= Runtime.getRuntime().exec(command);
			// connect to an OpenOffice.org instance running on port 8100
			connection= new SocketOpenOfficeConnection("127.0.0.1", 8100);
			connection.connect();

			// convert
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(inputFile, outputFile);
			return 0;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			connection.disconnect();
			// 关闭OpenOffice服务的进程
			pro.destroy();
		}
		return 1;
	}

	public static int PDFtoSWF(String pdfPath, String swfPath) {
		File srcPdf = new File(pdfPath);
		if (!srcPdf.exists()) {
			return -1; // 找不到源文件, 则返回-1
		}
		File targetSwfDir = new File(swfPath).getParentFile();
		if (targetSwfDir.exists()) {
			// 文件存在
			if (targetSwfDir.isFile()) {
				// 是标准文件（而不是目录）
				return -1; // 找不到源文件, 则返回-1
			}
		} else {
			// 文件不存在，尝试新建
			if (!targetSwfDir.mkdirs()) {
				// 新建目标目录失败
				return -1; // 找不到源文件, 则返回-1
			}
		}
		String command = PDF2SWF + " " + pdfPath + " " + swfPath + " -f -T 9 -G";
		Process process;
		// 错误消息
		// final StringBuilder errorMessage = new StringBuilder();
		try {
			System.out
					.println("Start converting File: " + pdfPath
							+ ", Swf will saved as :" + swfPath);
			process = Runtime.getRuntime().exec(command);
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			// 新建线程, 处理进程的标准输出
			new Thread() {
				public void run() {
					String message = "";
					try {
						while ((message = br.readLine()) != null) {
							System.out.println(message);
						}
					} catch (IOException e) {
					}
				}
			}.start();
			// 处理错误流
			final BufferedReader brError = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			// 新建线程, 处理进程的错误流
			new Thread() {
				public void run() {
					String message = "";
					try {
						while ((message = brError.readLine()) != null) {
							System.out.println(message);
							// errorMessage.append(message);
						}
					} catch (IOException e) {
					}
				}
			}.start();
		} catch (IOException e) {
			return 1;
		}

		// 等待进程执行完毕
		try {
			process.waitFor();
			process.destroy();
			System.out.println("Successed in converting File: " + pdfPath);
			return 0;
		} catch (Exception ex) {
			process.destroy();
			return 1;
		}
	}

}
