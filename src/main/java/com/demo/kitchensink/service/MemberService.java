package com.demo.kitchensink.service;

import com.demo.kitchensink.data.Member;
import com.demo.kitchensink.data.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository)
    {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(String id) {
        return  memberRepository.findById(id);
    }

    public Member registerMember(Member member) {
        return memberRepository.save(member);
    }

    public Member updateMember(Member memberDetails) throws Exception {
        Optional<Member> optionalMember = memberRepository.findById(memberDetails.getId());

        if(optionalMember.isEmpty())
        {
            throw new Exception("Member Doesn't exist.");
        }
        Member member = optionalMember.get();
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPhoneNumber(memberDetails.getPhoneNumber());
        return memberRepository.save(member);
    }

    public void deleteMember(String id) {
        memberRepository.deleteById(id);
    }
}
