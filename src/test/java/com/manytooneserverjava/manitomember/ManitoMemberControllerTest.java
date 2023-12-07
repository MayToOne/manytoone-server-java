package com.manytooneserverjava.manitomember;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.manito.domain.ManitoRepository;
import com.manytooneserverjava.manito.service.ManitoService;
import com.manytooneserverjava.manito.web.dto.ManitoCreateForm;
import com.manytooneserverjava.manitomember.domain.ManitoMember;
import com.manytooneserverjava.manitomember.domain.ManitoMemberRepository;
import com.manytooneserverjava.manitomember.service.ManitoMemberService;
import com.manytooneserverjava.manitomember.web.ManitoMemberController;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ManitoMemberControllerTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ManitoRepository manitoRepository;
    @Autowired
    ManitoMemberRepository manitoMemberRepository;
    @Autowired
    ManitoService manitoService;
    @Autowired
    ManitoMemberService manitoMemberService;
    @Autowired
    ManitoMemberController manitoMemberController;
    @Autowired
    MockMvc mockMvc;

    private final String BASE_URI = "/api/manitoMembers";

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
    void inviteManitoMember() throws Exception {
        Manito findManito = manitoRepository.findByName("testManito2").get();
        Member findMember = memberRepository.findByName("tester03").get();
        String req = """
            {
                "manitoId" : "%d",
                "memberId" : "%d"
            }
                """;
        mockMvc.perform(post(BASE_URI)
                        .content(req.formatted(findManito.getId(), findMember.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void updateInviteStatus() throws Exception {
        String req = """
            {
                "manitoMemberId" : "%d",
                "manitoMemberStatus" : "%d"
            }
                """;
        Manito findManito = manitoRepository.findByName("testManito1").get();
        Member findMember = memberRepository.findByName("tester03").get();
        ManitoMember findManitoMember = manitoMemberRepository.findByMemberIdAndManitoId(findMember.getId(), findManito.getId(), null).get();
        mockMvc.perform(put(BASE_URI + "/updateStatus")
                        .content(req.formatted(findManitoMember.getId(), 1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void findMyManitoInvites() throws Exception {
        Member findMember = memberRepository.findByName("tester03").get();
        mockMvc.perform(get(BASE_URI + "/" + findMember.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void updateGiftInfo() throws Exception {
        String req = """
            {
                "manitoMemberId" : "%d",
                "wantedGift" : "%s",
                "unwantedGift" : "%s"
            }
                """;
        Manito findManito = manitoRepository.findByName("testManito2").get();
        Member findMember = memberRepository.findByName("tester02").get();
        ManitoMember findManitoMember = manitoMemberRepository.findByMemberIdAndManitoId(findMember.getId(), findManito.getId(), null).get();
        mockMvc.perform(put(BASE_URI + "/updateGiftInfo")
                        .content(req.formatted(findManitoMember.getId(), "guitar", "lamp"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void leaveManitoSuccess() throws Exception {
        Manito findManito = manitoRepository.findByName("testManito1").get();
        Member findMember = memberRepository.findByName("tester02").get();
        ManitoMember findManitoMember = manitoMemberRepository.findByMemberIdAndManitoId(findMember.getId(), findManito.getId(), null).get();
        mockMvc.perform(delete(BASE_URI + "/" + findManitoMember.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    void leaveManitoFailInitiated() throws Exception {
        Manito findManito = manitoRepository.findByName("testManito1").get();
        Member findMember = memberRepository.findByName("tester02").get();
        ManitoMember findManitoMember = manitoMemberRepository.findByMemberIdAndManitoId(findMember.getId(), findManito.getId(), null).get();
        manitoService.initiateManito(findManito.getId());
        mockMvc.perform(delete(BASE_URI + "/" + findManitoMember.getId()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void leaveManitoFailIsLeader() throws Exception {
        Manito findManito = manitoRepository.findByName("testManito1").get();
        Member findMember = memberRepository.findByName("tester01").get();
        ManitoMember findManitoMember = manitoMemberRepository.findByMemberIdAndManitoId(findMember.getId(), findManito.getId(), null).get();
        manitoService.initiateManito(findManito.getId());
        mockMvc.perform(delete(BASE_URI + "/" + findManitoMember.getId()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}