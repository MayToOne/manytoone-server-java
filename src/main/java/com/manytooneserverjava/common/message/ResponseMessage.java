package com.manytooneserverjava.common.message;

public interface ResponseMessage {
    // manito
    String MANITO_CREATE_SUCCESS = "마니또 생성 성공";
    String MANITO_UPDATE_SUCCESS = "마니또 상태 수정 성공";
    String MANITO_INITIATE_SUCCESS = "마니또 활성화 성공";
    String MANITO_DELETE_SUCCESS = "마니또 삭제 성공";
    String MANITO_FIND_ONE_SUCCESS = "마니또 상세페이지 조회 성공";
    String MANITO_FIND_MY_SUCCESS = "가입된 마니또 리스트 성공";

    // manitoMembmer
    String MANITO_MEMBER_INVITE_SUECCESS = "마니또 회원 초대 성공";
    String MANITO_MEMBER_INVITE_UPDATE_SUCCESS = "마니또 회원 초대 수락/거절 성공";
    String MANITO_MEMBER_SELECT_SUECCESS = "마니또 회원 초대 조회 성공";
    String MANITO_MEMBER_UPDATE_GIFT_SUECCESS = "마니또 회원 선물 정보 수정 성공";
    String MANITO_MEMBER_LEAVE_SUECCESS = "마니또 회원 탈퇴 성공";
    // member

    // memberRelation
}
