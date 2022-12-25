package hello.servlet.web.servlet.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringMemberFormControllerV1 {

    @RequestMapping("/spring/mvc/v1/members/newform")
    public ModelAndView process() {
        return new ModelAndView("newform");
    }
}

