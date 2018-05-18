package com.hotent.platform.ip.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.cxf.common.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;



import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hotent.core.util.DateFormatUtil;




public class ExcelOperationUtil {
	/**
	 * 导入Excel
	 * @param c
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public <T> Set<T> importExcel2Set(Class<T> c,File file,String[] fieldArray)throws Exception{
		Set<T> set=null;
		if(isExcel(file)){
			set=readXls2Set(c,file,fieldArray);
		}
		return set;
	}
	/**
	 * 导入excel
	 * @param c
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public <T> Set<T> importExcel2Set(Class<T> c,InputStream in,String[] fieldArray)throws Exception{
		Set<T> set=readFromInputStream2Set(c,in,fieldArray);
		return set;
	}
	
	/**
	 * 导入Excel
	 * 提供一个list的方法，list是有序的集合，set是无序的。但set可以过滤掉重复记录
	 * @param c
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> importExcel2List(Class<T> c,File file,String[] fieldArray)throws Exception{
		List<T> list=null;
		if(isExcel(file)){
			list=readXls2List(c,file,fieldArray);
		}
		return list;
	}
	/**
	 * 导入excel
	 * @param c
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> importExcel2List(Class<T> c,InputStream in,String[] fieldArray)throws Exception{
		List<T> list=readFromInputStream2List(c,in,fieldArray);
		return list;
	}
	
	/**
	 * 判断是否是Excel文档
	 * @param file
	 * @return
	 */
	private static boolean isExcel(File file){
		if("xls".equals(getFileType(file))||"xlsx".equals(getFileType(file))){
			return true;
		}
		return false;
	}
	/**
	 * 判断文档类型
	 * @param file
	 * @return
	 */
	private static String getFileType(File file){
		String suffix=file.getName().substring(file.getName().lastIndexOf(".")+1);
		if(StringUtils.isEmpty(suffix)){
			suffix=FileTypeUtil.getFileTypeByFile(file);
		}
		return suffix;
	}
	/**
	 * 读入excel文档
	 * @param file
	 */
	public <T> Set<T> readXls2Set(Class<T> c,File file,String[] orderField)throws Exception{
		InputStream is = new FileInputStream(file);//读入文件
        return readFromInputStream2Set(c, is, orderField);
	}
	
	/**
	 * 读入excel文档
	 * @param file
	 */
	public <T> List<T> readXls2List(Class<T> c,File file,String[] orderField)throws Exception{
		InputStream is = new FileInputStream(file);//读入文件
        return readFromInputStream2List(c, is, orderField);
	}
	/**
	 * 
	 * @param c
	 * @param in
	 * @param orderField
	 * @return
	 * @throws Exception
	 */
	private <T> List<T> readFromInputStream2List(Class<T> c,InputStream in ,String[] orderField)throws Exception{
		List<T> list=new ArrayList<T>();
		readFromInputStream(c,in,orderField,list);
        return list;
	}
	
	
	/**
	 * 通过输入流获取excel文件
	 * @param c
	 * @param in
	 * @param orderField
	 * @return
	 * @throws Exception
	 */
	private <T> Set<T> readFromInputStream2Set(Class<T> c,InputStream in,String[] orderField)throws Exception{
		Set<T> set=new HashSet<T>();
		readFromInputStream(c,in,orderField,set);
        return set;
	}
	
