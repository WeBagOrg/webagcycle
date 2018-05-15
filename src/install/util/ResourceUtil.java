package install.util;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ResourceUtil {
	private final static String propertyFiles="/install.properties";
	private static Properties pro=null;
	
	private static synchronized void init() throws Exception{
		if(null==pro){
			//先判断文件是否存在
			URI uri = new URI(ResourceUtil.class.getResource("/").getPath());
			File file=new File(uri.getPath()+propertyFiles);
			if(!file.exists()){
				throw new Exception("install.properties dose not exists!");
			}
			Resource resource=new ClassPathResource(propertyFiles);
			pro=PropertiesLoaderUtils.loadProperties(resource);
		}
	}
	
	public static String getPropertyValue(String name){
		try {
			if(pro==null){
				init();
			}
			return pro.getProperty(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getPropertyValue("system.name"));
	}
}
