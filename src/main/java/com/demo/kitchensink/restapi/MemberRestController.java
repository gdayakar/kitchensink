package com.demo.kitchensink.restapi;

import com.demo.kitchensink.data.Member;
import com.demo.kitchensink.service.MemberService;
import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
//import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/members")
public class MemberRestController {

    private MemberService memberService;

    //private Validator validator;

    public MemberRestController(MemberService memberService)
    {
        this.memberService = memberService;
//        this.validator = validator;

    }

    // Get all members
    @GetMapping(produces = "application/json")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    // Get a specific member by ID
    @GetMapping("/{id:[0-9][0-9]*}")
    public ResponseEntity<Member> getMemberById(@PathVariable String id) throws Exception {

        Optional<Member> member = memberService.getMemberById(id);

        return member.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new member
    @PostMapping(consumes = "application/json")
    public Response createMember(@RequestBody Member member) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates member using bean validation
            //validateMember(member);

            memberService.registerMember(member);

            // Create an "ok" response
//            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (jakarta.validation.ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        return builder.build();
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

    private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
//        // Create a bean validator and check for issues.
//        Set<ConstraintViolation<Member>> violations = validator.validate(member);
//
//        if (!violations.isEmpty()) {
//            throw new ConstraintViolationException(new HashSet<>(violations));
//        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException(null);
        }
    }

    public boolean emailAlreadyExists(String email) {
        Optional<Member> optionalMember = null;
        try {
             optionalMember = memberService.getMemberByEmail(email);
        } catch (Exception e) {
            // ignore
        }
        return optionalMember != null;
    }

    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
}

