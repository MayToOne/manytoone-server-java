package com.manytooneserverjava.manito;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.manito.domain.ManitoRepository;
import com.manytooneserverjava.manito.service.ManitoService;
import com.manytooneserverjava.manito.web.ManitoController;
import com.manytooneserverjava.manito.web.dto.ManitoCreateForm;
import com.manytooneserverjava.manitomember.service.ManitoMemberService;
import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ManitoControllerTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ManitoRepository manitoRepository;
    @Autowired
    ManitoService manitoService;
    @Autowired
    ManitoMemberService manitoMemberService;
    @Autowired
    ManitoController manitoController;
    @Autowired
    MockMvc mockMvc;

    private final String BASE_URI = "/api/manitos";

    @BeforeEach
    void beforeEach() {
        Member savedMember1 = memberRepository.save(Member.builder()
                .name("tester01")
                .passwd("testing")
                .email("tester01")
                .build());

        Member savedMember2 = memberRepository.save(Member.builder()
                .name("tester02")
                .passwd("testing")
                .email("tester02")
                .build());

        Member savedMember3 = memberRepository.save(Member.builder()
                .name("tester03")
                .passwd("testing")
                .email("tester03")
                .build());

        ManitoCreateForm createForm = new ManitoCreateForm(savedMember1.getId(), "testManito1", LocalDateTime.now().plusDays(3));
        Long manito = manitoService.createManito(createForm);

        createForm = new ManitoCreateForm(savedMember2.getId(), "testManito2", LocalDateTime.now().plusDays(3));
        manitoService.createManito(createForm);
        Long manitoMemberId = manitoMemberService.inviteManitoMember(manito, savedMember2.getId());
        manitoMemberService.inviteManitoMember(manito, savedMember3.getId());
        manitoMemberService.updateManitoMemberStatus(manitoMemberId, 1);
    }


    @Test
    @Transactional
    void createManitoSuccess() throws Exception {
        Member tester01 = memberRepository.findByName("tester01").get();
        String req = """
                {
                    "memberId" : "%d",
                    "name" : "%s",
                    "endDateTime" : "%s"
                }
                    """;

        mockMvc.perform(
                        post(BASE_URI)
                                .content(req.formatted(tester01.getId(), "mockManito", "2023-12-25 12:00"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
        assertThat(manitoRepository.findByName("mockManito").isPresent()).isTrue();
    }

    @Test
    @Transactional
    void createManitoValidationFail() throws Exception {
        Member tester01 = memberRepository.findByName("tester01").get();
        String req = """
                {
                    "memberId" : "%d",
                    "name" : "%s",
                    "endDateTime" : "%s"
                }
                    """;

        mockMvc.perform(
                        post(BASE_URI)
                                .content(req.formatted(null, "", ""))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
        assertThat(manitoRepository.findByName("mockManito").isPresent()).isFalse();
    }

    @Test
    @Transactional
    void updateManitoSuccess() throws Exception {
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        String req = """
                {
                    "status" : "%d",
                    "endDateTime" : "%s"
                }
                    """;
        mockMvc.perform(put(BASE_URI + "/update/" + testManito1.getId())
                        .content(req.formatted(1, "2023-12-25 12:00"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @Transactional
    void updateManitoValidationFali() throws Exception {
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        String req = """
                {
                    "status" : "%d",
                    "endDateTime" : "%s"
                }
                    """;
        mockMvc.perform(put(BASE_URI + "/update/" + testManito1.getId())
                        .content(req.formatted(null, ""))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(put(BASE_URI + "/update/" + testManito1.getId())
                        .content(req.formatted(4, "2023-12-25 12:00"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @Transactional
    void initiateManito() throws Exception {
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        mockMvc.perform(get(BASE_URI + "/initiate/" + testManito1.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteManito() throws Exception {
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        Long id = testManito1.getId();
        mockMvc.perform(delete(BASE_URI + "/" + testManito1.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        assertThat(manitoRepository.findById(id).isEmpty()).isTrue();
    }

    @Test
    @Transactional
    void findManito() throws Exception {
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        mockMvc.perform(get(BASE_URI + "/" + testManito1.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void findMyManitos() throws Exception {
        Member tester02 = memberRepository.findByName("tester02").get();
        mockMvc.perform(get(BASE_URI + "/joined/" + tester02.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}