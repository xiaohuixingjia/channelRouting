package com.umpay.channelRouting.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


public class FileOperatorUtils {

	/**
	 * 创建一般文件/创建文件夹
	 * 
	 * @param destFileName
	 */
	public static void createFileForMkdirs(String destFileName) {
		try {
			File generalFile = new File(destFileName);
			// 如果文件存在,则删掉文件
			if (generalFile.exists()) {
				generalFile.delete();
			}
			// 创建文件夹
			generalFile.mkdirs();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	/**
	 * 重命名文件名称
	 * 
	 * @param newFileName
	 */
	public static void renameGeneralFile(String srcFileName, String newFileName) {
		try {
			File generalFile = new File(srcFileName);
			if (generalFile.exists()) {
				generalFile.renameTo(new File(newFileName));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 删除指定文件
	 * 
	 * @param srcFileName
	 */
	public static void deleteGeneralFile(String srcFileName) {
		try {
			File generalFile = new File(srcFileName);
			if (generalFile.exists()) {
				generalFile.delete();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param destFileName
	 */
	public static void deleteFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹所有文件
	 * 
	 * @param destFileName
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				deleteFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 创建文件
	 * 
	 * @param destFileName
	 * @throws IOException 
	 */
	public static void createGeneralFile(String destFileName) throws IOException {
			File generalFile = new File(destFileName);
			// 如果文件存在,return
			if (generalFile.exists()) {
				return;
			}
			new File(generalFile.getParent()).mkdirs();
			// 创建文件
			generalFile.createNewFile();
	}

	/**
	 * 写Txt文件 r
	 * 
	 * @param newStr
	 *            新内容
	 * @throws IOException
	 */
	public static void writeTxtFile(String newStr, String filenameTemp)
			throws Exception {
		BufferedWriter buf = null;
		try {
			File file = new File(filenameTemp);
			buf = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			buf.write(newStr);
			buf.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if(buf!=null){
				try {
					buf.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static final byte[] compress(byte[] data) {
		if (data == null) {
			return null;
		}
		if (data.length == 0) {
			return new byte[0];
		}
		Deflater compressor = new Deflater();
		compressor.setLevel(9);
		compressor.setInput(data);
		compressor.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		byte[] buf = new byte[1024];
		while (!compressor.finished()) {
			int count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}
		try {
			bos.close();
		} catch (IOException e) {
		    
		}

		compressor.end();
		return bos.toByteArray();
	}
	
	public static String decompression(byte[] data) {
		
		 Inflater decompresser = new Inflater();
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
		 decompresser.setInput(data);
		 byte[] result = new byte[1024];
		 try {
			while (!decompresser.finished()) {
				int resultLength = decompresser.inflate(result);
				bos.write(result, 0, resultLength);
			}
			String outputString = bos.toString();
			bos.close();
			
			return outputString;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 decompresser.end();
		 return null;
	}
	
	/**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            StringBuilder sb = new StringBuilder();
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
            	sb.append(tempString);
                //System.out.println("line " + line + ": " + tempString);
                //line++;
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return "";
    }
	
	public static void main(String[] args) {
//		String test = "ceshiceshi测试743178&*（）aaa……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）……ceshiceshi测试743178&*（）xx……";
//		byte[] bates = compress(test.getBytes());
//		System.out.println(decompression(bates));
		
		String a = "KLog\0012.3\001imei\001860108022378194\0010123456789ABCDEF\0011.0\0011\001cic\0014.1.2\001zh\001cn\001Lenovo A820e\001LENOVO\001540x960\001550560109\0010\0011389341833092\0013\001\001\001__INITIAL__\001initial\0010\0010.000000\001\001\001\001\001\001\001\001\001\001\0011876956662\00113\0010\0010\0010\001860108022378194\001460030966311714\0017";
		String[] arry1 = a.split("\001");
		
		String b = "2.4.3\0010\001MHNLC6UPB7EU\001864736012220467\001\001613963153\001480x728\0012049011854\0011970-01-01 08:00:00.000\0011970-01-01 08:00:00.000\0011970-01-01 08:00:00.000\0012013-12-30 10:47:22.114\0018\001Sysopt_RP\001ProcessInfo\0012013-12-30%2010%3A38%3A09%010%01680%0110056%0110056%010%0198%01com.lenovo.worldtimewidget%01com.lenovo.worldtimewidget%01%E4%B8%96%E7%95%8C%E6%97%B6%E9%92%9F%014.5078125%012892%0123%01S%01300%010%01%0120%010%014211008%01357.0%01430.0%010.0%010.0%010%010%0121.800781%01459.6953%014096.0%010.0%01com.lenovo.worldtimewidget\0010\001\001\0010.1\0011\0014.0.3\001zh\001cn\001Lenovo S850e\001\001LENOVO\001219.143.8.242\0013\001imei\001864736012220467\0012013-12-30 10:47:11.501\001CIC\0012013\001FOURTH\00112\00152\00130\00110\001CN\00122\001Beijing\00139.928894\001116.388306\001TEL\001\001\001\0010\001\001864736012220467\001460036110945747\0016\0012013-12-30 10:47:22.114\001A1000030CAE9879\001\0012013-12-30";
		String[] arry2 = b.split("\001");
		
		System.out.println(arry1.length);
		System.out.println(arry2.length);
	}

}
