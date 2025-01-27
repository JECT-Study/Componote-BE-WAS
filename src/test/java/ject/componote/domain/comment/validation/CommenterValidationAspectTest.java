package ject.componote.domain.comment.validation;

import ject.componote.domain.member.domain.Member;
import ject.componote.domain.member.model.AuthPrincipal;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.error.NotFoundCommentException;
import ject.componote.fixture.CommentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ject.componote.fixture.MemberFixture.KIM;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CommenterValidationAspectTest {
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommenterValidationAspect commenterValidationAspect;

    final Member member = KIM.생성(1L);
    final AuthPrincipal authPrincipal = AuthPrincipal.from(member);

    @ParameterizedTest
    @DisplayName("댓글 작성자 검증")
    @EnumSource(CommentFixture.class)
    public void validate(final CommentFixture fixture) throws Exception {
        // given
        final Long memberId = member.getId();
        final Comment comment = fixture.생성(memberId);
        final Long commentId = comment.getId();

        // when
        doReturn(true).when(commentRepository)
                .existsByIdAndMemberId(commentId, memberId);

        // then
        assertDoesNotThrow(
                () -> commenterValidationAspect.validate(authPrincipal, commentId)
        );
    }

    @ParameterizedTest
    @DisplayName("댓글 작성자가 아닌 경우 예외 발생")
    @EnumSource(CommentFixture.class)
    public void validateWhenInvalidMemberId(final CommentFixture fixture) throws Exception {
        // given
        final Long memberId = member.getId();
        final Comment comment = fixture.생성();
        final Long commentId = comment.getId();

        // when
        doReturn(false).when(commentRepository)
                .existsByIdAndMemberId(commentId, memberId);

        // then
        assertThatThrownBy(() -> commenterValidationAspect.validate(authPrincipal, commentId))
                .isInstanceOf(NotFoundCommentException.class);
    }
}