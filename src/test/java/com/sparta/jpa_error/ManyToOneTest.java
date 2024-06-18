package com.sparta.jpa_error;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ManyToOneTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    @DisplayName("다대일 관계에서 주로 발생하는 에러1")
    void test1() {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        Team team = new Team("wooteco");
        em.persist(team); // team 객체를 영속상태로 만든다.

        Member member = new Member("kth990303", team, "admin");
        em.persist(member); // member 객체를 영속상태로 만든다.

        // team 객체에 멤버를 넣어주지 않았으니 아무것도 나오지 않는다.
        System.out.println("===========================================");
        List<Member> findMembers = team.getMembers();
        for (Member findMember : findMembers) {
            System.out.println(findMember.getUsername());
        }
        System.out.println("===========================================");

        Team findTeam = em.find(Team.class, team.getId()); // team
        List<Member> teamMembers = findTeam.getMembers();
        assertThrows(IndexOutOfBoundsException.class, () -> teamMembers.get(0).setUsername("kth990202"));

        System.out.println(member.getUsername());

        transaction.commit();
    }

    @Test
    @Transactional
    @DisplayName("다대일 관계에서 주로 발생하는 에러1 : 해결")
    void test2() {
        Team team = new Team("wooteco");
        em.persist(team); // team 객체를 영속상태로 만든다.

        Member member = new Member("kth990303", team, "admin");
        em.persist(member); // member 객체를 영속상태로 만든다.

        // team 객체에 멤버를 넣어주지 않았으니 아무것도 나오지 않는다.
        System.out.println("===========================================");
        List<Member> findMembers = team.getMembers();
        for (Member findMember : findMembers) {
            System.out.println(findMember.getUsername());
        }
        System.out.println("===========================================");

        Team findTeam = em.find(Team.class, team.getId()); // team
        List<Member> teamMembers = findTeam.getMembers();
        teamMembers.get(0).setUsername("kth990202");

        System.out.println(member.getUsername());
    }

    @Test
    @Transactional
    @DisplayName("다대일 관계에서 주로 발생하는 에러2")
    void test3() {
        Team team = new Team("wooteco");
        em.persist(team); // team 객체를 영속상태로 만든다.

        Member member = new Member("kth990303", team, "admin");
        em.persist(member); // member 객체를 영속상태로 만든다.

        em.flush();
        em.clear();

        member.setUsername("kth990202");
        Member findMember = em.find(Member.class, member.getId());

        System.out.println(findMember.getUsername());
    }
    @Test
    @Transactional
    @DisplayName("다대일 관계에서 주로 발생하는 에러2 : 해결")
    void test4() {
        Team team = new Team("wooteco");
        em.persist(team); // team 객체를 영속상태로 만든다.

        Member member = new Member("kth990303", team, "admin");
        em.persist(member); // member 객체를 영속상태로 만든다.

        em.flush();

        member.setUsername("kth990202");

        Member findMember = em.find(Member.class, member.getId());

        System.out.println(findMember.getUsername());
    }
}
