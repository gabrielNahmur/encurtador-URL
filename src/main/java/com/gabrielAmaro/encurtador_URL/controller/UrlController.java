package com.gabrielAmaro.encurtador_URL.controller; 

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gabrielAmaro.encurtador_URL.service.UrlService;

import java.net.URI;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/encurtar")
    public ResponseEntity<Map<String, String>> shorten(@RequestBody Map<String, String> request, HttpServletRequest servletRequest) {
        
        String longUrl = request.get("urlLonga"); 
        if (longUrl == null || longUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "A URL longa é obrigatória"));
        }

       
        String shortCode = urlService.shortenUrl(longUrl); 
        
        String baseUrl = servletRequest.getRequestURL().toString().replace(servletRequest.getRequestURI(), "");
        String shortenedUrl = baseUrl + "/" + shortCode;

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("urlEncurtada", shortenedUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) { 
        
        String originalUrl = urlService.getOriginalUrl(shortCode); 

        if (originalUrl != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(originalUrl));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}