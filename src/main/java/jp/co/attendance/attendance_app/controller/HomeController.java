package jp.co.attendance.attendance_app.controller;

import jp.co.attendance.attendance_app.service.HomeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public String showHome() {
        homeService.initializeHome();
        return "home";
    }
}
