package com.sistemaVeterinario.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@Controller
public class LanguageController {

    @GetMapping("/changeLanguage")
    public String changeLanguage(
            @RequestParam("lang") String lang,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 1. Obtener el LocaleResolver y cambiar el idioma
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            localeResolver.setLocale(request, response, new Locale(lang));
        }

        // 2. Redirigir a la página anterior (usando Referer) o a home si no hay Referer
        String referer = request.getHeader("Referer");
        String redirectUrl = (referer != null) ? referer : "/";

        return "redirect:" + redirectUrl;
    }
}