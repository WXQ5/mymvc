package bean;

import dao.DeptDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class DeptBean {
   private DeptDao deptDao = new DeptDao();
    public Object searchAll(HttpServletRequest req, HttpServletResponse resp){
        ArrayList<HashMap> list= deptDao.searchAll();
        HashMap map = new HashMap();
        map.put("list",list);
        return map;
    }
}
