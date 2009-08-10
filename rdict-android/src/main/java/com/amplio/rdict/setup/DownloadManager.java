package com.amplio.rdict.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Handler;

public class DownloadManager implements Runnable {
	//"http://www.google.ca/intl/en_ca/images/logo.gif";
	public final static String SOURCE_URL_DB = "http://s3.amazonaws.com/rdict/word.cdb";
	public final static String SOURCE_URL_INDEX = "http://s3.amazonaws.com/rdict/word.index";
	public final static String WRITE_PATH_DB = "/sdcard/rdict/word.cdb";
	public final static String WRITE_PATH_INDEX = "/sdcard/rdict/word.index";
	
	public long download_file_length = 0;
	public long tot_bytes_downloaded = 0;
	
	Handler handler = null;
	Runnable runnable = null;
	
	String[] sourceURLs = null;
	String[] writePaths = null;
	
	public void startDownload(String[] sourceURLs, String[] writePaths, Handler handler, Runnable runnable) {
		this.sourceURLs = sourceURLs;
		this.writePaths = writePaths;
		
		this.handler = handler;
		this.runnable = runnable;
		
		if(handler != null && runnable != null){
			File file = new File("sdcard/rdict");
			file.mkdir();
		}
		
		new Thread(this).start();
	}
	
	public void run() {
		this.download_file_length = this.getRemoteFilesize(sourceURLs[0]);
		this.download_file_length += this.getRemoteFilesize(sourceURLs[1]);
		
		this.downloadFile(this.sourceURLs[0], this.writePaths[0]);
		this.downloadFile(this.sourceURLs[1], this.writePaths[1]);
	}
	
	public boolean isDownloading() {
		return this.tot_bytes_downloaded < this.download_file_length;
	}
	
	public int getProgress() {
		return new Double((new Long(this.tot_bytes_downloaded).doubleValue() / this.download_file_length) * 100).intValue();
	}

	public boolean downloadFile(String sourceURL, String targetPath) {
		HttpURLConnection c = this.getHttpConnection(sourceURL);
		
		FileOutputStream f = null;
		try {
			File outputFile = new File(targetPath);
			f = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		InputStream in = null;
		try {
			in = c.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		byte[] buffer = new byte[1024];
		int len1 = 0;
		
		try {
			while ( (len1 = in.read(buffer)) > 0 ) {
				f.write(buffer,0, len1);
				
				this.tot_bytes_downloaded += new Integer(len1).longValue();
				
				if(this.handler != null && this.runnable!= null)
					this.handler.post(this.runnable);
			}
			f.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int getRemoteFilesize(String fileURL) {
		URL u = null;
		try {
			u = new URL(fileURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		HttpURLConnection c = null;
		try {
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return c.getContentLength(); 
	}
	
	public HttpURLConnection getHttpConnection(String sourceURL) {
		URL u = null;
    	
		try {
			u = new URL(sourceURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		HttpURLConnection c = null;

		try {
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return c;
	}

	public String calcMd5(File f) {
		try {  
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			
			InputStream is = new FileInputStream(f);
			
			byte[] buf = new byte[1024];
			
			while (true) {
				int byteCount = is.read(buf);
				if(byteCount == -1) break;
				digest.update( buf, 0, byteCount);
			}
			  
			byte messageDigest[] = digest.digest();
			
			return getHexString(messageDigest).toString();
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		} catch( FileNotFoundException e ) {
	        e.printStackTrace();
        } catch( IOException e ) {
	        e.printStackTrace();
        }  
		
		return null;  
    }
	
	public static String getHexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < b.length; i++) {
			sb.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
		}
		return sb.toString();
	}
}
