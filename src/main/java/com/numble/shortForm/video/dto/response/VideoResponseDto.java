package com.numble.shortForm.video.dto.response;

import com.numble.shortForm.user.entity.ProfileImg;
import com.numble.shortForm.video.entity.Thumbnail;
import com.numble.shortForm.video.entity.VideoType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Slf4j
public class VideoResponseDto {

    private Long videoId;

    private Long usersId;

    private ProfileImg profileImg;

    private VideoType videoType;

    private String url;

    private String nickname;

    private Long showId;

    private String title;

    private Thumbnail thumbnail;

    private boolean isBlock;

    private Long view;

    private LocalDate created_at;

    private Long duration;

    private Long likes;

    private String description;

    private boolean isLiked;

    private boolean isOwner;

    private List<String> tags;



    @QueryProjection
    public VideoResponseDto(Long videoId) {
        this.videoId = videoId;
    }

    @QueryProjection
    public VideoResponseDto(Long videoId, Long usersId, String nickname, Long showId, String title, Thumbnail thumbnail, boolean isBlock, Long view, LocalDateTime created_at, Long duration, Integer likes,ProfileImg profileImg) {
        this.videoId = videoId;
        this.usersId = usersId;
        this.nickname = nickname;
        this.showId = showId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.isBlock = isBlock;
        this.view = view;
        this.created_at = created_at.toLocalDate();
        this.duration = duration;
        this.likes = likes.longValue();
        this.profileImg =profileImg;

    }
    @QueryProjection
    public VideoResponseDto(Long videoId, Long usersId, String nickname, Long showId, String title, Thumbnail thumbnail, boolean isBlock, Long view, LocalDateTime created_at, Long duration, Integer likes, String description,boolean isOwner,VideoType videoType,ProfileImg profileImg) {
        this.videoId = videoId;
        this.usersId = usersId;
        this.nickname = nickname;
        this.showId = showId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.isBlock = isBlock;
        this.view = view;
        this.created_at = created_at.toLocalDate();
        this.duration = duration;
        this.likes = likes.longValue();
        this.description = description;
        this.videoType = videoType;
        this.isOwner = isOwner;
        this.profileImg =profileImg;
    }

    @QueryProjection
    public VideoResponseDto(Long videoId, Long usersId, String nickname, Long showId, String title, Thumbnail thumbnail, boolean isBlock, Long view, LocalDateTime created_at, Long duration, Integer likes, String description,boolean isOwner,VideoType videoType,String url,ProfileImg profileImg) {
        this.videoId = videoId;
        this.usersId = usersId;
        this.nickname = nickname;
        this.showId = showId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.isBlock = isBlock;
        this.view = view;
        this.created_at = created_at.toLocalDate();
        this.duration = duration;
        this.likes = likes.longValue();
        this.description = description;
        this.isOwner = isOwner;
        this.videoType = videoType;
        this.url = url;
        this.profileImg =profileImg;
    }
}
