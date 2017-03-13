package com.duowan.fw.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;

import android.content.Context;

public class JHttpMultipartPost {

	public static final int NO_COMPRESS = 0;
	public static final int ZLIB_COMPRESS = 1;
	public static final String ZIP_TYPE = "application/zip";
    public static final String TEXT_TYPE = "text/plain";

	static final int INIT_BUF_SIZE = 4096;

	private class ZlibUtils {

		private MultipartDataPacker mPacker = null;

		public ZlibUtils(MultipartDataPacker packer) {
			mPacker = packer;
		}

		void addZlibCompressData(byte[] data) {
			Deflater compressor = new Deflater();
			compressor.setInput(data);
			compressor.finish();
			getZlibData(compressor);
			compressor.end();
		}

		void addZlibCompressData(String fileName) {
			Deflater compressor = new Deflater();
			if (addZlibDataFromFile(compressor, fileName)) {
				getZlibData(compressor);
				compressor.end();
			}
		}

		private void getZlibData(Deflater compressor) {
			int bytes = 0;
			byte[] buffer = new byte[INIT_BUF_SIZE];
			while (!compressor.finished()) {
				bytes = compressor.deflate(buffer);
				mPacker.mout.write(buffer, 0, bytes);
			}
		}

		private boolean addZlibDataFromFile(Deflater compressor, String fileName) {
            FileInputStream fis = null;
			try {
				fis = new FileInputStream(fileName);
				byte[] buffer = new byte[INIT_BUF_SIZE];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while (fis.read(buffer) != -1) {
                    baos.write(buffer);
				}
                compressor.setInput(baos.toByteArray());
				compressor.finish();
				return true;
			} catch (Exception e) {
				JLog.error(this, "addZlibDataFromFile");
				JLog.error(this, e);
				return false;
			} finally {
                if (null != fis) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

	}

	public class MultipartDataPacker {

		static final String TEMP_FILE = "multiData.tmp";

		static final String BOUNDARY = "----------V1234567890abcD";
		static final String ITEM_HEADER = "--" + BOUNDARY
			+ "\r\nContent-Disposition: form-data; name=\"";
		static final String CONTENT_TYPE = "multipart/form-data; boundary=" + BOUNDARY;

		private JFileUtils mout;
		private int mCompress = 0;
		private ZlibUtils mZlib;
		private boolean mFinished = false;

		public MultipartDataPacker(Context context, int compress) throws Exception {
			final int MAX_TRY = 3;
			int tryTimes = 0;
			boolean run = true;
			String fileName = TEMP_FILE;
			while (run && tryTimes < MAX_TRY) {
				try {
					mout = JFileUtils.createTempFile(context, fileName);
					run = false;
				}
				catch (Exception e) {
					tryTimes++;
					if (tryTimes == MAX_TRY) {
						throw e;
					}
					fileName = TEMP_FILE + tryTimes;
				}
			}
			mCompress = compress;
			mZlib = new ZlibUtils(this);
		}

		private void addFile(String fileName) {
			mout.write(fileName);
		}

		private String getFilePartHeader(String name, String contentType) {
			String itemStr = ITEM_HEADER;
			itemStr += (name + "\"; filename=\"" + name + "\"");
			if (contentType != null) {
				itemStr += "\r\nContent-Type: " + contentType;
			}
			itemStr += "\r\n\r\n";
			return itemStr;
		}

		public void addPart(String name, String value) {
			String itemStr = ITEM_HEADER;
			itemStr += (name + "\"\r\n\r\n" + value + "\r\n");
			mout.write(itemStr.getBytes());
		}

        public void addPart(String name, String contentType, String value) {
            String itemStr = ITEM_HEADER;
            itemStr += (name + "\"\r\nContent-Type: " + contentType);
            itemStr += ("\r\n\r\n" + value + "\r\n");
            mout.write(itemStr.getBytes());
        }

		public void addPart(String name, byte[] value) {
			String itemStr = ITEM_HEADER + name + "\"\r\n\r\n";
			mout.write(itemStr.getBytes());
			mout.write(value);
			String itemEnd = "\r\n";
			mout.write(itemEnd.getBytes());
		}

		public void addBase64Part(String name, byte[] value) {
			String itemEnd = "\r\n";
			mout.write(itemEnd.getBytes());
		}

		public void addFilePart(String name, byte[] data) {
			addFilePart(name, data, null);
		}

		public void addFilePart(String name, byte[] data, String contentType) {
			if (data != null) {
				mout.write(getFilePartHeader(name, contentType).getBytes());
				if (mCompress == ZLIB_COMPRESS) {
					mZlib.addZlibCompressData(data);
				}
				else {
					mout.write(data);
				}
				String itemEnd = "\r\n";
				mout.write(itemEnd.getBytes());
			}
		}

		public void addFilePart(String name, String fileName) {
			addFilePart(name, fileName, null);
		}

		public void addFilePart(String name, String fileName, String contentType) {
			if (fileName != null) {
				mout.write(getFilePartHeader(name, contentType).getBytes());
				if (mCompress == ZLIB_COMPRESS) {
					mZlib.addZlibCompressData(fileName);
				}
				else {
					addFile(fileName);
				}
				String itemEnd = "\r\n";
				mout.write(itemEnd.getBytes());
			}
		}

		public void finish() {
			String endTag = "--" + BOUNDARY + "--\r\n";
			mout.write(endTag.getBytes());
			mout.close();
			mFinished = true;
		}

		public FileEntity getDataEntity() {
			if (mFinished) {
				FileEntity se = new FileEntity(mout.getFile(), "multipart/form-data");
				return se;
			}
			return null;
		}

	}

	private byte[] mRecvData = null;
	private String mExceptionName = null;
	private JHttpPostUtil mPostUtil = new JHttpPostUtil();

	public MultipartDataPacker getDataPacker() throws Exception {
		return getDataPacker(null, NO_COMPRESS);
	}

	public MultipartDataPacker getDataPacker(int compress) throws Exception {
		return getDataPacker(null, compress);
	}

	public MultipartDataPacker getDataPacker(Context context, int compress) throws Exception {
		MultipartDataPacker packer = new MultipartDataPacker(context, compress);
		return packer;
	}

	public int submit(String url, MultipartDataPacker dataPacker) {
		int resCode = 0;
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(dataPacker.getDataEntity());
			httppost.setHeader("User-Agent", "Apache-HttpClient/android");
			httppost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;");
			httppost.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			httppost.setHeader("Keep-alive", "115");
			httppost.setHeader("Content-Type", MultipartDataPacker.CONTENT_TYPE);
			
			mPostUtil.setHttpPost(httppost);
			resCode = mPostUtil.postForResCode(url, dataPacker.getDataEntity());
		}
		catch (Exception e) {
			mPostUtil.setHttpPost(null);
			JLog.error(this, "Exception happens in HttpMultipartPost.submit");
			JLog.error(this, e);
		}
		return resCode;
	}

	public String getLastException() {
		return mExceptionName;
	}

	public byte[] getResData() {
		return mRecvData;
	}
}
