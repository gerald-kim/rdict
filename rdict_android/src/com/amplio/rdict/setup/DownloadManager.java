package com.amplio.vkbl8r.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;

public class DownloadManager implements Runnable{

	String sourceURL = null;
	String writePath = null;
	
	public long download_file_length = 0;
	public long tot_bytes_downloaded = 0;
	
	Handler handler = null;
	Runnable runnable = null;
	
	public void startDownload(String sourceURL, String writePath, Handler handler, Runnable runnable) {
		this.sourceURL = sourceURL;
		this.writePath = writePath;
		
		this.handler = handler;
		this.runnable = runnable;
		
		if(handler != null && runnable != null){
			File file = new File("sdcard/vkbl8r");
			file.mkdir();
		}
		
		this.download_file_length = this.getRemoteFilesize(sourceURL);
		
		new Thread(this).start();
	}
	
	public void run() {
		this.downloadFile(this.sourceURL, this.writePath);
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
}
