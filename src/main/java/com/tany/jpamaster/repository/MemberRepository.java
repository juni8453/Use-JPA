package com.tany.jpamaster.repository;

import com.tany.jpamaster.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
