package Philately.web;

import Philately.stamp.model.Stamp;
import Philately.stamp.model.WishedStamp;
import Philately.stamp.service.StampService;
import Philately.user.model.User;
import Philately.user.service.UserService;
import Philately.web.dto.LoginRequest;
import Philately.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;


@Controller
public class IndexController {

    private final UserService userService;
    private final StampService stampService;

    @Autowired
    public IndexController(UserService userService, StampService stampService) {
        this.userService = userService;
        this.stampService = stampService;
    }

    @GetMapping("/")
    public String getIndexPage() {

        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView processRegisterUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ModelAndView("register");
        }
        userService.registerUser(registerRequest);
        return new ModelAndView("redirect:/login");
    }



    @GetMapping("/login")
    public ModelAndView getLoginPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView processLoginUser(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            return new ModelAndView("login");
        }
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.loginUser(loginRequest);
        session.setAttribute("user_id", user.getId());
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        User user = userService.getUserById(userId);
        List<Stamp> offeredStamps = stampService.getOfferedStamps(user);
        List<WishedStamp> wishedStamps = stampService.getWishedStamps(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("offeredStamps", offeredStamps);
        modelAndView.addObject("wishedStamps", wishedStamps);
        return modelAndView;
    }

    @GetMapping("/logout")
    public String getLogoutPage(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

}

