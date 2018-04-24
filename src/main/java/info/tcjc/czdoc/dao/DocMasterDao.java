package info.tcjc.czdoc.dao;

import info.tcjc.czdoc.entity.DocMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DocMasterDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int add(DocMaster master){
        int i = jdbcTemplate.update("insert into doc_master( doc_name, doc_title, doc_auth, doc_date, creation_date)" +
                "values (?,?,?,?,now())", master.getDocName(), master.getDocTitle(), master.getDocAuth(), master.getDocDate());

        return i;
    }

    public List<Map<String, Object>>  findByCond(String... conds){
        String sql ="select a.*,b.doc_key from doc_master a left outer join doc_detail b " +
                "  on a.id = b.doc_id " +
                "  where (a.doc_title like ? or doc_auth like ? or doc_date like ? or b.doc_key like ?) " +
                "  order by 5 desc";

        List<Object> queryList=new ArrayList<Object>();
        queryList.add("%" + conds[0] + "%");
        queryList.add("%" + conds[0] + "%");
        queryList.add("%" + conds[0] + "%");
        queryList.add("%" + conds[0] + "%");
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, queryList.toArray());
        return mapList;
    }
}
