package info.tcjc.czdoc.controller;

import info.tcjc.czdoc.service.DocMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@Controller
public class SearchController {

    @Autowired
    DocMasterService service;
    @RequestMapping("/r")
    public String query(Model model, @RequestParam("k") String queryCond){
        String[] strings = queryCond.split(" ");
        Set<Map<String, Object>> lists = service.findByCond(strings);
        model.addAttribute("searchWords",queryCond);
        model.addAttribute("result",lists);
        return "resultPage";
    }
}
