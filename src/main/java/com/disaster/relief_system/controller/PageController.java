package com.disaster.relief_system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Open app -> Login page directly
    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "login";
        }
        return "redirect:/post-login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/post-login")
    public String postLogin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/login";
        }

        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                ? "redirect:/admin-dashboard"
                : authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CAMP_MANAGER"))
                ? "redirect:/camp-dashboard"
                : authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_WAREHOUSE_MANAGER"))
                ? "redirect:/warehouse-dashboard"
                : authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_LOGISTICS_OFFICER"))
                ? "redirect:/logistics-dashboard"
                : "redirect:/error";
    }

    // Dashboards (4 Roles)

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/camp-dashboard")
    public String campDashboard() {
        return "camp-dashboard";
    }

    @GetMapping("/warehouse-dashboard")
    public String warehouseDashboard() {
        return "warehouse-dashboard";
    }

    @GetMapping("/logistics-dashboard")
    public String logisticsDashboard() {
        return "logistics-dashboard";
    }

    // Pages

    @GetMapping("/camp-page")
    public String campPage() {
        return "camp";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping("/requests")
    public String requests() {
        return "requests";
    }

    @GetMapping("/resources")
    public String resources() {
        return "resources";
    }

    @GetMapping("/warehouses")
    public String warehouses() {
        return "warehouses";
    }

    @GetMapping("/inventory")
    public String inventory() {
        return "inventory";
    }

    @GetMapping("/deliveries")
    public String deliveries() {
        return "deliveries";
    }

    @GetMapping("/allocations")
    public String allocations() {
        return "allocations";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}