package ject.componote.domain.comment.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.like.event.CommentLikeNotificationEvent;
import ject.componote.fixture.CommentFixture;
import ject.componote.infra.notification.application.NotificationProducer;
import ject.componote.infra.notification.dto.create.request.CommentLikeNotificationCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static ject.componote.fixture.MemberFixture.이메일O_회원;
import static ject.componote.fixture.MemberFixture.이메일X_회원;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentLikeNotificationEventListenerTest {
    @Mock
    CommentRepository commentRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    NotificationProducer notificationProducer;

    @InjectMocks
    CommentLikeNotificationEventListener commentLikeNotificationEventListener;

    @ParameterizedTest
    @DisplayName("댓글 좋아요 발생 시 메시지 발행")
    @EnumSource(value = CommentFixture.class)
    public void handleLikeNotificationEvent(final CommentFixture fixture) throws Exception {
        // given
        final Member receiver = spy(이메일O_회원.생성(1L));
        final Member sender = spy(이메일O_회원.생성(2L));
        doReturn(1L).when(receiver)
                .getId();
        doReturn(2L).when(sender)
                .getId();

        final Long senderId = sender.getId();
        final Long receiverId = receiver.getId();

        final Comment comment = fixture.생성(receiverId);
        final Long commentId = comment.getId();

        final CommentLikeNotificationEvent event = CommentLikeNotificationEvent.of(comment, senderId);
        final CommentLikeNotificationCreateRequest request = CommentLikeNotificationCreateRequest.of(sender, receiver, comment);

        // when
        doReturn(Optional.of(sender)).when(memberRepository)
                .findById(senderId);
        doReturn(Optional.of(receiver)).when(memberRepository)
                .findById(receiverId);
        doReturn(Optional.of(comment)).when(commentRepository)
                .findById(commentId);
        doNothing().when(notificationProducer).sendCommentLikeNotification(request);
        
        // then
        commentLikeNotificationEventListener.handleLikeNotificationEvent(event);
        verify(notificationProducer, timeout(1000))
                .sendCommentLikeNotification(request);
    }

    @ParameterizedTest
    @DisplayName("댓글 좋아요 발생 시 이메일이 없는 회원은 메시지 발행 X")
    @EnumSource(value = CommentFixture.class)
    public void handleLikeNotificationEventWhenReceiverHasNoEmail(final CommentFixture fixture) throws Exception {
        // given
        final Member sender = 이메일X_회원.생성(2L);
        final Member receiver = 이메일X_회원.생성(2L);

        final Long senderId = sender.getId();
        final Long receiverId = receiver.getId();

        final Comment comment = fixture.생성(receiverId);
        final Long commentId = comment.getId();

        final CommentLikeNotificationEvent event = CommentLikeNotificationEvent.of(comment, senderId);

        // when
        doReturn(Optional.of(receiver)).when(memberRepository)
                .findById(receiverId);
        doReturn(Optional.of(comment)).when(commentRepository)
                .findById(commentId);

        // then
        commentLikeNotificationEventListener.handleLikeNotificationEvent(event);
        verify(notificationProducer, never())
                .sendCommentLikeNotification(any());
    }
}