package com.example.corespringsecurity.controller.login;

import com.example.corespringsecurity.domain.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @GetMapping(value={"/login", "/api/login"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return "redirect:/";
        }

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "user/login/login";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/login";
    }

    @GetMapping(value= {"/denied", "/api/denied"})
    public String accessDenied(@RequestParam(value = "exception", required = false) String exception,
                               @RequestParam(value = "authorize", required = false) String authorize,
                               Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /*
            ????????? CustomAuthenticationProvider ?????? UsernamePasswordAuthenticationToken ??????
            principal ??? Account ???????????? ???????????????, SecurityContextHolder ??? SecurityContext ?????? Authentication ????????????
            principal ??? ????????? ?????? ???????????? Account ???????????? ??? ???????????? ????????????.
         */
        Account account = (Account) authentication.getPrincipal();
        model.addAttribute("username", account.getUsername());
        model.addAttribute("exception", exception);
        model.addAttribute("authorize", authorize);

        return "user/login/denied";
    }
}
