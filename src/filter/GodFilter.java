package filter;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import dao.BaseDao;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*手写mvc架构*/

//注解要拦截的路径 /*表示当前相对路径下所有的都拦截
@WebFilter("/*")
public class GodFilter implements Filter {
    private HashMap<String,String> map = new HashMap<>();
    private HashMap<String,Object> beanMap = new HashMap<>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //初始化 init只执行一遍
        URL url = GodFilter.class.getResource("/application.xml");
        try (
                InputStream in = url.openStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
        ) {
            Document document = XmlUtil.readXML(br);
            Element root = XmlUtil.getRootElement(document);
            List<Element> list = XmlUtil.getElements(root,"bean");
            Iterator<Element> iterator=list.iterator();
            while (iterator.hasNext()){
                Element bean = iterator.next();
                String path = bean.getAttribute("path");
                String classAttr=bean.getAttribute("class");
                map.put(path,classAttr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //扫描sql文件夹中所有文件
        url=GodFilter.class.getResource("/sql");
        File folder = new File(url.getPath());
        File[] files = folder.listFiles();
      //  System.out.println(files.length);
        for(File one:files){
            try (
                    FileReader  reader = new  FileReader(one);
                    BufferedReader br = new BufferedReader(reader);
            ) {
                Document document = XmlUtil.readXML(br);
                Element root = XmlUtil.getRootElement(document);
                String name = root.getAttribute("name");
                //System.out.println(name);
                List<Element> list = XmlUtil.getElements(root,"sql");
                Iterator<Element> iterator=list.iterator();
                while (iterator.hasNext()){
                    Element element = iterator.next();
                    String id = element.getAttribute("id");
                    //String sql=XmlUtil.elementText(root,"sql").trim();
                   String sql = element.getTextContent().trim();
                   // System.out.println(name);
                   // System.out.println(sql);
                    BaseDao.sqlMap.put(name+"."+id,sql);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //过滤器接到请求 doFilter马上开始执行
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");

        String uri = req.getRequestURI();
        uri = uri.replace("/mymvc", "");
        if (uri.indexOf("/page/") != -1) {
            chain.doFilter(req, resp);
        } else {
            resp.setCharacterEncoding("UTF-8");
            ///demo/sayHello
            //反射对象调方法
            int i = uri.lastIndexOf("/");
            String path = uri.substring(0,i);
            String methodName =uri.substring(i+1);
            if(map.containsKey(path)){
                String classpath = map.get(path);
                try {
                    Object obj = null;
                    if (beanMap.containsKey(path)){
                        obj=beanMap.get(path);
                    }
                    else{
                        obj =Class.forName(classpath).newInstance();
                        beanMap.put(path,obj);
                    }

                    Method method = obj.getClass().getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
                    Object result =  method.invoke(obj,req,resp);
                    String json = JSONUtil.toJsonStr(result);
                    //在响应头里面标记mime类型 以下对应的是jison格式的
                    resp.setContentType("application/json");
                    Writer w = resp.getWriter();
                    w.write(json);
                    w.close();
                }catch (Exception e){
                    e.printStackTrace();
                    resp.setStatus(500);
                }
            }
            else{
                //没找到对应类就抛出404
                resp.setStatus(404);
            }
        }

    }
}
