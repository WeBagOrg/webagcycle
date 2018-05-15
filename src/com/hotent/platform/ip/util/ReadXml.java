package com.hotent.platform.ip.util;
import java.lang.reflect.Method;
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;
import java.util.Map;  

import org.dom4j.Attribute;
import org.dom4j.Document;  
import org.dom4j.DocumentException;  
import org.dom4j.DocumentHelper;  
import org.dom4j.Element;  

import com.hotent.platform.model.ip.TreeModel;

public class ReadXml {
	
	 public static Map<String, String> readStringXmlOut(String xml) {  
	     Map<String, String> map = new HashMap<String, String>();  
	     Document doc = null;  
	     try {  
	         doc = DocumentHelper.parseText(xml); // 将字符串转为XML  
	         Element rootElt = doc.getRootElement(); // 获取根节点  
	         Iterator<?> iter = rootElt.elementIterator("Data"); // 获取Data
	         // 遍历head节点  
	         while (iter.hasNext()) {  
	             Element recordEle = (Element) iter.next();  
	             String an = recordEle.attributeValue("an"); // 拿到申请号
	             map.put("an", an);  
	             
	             String pngPath = recordEle.attributeValue("pngPath"); // 全文路径
	             map.put("pngPath", pngPath);
	             
	             String pages = recordEle.attributeValue("pages"); // 总页数
	             map.put("pages", pages);
	          }  
	      } catch (DocumentException e) {  
	          e.printStackTrace();  
	 
	      } catch (Exception e) {  
	          e.printStackTrace();  
	      }  
	      return map;  
	  }  
	 /**
	  * 获取结构树
	  * @param list
	  * @param modelClass
	  * @param elements
	  * @throws Exception
	  */
	 @SuppressWarnings("unchecked")
	 public static void getTreeDate(List<TreeModel> list,Class<?> modelClass,List<Element> elements) throws Exception{
		 for(int i=0;i<elements.size();i++){
			  Element itemElement=elements.get(i);
			  TreeModel model=(TreeModel) modelClass.newInstance();
			  //构造树节点的对象
			  Iterator<Attribute> iter=itemElement.attributeIterator();
			  while(iter.hasNext()){
				  Attribute attribute=iter.next();
				  if(attribute!=null){
					  //使用反射机制实现
					  String methodStr=attribute.getName().substring(0,1).toUpperCase()+attribute.getName().substring(1);
					  Method method=model.getClass().getMethod("set"+methodStr,new Class[]{String.class});
					  if(method!=null){
						  method.invoke(model, attribute.getValue());
					  }
				  }
			  }
			  list.add(model);
		  }
	 }
}
