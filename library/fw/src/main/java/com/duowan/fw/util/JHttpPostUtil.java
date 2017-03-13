package com.duowan.fw.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class JHttpPostUtil {
	private final static int TIMEOUT_CONNECTION = (int) TimeUnit.SECONDS.toMillis(10);
	private final static int TIMEOUT_SOCKET = (int) TimeUnit.SECONDS.toMillis(60);
	private final static int BUFFER_SIZE = 4096;
	private HttpPost mHttpPost = null;

	public InputStream post(String url, HttpEntity entity) throws IOException {
		HttpResponse response = getResponse(url, entity);
		int ret = response.getStatusLine().getStatusCode();
		JLog.debug(this, "HttpPostUtil end: " + url + ", HTTP response:" + ret);
		if (ret == HttpURLConnection.HTTP_OK) {
			final HttpEntity res = response.getEntity();
			if (res != null) {
				return res.getContent();
			}
		}
		else {
			throw new IOException("Failed download with result:" + ret);
		}
		return null;
	}
	
	public int postForResCode(String url, HttpEntity entity) throws IOException {
		HttpResponse response = getResponse(url, entity);
		int resCode = response.getStatusLine().getStatusCode();
		try {
    		final HttpEntity res = response.getEntity();
            if (res != null) {
                InputStream is = res.getContent();
                InputStreamReader reader = new InputStreamReader(is);
                StringBuilder builder = new StringBuilder();
                char[] charBuffer = new char[BUFFER_SIZE];
                int chars = 0;
                while ((chars = reader.read(charBuffer)) != -1) {
                    builder.append(charBuffer, 0, chars);
                }
                is.close();
                JLog.debug("HttpPostUtil", "postForResCode, return msg = " + builder.toString());
            }
		}
		catch (Exception e) {
		}
		return resCode;
	}

	public byte[] postForByteArray(String url, HttpEntity entity) throws IOException {
		HttpResponse response = getResponse(url, entity);
		int ret = response.getStatusLine().getStatusCode();
		JLog.debug(this, "HttpPostUtil end: " + url + ", HTTP response:" + ret);
		if (ret == HttpURLConnection.HTTP_OK) {
			final HttpEntity res = response.getEntity();
			if (res != null) {
				InputStream is = res.getContent();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytes = 0;
				while ((bytes = is.read(buffer)) != -1) {
					bos.write(buffer, 0, bytes);
				}
				is.close();
				bos.flush();
				byte[] result = bos.toByteArray();
				bos.close();
				return result;
			}
		}
		else {
			throw new IOException("Failed download with result:" + ret);
		}
		return null;
	}
	
	public void setHttpPost(HttpPost post) {
		mHttpPost = post;
	}

	public String postForString(String url, HttpEntity entity) throws IOException {
		HttpResponse response = getResponse(url, entity);
		int ret = response.getStatusLine().getStatusCode();
		JLog.debug(this, "HttpPostUtil end: " + url + ", HTTP response:" + ret);
		if (ret == HttpURLConnection.HTTP_OK) {
			final HttpEntity res = response.getEntity();
			if (res != null) {
				InputStream is = res.getContent();
				InputStreamReader reader = new InputStreamReader(is);
				StringBuilder builder = new StringBuilder();
				char[] charBuffer = new char[BUFFER_SIZE];
				int chars = 0;
				while ((chars = reader.read(charBuffer)) != -1) {
					builder.append(charBuffer, 0, chars);
				}
				is.close();
				return builder.toString();
			}
		}
		else {
			throw new IOException("Failed download with result:" + ret);
		}
		return null;
	}

	private HttpResponse getResponse(String url, HttpEntity entity) throws IOException {
		JLog.debug(this, "HttpPostUtil start: " + url);
		HttpClient client = null;
		HttpResponse response = null;
		try {
		    InetSocketAddress proxy = JNetworkUtil.getTunnelProxy();
	        HttpParams httpParameters = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECTION);
	        HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);
	        if (proxy != null && proxy.getAddress() != null) {
	            String ip = proxy.getAddress().getHostAddress();
	            int port = proxy.getPort();
	            HttpHost proxyHost = new HttpHost(ip, port);
	            httpParameters.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
	        }
	        client = new DefaultHttpClient(httpParameters);
	        HttpPost post = mHttpPost;
	        if (post == null) {
	            post = new HttpPost(url);
	        }
	        post.setEntity(entity);	        
			response = client.execute(post);
		}
		catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			}
			else {
				JLog.error(this, e);
				throw new IOException(e.getMessage());
			}
		}finally{
		    if (client != null) {
		        client.getConnectionManager().shutdown();
		    }
        }
		mHttpPost = null;
		return response;
	}
}
