package com.hotent.webag.until;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotent.core.util.AppConfigUtil;

public class DownLoadFiles {
	/**
	 * 把打包好的文件放入临时文件夹中
	 * @param files 所有要打包的文件
	 * @param fileNameList 文件中文名
	 * @param fileDir 临时存储路径
	 */
	public static void downLoadFiles(List<File> files, List<String> fileNameList,String fileDir) {
		try {
            /**创建一个临时压缩文件，
             * 我们会把文件流全部注入到这个文件中
             * 这里的文件你可以自定义是.rar还是.zip
             */
            File file = new File(fileDir);
            if (!file.exists()){   
                file.createNewFile();   
            }
            //创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);   
            /**打包的方法我们会用到ZipOutputStream这样一个输出流,
             * 所以这里我们把输出流转换一下
             */
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            /**这个方法接受的就是一个所要打包文件的集合，
             * 还有一个ZipOutputStream*/
            zipFile(files,fileNameList, zipOut);
            zipOut.close();
            fous.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
	}
	/**
	 * 将临时文件夹中的文件打包下载
	 * @param fileDir
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public static HttpServletResponse downLoadFiles(String fileDir,HttpServletRequest request, HttpServletResponse response)throws Exception {
        try {
            /**创建一个临时压缩文件，
             * 我们会把文件流全部注入到这个文件中
             * 这里的文件你可以自定义是.rar还是.zip*/
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		String dir=AppConfigUtil.get("zipPath")!=null?AppConfigUtil.get("zipPath"):"C:/zip/";
        	String temp=dir+df.format(new Date())+".rar";
            File file = new File(temp);
            if (!file.exists()){   
                file.createNewFile();   
            }
            response.reset();
            //创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);   
            /**打包的方法我们会用到ZipOutputStream这样一个输出流,
             * 所以这里我们把输出流转换一下*/
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            /**这个方法接受的就是一个所要打包文件的集合，
             * 还有一个ZipOutputStream*/
            zipFile(fileDir, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file,response,fileDir);
        }catch (Exception e) {
            e.printStackTrace();
        }
        /**直到文件的打包已经成功了，
         * 文件的打包过程被我封装在FileUtil.zipFile这个静态方法中，
         * 稍后会呈现出来，接下来的就是往客户端写数据了*/
        return response ;
    }
	/**
	 * 将临时文件夹中的pdf文件打包下载
	 * @param fileDir
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public static HttpServletResponse downLoadPDFFiles(String fileDir,HttpServletRequest request, HttpServletResponse response)throws Exception {
        try {
            /**创建一个临时压缩文件，
             * 我们会把文件流全部注入到这个文件中
             * 这里的文件你可以自定义是.rar还是.zip*/
    		String dir=AppConfigUtil.get("zipPath")!=null?AppConfigUtil.get("zipPath"):"C:/zip/";
        	String temp=dir+"patentPDF.zip";
            File file = new File(temp);
            if (!file.exists()){   
                file.createNewFile();   
            }
            response.reset();
          //创建文件输出流
            FileOutputStream fous = new FileOutputStream(file);   
            /**打包的方法我们会用到ZipOutputStream这样一个输出流,
             * 所以这里我们把输出流转换一下*/
            ZipOutputStream zipOut = new ZipOutputStream(fous);
            /**这个方法接受的就是一个所要打包文件的集合，
             * 还有一个ZipOutputStream*/
           zipFile(fileDir, zipOut);
            zipOut.close();
            fous.close();
            return downloadZip(file,response,fileDir);
        }catch (Exception e) {
            e.printStackTrace();
        }
        /**直到文件的打包已经成功了，
         * 文件的打包过程被我封装在FileUtil.zipFile这个静态方法中，
         * 稍后会呈现出来，接下来的就是往客户端写数据了*/
        return response ;
    }
    /**
     * 将临时文件夹中的全部文件打成压缩包 
     * @param fileDir 临时文件夹目录 
     * @param org.apache.tools.zip.ZipOutputStream  
     */
     public static void zipFile(String fileDir,ZipOutputStream outputStream) {
         File file = new File(fileDir);
         zipFile(file,null, outputStream);
     }
     
   /**
    * 把接受的全部文件打成压缩包 
    * @param List<File>;  
    * @param org.apache.tools.zip.ZipOutputStream  
    */
    public static void zipFile(List files,List<String> fileNameList,ZipOutputStream outputStream) {
        int size = files.size();
        for(int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            String fileName=fileNameList.get(i);
            zipFile(file,fileName, outputStream);
        }
    }
    /**
     * 将文件输出到前台页面，使用户可以选择导出位置
     * @param file
     * @param response
     * @return
     */
    public static HttpServletResponse downloadZip(File file,HttpServletResponse response,String fileDir) {
        try {
	        // 以流的形式下载文件。
	        InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
	        byte[] buffer = new byte[fis.available()];
	        fis.read(buffer);
	        fis.close();
	        // 清空response
	        response.reset();
	
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        response.setContentType("application/octet-stream");
	
	        //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
	        response.setHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode(file.getName(), "UTF-8"));
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();
        } catch (IOException ex) {
        	ex.printStackTrace();
        }finally{
             try{
                File f = new File(file.getPath());
                f.delete();
                File dirFile=new File(fileDir);
                deleteDir(dirFile);
             }catch (Exception e) {
                e.printStackTrace();
             }
        }
        return response;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
   /**  
    * 根据输入的文件与输出流对文件进行打包
    * @param File
    * @param org.apache.tools.zip.ZipOutputStream
    */
    public static void zipFile(File inputFile,String fileName,ZipOutputStream ouputStream) {
        try {
            if(inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，
                 * 至于目录的打包正在研究中*/
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    //org.apache.tools.zip.ZipEntry
                    ZipEntry entry = null;
                    if(fileName!=null && !"".equals(fileName)){
                    	entry = new ZipEntry(fileName);
                    }else{
                    	entry = new ZipEntry(inputFile.getName());
                    }
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据   
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象   
                    bins.close();
                    IN.close(); 
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i],fileName, ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
