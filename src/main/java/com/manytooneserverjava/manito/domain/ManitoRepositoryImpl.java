package com.manytooneserverjava.manito.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManitoRepositoryImpl implements ManitoRepositoryCustom {
    private final JPAQueryFactory query;
}
