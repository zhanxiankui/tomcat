package com.commons.study.threedemo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 *   文件内容转成字节数组，文件以字节流处理。
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>succez</p>
 * @author zhanxk
 * @createdate 2019年8月1日
 */

public class FileToByte {

	/**
	 * 将文件内容转换成byte数组返回,如果文件不存在或者读入错误返回null
	 * @throws Exception 
	 */
	public byte[] file2buf(File fobj) throws Exception {
		FileInputStream fis;
		ByteArrayOutputStream bos;
		byte[] end;

		if (fobj != null && fobj.exists()) {
			fis = new FileInputStream(fobj);
			try {
				bos = new ByteArrayOutputStream(1024);
				try {
					byte[] bt = new byte[1024];
					int len = -1;
					while ((len = fis.read(bt)) != -1) {
						bos.write(bt, 0, len);
					}
					end = bos.toByteArray();

				}
				finally {
					bos.close();
				}
			}
			finally {
				fis.close();
			}

			return end;
		}

		return null;
	}

}
