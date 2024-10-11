package com.demo.kitchensink.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, String> {

    @Override
    Optional<Member> findById(String id);

    @Override
    List<Member> findAll();

    Optional<Member> findByEmail(String email);
}
