package ject.componote.domain.comment.application;

import ject.componote.domain.auth.domain.Job;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.model.ProfileImage;
import ject.componote.domain.comment.dao.CommentFindByComponentDao;
import ject.componote.domain.comment.dao.CommentFindByMemberDao;
import ject.componote.domain.comment.dao.CommentLikeRepository;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;
import ject.componote.domain.comment.dto.create.response.CommentCreateResponse;
import ject.componote.domain.comment.dto.find.response.CommentFindByComponentResponse;
import ject.componote.domain.comment.dto.find.response.CommentFindByMemberResponse;
import ject.componote.domain.comment.dto.like.event.CommentLikeEvent;
import ject.componote.domain.comment.dto.like.event.CommentUnlikeEvent;
import ject.componote.domain.comment.dto.update.request.CommentUpdateRequest;
import ject.componote.domain.comment.error.AlreadyLikedException;
import ject.componote.domain.comment.error.NoLikedException;
import ject.componote.domain.comment.error.NotFoundParentCommentException;
import ject.componote.domain.comment.model.CommentContent;
import ject.componote.domain.comment.model.CommentImage;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.common.model.Count;
import ject.componote.fixture.CommentFixture;
import ject.componote.infra.file.application.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ject.componote.fixture.CommentFixture.답글_이미지X;
import static ject.componote.fixture.MemberFixture.KIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    CommentRepository commentRepository;

    @Mock
    CommentLikeRepository commentLikeRepository;

    @Mock
    FileService fileService;

    @InjectMocks
    CommentService commentService;

    final Member member = KIM.생성(1L);
    final AuthPrincipal authPrincipal = AuthPrincipal.from(member);
    final Pageable pageable = PageRequest.of(0, 10);

    @ParameterizedTest
    @DisplayName("댓글 생성")
    @EnumSource(value = CommentFixture.class)
    public void create(final CommentFixture fixture) throws Exception {
        // given
        final CommentCreateRequest createRequest = fixture.toCreateRequest();
        final Comment comment = fixture.생성(authPrincipal.id());
        final CommentCreateResponse expect = CommentCreateResponse.from(comment);

        // when
        final Long parentId = comment.getParentId();
        if (parentId != null) {
            doReturn(true).when(commentRepository)
                    .existsById(parentId);
        }

        doReturn(comment).when(commentRepository)
                .save(any());
        doNothing().when(fileService)
                .moveImage(comment.getImage().getImage());
        final CommentCreateResponse actual = commentService.create(authPrincipal, createRequest);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("댓글 생성시 잘못된 parentId가 입력된 경우 예외 발생")
    public void createWhenInvalidParentId() {
        // given
        final CommentCreateRequest createRequest = 답글_이미지X.toCreateRequest();
        final Long parentId = createRequest.parentId();

        // when
        doReturn(false).when(commentRepository)
                .existsById(parentId);
        // then
        assertThatThrownBy(() -> commentService.create(authPrincipal, createRequest))
                .isInstanceOf(NotFoundParentCommentException.class);
    }

    @Test
    @DisplayName("마이 페이지 댓글 페이징 조회")
    public void getCommentsByMemberId() throws Exception {
        // given
        final Long memberId = authPrincipal.id();
        final List<CommentFindByMemberDao> content = List.of(
                new CommentFindByMemberDao(1L, null, "컴포넌트 제목1", null, CommentContent.from("댓글 내용1"), LocalDateTime.now(), false),
                new CommentFindByMemberDao(2L, 1L, "컴포넌트 제목2", CommentContent.from("댓글 내용1"), CommentContent.from("댓글 내용2"), LocalDateTime.now(), true)
        );
        final Page<CommentFindByMemberDao> page = new PageImpl<>(content, pageable, content.size());
        final PageResponse<CommentFindByMemberResponse> expect = PageResponse.from(
                page.map(CommentFindByMemberResponse::from)
        );

        // when
        doReturn(page).when(commentRepository)
                .findAllByMemberIdWithPagination(memberId, pageable);
        final PageResponse<CommentFindByMemberResponse> actual = commentService.getCommentsByMemberId(authPrincipal, pageable);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("비로그인 컴포넌트 댓글 페이징 조회")
    public void getCommentsByComponentIdNoLoggedIn() throws Exception {
        // given
        final Long componentId = 1L;
        final List<CommentFindByComponentDao> content = List.of(
                new CommentFindByComponentDao(1L, Nickname.from("닉네임1"), ProfileImage.from(null), Job.DEVELOPER, 1L, null, CommentImage.from(null), CommentContent.from("댓글 내용1"), LocalDateTime.now(), Count.create(), Count.create(), false, false),
                new CommentFindByComponentDao(2L, Nickname.from("닉네임2"), ProfileImage.from(null), Job.DEVELOPER, 2L, 1L, CommentImage.from(null), CommentContent.from("댓글 내용2"), LocalDateTime.now(), Count.create(), Count.create(), false, true)
        );
        final Page<CommentFindByComponentDao> page = new PageImpl<>(content, pageable, content.size());
        final PageResponse<CommentFindByComponentResponse> expect = PageResponse.from(
                page.map(CommentFindByComponentResponse::from)
        );

        // when
        doReturn(page).when(commentRepository)
                .findAllByComponentIdWithPagination(componentId, pageable);
        final PageResponse<CommentFindByComponentResponse> actual = commentService.getCommentsByComponentId(null, componentId, pageable);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("로그인 컴포넌트 댓글 페이징 조회")
    public void getCommentsByComponentIdWhenLoggedIn() throws Exception {
        // given
        final Long componentId = 1L;
        final Long memberId = authPrincipal.id();
        final List<CommentFindByComponentDao> content = List.of(
                new CommentFindByComponentDao(1L, Nickname.from("닉네임1"), ProfileImage.from(null), Job.DEVELOPER, 1L, null, CommentImage.from(null), CommentContent.from("댓글 내용1"), LocalDateTime.now(), Count.create(), Count.create(), false, false),
                new CommentFindByComponentDao(2L, Nickname.from("닉네임2"), ProfileImage.from(null), Job.DEVELOPER, 2L, 1L, CommentImage.from(null), CommentContent.from("댓글 내용2"), LocalDateTime.now(), Count.create(), Count.create(), false, true)
        );
        final Page<CommentFindByComponentDao> page = new PageImpl<>(content, pageable, content.size());
        final PageResponse<CommentFindByComponentResponse> expect = PageResponse.from(
                page.map(CommentFindByComponentResponse::from)
        );

        // when
        doReturn(page).when(commentRepository)
                .findAllByComponentIdWithLikeStatusAndPagination(componentId, memberId, pageable);
        final PageResponse<CommentFindByComponentResponse> actual = commentService.getCommentsByComponentId(authPrincipal, componentId, pageable);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @ParameterizedTest
    @DisplayName("댓글 이미지만 수정")
    @EnumSource(value = CommentFixture.class)
    public void updateImage(final CommentFixture fixture) throws Exception {
        // given
        final Long memberId = authPrincipal.id();
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final String content = comment.getContent().getValue();
        final String newObjectKey = "/comment/new.jpg";
        final CommentUpdateRequest request = new CommentUpdateRequest(newObjectKey, content);

        // when
        doReturn(Optional.of(comment)).when(commentRepository)
                .findByIdAndMemberId(commentId, memberId);

        // then
        assertDoesNotThrow(
                () -> commentService.update(authPrincipal, commentId, request)
        );
        assertThat(comment.getImage().getImage().getObjectKey()).isEqualTo(newObjectKey);
    }

    @ParameterizedTest
    @DisplayName("댓글 내용만 수정")
    @EnumSource(value = CommentFixture.class)
    public void updateContent(final CommentFixture fixture) throws Exception {
        // given
        final Long memberId = authPrincipal.id();
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final String objectKey = comment.getImage().getImage().getObjectKey();
        final String newContent = "수정된 내용";
        final CommentUpdateRequest request = new CommentUpdateRequest(objectKey, newContent);

        // when
        doReturn(Optional.of(comment)).when(commentRepository)
                .findByIdAndMemberId(commentId, memberId);

        // then
        assertDoesNotThrow(
                () -> commentService.update(authPrincipal, commentId, request)
        );
        assertThat(comment.getContent().getValue()).isEqualTo(newContent);
    }

    @ParameterizedTest
    @DisplayName("댓글 이미지, 내용 모두 수정")
    @EnumSource(value = CommentFixture.class)
    public void updateAll(final CommentFixture fixture) throws Exception {
        // given
        final Long memberId = authPrincipal.id();
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final String newContent = "수정된 내용";
        final String newObjectKey = "/comment/new.jpg";
        final CommentUpdateRequest request = new CommentUpdateRequest(newObjectKey, newContent);

        // when
        doReturn(Optional.of(comment)).when(commentRepository)
                .findByIdAndMemberId(commentId, memberId);

        // then
        assertDoesNotThrow(
                () -> commentService.update(authPrincipal, commentId, request)
        );
        assertThat(comment.getImage().getImage().getObjectKey()).isEqualTo(newObjectKey);
        assertThat(comment.getContent().getValue()).isEqualTo(newContent);
    }

    @ParameterizedTest
    @DisplayName("댓글 삭제")
    @EnumSource(value = CommentFixture.class)
    public void delete(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();

        // when
        doNothing().when(commentRepository)
                .deleteByIdAndMemberId(commentId, memberId);

        // then
        assertDoesNotThrow(
                () -> commentService.delete(authPrincipal, commentId)
        );
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요")
    @EnumSource(value = CommentFixture.class)
    public void likeComment(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();
        final CommentLikeEvent event = CommentLikeEvent.of(authPrincipal, commentId);

        // when
        doReturn(false).when(commentLikeRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);
        doNothing().when(eventPublisher)
                .publishEvent(event);

        // then
        assertDoesNotThrow(
                () -> commentService.likeComment(authPrincipal, commentId)
        );
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요 취소")
    @EnumSource(value = CommentFixture.class)
    public void unlikeComment(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();
        final CommentUnlikeEvent event = CommentUnlikeEvent.of(authPrincipal, commentId);

        // when
        doReturn(true).when(commentLikeRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);
        doNothing().when(eventPublisher)
                .publishEvent(event);

        // then
        assertDoesNotThrow(
                () -> commentService.unlikeComment(authPrincipal, commentId)
        );
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요시 좋아요를 이미 좋아요를 눌렀다면 예외 발생")
    @EnumSource(value = CommentFixture.class)
    public void likeCommentWhenAlreadyLiked(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();

        // when
        doReturn(true).when(commentLikeRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);

        // then
        assertThatThrownBy(() -> commentService.likeComment(authPrincipal, commentId))
                .isInstanceOf(AlreadyLikedException.class);
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요 취소시 좋아요를 누른적이 없는 경우 예외 발생")
    @EnumSource(value = CommentFixture.class)
    public void unlikeCommentWhenNoLike(final CommentFixture fixture) throws Exception {
        // given
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();
        final Long memberId = authPrincipal.id();

        // when
        doReturn(false).when(commentLikeRepository)
                .existsByCommentIdAndMemberId(commentId, memberId);

        // then
        assertThatThrownBy(() -> commentService.unlikeComment(authPrincipal, commentId))
                .isInstanceOf(NoLikedException.class);
    }
}