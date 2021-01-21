package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeptDao {
    public ArrayList<HashMap> searchAll(){
        String sql = BaseDao.sqlMap.get("dept.searchAll");
        try (Connection con = ConnectionFactory.getConnection();){
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet set =pst.executeQuery();
            ArrayList list =new ArrayList();
            while (set.next()){
                HashMap map = new HashMap();
                ResultSetMetaData metaData = set.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 1; i <= count ; i++) {
                    String name = metaData.getColumnName(i).toLowerCase();
                    map.put(name,set.getString(name));
                }
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
