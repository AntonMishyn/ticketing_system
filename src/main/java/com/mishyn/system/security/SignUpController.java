package com.mishyn.system.security;

import com.mishyn.system.dto.NewUserDTO;
import com.mishyn.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/sign-up")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @GetMapping
    public String getSignUpForm(@ModelAttribute NewUserDTO newUserDTO, BindingResult bindingResult, Model model) {
        return "sign-up";
    }

    @PostMapping
    public String createUser(@ModelAttribute NewUserDTO newUserDTO, BindingResult bindingResult, Model model) {
        if (userService.userExistsByName(newUserDTO.getUsername())) {
            bindingResult.rejectValue("username", "error.newUserDTO",
                    "User with name " + newUserDTO.getUsername() + " already exists.");
        }
        if (!Objects.equals(newUserDTO.getPassword(), newUserDTO.getPasswordConfirmation())) {
            bindingResult.rejectValue("password", "error.newUserDTO",
                    "Confirmation doesn't match original password");
        }

        if (bindingResult.hasFieldErrors()) {
            return "sign-up";
        }

        userService.createNewUser(newUserDTO);
        return "redirect: login";
    }
}
