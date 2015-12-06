/* SDCardBase.java
* Created on 2011-9-8
*/
package org.android.bookkeeping.common.sdcard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.os.StatFs;

/**
 * 
 * Add one sentence class summary here.
 * Add class description here.
 *
 * @author Johnson_Jiang01
 * @version 1.0, 2011-9-8
 */
public class SDCardBase {

	public boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	public String getSDCardStatus(){
		return Environment.getExternalStorageState();
	}
	
	public File getSDCardPath(){
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * Copy file to sd card
	 * @param mfile it includes the whole path and file name for output
	 */
	public void copyFileToSDCard(String mfile){
		copyFileToSDCard(mfile, null);
	}
	
	/**
	 * 
	 * @param fromFile the file name including the whole path, which will be copied
	 * @param toDir where the file should be copied there.
	 */
	public void copyFileToSDCard(String fromFile, String toDir){
		try {
			File file = new File(fromFile);
			FileInputStream inputStream = new FileInputStream(file);   
			byte[] inBuffer = new byte[1024];   
			inputStream.read(inBuffer); 
			
			if (toDir != null && !toDir.trim().equals("")) {
				toDir = getSDCardPath().getAbsolutePath() + handleDirectoryWithSeparator(toDir);
				File f = new File(toDir);
				if (!f.exists())
					f.mkdirs();
			} else {
				toDir = getSDCardPath().getAbsolutePath() + File.separator;
			}
			String toFile = toDir + file.getName();
			OutputStream bos = new FileOutputStream(toFile);
			
			int bytesRead = 0;
			byte[] outBuffer = new byte[8192];
			while ((bytesRead = inputStream.read(outBuffer, 0, 8192)) != -1){
				bos.write(outBuffer, 0, bytesRead);
			}
			bos.close();
			inputStream.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Delete a file from the specified path in sd card
	 * @param dir if null, means root path.
	 * @param fileName file name
	 */
	public void deleteFile(String dir, String fileName){
		String file = getSDCardPath().getAbsolutePath()
				+ handleDirectoryWithSeparator(dir) + fileName;
		File f = new File(file);
		f.delete();
	}
	
	public String handleDirectoryWithSeparator(String dir){
		return (dir.startsWith(File.separator) ? "" : File.separator) + dir
				+ (dir.endsWith(File.separator) ? "" : File.separator);
	}
	
	public boolean isFreeSpaceAvailable(File file){
		FileInputStream fis;
		int fileSize; 
		try {
			fis = new FileInputStream(file);
			fileSize = fis.available();
		} catch (Exception e) {
			throw new RuntimeException("can not get the size of the file", e);
		}
		
		StatFs statfs=new StatFs(getSDCardPath().getPath());
		long blockSize=statfs.getBlockSize(); 
		long availableBlocks=statfs.getAvailableBlocks(); 
		
		return fileSize > (availableBlocks * blockSize) ? false : true;
	}
	
}
