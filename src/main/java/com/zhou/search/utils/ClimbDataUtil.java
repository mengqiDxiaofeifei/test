package com.zhou.search.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhou.search.entity.Category;
import com.zhou.search.entity.Spu;
import com.zhou.search.entity.express.Express;
import com.zhou.search.entity.express.ExpressInfo;
import com.zhou.search.entity.express.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: zhou_search
 * @Auther: z_houjun
 * @Date: 2018/12/5 10:57
 * @Description:
 */
public class ClimbDataUtil {

    private static String baseUrl = "https://search.jd.com/Search?enc=utf-8";
    private final static String CONDITION_KEYWORD = "&keyword=";
    private final static String CONDITION_PAGE = "&page=";

    private static String experssUrl = "https://sp0.baidu.com/9_Q4sjW91Qh3otqbppnN2DJv/pae/channel/data/asyncqury?appid=4001";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String baseurl = "https://kuaiyinshi.com/api/kuai-shou/recommend/";

        String s = sendGet(baseurl,"callback=showData&_=1552643520427");
        System.out.println("s = " + s);
    }

    /**
     * 单号查询
     */
    public static Express getExpress(String num) {
        String baseurl = "http://www.kuaidi100.com/autonumber/autoComNum";
        String s = sendPost(baseurl, "?resultv2=1&text=" + num);
        String url = "http://www.kuaidi100.com/query";
        Response response = JSONObject.parseObject(s.substring(2, s.length()), Response.class);
        String comCode = response.getAuto().get(0).getComCode();
        String expressStr = sendGet(url, "type=" + comCode + "&postid=" + num + "&temp=0.7061593331120958&phone=");
        Express express = JSONObject.parseObject(expressStr.substring(2, expressStr.length()), Express.class);
        List<ExpressInfo> collect = express.getData().stream().sorted(Comparator.comparing(ExpressInfo::getTime).reversed()).collect(Collectors.toList());
        express.setData(new LinkedHashSet<>(collect));
        return express;
    }


    /**
     * 爬取数据,
     *
     * @param keyword 关键字
     * @param count   数量(30的整数倍)
     * @return
     */
    public static void getData(String keyword, int count) {
        if (count % 30 != 0) {
            return;
        }
        Integer page = (count / 30) * 2;
        int countTotal = 0;
        long start = System.currentTimeMillis();
        for (int i = 2; i <= page; i += 2) {
            Map<String, Object> map = loadData(keyword, i + "", 1l);
            List<Spu> data = (List<Spu>) map.get("data");
            for (int j = 0; j < data.size(); j++) {
                //spuMapper.insert(data.get(i));
            }
            countTotal += (int) map.get("count");
        }
        long end = System.currentTimeMillis();
        System.out.println("本次获取到" + countTotal + "条数据,一共耗时" + (end - start) + "ms");
    }

    public static List<Category> loadCateGory(String keyword, String page) {
        List<Category> list = new ArrayList<>();
        String requestUrl = baseUrl + CONDITION_KEYWORD + keyword + CONDITION_PAGE + page;
        String result = getHtmlData(requestUrl);
        Document doc = Jsoup.parse(result);
        Elements element = doc.select("ul[class=J_valueList v-fixed]").get(0).children();
        for (int i = 0; i < element.size(); i++) {
            String src = "";
            try {
                src = element.get(i).child(0).child(1).attr("src");
            } catch (IndexOutOfBoundsException e) {
                src = "";
            }
            String name = element.get(i).child(0).attr("title");
            Category category = new Category();
            category.setName(name);
            category.setImage(src);
            list.add(category);
            //  System.out.println("src = " + src+"\n"+name.split(" ")[0]);
        }
        return list;
    }


    public static Map<String, Object> loadData(String keyword, String page, Long cid) {
        Map<String, Object> map = new HashMap<>();
        List<Spu> spus = new ArrayList<>();
        String requestUrl = baseUrl + CONDITION_KEYWORD + keyword + CONDITION_PAGE + page;
        String result = getHtmlData(requestUrl);
        Document doc = Jsoup.parse(result);
        Elements element = doc.select("li[class=gl-item]");
        int total = 0;
        for (int i = 0; i < element.size(); i++) {
            Element ele = element.get(i);
            Element imgDivATag = ele.select("div[class=p-img]").get(0).child(0);
            Element iTag = ele.select("div[class=p-price]").get(0).child(0).child(1);
            String subTitle = ele.select("div[class=p-name p-name-type-2]").get(0).child(0).attr("title");
            String shopName = "";
            try {
                shopName = ele.select("div[class=p-shop]").get(0).child(0).child(0).attr("title");
            } catch (Exception e) {
                shopName = "";
            }
            Double price;
            String title = imgDivATag.attr("title");
            Element imgTag = imgDivATag.child(0);
            String image = "https:" + imgTag.attr("source-data-lazy-img");
            Spu spu = new Spu();
            spu.setTitle(title);
            spu.setSubTitle(subTitle);
            spu.setCreateTime(new Date());
            spu.setImage(image);
            spu.setShopName(shopName);
            spu.setCid(cid);
            try {
                price = Double.parseDouble(iTag.html());
            } catch (Exception e) {
                price = 0.0;
            }
            spu.setPrice(price);
            spus.add(spu);
            total++;
        }
        map.put("count", total);
        map.put("data", spus);
        return map;
    }


    public static String getHtmlData(String baseUrl) {
        URL url = null;
        URLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder content = new StringBuilder();
        try {
            url = new URL(baseUrl);
            connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String temp = "";
            while (null != (temp = bufferedReader.readLine())) {
                content.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + "?" + param;
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "/n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "/n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


}
