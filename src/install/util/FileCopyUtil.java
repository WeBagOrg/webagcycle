package install.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileCopyUtil {
	// 定义缓冲区
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	// 调用函数
	public static void copyDirectory(File srcDir, File destDir)
			throws IOException {
		copyDirectory(srcDir, destDir, true);
	}

	public static void copyDirectory(File srcDir, File destDir,
			boolean preserveFileDate) throws IOException {
		copyDirectory(srcDir, destDir, null, preserveFileDate);
	}

	public static void copyDirectory(File srcDir, File destDir,FileFilter filter) throws IOException {
		copyDirectory(srcDir, destDir, filter, true);
	}

	// 开始复制目录
	public static void copyDirectory(File srcDir, File destDir,FileFilter filter, boolean preserveFileDate) throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (srcDir.exists() == false) {
			throw new FileNotFoundException("Source '" + srcDir+ "' does not exist");
		}
		if (srcDir.isDirectory() == false) {
			throw new IOException("Source '" + srcDir+ "' exists but is not a directory");
		}
		/*if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
			throw new IOException("Source '" + srcDir + "' and destination '"+ destDir + "' are the same");
		}*/
		if(srcDir.isDirectory()&&!destDir.getName().equals(srcDir.getName())){
			destDir=new File(destDir+File.separator+srcDir.getName());//追加上src目录了
		}

		List<String> exclusionList = null;
		//getCanonicalPath返回目录名称，destDir目录是srcDir目录的子目录。即destDir目录包含srcDir目录
		if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
			File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
			if (srcFiles != null && srcFiles.length > 0) {
				exclusionList = new ArrayList<String>(srcFiles.length);//不必拷贝的文件
				for (int i = 0; i < srcFiles.length; i++) {
					File copiedFile = new File(destDir, srcFiles[i].getName());
					exclusionList.add(copiedFile.getCanonicalPath());
				}
			}
		}
		doCopyDirectory(srcDir, destDir, filter, preserveFileDate,exclusionList);
	}

	// 复制目录
	private static void doCopyDirectory(File srcDir, File destDir,FileFilter filter, boolean preserveFileDate, List<String> exclusionList)
			throws IOException {
		if (destDir.exists()) {
			if (destDir.isDirectory() == false) {
				throw new IOException("Destination '" + destDir
						+ "' exists but is not a directory");
			}
		} else {
			if (destDir.mkdirs() == false) {
				throw new IOException("Destination '" + destDir
						+ "' directory cannot be created");
			}
			if (preserveFileDate) {
				destDir.setLastModified(srcDir.lastModified());
			}
		}
		if (destDir.canWrite() == false) {
			throw new IOException("Destination '" + destDir
					+ "' cannot be written to");
		}
		// recurse
		File[] files = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + srcDir);
		}
		for (int i = 0; i < files.length; i++) {
			File copiedFile = new File(destDir, files[i].getName());
			System.out.println(files[i].getCanonicalPath());
			if (exclusionList == null
					|| !exclusionList.contains(files[i].getCanonicalPath())) {
				if (files[i].isDirectory()) {//如果是目录则递归
					doCopyDirectory(files[i], copiedFile, filter,
							preserveFileDate, exclusionList);
				} else {//文件则拷贝
					doCopyFile(files[i], copiedFile, preserveFileDate);
				}
			}
		}
	}

	// 开始复制
	public static void doCopyFile(File srcFile, File destFile,boolean preserveFileDate) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is a directory");
		}

		FileInputStream input = new FileInputStream(srcFile);
		try {
			FileOutputStream output = new FileOutputStream(destFile);
			try {
				copy(input, output);
			} finally {
				try {
					output.close();
				} catch (IOException ioe) {
					// do nothing
				}
			}
		} finally {
			try {
				input.close();
			} catch (IOException ioe) {
				// do nothing
			}
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '"
					+ srcFile + "' to '" + destFile + "'");
		}
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}
	}

	// 测试出要复制的文件数量
	private static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	// 复制大文件
	private static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	
	public static void main(String[] args) {
		//System.out.println(new File("C:/Users/Administrator/Desktop/shopxx_premium_3.0RELEASE").getName());
		/*try {
			copyDirectory(new File("C:/Users/Administrator/Desktop/BPMX3文档/BPMX3文档/db"),new File("C:/Users/Administrator/Desktop/BPMX3文档/BPMX3文档/db/pdm"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			copyDirectory(new File("C:/Users/Administrator/Desktop/shopxx_premium_3.0RELEASE"),new File("D:/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
