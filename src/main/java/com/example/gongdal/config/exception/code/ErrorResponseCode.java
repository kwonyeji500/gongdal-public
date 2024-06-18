package com.example.gongdal.config.exception.code;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorResponseCode implements ResponseCode {
  UNKNOWN_ERROR(HttpStatus.BAD_REQUEST, "GONGDAL-UNKNOWN", "An unknown error occurred. Please contact your representative."),

  /* user */
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "USER-CHECK-0", "해당 아이디의 유저를 찾을 수 없습니다."),
  DUPLICATE_USERID(HttpStatus.BAD_REQUEST, "USER-CHECK-1", "이미 존재하는 이메일아이디입니다."),
  USER_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "USER-CHECK-2", "올바르지 않은 비밀번호 입니다."),
  NOT_VALID_SOCIAL_TOKEN(HttpStatus.BAD_REQUEST, "USER-TOKEN-1", "올바르지 않은 소셜 토큰입니다."),
  USER_TOKEN_VALIDA(HttpStatus.BAD_REQUEST, "USER-TOKEN-2", "엑세스 토큰이 만료되지 않았습니다."),
  NOT_FOUND_SOCIAL(HttpStatus.BAD_REQUEST, "USER-SOCIAL-1", "소셜 타입을 찾을 수 없습니다."),

  /* file */
  NOT_FOUND_FILE(HttpStatus.BAD_REQUEST, "FILE-CHECK-0", "해당 파일을 찾을 수 없습니다."),

  /* schedule */
  NOT_FOUND_SCHEDULE(HttpStatus.BAD_REQUEST, "SCHEDULE-CHECK-0", "해당 일정을 찾을 수 없습니다."),
  NOT_AUTH_SCHEDULE(HttpStatus.BAD_REQUEST, "SCHEDULE-CHECK-1", "해당 일정에 대한 권한이 없습니다."),

  /* comment */
  NOT_WRITE_PERSONAL(HttpStatus.BAD_REQUEST, "COMMENT-CHECK-0", "개인일정은 댓글을 작성할 수 없습니다."),
  NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "COMMENT-CHECK-1","댓글을 찾을 수 업습니다."),
  NOT_AUTH_COMMENT(HttpStatus.BAD_REQUEST, "COMMENT-CHECK-2", "댓글의 권한이 없습니다."),
  NOT_WRITE_REPLY(HttpStatus.BAD_REQUEST, "COMMENT-CHECK-3", "대댓글에는 댓글을 달 수 없습니다."),

  /* group */
  NOT_FOUND_GROUP(HttpStatus.BAD_REQUEST, "GROUP-CHECK-0", "해당 아이디의 그룹을 찾을 수 없습니다."),
  GROUP_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "GROUP-CHECK-1", "올바르지 않은 비밀번호 입니다."),
  NOT_FOUND_GROUP_USER(HttpStatus.BAD_REQUEST, "GROUP-CHECK-2", "그룹에 가입되지 않은 회원입니다."),
  NOT_EXIT_LEADER(HttpStatus.BAD_REQUEST, "GROUP-CHECK-3", "방장은 그룹에서 탈퇴할 수 없습니다. 방장 위임 후 탈퇴해 주세요."),
  NOT_AUTH_GROUP(HttpStatus.BAD_REQUEST, "GROUP-AUTH-4", "해당 그룹의 권한이 없습니다."),
  NOT_MATCH_KICK(HttpStatus.BAD_REQUEST, "GROUP-CHECK-5", "해당 그룹에 강퇴할 유저가 존재하지 않습니다."),
  NOT_VALID_KICK(HttpStatus.BAD_REQUEST, "GROUP-CHECK-6", "본인을 강퇴할 수 없습니다."),
  NOT_KICK_LEADER(HttpStatus.BAD_REQUEST, "GROUP-CHECK-7", "방장을 강퇴할 수 없습니다."),
  ALREADY_JOIN_GROUP(HttpStatus.BAD_REQUEST, "GROUP-CHECK-8", "이미 가입된 그룹입니다."),
  NOT_VALID_ROLE(HttpStatus.BAD_REQUEST, "GROUP-CHECK-9", "본인의 역할을 변경할 수 없습니다."),
  GROUP_CREATE_RANGE_OVER(HttpStatus.BAD_REQUEST, "GROUP-CHECK-10", "그룹 생성 갯수를 초과하였습니다."),
  GROUP_JOIN_RANGE_OVER(HttpStatus.BAD_REQUEST, "GROUP-CHECK-10", "그룹 가입 갯수를 초과하였습니다."),
  GROUP_COUNT_RANGE_OVER(HttpStatus.BAD_REQUEST, "GROUP-CHECK-10", "그룹 가입 인원을 초과하였습니다."),

  /* news */
  NOT_FOUND_NEWS(HttpStatus.BAD_REQUEST, "NEWS-CHECK-0", "해당 공지사항을 찾을 수 없습니다.");

  public final HttpStatus status;
  public final String code;
  public final String msg;
}
