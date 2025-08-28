package Philately.web;

import Philately.stamp.model.Stamp;
import Philately.stamp.service.StampService;
import Philately.user.model.User;
import Philately.user.service.UserService;
import Philately.web.dto.AddingStampRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/stamps")
public class StampController {

    private final UserService userService;
    private final StampService stampService;

    @Autowired
    public StampController(UserService userService, StampService stampService) {
        this.userService = userService;
        this.stampService = stampService;
    }

    @GetMapping("/new")
    public ModelAndView getAddStampPage(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("addingStampRequest", new AddingStampRequest());
        modelAndView.setViewName("add-stamp");
        return modelAndView;
    }

    @PostMapping("/new")
    public ModelAndView processAddStamp(@Valid AddingStampRequest addingStampRequest, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("add-stamp");
            modelAndView.addObject("addingStampRequest", addingStampRequest);
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView();
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(userId);
        stampService.addNewStamp(user, addingStampRequest);
        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/wish/{id}")
    public String processToWishlist(@PathVariable UUID id, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(userId);
        stampService.addWishedStamp(user, id);
        return "redirect:/home";
    }

    @DeleteMapping("/remove/{id}")
    public String processRemoveWishedStamp(@PathVariable UUID id, HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getUserById(userId);
        stampService.removeWishedStamp(id);
        return "redirect:/home";
    }
}
