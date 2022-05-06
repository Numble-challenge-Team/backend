package com.numble.shortForm.video.controller;

import com.numble.shortForm.exception.CustomException;
import com.numble.shortForm.exception.ErrorCode;
import com.numble.shortForm.exception.ErrorResponse;
import com.numble.shortForm.exception.ServerErrorResponse;
import com.numble.shortForm.request.PageDto;
import com.numble.shortForm.response.Response;
import com.numble.shortForm.security.AuthenticationFacade;
import com.numble.shortForm.user.entity.Users;
import com.numble.shortForm.user.repository.UsersRepository;
import com.numble.shortForm.video.dto.request.EmbeddedVideoRequestDto;
import com.numble.shortForm.video.dto.response.VideoResponseDto;
import com.numble.shortForm.video.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/videos/")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "비디오 API")
public class VideoApiController {

    private final VideoService videoService;
    private final AuthenticationFacade authenticationFacade;
    private final UsersRepository usersRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @ApiOperation(value = "임베디드 동영상 업로드", notes = "<big>임베디드 동영상 업로드</big>")
    @ApiResponses({
            @ApiResponse(code = 200, message = "동영상 생성 완료", response = Response.class),
            @ApiResponse(code = 403, message = "권한 없음", response = ErrorResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "유저 NOT Found 오류", response = ErrorResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerErrorResponse.class, responseContainer = "List")
    })
    @PostMapping("/upload/embedded")
    public ResponseEntity<?> uploadEmbeddedVideo(@ModelAttribute EmbeddedVideoRequestDto embeddedVideoRequestDto) throws IOException {

        String userEmail = authenticationFacade.getAuthentication().getName();

        Users users = usersRepository.findByEmail(userEmail).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER, "해당 유저 토큰에 해당하는 정보를 찾을수가 없습니다."));

        videoService.uploadEmbeddedVideo(embeddedVideoRequestDto, users.getId());

        return ResponseEntity.ok().body("ok");
    }

    @ApiOperation(value = "모든 동영상 조회", notes = "테스트하실때 확인하실 동영상 리스트 조회, size는 parameter로 /  기본값 5")
    @ApiResponses({
            @ApiResponse(code = 209, message = "content 내부  동영상 구조", response = VideoResponseDto.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerErrorResponse.class, responseContainer = "List")
    })
    @GetMapping("/retrieve/all")
    public Page<VideoResponseDto> retrieveVideoAll(@RequestParam(defaultValue = "5") int size) {

        return videoService.retrieveAll(Pageable.ofSize(size));
    }


    @GetMapping("/main")
    public Page<VideoResponseDto> mainVideoList(Pageable pageable) {

        log.info("pageable {}",pageable);
//        videoService.
        return null;
    }


    @ApiOperation(value = "비디오 상세페이지", notes = "비디오 Id 값을 기준으로 조회")
    @GetMapping("/retrieve/{videoId}")
    public VideoResponseDto retrieveVideoDetail(@PathVariable(name = "videoId") Long videoId) {
//        comment 개발후 작성
        String userEmail = authenticationFacade.getAuthentication().getName();
        String ip = getIp();
        return   videoService.retrieveDetail(videoId,ip,userEmail);
    }

    @ApiOperation(value = "로그인 유저의 모든 비디오 조회", notes = "테스트하실때 확인하실 동영상 리스트 조회, size는 parameter로")
    @GetMapping("/retrieve/myVideo")
    public Page<VideoResponseDto> retrieveMyVideo(@ModelAttribute PageDto pageDto) {
        String userEmail = authenticationFacade.getAuthentication().getName();


        return videoService.retrieveMyVideo(userEmail,pageDto);
    }


    @ApiOperation(value = "비디오 좋아요버튼", notes = "좋아요 생성,혹은 제거 메시지가 표시됌")
    @PostMapping("/like/{videoId}")
    public ResponseEntity requestLikeVideo(@PathVariable(name = "videoId")Long videoId ) {
        String userEmail = authenticationFacade.getAuthentication().getName();

        boolean bol = videoService.requestLikeVideo(userEmail, videoId);
        String message = bol == true ? "좋아요 생성" : "좋아죠 제거";
        return Response.success(bol,message,HttpStatus.OK);
    }

    @ApiOperation(value = "비디오 검색", notes = "검색기능, 현재 제목과, description 조회(해시태그 포함예정)")
    @GetMapping("/search")
    public List<VideoResponseDto> searchVideo(@RequestParam String query,Pageable pageable) {

        log.info("pageable sort {}",pageable.getSort());
        log.info("pageable get offset {}",pageable.getOffset());
        log.info("pageable get page {}",pageable.getPageSize());
        log.info("pageable get pagenumber {}",pageable.getPageNumber());

        List<VideoResponseDto> fetch=videoService.searchVideoQuery(query,pageable);
        return fetch;
    }


    @ApiOperation(value = "관심 동영상 리스트 반환", notes = "유저의 시청기록을 기반으로 관심영상 반환")
    @GetMapping("/concernVideo/{videoId}")
    public List<VideoResponseDto> getConcernVideo(@ModelAttribute PageDto pageDto,@PathVariable("videoId")Long videoId ) {
        String userEmail = authenticationFacade.getAuthentication().getName();
        videoService.retrieveConcernVideos(pageDto,userEmail,videoId);
        return null;
    }

    // ip 조회
     private String getIp() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = req.getRemoteAddr();
        return ip;
    }
}
