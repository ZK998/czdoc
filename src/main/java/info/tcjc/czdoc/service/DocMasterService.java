package info.tcjc.czdoc.service;

import info.tcjc.czdoc.dao.DocMasterDao;
import info.tcjc.czdoc.entity.DocMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DocMasterService {
    @Autowired
    DocMasterDao masterDao;

    public int add(DocMaster master){
       return masterDao.add(master);
    }

    public Set<Map<String, Object>> findByCond(String... conds){

        List<Map<String, Object>> mapList = masterDao.findByCond(conds);
        List<Map<String, Object>> resultList = new ArrayList<>();

        resultList = mapList;

        if (conds.length>1){
            //根据其他输入条件再筛选
            int i=1;
            while (conds.length>i){
                resultList = resultFilter(resultList,conds[i]);
                i++;
            }
        }

        return  new HashSet<>(resultList);
    }

    private List<Map<String, Object>>  resultFilter( List<Map<String, Object>> mapList,String cond){
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            if((map.get("doc_title")!=null?map.get("doc_title"):"").toString().contains(cond) ||
                    (map.get("doc_date")!=null?map.get("doc_date"):"").toString().contains(cond)||
                    (map.get("doc_key")!=null?map.get("doc_key"):"").toString().contains(cond)){
                resultList.add(map);
            }
        }
        return resultList;
    }
}
