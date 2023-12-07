package com.manytooneserverjava.manito.web;

import com.manytooneserverjava.manito.service.ManitoService;
import com.manytooneserverjava.manito.web.dto.ManitoCreateForm;
import com.manytooneserverjava.manito.web.dto.ManitoDto;
import com.manytooneserverjava.manito.web.dto.ManitoResponse;
import com.manytooneserverjava.manito.web.dto.ManitoUpdateForm;
import com.manytooneserverjava.manitomember.service.ManitoMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import static com.manytooneserverjava.common.message.ResponseMessage.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PERMANENT_REDIRECT;

@RestController
@RequestMapping("/api/manitos")
@RequiredArgsConstructor
public class ManitoController {
    private final ManitoService manitoService;
    private final ManitoMemberService manitoMemberService;

    @PostMapping
    public ResponseEntity<?> createManito(@RequestBody @Valid ManitoCreateForm form) {
        Long createdManitoId = manitoService.createManito(form);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{memberId}")
                                .buildAndExpand(createdManitoId)
                                .toUri()
                )
                .body(new ManitoResponse<Long>(PERMANENT_REDIRECT.value(), MANITO_CREATE_SUCCESS, createdManitoId));
    }

    @PutMapping("/update/{manitoId}")
    public ResponseEntity<ManitoResponse<Long>> updateManito(@PathVariable(name = "manitoId") Long manitoId, @RequestBody @Valid ManitoUpdateForm form) {
        ManitoResponse<Long> response = new ManitoResponse<>(OK.value(), MANITO_UPDATE_SUCCESS, manitoService.updateManito(manitoId, form));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/initiate/{manitoId}")
    public ResponseEntity<ManitoResponse<Long>> initiateManito(@PathVariable(name = "manitoId") Long manitoId) {
        ManitoResponse<Long> response = new ManitoResponse<>(OK.value(), MANITO_INITIATE_SUCCESS, manitoService.initiateManito(manitoId));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{manitoId}")
    public ResponseEntity<ManitoResponse<?>> deleteManito(@PathVariable(name = "manitoId") Long manitoId) {
        manitoService.deleteManito(manitoId);
        ManitoResponse<?> response = new ManitoResponse<>(OK.value(), MANITO_DELETE_SUCCESS, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{manitoId}")
    public ResponseEntity<ManitoResponse<ManitoDto>> findManito(@PathVariable(name = "manitoId") Long manitoId) {
        ManitoResponse<ManitoDto> response = new ManitoResponse<>(OK.value(), MANITO_FIND_ONE_SUCCESS, manitoService.findManito(manitoId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/joined/{memberId}")
    public ResponseEntity<ManitoResponse<List<ManitoDto>>> findMyManitos(@PathVariable(name = "memberId") Long memberId) {
        ManitoResponse<List<ManitoDto>> response = new ManitoResponse<>(OK.value(), MANITO_FIND_MY_SUCCESS, manitoMemberService.findMyManitos(memberId));
        return ResponseEntity.ok(response);
    }
}
