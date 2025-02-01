package com.example.sone.sone.controllers;

import com.example.sone.sone.dto.LoginResponseDTO;
import com.example.sone.sone.dto.VendorLoginDTO;
import com.example.sone.sone.entity.Vendor;
import com.example.sone.sone.services.VendorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Tag(name = "Vendors", description = "APIs for managing vendors")
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/register")
    public Optional<Vendor> registerVendor(@RequestBody Vendor vendor) {
        return Optional.of(vendorService.createVendor(vendor));
    }

    @PostMapping("/login")
    public LoginResponseDTO loginVendor(@RequestBody VendorLoginDTO vendor) {
        return vendorService.loginVendor(vendor);
    }
}
