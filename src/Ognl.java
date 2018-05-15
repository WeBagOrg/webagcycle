import com.hotent.core.util.BeanUtils;

/**
 * Ognl工具类
 * <p>
 * 主要是为了在ognl表达式访问静态方法时可以减少长长的类名称编写 Ognl访问静态方法的表达式
 * </p>
 * 
 * @class@method(args) 示例使用:
 * 
 *                     <pre>
 * 	&lt;if test=&quot;@Ognl@isNotEmpty(userId)&quot;&gt;
 * 	and user_id = #{userId}
 * &lt;/if&gt;
 * </pre>
 * 
 * @author badqiu
 */
public class Ognl   {
	/**
	 * 可以用于判断 Map,Collection,String,Array,Long是否为空
	 * 
	 * @param o
	 *            java.lang.Object.
	 * @return boolean.
	 */
	public static boolean isEmpty(Object o) throws IllegalArgumentException {
		return BeanUtils.isEmpty(o);
	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	/**
	 * 可以用于判断Long类型是否不为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Long o) {
		return !isEmpty(o);
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNumber(Object o) {
		return BeanUtils.isNumber(o);
	}

	/**
	 * 判断是否相等
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equals(Object o1, Object o2) {
		return o1.equals(o2);
	}
	/**
	 * 判断是否相等
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean notEquals(Object o1, Object o2) {
		return !equals(o1, o2);
	}
	
	//ipph add 2015-10-16
	/**
     * 判断一个对象是否是数组
     * @param obj
     * @return
     */
    public static boolean isStringArray(Object obj) {
        if(obj== null ) 
            return false;
        if(obj instanceof Object[])
        	return true;
        return false;
    }
    
    public static boolean isStringValue(Object obj) {
        if(obj== null ) 
            return false;
        if(obj instanceof String && !isBlank(obj))
        	return true;
        return false;
    }
    /**
     * 是否包含
     * @param obj
     * @param temp
     * @return
     */
    public static boolean isContain(Object obj,String temp) {
        if(obj== null ) 
            return false;
        else if(obj instanceof String && !isBlank(obj)&&obj.equals(temp))
        	return true;
        else if(obj instanceof Object[]){
        	Object[] tempObj=(Object[]) obj;
        	for(int i=0;i<tempObj.length;i++){
        		if(tempObj[i].equals(temp))
        			return true;
        	}
        }
        return false;
    }
    /**
     * 数组中是否大于给定值
     * @param obj
     * @param temp
     * @return
     */
    public static boolean isLess(Object obj,String temp) {
        if(obj== null ) 
            return false;
        else if(obj instanceof Object[]){
        	Object[] tempObj=(Object[]) obj;
        	for(int i=0;i<tempObj.length;i++){
        		if(Integer.parseInt((String)tempObj[i])<Integer.parseInt(temp))
        			return true;
        	}
        }
        return false;
    }
    public static boolean isBlank(Object o) {
        if(o == null)
                return true;
        if(o instanceof String) {
                String str = (String)o;
                return isBlank(str);
        }
        return false;
    }

    public static boolean isBlank(String str) {
        if(str == null || str.length() == 0) {
                return true;
        }
       
        for (int i = 0; i < str.length(); i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                        return false;
                }
        }
        return true;
    }
}
