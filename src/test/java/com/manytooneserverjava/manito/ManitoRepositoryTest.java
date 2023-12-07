package com.manytooneserverjava.manito;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.manito.domain.ManitoRepository;
import com.manytooneserverjava.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ManitoRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ManitoRepository manitoRepository;

    @BeforeEach
    void beforeEach() {
        Manito testManito1 = Manito.builder()
                .name("test1")
                .status(0)
                .endDateTime(LocalDateTime.now().plusHours(3))
                .build();

        Manito testManito2 = Manito.builder()
                .name("test2")
                .status(0)
                .endDateTime(LocalDateTime.now().plusHours(3))
                .build();

        Manito testManito3 = Manito.builder()
                .name("test3")
                .status(0)
                .endDateTime(LocalDateTime.now().plusHours(3))
                .build();
        manitoRepository.save(testManito1);
        manitoRepository.save(testManito2);
        manitoRepository.save(testManito3);
    }

    @AfterEach
    void afterEach() {
        List<Manito> findManitos = manitoRepository.findAll();
        if (!findManitos.isEmpty()) manitoRepository.deleteAll(findManitos);
    }

    @Test
    @Transactional
    void saveTest() {
        Manito testManito = Manito.builder()
                .name("saveTest")
                .status(0)
                .endDateTime(LocalDateTime.now().plusHours(3))
                .build();
        manitoRepository.save(testManito);
        Manito findManito = manitoRepository.findById(testManito.getId()).get();
        assertThat(findManito).isEqualTo(testManito);
    }

    @Test
    @Transactional
    void updateTest() {
        Manito findManito = manitoRepository.findByName("test1").get();
        LocalDateTime updateEndDateParam = LocalDateTime.now().plusDays(7);
        Integer updateStatusParam = 1;
        findManito.updateManito(updateStatusParam, updateEndDateParam);
        Manito result = manitoRepository.findById(findManito.getId()).get();
        assertThat(result.getEndDateTime()).isEqualTo(updateEndDateParam);
        assertThat(result.getStatus()).isEqualTo(1);
    }

    @Test
    @Transactional
    void deleteTest() {
        Manito findManito = manitoRepository.findByName("test1").get();
        Long id = findManito.getId();
        manitoRepository.delete(findManito);
        assertThatThrownBy(() -> manitoRepository.findById(id).get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @Transactional
    void selectAllTest() {
        List<Manito> findManitos = manitoRepository.findAll();
        assertThat(findManitos.size()).isEqualTo(3);
    }
}
