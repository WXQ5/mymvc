package bean;

import dao.EmpDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

//将Dao文件里面从数据库中查询获取的数据得到 以便响应给浏览器
public class EmpBean {
    //new 一个EmpDao 以下调用其searchAll方法查询数据库数据
    private EmpDao empDao = new EmpDao();
    public Object searchAllByPage(HttpServletRequest req, HttpServletResponse resp){
        String temp = req.getParameter("empno");
        Integer empno;
        if (temp == null || temp.length() == 0){
            empno = null;
        }else {
            empno = Integer.parseInt(temp);
        }

        String ename = req.getParameter("ename");
        if (ename == null || ename.length() == 0){
            ename = null;
        }

        temp = req.getParameter("deptno");
        Integer deptno;
        if (temp == null || temp.length() == 0){
            deptno = null;
        }
        else {
            deptno = Integer.parseInt(temp);
        }

        Integer page = Integer.parseInt(req.getParameter("page"));
        Integer size = Integer.parseInt(req.getParameter("size"));

        ArrayList<HashMap> list = empDao.searchAllByPage(empno,ename,deptno,page,size);
        HashMap map = new HashMap();
        map.put("result",list);
        return map;
    }

    public Object searchCount(HttpServletRequest req, HttpServletResponse resp){
        HashMap map = new HashMap();
        map.put("count",empDao.searchCount());
        return map;
    }
    public Object deleteByEmpno(HttpServletRequest req, HttpServletResponse resp){
       // System.out.println("xxx");
        String temp = req.getParameter("empnoList");
        String[] empnoList = temp.split(",");
        int rows = empDao.deleteByEmpno(empnoList);
        HashMap map = new HashMap();
        map.put("rows",rows);
        return map;

    }


}
