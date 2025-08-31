package com.jiny.jinyapp.controller;

import com.jiny.jinyapp.service.PostService;
import com.jiny.jinyapp.service.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final VisitorService visitorService;

//    @GetMapping("/")
//    public String home() {
//        return "redirect:/posts";
//    }

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        log.debug("Remote Host of Client: {}", request.getRemoteHost());
        visitorService.increaseVisitorCount();
        // TODO 추후 아래 메소드로 변경
//        visitorService.increaseVisitorCount(request);

        model.addAttribute("topPosts", postService.getTop5Posts());
        model.addAttribute("recentPosts", postService.getRecentPosts());

        model.addAttribute("todayVisitors", visitorService.getTodayCount());
        model.addAttribute("totalVisitors", visitorService.getTotalCount());

        return "home"; // resources/templates/home.html
    }
}

