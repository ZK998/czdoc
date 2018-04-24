package info.tcjc.czdoc;

import info.tcjc.czdoc.entity.DocMaster;
import info.tcjc.czdoc.service.DocMasterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CzdocApplicationTests {

    @Autowired
    DocMasterService service;

    @Test
    public void contextLoads() {
        String[] conds =  {"数据","w"};
        Set<Map<String, Object>> objects = service.findByCond(conds);

        objects.forEach(item-> System.out.println(item.toString()));

    }

}
