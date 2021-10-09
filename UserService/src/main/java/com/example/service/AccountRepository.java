package com.example.service;

import com.example.entity.account.Account;
import com.example.entity.zone_tag.ResponseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUserId(String userId);

    @Query("select a from Account a left join fetch a.tags where a.userId = :userId")
    Account findByUserIdWithTags(@Param("userId") String userId);

    @Query("select a from Account a left join fetch a.zones where a.userId = :userId")
    Account findByUserIdWithZones(@Param("userId") String userId);

    @Query(value = "select a.* from Account a left join account_tag t on a.id = t.account_id where t.tag_id = :tagId", nativeQuery = true)
    List<Account> findByTagId(long tagId);

    @Query(value = "select a.* from Account a left join account_zone z on a.id = z.account_id where z.zone_id = :zoneId", nativeQuery = true)
    List<Account> findByZoneId(long zoneId);
}
