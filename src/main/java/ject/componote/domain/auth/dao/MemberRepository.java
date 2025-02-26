package ject.componote.domain.auth.dao;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.Email;
import ject.componote.domain.auth.model.Nickname;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsBySocialAccountId(final Long socialAccountId);
    Optional<Member> findBySocialAccountId(final Long socialAccountId);

    @Query("SELECT NEW ject.componote.domain.auth.dao.MemberSummaryDao(m.email, m.nickname, m.profileImage, m.job) " +
            "FROM Member m " +
            "WHERE m.id =:id")
    Optional<MemberSummaryDao> findSummaryById(@Param("id") final Long id);

    boolean existsByNickname(final Nickname nickname);
    boolean existsByEmail(final Email email);
}
