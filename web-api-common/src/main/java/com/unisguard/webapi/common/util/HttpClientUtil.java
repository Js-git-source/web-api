package com.unisguard.webapi.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientUtil {

    private static CloseableHttpClient httpClient;
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;

    public HttpClientUtil(int connectionTimeout, int requestTimeout) {
        if (httpClient == null) {
            httpClient = getHttpClient(connectionTimeout, requestTimeout);
        }
    }

    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient getHttpClient(int connectTimeout, int socketTimeout) {
        return HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .setMaxConnTotal(200)//连接池最大连接数
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectTimeout(connectTimeout)
                                .setSocketTimeout(socketTimeout)//请求超时时间
                                .build()
                )
                .build();
    }

    public String httpGet(String url, Map<String, String> requestParams, Map<String, String> headerParams) {

        HttpGet httpGet = null;
        String result = "";
        try {
            if (requestParams == null || requestParams.isEmpty()) {
                httpGet = new HttpGet(url);
            } else {
                StringBuilder builder = new StringBuilder(url);
                builder.append("?");
                for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                    builder.append((String) entry.getKey());
                    builder.append("=");
                    builder.append((String) entry.getValue());
                    builder.append("&");
                }
                String address = builder.toString();
                address = address.substring(0, address.length() - 1);
                httpGet = new HttpGet(address);
            }

            if (headerParams != null) {
                for (Map.Entry<String, String> entry : headerParams.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
//			if (log.isDebugEnabled()) {
//				log.debug("httpClient httpGet executing:[url=" + url +" Params："+JSONObject.toJSONString(requestParams)+ "],[header=" + JSONObject.toJSONString(headerParams) + "]");
//			}
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                log.error("httpClient httpGet execute failed:[code=" + statusCode + "],[url=" + url + " Params：" + JSONObject.toJSONString(requestParams) + "],[header=" + JSONObject.toJSONString(headerParams) + "]");
//				return null;
            }
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            log.error("http请求失败:" + url, e);
        } finally {
            if (httpGet != null) {
                httpGet.abort();
            }
        }
        return result;
    }

    public String httpPost(String url, Map<String, String> requestParams, String urlEncode) {

        HttpPost httpPost = null;
        String result = "";
        try {
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
//			if (log.isDebugEnabled()) {
//				log.debug("httpClient httpPost executing:[url=" + url + "],[param=" + JSONObject.toJSONString(requestParams) + "],[encode=" + urlEncode + "]");
//			}
            httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params, urlEncode));
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                log.error("httpClient httpPost execute failed:[code=" + statusCode + "],[url=" + url + "],[param=" + JSONObject.toJSONString(requestParams) + "],[encode=" + urlEncode + "]");
//				return null;
            }
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            log.error("http请求失败:" + url, e);
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }

    public String httpPostWithHeader(String url, Map<String, String> requestParams, Map<String, String> headerParams, String urlEncode) {

        HttpPost httpPost = null;
        String result = "";
        try {
            // 参数设置
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
//			if (log.isDebugEnabled()) {
//				log.debug("httpClient httpPost executing:[url=" + url + "],[param=" + JSONObject.toJSONString(requestParams) + "],[header=" + JSONObject.toJSONString(headerParams) + "],[encode=" + urlEncode + "]");
//			}
            httpPost = new HttpPost(url);
            // 设置参数header
            for (String key : headerParams.keySet()) {
                httpPost.addHeader(key, headerParams.get(key));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, urlEncode));
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                log.error("httpClient httpPost execute failed:[code=" + statusCode + "],[url=" + url + "],[param=" + JSONObject.toJSONString(requestParams) + "],[header=" + JSONObject.toJSONString(headerParams) + "],[encode=" + urlEncode + "]");
                return null;
            }
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            log.error("http请求失败:" + url, e);
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }

    public String httpPostForHeaderAndBody(String url, String jsonString, Map<String, String> headerParams, String urlEncode) {

        HttpPost httpPost = null;
        String result = "";
        try {
//			if (log.isDebugEnabled()) {
//				log.debug("httpClient httpPost executing:[url=" + url + "],[body=" + jsonString + "],[header=" + JSONObject.toJSONString(headerParams) + "],[encode=" + urlEncode + "]");
//			}
            httpPost = new HttpPost(url);
            // 设置参数header
            httpPost.setHeader("Content-type", "application/json");
            for (String key : headerParams.keySet()) {
                httpPost.addHeader(key, headerParams.get(key));
            }
            // 设置参数body
            httpPost.setEntity(new StringEntity(jsonString, urlEncode));
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            log.error("调用请求返回的状态码：" + statusCode);
            if (statusCode != 200) {
                log.error("httpClient httpPost execute failed:[code=" + statusCode + "],[url=" + url + "],[body=" + jsonString + "],[header=" + JSONObject.toJSONString(headerParams) + "],[encode=" + urlEncode + "]");
            }
            HttpEntity httpEntity = response.getEntity();
            log.error("httpEntity" + httpEntity);
            result = EntityUtils.toString(httpEntity);
            log.error("返回结果" + result);
        } catch (Exception e) {
            log.error("http请求失败:" + url, e);
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        return result;
    }


    public String httpPut(String url, String jsonString, Map<String, String> headerParams, String urlEncode) {
        HttpPut httpput = null;
        String result = "";
        try {
//			if (log.isDebugEnabled()) {
//				log.debug("httpClient httpPost executing:[url=" + url + "],[body=" + jsonString + "],[header=" + JSONObject.toJSONString(headerParams) + "],[encode=" + urlEncode + "]");
//			}
            httpput = new HttpPut(url);
            // 设置参数header
            httpput.setHeader("Content-type", "application/json");
            for (String key : headerParams.keySet()) {
                httpput.addHeader(key, headerParams.get(key));
            }
            // 设置参数body
            httpput.setEntity(new StringEntity(jsonString, urlEncode));
            HttpResponse response = httpClient.execute(httpput);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                log.error("httpClient httpPost execute failed:[code=" + statusCode + "],[url=" + url + "],[body=" + jsonString + "],[header=" + JSONObject.toJSONString(headerParams) + "],[encode=" + urlEncode + "]");
//				return null;
            }
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            log.error("http请求失败:" + url, e);
        } finally {
            if (httpput != null) {
                httpput.abort();
            }
        }
        return result;
    }

    /*public  String httpPostFormMultipart(String url, Map<String, String> params, File file, Map<String, String> headers, String encode) {

        if (encode == null) {
            encode = "utf-8";
        }
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPost httpost = new HttpPost(url);

        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mEntityBuilder.setCharset(Charset.forName(encode));

        // 普通参数
        ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));//解决中文乱码
        if (params != null && params.size() > 0) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                mEntityBuilder.addTextBody(key, params.get(key), contentType);
            }
        }
        //二进制参数
        if (!file.exists()) {
            return "";
        }
        mEntityBuilder.addBinaryBody("file", file);

        httpost.setEntity(mEntityBuilder.build());
        String content = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpost);
            HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, encode);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {  //关闭连接、释放资源
            closeableHttpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }*/
}
