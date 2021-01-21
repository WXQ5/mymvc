package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
//将从map.xml里面获取的SQL语句进行处理 从数据库获取相应的数据
public class EmpDao {
    public ArrayList<HashMap> searchAllByPage(Integer empno,String ename,Integer deptno,int page,int size){
       String sql = BaseDao.sqlMap.get("emp.searchAllByPage");
        //System.out.println("数据库查询");
       // System.out.println(sql);
        try (Connection con = ConnectionFactory.getConnection();){
            String sql_1 = sql.split("#condition#")[0];
            String sql_2 = sql.split("#condition#")[1];
            ArrayList param = new ArrayList();
            if (empno != null){
                param.add(empno);
                sql_1 += "AND empno=? ";
            }
            if (ename != null){
                param.add(ename);
                sql_1 += "AND ename=? ";
            }
            if (deptno != null){
                param.add(deptno);
                sql_1 += "AND deptno=? ";
            }
            sql_1 += sql_2;
            param.add((page-1) * size);
            param.add(size);

            PreparedStatement pst = con.prepareStatement(sql_1);
            for (int i = 0; i < param.size() ; i++) {
                pst.setObject(i+1,param.get(i));
            }
            ResultSet set =pst.executeQuery();
            ArrayList list =new ArrayList();
            while (set.next()){
                HashMap map = new HashMap();
               /* map.put("empno",set.getObject("empno"));
                map.put("ename",set.getObject("ename"));
                map.put("job",set.getObject("job"));
                map.put("mgr",set.getObject("mgr"));
                map.put("hiredate",set.getObject("hiredate"));
                map.put("sal",set.getObject("sal"));
                map.put("comm",set.getObject("comm"));
                map.put("deptno",set.getObject("deptno"));
                */
                //将查询内容动态封装到map里面 比上述直接封装简单
                ResultSetMetaData metaData = set.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 1; i <= count ; i++) {
                    String name = metaData.getColumnName(i).toLowerCase();
                    map.put(name,set.getString(name));
                }
                list.add(map);
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }


    }
    public long searchCount(){
        String sql = BaseDao.sqlMap.get("emp.searchCount");
        try (Connection con = ConnectionFactory.getConnection();){
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet set =pst.executeQuery();
            set.next();
            long count = set.getLong("ct");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int deleteByEmpno(String[] empnoList){
        String sql = BaseDao.sqlMap.get("emp.deleteByEmpno");
        sql += "(";
        for (String empno: empnoList){
            sql += "?,";
        }
        sql = sql.substring(0,sql.length() - 1);
        sql += ")";
        try (Connection con = ConnectionFactory.getConnection();){
            PreparedStatement pst = con.prepareStatement(sql);
            for (int i = 0; i < empnoList.length; i++){
                pst.setObject(i+1,empnoList[i]);
            }
           int rows = pst.executeUpdate();
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
