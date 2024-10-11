package com.demo.kitchensink.web;

import com.demo.kitchensink.data.Member;
import com.demo.kitchensink.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // Initialize a new Member object when the form is accessed
    @GetMapping("/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("newMember", new Member());
        return "registerForm"; // This refers to the view page like registerForm.html (using Thymeleaf or JSP)
    }

    @PostMapping("/register")
    public String register(Member newMember, RedirectAttributes redirectAttributes) {
        try {
            memberService.registerMember(newMember);
            // Add a success message to be shown on the redirected page
            redirectAttributes.addFlashAttribute("message", "Registration successful!");
            return "redirect:/members/new"; // Redirect to the registration form again
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            // Add error message to be displayed after redirect
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/members/new";
        }
    }

    // Extract root cause of exception
    private String getRootErrorMessage(Exception e) {
        String errorMessage = "Registration failed. See server log for more information.";
        if (e == null) {
            return errorMessage;
        }
        Throwable t = e;
        while (t != null) {
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        return errorMessage;
    }
}