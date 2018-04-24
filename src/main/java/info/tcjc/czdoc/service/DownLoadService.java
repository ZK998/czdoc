package info.tcjc.czdoc.service;

import info.tcjc.czdoc.dao.DocMasterDao;
import info.tcjc.czdoc.entity.DocMaster;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
@Service
public class DownLoadService {

    @Autowired
    DocMasterDao dao;

    @Value("${custom.downLoadPath.ljm}")
    String filePath;

    public void downLoadFromLJM(int startPage,int endPage) {

        String baseUrl = "http://ni.medtion.com/info/";

        for (int i = startPage; i <= endPage; i++) {
            String urlStr = baseUrl + i +".jspx";
            if (isAvailable(urlStr)){
                Get_Url(urlStr,i+".html");
            }
        }

    }

    //判断是否有效url
    public Boolean isAvailable(String _url){

        try {
            URL url = new URL(_url);
            InputStream in = url.openStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //根据url下载文件
    public  void Get_Url(final String url, String fileName) {
        try {
            File file = new File(filePath+fileName);
            if (!file.exists()){
                Document doc = Jsoup.connect(url).get();
                //得到html的所有东西
                //Element content = doc.getElementsByClass("details").first();
                // 扩展名为.png的图片
                Elements pngs = doc.select("img[src$=.png]");
                List<String> pngList = new ArrayList<String>();
                for ( Element png:pngs) {
                    String pngSrc = png.attr("src");
                    // System.out.println(pngSrc);
                    String pngName =pngSrc;
                    //   System.out.println(pngName);
                    int pos = pngName.indexOf(".png",1);
                    //将图片的url存入到list中
                    // System.out.println(pngName.substring(0,pos) + ".png");
                    pngList.add(pngName.substring(0,pos) + ".png");
                }

                String pngBaseUrl = "http://ni.medtion.com";

                //下载图片,
                pngList.forEach(item->{
                    try {
                        downloadImg(pngBaseUrl+item);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                //下载文字内容

                //或者通过获取class等于details的div标签
                Element title = doc.select("div.details_heading").first();
                //将计数去掉
                title.child(2).remove();
                Element abs = doc.select("div.abstract").first();
                Element contentText = doc.select("div.content_text").first();
                String str="";
                if (abs != null){
                    str = title.toString() + abs.toString() + contentText.toString();
                }else{
                    str = title.toString() + contentText.toString();
                }


                //把title和fileName写入数据库
                String titleStr = title.select("p.title_details").text();
                String titleTime = title.select("p.title_time").text();

                DocMaster master = new DocMaster();
                master.setDocDate(titleTime);
                master.setDocTitle(titleStr);
                master.setDocName(fileName);

                dao.add(master);

                //将内容中图片地址替换,以便本地html访问
                //转换前的内容：src="/uploads/1/image/public/201711/20171107185205_swxia0r5n0.png"
                //转换后的内容 ：src="20171107185205_swxia0r5n0.png"
                for (String item : pngList) {
                    int pos = item.lastIndexOf("/");
                    item.substring(pos + 1);
                    str = str.replace(item, item.substring(pos + 1));
                }

                //拼成html，将来直接访问文件
                String htmlStr = "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "  <meta charset=\"UTF-8\">\n" +
                        "  <title>"+titleStr+"</title>\n" +
                        "  <style>\n" +
                        "    .title_details{ font-size: 20px }\n" +
                        "    .detail{    width: 650px;margin: 0 auto;} \n" +
                        "  </style>\n" +
                        "</head>\n" +
                        "<body><div class='detail'>\n" +
                        str +
                        "</div><div style='text-align:center'>原文链接："+url+"</div></body>\n" +
                        "</html>";



                // file.createNewFile();

                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(htmlStr.getBytes("utf-8"));
                outputStream.flush();
                outputStream.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void downloadImg(String _url) throws Exception{

        try {
            int pos = _url.lastIndexOf("/");
            String path = filePath+_url.substring(pos+1);

// 构造URL
            URL url = new URL(_url);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            InputStream is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf=new File(path);

            OutputStream os = new FileOutputStream(sf);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();


        } catch (IOException e) {

            System.out.println("downloadImg error:"+e.getMessage());
        }
    }
}