	/**
	 * 读取excel数据到一个集合中，并封装成对象
	 * @param c
	 * @param in
	 * @param orderField
	 * @param collection
	 * @return
	 * @throws Exception
	 */
	private <T> Collection<T> readFromInputStream(Class<T> c,InputStream in,String[] orderField,Collection<T> collection)throws Exception{
      //  HSSFWorkbook hssfWorkbook = //构造Excel工作簿
        
        Workbook hssfWorkbook = null;        
        if (true)  
        {  
        	hssfWorkbook = new HSSFWorkbook(in); 
        }  
        /*else  2007excel构造使用
        {  
        	hssfWorkbook = new XSSFWorkbook(in);  
        }*/  
        
        Method[] setMethod=getOrSetMethod(orderField,"set",c);
        Field[] fieldTypes=getFiledTypes(c,orderField);
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {//获取一个excel工作簿中的sheet数量，并循环每个sheet
            Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);//获取sheet
            if (hssfSheet == null) {
                continue;
            }
            T model = null;//构造对象
            // 循环行Row，略过第一行（标题行）,excel默认第一行是标题行，下标为0，所以要跳过第一行
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {//获取一个sheet的行数
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null||null==hssfRow.getCell(0)) {
                    continue;
                }
                model = c.newInstance();
                for(int celNum=0;celNum<orderField.length;celNum++){//循环excel表格的列字段
                	Cell cellValue = hssfRow.getCell(celNum);//获取值
                	if(null==cellValue||StringUtils.isEmpty(cellValue.toString())) break;
                	
                	Object obj=getValue(cellValue);
                	/*if(fieldTypes[celNum].getType()==double.class){
                		//fieldTypes[celNum].getDouble(obj)无法访问私有成员
                		setMethod[celNum].invoke(model,new Object[]{new Double(obj.toString().trim())});
                	}
                	else if(fieldTypes[celNum].getType()==int.class){
                		//fieldTypes[celNum].getDouble(obj)无法访问私有成员
                		if(obj.toString().indexOf(".")!=-1)
                			setMethod[celNum].invoke(model,new Object[]{new Integer(obj.toString().substring(0,obj.toString().indexOf(".")))});
                		else
                			setMethod[celNum].invoke(model,new Object[]{new Integer(obj.toString().trim())});
                	}
                	else if(fieldTypes[celNum].getType()==Date.class){
                		//fieldTypes[celNum].getDouble(obj)无法访问私有成员
                		if(obj.toString().indexOf(".")!=-1)
                			setMethod[celNum].invoke(model,new Object[]{DateFormatUtil.parse(obj.toString().trim(),"yyyy.MM.dd")});
                	}
                	else{
                		setMethod[celNum].invoke(model,new Object[]{obj});
                	}*/
                	if(fieldTypes[celNum].getType()==double.class){
                		//fieldTypes[celNum].getDouble(obj)无法访问私有成员
                		setMethod[celNum].invoke(model,new Object[]{new Double(obj.toString().trim())});
                	}
                	else if(fieldTypes[celNum].getType()==int.class){
                		//fieldTypes[celNum].getDouble(obj)无法访问私有成员
                		if(obj.toString().indexOf(".")!=-1)
                			setMethod[celNum].invoke(model,new Object[]{new Integer(obj.toString().substring(0,obj.toString().indexOf(".")))});
                		else
                			setMethod[celNum].invoke(model,new Object[]{new Integer(obj.toString().trim())});
                	}
                	else if(fieldTypes[celNum].getType()==Date.class){
                		//fieldTypes[celNum].getDouble(obj)无法访问私有成员
                		if(obj instanceof Date){
                			setMethod[celNum].invoke(model,new Object[]{obj});
                		}
                		else{
                			if(obj.toString().indexOf("-")!=-1)
                    			setMethod[celNum].invoke(model,new Object[]{DateFormatUtil.parse(obj.toString().trim(),"yyyy-MM-dd")});
                    		else if(obj.toString().indexOf("/")!=-1)
                    			setMethod[celNum].invoke(model,new Object[]{DateFormatUtil.parse(obj.toString().trim(),"yyyy/MM/dd")});
                    		else if(obj.toString().indexOf(".")!=-1)
                    			setMethod[celNum].invoke(model,new Object[]{DateFormatUtil.parse(obj.toString().trim(),"yyyy.MM.dd")});
                		}
                	}
                	else{
                		setMethod[celNum].invoke(model,new Object[]{obj});
                	}
                }
                collection.add(model);
            }
        }
        return collection;
	}
	/**
	 * 获取对象中的get和set方法，boolean类型也使用set方法，而不是使用is开头的方法
	 * 注：需要为相应的boolean类型提供set方法
	 * @param field
	 * @param type
	 * @return
	 */
	private Method[] getOrSetMethod(String[] field,String type,Class<?> c)throws Exception{
		Method[] temp=new Method[field.length];
		for(int i=0;i<field.length;i++){
			String methodName=type+ field[i].substring(0, 1).toUpperCase() + field[i].substring(1);
			if(type.equals("set")){
				Field f=c.getDeclaredField(field[i]);
				temp[i]=c.getDeclaredMethod(methodName,f.getType());
			}
			else{
				temp[i]=c.getDeclaredMethod(methodName,new Class[]{});
			}
		}
		return temp;
	}
	/**
	 * 获取字段的类型
	 * @param c
	 * @param field
	 * @return
	 * @throws Exception
	 */
	private Field[] getFiledTypes(Class<?> c,String[] field)throws Exception{
		Field[] temp=new Field[field.length];
		for(int i=0;i<field.length;i++){
			Field f=c.getDeclaredField(field[i]);
			temp[i]=f;
		}
		return temp;
	}
	
	/**
	 * 获取对象中的get和set方法，boolean类型也使用set方法，而不是使用is开头的方法
	 * 注：需要为相应的boolean类型提供set方法
	 * @param field
	 * @param type
	 * @return
	 */
	public String[] getOrSetMethodStr(String[] field,String type){
		String temp[]=new String[field.length];
		for(int i=0;i<field.length;i++){
			String getMethodName = type+ field[i].substring(0, 1).toUpperCase() + field[i].substring(1);//利用反射获取get方法名
			temp[i]=getMethodName;
		}
		return temp;
	}
	/**
	 * 获取excel表中的值
	 * @param cellValue
	 * @return
	 */
	private Object getValue(Cell cellValue) {
        if (cellValue.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return cellValue.getBooleanCellValue();
        } 
        else if (cellValue.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
        	if(HSSFDateUtil.isCellDateFormatted(cellValue)){
        		double d = cellValue.getNumericCellValue();  
                return HSSFDateUtil.getJavaDate(d);
        	}
            // 返回数值类型的值
            return cellValue.getNumericCellValue();
        } else {
            // 返回字符串类型的值
            return cellValue.getStringCellValue();
        }
    }
	/**
	 * 导出Excel
	 * @param <T>
	 * @param title
	 * @param headers
	 * @param dataset
	 * @param out
	 * @param pattern
	 */
	public <T> void exportExcel(String title, String[] headers,String[] field, List<T> models, OutputStream out, String pattern) {
	      // 声明一个工作薄
	      HSSFWorkbook workbook = new HSSFWorkbook();
	      // 生成一个表格
	      HSSFSheet sheet = workbook.createSheet(title);
	      // 设置表格默认列宽度为15个字节	    
	     // sheet.autoSizeColumn(6);	   
	      sheet.setDefaultColumnWidth(20);
	      // 生成一个样式
	      HSSFCellStyle style = workbook.createCellStyle();
	      // 设置样式，用于标题行显示的样式
	      //客户觉得背景颜色太丑 去掉 
	    /*  style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//前景色为天蓝色
	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充模式                         
*/	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
	      // 生成一个字体
	      HSSFFont font = workbook.createFont();
//	      font.setColor(HSSFColor.VIOLET.index);  //客户觉得难看，去掉颜色
	      font.setFontHeightInPoints((short)12);
	      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体
	      // 把字体应用到当前的样式
	      style.setFont(font);
	      // 生成并设置另一个样式，正文显示的样式
	      HSSFCellStyle style2 = workbook.createCellStyle();
	      //客户觉得背景颜色太丑 去掉 
	   /*   style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
	      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充模式
*/	      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
	      // 生成另一个字体
	      HSSFFont font2 = workbook.createFont();
	      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	      // 把字体应用到当前的样式
	      style2.setFont(font2);
	      // 声明一个画图的顶级管理器
	      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();//用于向excel中插入图片，patriarch:家长，鼻祖
	      // 定义注释的大小和位置,详见文档
	      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	      // 设置注释内容
	      comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
	      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	      comment.setAuthor("leno");
	      //产生表格标题行
	      HSSFRow row = sheet.createRow(0);//创建一行
	      for (int i = 0; i < headers.length; i++) {//headers为指定生产的标题行的头部
	         HSSFCell cell = row.createCell(i);//在行中生成单元格
	         cell.setCellStyle(style);//设置单元格的样式
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);//为文本应用丰富的样式
	         cell.setCellValue(text);//设置单元格值
	      }
	      //遍历集合数据，产生数据行
	      Iterator<T> it = models.iterator();//展示数据区域
	      int loopQuantity = 0;
	      int rowIndex = 0;//合并行标识
	      boolean flag = false;//判断标记
	      int index = 0;//行标记,记录创建到第几行了
	      String applyCodeString = "";//记录申请号
	     // PatentExcelModel excelModel = null;
	      while (it.hasNext()) {
	         index++;
	         row = sheet.createRow(index);//创建行
	         T t = (T) it.next();//利用泛型，解析出list中的对象类型
	         //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
	         //Field[] fields = t.getClass().getDeclaredFields();//获取对象的所有属性
	         String[] getMethodNames=getOrSetMethodStr(field,"get");
	         for (int i = 0; i < getMethodNames.length; i++) {
	            HSSFCell cell = row.createCell(i);
	            cell.setCellStyle(style2);//设置单元格的样式
	            //Field field = fields[i];
	            //String fieldName = field.getName();
	            //String getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);//利用反射获取get方法名
	            try {
	                Class tCls = t.getClass();
	                Method getMethod = tCls.getMethod(getMethodNames[i], new Class<?>[]{});//反射获取方法对象
	                Object value = getMethod.invoke(t, new Object[] {});
	                if (i == 0 && null != value) {
	                	applyCodeString = value.toString();
	                }
	                //判断值的类型后进行强制类型转换
	                String textValue = null;
	                if (value instanceof Boolean) {//解析boolean型
	                   boolean bValue = (Boolean) value;
	                   textValue = "男";
	                   if (!bValue) {
	                      textValue ="女";
	                   }
	                } else if (value instanceof Date) {//解析日期型
	                   Date date = (Date) value;
	                   SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	                   textValue = sdf.format(date);
	                }  else if (value instanceof byte[]) {
	                   // 有图片时，设置行高为60px;
	                   row.setHeightInPoints(60);
	                   // 设置图片所在列宽度为80px,注意这里单位的一个换算
	                   //sheet.setColumnWidth(i, (short) (35.7 * 80));
	                   sheet.autoSizeColumn(i);
	                   byte[] bsValue = (byte[]) value;
	                   //It anchors against a top-left and buttom-right cell
	                   HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,1023, 255, (short) i, index, (short) i, index);
	                   anchor.setAnchorType(2);
	                   patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
	                } else{
	                	if (value == null) {
	                		textValue = "";
	                	} else {
	 	                   //其它数据类型都当作字符串简单处理
	 	                   textValue = value.toString();
	                	}
	                }
	                //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
	                if(textValue!=null){
                      //  HSSFRichTextString richString = new HSSFRichTextString(textValue);
                     //   HSSFFont font3 = workbook.createFont();
                        //客户觉得颜色不好看 暂时不要颜色
                     //   font3.setColor(HSSFColor.BLUE.index);
                     //   richString.applyFont(font3);
                       // cell.setCellValue(richString);
	                	cell.setCellValue(textValue);
	                }
	            } catch (SecurityException e) {
	                e.printStackTrace();
	            } catch (NoSuchMethodException e) {
	                e.printStackTrace();
	            } catch (IllegalArgumentException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            } catch (InvocationTargetException e) {
	                e.printStackTrace();
	            } finally {
	                //清理资源
	            }
	         }
	    	 /* Search excelModel = null;
	 		  if(index != models.size()){
	 			  excelModel = (Search)models.get(index);	
	 		  }
	 		  if (null != excelModel) {
		 		  flag = applyCodeString.equals(excelModel);		//下一条记录不等于当前的记录对比值时进行相应的处理
	 		  }
	 		  if(!flag || excelModel == null){
		 		  loopQuantity ++;
		 		  rowIndex = index + 1;
				  //进行跨列处理
				  sheet.addMergedRegion(new CellRangeAddress(rowIndex-loopQuantity,rowIndex-1,(short)0,(short)0));
				  sheet.addMergedRegion(new CellRangeAddress(rowIndex-loopQuantity,rowIndex-1,(short)1,(short)1));
				  sheet.addMergedRegion(new CellRangeAddress(rowIndex-loopQuantity,rowIndex-1,(short)2,(short)2));
				  sheet.addMergedRegion(new CellRangeAddress(rowIndex-loopQuantity,rowIndex-1,(short)3,(short)3));
				  sheet.addMergedRegion(new CellRangeAddress(rowIndex-loopQuantity,rowIndex-1,(short)4,(short)4));
				  sheet.addMergedRegion(new CellRangeAddress(rowIndex-loopQuantity,rowIndex-1,(short)5,(short)5));
		 		  //清空统计数据
				  loopQuantity = 0;
	 		  } else {
		 		  loopQuantity ++;
			}*/
	      }
	      try {
	          workbook.write(out);
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
	/**
	 * 获取专利列表
	 * @param in
	 * @param type 
	 * @return
	 * @throws IOException 
	 */
	public List<String> getPatentList(InputStream in, String type) throws IOException{
		List<String> appNumbers=new ArrayList<String>();
		 Workbook hssfWorkbook = null;        
	        if ("xls".equals(type))  
	        {  
	        	hssfWorkbook = new HSSFWorkbook(in); 
	        }  
	        else  
	        {  
	        	hssfWorkbook = new XSSFWorkbook(in);  
	        }  
		//HSSFWorkbook hssfWorkbook = new HSSFWorkbook(in);//构造Excel工作簿
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {//获取一个excel工作簿中的sheet数量，并循环每个sheet
            Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);//获取sheet
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row，略过第一行（标题行）,excel默认第一行是标题行，下标为0，所以要跳过第一行
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {//获取一个sheet的行数
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null||null==hssfRow.getCell(0)) {
                    continue;
                }
                //只有1列
            	Cell cellValue = hssfRow.getCell(0);//获取值
            	if(null==cellValue||StringUtils.isEmpty(cellValue.toString())) break;
                appNumbers.add(cellValue.toString());
            }
        }
		return appNumbers;
	}
}
