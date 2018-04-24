package info.tcjc.czdoc.controller;

import info.tcjc.czdoc.service.DownLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @RequestMapping(value = {"","/","/login","/login/"})
    public String login(String username, String password, Model model, HttpSession session){
        if (!StringUtils.isEmpty(username) ){
            if (username.equals("admin") && password.equals("123")){
                session.setAttribute("username",username);
                return "redirect:/admin/main.html";
            }else{
                model.addAttribute("username",username);
                model.addAttribute("msg","用户名密码不正确");
                return "login";
            }
        } else{
            return "login";
        }
    }

    @Autowired
    DownLoadService downLoadService;
    @ResponseBody
    @GetMapping("/download/{startPage}/{endPage}")
    public String download(@PathVariable("startPage") int startPage,@PathVariable("endPage") int endPage){
        downLoadService.downLoadFromLJM(startPage,endPage);
        return "download success";
    }
}
