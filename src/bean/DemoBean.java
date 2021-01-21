package bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
//反射调用的类
public class DemoBean {

    public Object sayHello(HttpServletRequest req, HttpServletResponse resp){
        String username=req.getParameter("username");
        HashMap map = new HashMap();
        map.put("result","Hello "+username);
        return map;
    }

    public Object sayHi(HttpServletRequest req, HttpServletResponse resp){
        String username=req.getParameter("username");
        HashMap map = new HashMap();
        map.put("result","Hi "+username);
        return map;
    }
}
