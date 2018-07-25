package com.umpay.channelRouting.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * 将json串和对象之间相互转化的常用工具类
 * 
 * @author xuxiaojia
 */
public class JacksonUtil {
	private static ObjectMapper mapper=new ObjectMapper();;

	/**
	 * 将json串转换为指定的java对象 并创建该对象的实例进行返回
	 * @param jsonString  要转换的json字符串
	 * @param classOft    要转换成的对象的类
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws Exception
	 */
	public static <T> T jsonString2Obj(String jsonString, Class<T> classOft) throws JsonParseException, JsonMappingException, IOException {
		T t = mapper.readValue(jsonString, classOft);
		return t;
	}
	
	/**
	 * 将json串转换为指定的java对象 并创建该对象的集合进行返回
	 * @param src
	 * @param collectionClass
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jacksonToCollection(String src,Class<?> collectionClass, Class<?>... valueType)  
            throws Exception {  
        JavaType javaType= mapper.getTypeFactory().constructParametricType(collectionClass, valueType);   
        return (T)mapper.readValue(src, javaType);  
    }  
	
	/**
	 * 将java对象转换为json字符串
	 * @param t  java对象
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> String obj2json(T t) throws JsonGenerationException, JsonMappingException, IOException{
		return mapper.writeValueAsString(t);
	}

}
