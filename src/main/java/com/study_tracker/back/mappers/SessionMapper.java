package com.study_tracker.back.mappers;

import com.study_tracker.back.dto.sessionDto.SessionRequestDto;
import com.study_tracker.back.dto.sessionDto.SessionResponseDto;
import com.study_tracker.back.dto.sessionDto.SessionToEditRequestDto;
import com.study_tracker.back.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    SessionRequestDto sToSReqDto(Session session);
    List<SessionResponseDto> sListToSResDtoList(List<Session> sessionList);
    SessionResponseDto sToSResDto(Session session);

    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "user", ignore = true)
    Session sReqDtoToS(SessionRequestDto dto);

    Session sReqToEditToS(SessionToEditRequestDto sessionToEditReqDto);

}
