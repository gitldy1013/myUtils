import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

  public static String doGet(String url, Map<String, String> param) {

    // 创建Httpclient对象
    CloseableHttpClient httpclient = HttpClients.createDefault();

    String resultString = "";
    CloseableHttpResponse response = null;
    try {
      // 创建uri
      URIBuilder builder = new URIBuilder(url);
      if (param != null) {
        for (String key : param.keySet()) {
          builder.addParameter(key, param.get(key));
        }
      }
      URI uri = builder.build();

      // 创建http GET请求
      HttpGet httpGet = new HttpGet(uri);

      // 执行请求
      response = httpclient.execute(httpGet);
      // 判断返回状态是否为200
      if (response.getStatusLine().getStatusCode() == 200) {
        resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (response != null) {
          response.close();
        }
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return resultString;
  }

  public static String doGet(String url) {
    return doGet(url, null);
  }

  public static String doPost(String url, Map<String, String> param) {
    // 创建Httpclient对象
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = null;
    String resultString = "";
    try {
      // 创建Http Post请求
      HttpPost httpPost = new HttpPost(url);
      // 创建参数列表
      if (param != null) {
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        for (String key : param.keySet()) {
          paramList.add(new BasicNameValuePair(key, param.get(key)));
        }
        // 模拟表单
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
        httpPost.setEntity(entity);
      }
      // 执行http请求
      response = httpClient.execute(httpPost);
      resultString = EntityUtils.toString(response.getEntity(), "utf-8");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        response.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return resultString;
  }

  public static String doPost(String url) {
    return doPost(url, null);
  }

  public static String doPostJson(String url, String json) {
    // 创建Httpclient对象
    CloseableHttpClient httpClient = HttpClients.createDefault();
    CloseableHttpResponse response = null;
    String resultString = "";
    try {
      // 创建Http Post请求
      HttpPost httpPost = new HttpPost(url);
      // 创建请求内容
      StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
      httpPost.setEntity(entity);
      // 执行http请求
      response = httpClient.execute(httpPost);
      resultString = EntityUtils.toString(response.getEntity(), "utf-8");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        response.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return resultString;
  }

  public static String upload(String url, File file) {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    //CloseableHttpClient httpclient = HttpClientBuilder.create().build();
    try {
      HttpPost httppost = new HttpPost(url);
      httppost.setHeader("cookie", "_ga=GA1.2.1944399469.1530523919; _gid=GA1.2.323627556.1530523919; " +
          ".CNBlogsCookie=01FD041DB352084E55CAF8817B888D1C0E3A5934135382EC148782F50AE03F5FA74782B4144BAD5E2B1810C3DD4F1EFD272C67A55D11DD5B34B91ED408E5F479F4F8BA79FD901D118132DEB594004E8BCA043252F8AC03BC0E3024E693C22984BDB2EC9F; .Cnblogs.AspNetCore.Cookies=CfDJ8FHXRRtkJWRFtU30nh_M9mCkCRVASZ7mmBLEOyyw9PIY-bFAx1bAvNJzfgn3_fyBT1DIcMuSH4q0taKBZyoxy-QzSVylG5zU4e1MKEF-6mzAGDvbu42BLuCCG6tdcUJCNbnkiTbyaJaA-krbcW6Ya9MlrdRYg8dgwFZGi_nIuv1GQTWlVy6DGrccoh01ysxV3ZcKXcGO54rCub2K8i1yajnDxi85vDJMRXEYZVXrPk5p0V1n6BdDO68U7rg9Wsxmz1UH7HE7-wNO8dNnmdtjEwL8ftbkVI6YPoy6GzKHyY2vZszq1QK9nYVoRPjflwYxEA");
      httppost.setHeader(".CNBlogsCookie",
          "01FD041DB352084E55CAF8817B888D1C0E3A5934135382EC148782F50AE03F5FA74782B4144BAD5E2B1810C3DD4F1EFD272C67A55D11DD5B34B91ED408E5F479F4F8BA79FD901D118132DEB594004E8BCA043252F8AC03BC0E3024E693C22984BDB2EC9F");
      httppost.setHeader(".Cnblogs", "CfDJ8FHXRRtkJWRFtU30nh_M9mCkCRVASZ7mmBLEOyyw9PIY");
      httppost.setHeader("_ga", "GA1.2.1944399469.1530523919");
      httppost.setHeader("_gid", "GA1.2.323627556.1530523919");
      httppost.setHeader("x-mime-type", "image/png");
      httppost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
          "Gecko) Chrome/68.0.3423.2 Safari/537.36");
      httppost.setHeader("x-requested-with", "XMLHttpRequest");
      RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
      httppost.setConfig(requestConfig);

      FileBody bin = new FileBody(file);
      StringBody qqfile = new StringBody(file.getName(), ContentType.TEXT_PLAIN);
      System.out.println(file.getName() + "==================================");
      HttpEntity reqEntity = MultipartEntityBuilder.create()
          .addPart("qqfile", bin).addPart("qqfile", qqfile).build();

      httppost.setEntity(reqEntity);

      System.out.println("executing request " + httppost.getRequestLine());
      CloseableHttpResponse response = httpclient.execute(httppost);
      try {
        System.out.println(response.getStatusLine());
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
          String responseEntityStr = EntityUtils.toString(response.getEntity());
          System.out.println(responseEntityStr);
          System.out.println("Response content length: " + resEntity.getContentLength());
          return responseEntityStr;
        }
        EntityUtils.consume(resEntity);
      } finally {
        response.close();
      }
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return "";
  }


  public static void main(String[] args) {
    int fileNum = 0, folderNum = 0;
    File file = new File("C:\\Users\\root\\Desktop\\media");
    if (file.exists()) {
      LinkedList<File> list = new LinkedList<File>();
      File[] files = file.listFiles();
      for (File file2 : files) {
        if (file2.isDirectory()) {
          System.out.println("文件夹:" + file2.getAbsolutePath());
          list.add(file2);
          folderNum++;
        } else {
          System.out.println("文件:" + file2.getAbsolutePath());
          fileNum++;
          String s = upload("https://upload.cnblogs.com/imageuploader/processupload?host=www.cnblogs.com", file2);
          System.out.println(s);
        }
      }
      File temp_file;
      while (!list.isEmpty()) {
        temp_file = list.removeFirst();
        files = temp_file.listFiles();
        for (File file2 : files) {
          if (file2.isDirectory()) {
            System.out.println("文件夹:" + file2.getAbsolutePath());
            list.add(file2);
            folderNum++;
          } else {
            System.out.println("文件:" + file2.getAbsolutePath());
            fileNum++;
          }
        }
      }
    } else {
      System.out.println("文件不存在!");
    }
    System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
  }
}
