package com.manytooneserverjava.manito.web;

import com.manytooneserverjava.manito.service.ManitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manito")
@RequiredArgsConstructor
public class ManitoController {
    private final ManitoService manitoService;
    @PostMapping
    public ResponseEntity<Long> createManito(@RequestBody ManitoCreateForm form) {
        Long manitoId = manitoService.createManito(form);
        return ResponseEntity.ok(manitoId);
    }
}
