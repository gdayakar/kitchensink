package com.demo.kitchensink.restapi;

import com.demo.kitchensink.data.Member;
import com.demo.kitchensink.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
public class MemberRestController {

    private MemberService memberService;

    public MemberRestController(MemberService memberService)
    {
        this.memberService = memberService;
    }

    // Get all members
    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    // Get a specific member by ID
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable String id) {
        Optional<Member> member = memberService.getMemberById(id);
        return member.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new member
    @PostMapping(consumes = "application/json")
    public Member createMember(@RequestBody Member member) throws Exception {
        return memberService.registerMember(member);
    }

    // Update an existing member
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable String id, @RequestBody Member memberDetails) throws Exception {
        Optional<Member> optionalMember = memberService.getMemberById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setName(memberDetails.getName());
            member.setEmail(memberDetails.getEmail());
            member.setPhoneNumber(memberDetails.getPhoneNumber());
            return ResponseEntity.ok(memberService.updateMember(member));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable String id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}

