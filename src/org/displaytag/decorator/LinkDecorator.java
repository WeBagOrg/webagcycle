package org.displaytag.decorator;

import java.util.List;

import org.displaytag.decorator.TableDecorator;

import com.hotent.platform.model.ip.IpPatent;
import com.hotent.platform.model.ip.IpProduct;

public class LinkDecorator extends TableDecorator {
	
	public String getPatentLink(){
		IpProduct product=(IpProduct)getCurrentRowObject();
		StringBuffer link=new StringBuffer();
		if(product!=null&&product.getPatentList()!=null&&product.getPatentList().size()>0){
			List<IpPatent> patentList=product.getPatentList();
			for(IpPatent patent:patentList){
				link.append("<a href='/bpmx/platform/ip/patentManger/edit.ht?id="+patent.getId()+"'>");
				link.append(patent.getTitle());
				link.append("</a>");
				link.append("\t");
			}
		}
		return link.toString();
	}
}
