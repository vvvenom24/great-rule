package venom.greatrule.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    @Value("${login.password}")
    private String configPassword;
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String password, 
                       HttpSession session, 
                       Model model) {
        if (configPassword.equals(password)) {
            // 登录成功，设置session
            session.setAttribute("authenticated", true);
            return "redirect:/";  // 重定向到首页
        } else {
            // 登录失败，显示错误信息
            model.addAttribute("error", true);
            return "login";
        }
    }
} 