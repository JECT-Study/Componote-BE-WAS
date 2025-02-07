//package ject.componote.domain.comment.application;
//
//import ject.componote.domain.auth.domain.Member;
//import ject.componote.domain.comment.dao.CommentRepository;
//import ject.componote.domain.comment.domain.Comment;
//import ject.componote.domain.comment.dto.reply.event.CommentReplyCountIncreaseEvent;
//import ject.componote.domain.common.model.Count;
//import ject.componote.fixture.CommentFixture;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.EnumSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static ject.componote.fixture.CommentFixture.답글_이미지O;
//import static ject.componote.fixture.MemberFixture.이메일X_회원;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.spy;
//
//@ExtendWith(MockitoExtension.class)
//class CommentReplyCountEventListenerTest {
//    @Mock
//    CommentRepository commentRepository;
//
//    @InjectMocks
//    CommentReplyCountEventListener commentReplyCountEventListener;
//
//    Member member = 이메일X_회원.생성(1L);
//
//    @ParameterizedTest
//    @DisplayName("대댓글 개수 증가 이벤트 처리")
//    @EnumSource(value = CommentFixture.class)
//    public void handleCommentReplyCountIncreaseEvent(final CommentFixture fixture) throws Exception {
//        // given
//        final Long memberId = member.getId();
//        final Comment parent = fixture.생성(memberId);
//        final Long parentId = parent.getParentId();
//
//        final Comment comment = spy(답글_이미지O.생성());
//        doReturn(parentId).when(comment)
//                .getParentId();
//
//        final CommentReplyCountIncreaseEvent event = CommentReplyCountIncreaseEvent.from(comment);
//        final Count previousReplyCount = parent.getReplyCount();
//
//        // when
//        doReturn(Optional.of(parent)).when(commentRepository)
//                .findById(parent.getId());
//        commentReplyCountEventListener.handleCommentReplyCountIncreaseEvent(event);
//
//        // then
//        final Count currentReplyCount = parent.getReplyCount();
//        previousReplyCount.increase();
//        assertThat(currentReplyCount).isEqualTo(previousReplyCount);
//    }
//
//    @Test
//    @DisplayName("대댓글 수 증가 이벤트 처리 시 댓글 ID가 잘못된 경우 예외 발생")
//    public void handleCommentReplyCountIncreaseEventWhenInvalidCommentId() throws Exception {
//
//        // given
//
//        // when
//
//        // then
//
//    }
//}