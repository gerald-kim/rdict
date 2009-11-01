package com.amplio.vcbl8r.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DownloadUtils {	
	public static boolean downloadFile(String sourceURL, String targetPath, DownloadMonitor downloadMonitor) {
		HttpURLConnection c = getHttpConnection(sourceURL);
		
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
				
				if(downloadMonitor != null) {
					downloadMonitor.bytesDownloaded += new Integer(len1).longValue();
				
					if(downloadMonitor.handler != null && downloadMonitor.runnable!= null)
						downloadMonitor.handler.post(downloadMonitor.runnable);
				}
			}
			f.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static int getRemoteFilesize(String fileURL) {
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
	
	public static HttpURLConnection getHttpConnection(String sourceURL) {
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

	public static byte[] calcMd5Hash(File f) {
		try {  
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			
			InputStream is = new FileInputStream(f);
			
			byte[] buf = new byte[1024];
			
			while (true) {
				int byteCount = is.read(buf);
				if(byteCount == -1) break;
				digest.update( buf, 0, byteCount);
			}
			  
			return digest.digest();
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

	public static boolean checkFileIntegrity(File downloadedFile, File serversideMd5File) {
		String localMd5 = getHexString(calcMd5Hash(downloadedFile));
		String serversideMd5 = readMd5File(serversideMd5File);
		return localMd5.equals(serversideMd5);
    }
	
	public static String readMd5File(File f) {
		StringBuffer sb = new StringBuffer();
        BufferedReader reader;
        try {
	        reader = new BufferedReader(new FileReader(f));
	        
	        char[] buf = new char[1024];
	        int numRead = 0;
	        
	        while((numRead = reader.read(buf)) != -1){
	            sb.append(buf, 0, numRead);
	        }
	        reader.close();
	        
        } catch( FileNotFoundException e ) {
	        e.printStackTrace();
        } catch( IOException e ) {
	        e.printStackTrace();
        }
        
        return sb.toString();
    }
}
