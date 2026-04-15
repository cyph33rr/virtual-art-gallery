package com.aura.gallery.controller;
import com.aura.gallery.model.User;
import com.aura.gallery.repository.UserRepository;
import com.aura.gallery.security.JwtUtils;
import com.aura.gallery.service.OtpService;
import com.aura.gallery.util.OtpUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private OtpService otpService;

    @PostMapping("/signup")public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req){if(userRepo.existsByEmail(req.getEmail())){return ResponseEntity.badRequest().body(new ErrorResponse("Email already registered"));}User user=User.builder().name(req.getName()).email(req.getEmail()).password(encoder.encode(req.getPassword())).role(req.getRole()).isOtpVerified(true).build();userRepo.save(user);String token=jwtUtils.generateToken(user.getEmail(),user.getRole().name());return ResponseEntity.ok(new AuthResponse(token,user.getId(),user.getName(),user.getEmail(),user.getRole()));}

    @PostMapping("/login")public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req){return userRepo.findByEmail(req.getEmail()).filter(u->encoder.matches(req.getPassword(),u.getPassword())).map(u->{String token=jwtUtils.generateToken(u.getEmail(),u.getRole().name());return ResponseEntity.ok(new AuthResponse(token,u.getId(),u.getName(),u.getEmail(),u.getRole()));}).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse()));}

    @PostMapping("/signup-request")public ResponseEntity<?> signupRequest(@Valid @RequestBody SignupRequestOtp req){if(userRepo.existsByEmail(req.getEmail())){return ResponseEntity.badRequest().body(new ErrorResponse("Email already registered"));}if(!req.getEmail().contains("@")){return ResponseEntity.badRequest().body(new ErrorResponse("Invalid email format"));}String otp=OtpUtils.generateOtp();otpService.sendOtpEmail(req.getEmail(),otp);return ResponseEntity.ok(new OtpResponse("OTP sent to your email",req.getEmail()));}

    @PostMapping("/signup-verify")public ResponseEntity<?> signupVerify(@Valid @RequestBody SignupVerifyRequest req){if(userRepo.existsByEmail(req.getEmail())){return ResponseEntity.badRequest().body(new ErrorResponse("Email already registered"));}if(!OtpUtils.isValidOtp(req.getOtp())){return ResponseEntity.badRequest().body(new ErrorResponse("Invalid OTP format"));}User user=User.builder().name(req.getName()).email(req.getEmail()).password(encoder.encode(req.getPassword())).role(req.getRole()).isOtpVerified(true).build();userRepo.save(user);String token=jwtUtils.generateToken(user.getEmail(),user.getRole().name());return ResponseEntity.ok(new AuthResponse(token,user.getId(),user.getName(),user.getEmail(),user.getRole()));}

    @PostMapping("/login-request")public ResponseEntity<?> loginRequest(@Valid @RequestBody LoginRequestOtp req){var userOpt=userRepo.findByEmail(req.getEmail()).filter(u->encoder.matches(req.getPassword(),u.getPassword()));if(userOpt.isEmpty()){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Email or password is incorrect"));}User user=userOpt.get();String otp=OtpUtils.generateOtp();user.setOtp(otp);user.setOtpCreatedAt(java.time.LocalDateTime.now());user.setOtpExpiresAt(OtpUtils.getOtpExpirationTime());userRepo.save(user);otpService.sendOtpEmail(req.getEmail(),otp);return ResponseEntity.ok(new OtpResponse("OTP sent to your email",req.getEmail()));}

    @PostMapping("/login-verify")public ResponseEntity<?> loginVerify(@Valid @RequestBody LoginVerifyRequest req){var userOpt=userRepo.findByEmail(req.getEmail());if(userOpt.isEmpty()){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not found"));}User user=userOpt.get();if(user.getOtp()==null||!user.getOtp().equals(req.getOtp())){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid OTP"));}if(OtpUtils.isOtpExpired(user.getOtpExpiresAt())){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("OTP has expired"));}user.setOtp(null);user.setOtpCreatedAt(null);user.setOtpExpiresAt(null);user.setIsOtpVerified(true);userRepo.save(user);String token=jwtUtils.generateToken(user.getEmail(),user.getRole().name());return ResponseEntity.ok(new AuthResponse(token,user.getId(),user.getName(),user.getEmail(),user.getRole()));}

    static class SignupRequest{@NotBlank String name;@Email @NotBlank String email;@Size(min=6)@NotBlank String password;@NotNull User.Role role;public String getName(){return name;}public void setName(String name){this.name=name;}public String getEmail(){return email;}public void setEmail(String email){this.email=email;}public String getPassword(){return password;}public void setPassword(String password){this.password=password;}public User.Role getRole(){return role;}public void setRole(User.Role role){this.role=role;}}

    static class SignupRequestOtp{@Email @NotBlank String email;public String getEmail(){return email;}public void setEmail(String email){this.email=email;}}

    static class SignupVerifyRequest{@NotBlank String name;@Email @NotBlank String email;@Size(min=6)@NotBlank String password;@NotNull User.Role role;@NotBlank String otp;public String getName(){return name;}public void setName(String name){this.name=name;}public String getEmail(){return email;}public void setEmail(String email){this.email=email;}public String getPassword(){return password;}public void setPassword(String password){this.password=password;}public User.Role getRole(){return role;}public void setRole(User.Role role){this.role=role;}public String getOtp(){return otp;}public void setOtp(String otp){this.otp=otp;}}

    static class LoginRequest{@Email @NotBlank String email;@NotBlank String password;public String getEmail(){return email;}public void setEmail(String email){this.email=email;}public String getPassword(){return password;}public void setPassword(String password){this.password=password;}}

    static class LoginRequestOtp{@Email @NotBlank String email;@NotBlank String password;public String getEmail(){return email;}public void setEmail(String email){this.email=email;}public String getPassword(){return password;}public void setPassword(String password){this.password=password;}}

    static class LoginVerifyRequest{@Email @NotBlank String email;@NotBlank String otp;public String getEmail(){return email;}public void setEmail(String email){this.email=email;}public String getOtp(){return otp;}public void setOtp(String otp){this.otp=otp;}}

    static class AuthResponse{String token;Long id;String name;String email;User.Role role;public AuthResponse(){}public AuthResponse(String token,Long id,String name,String email,User.Role role){this.token=token;this.id=id;this.name=name;this.email=email;this.role=role;}public String getToken(){return token;}public void setToken(String token){this.token=token;}public Long getId(){return id;}public void setId(Long id){this.id=id;}public String getName(){return name;}public void setName(String name){this.name=name;}public String getEmail(){return email;}public void setEmail(String email){this.email=email;}public User.Role getRole(){return role;}public void setRole(User.Role role){this.role=role;}}

    static class OtpResponse{String message;String email;public OtpResponse(String message,String email){this.message=message;this.email=email;}public String getMessage(){return message;}public void setMessage(String message){this.message=message;}public String getEmail(){return email;}public void setEmail(String email){this.email=email;}}

    static class ErrorResponse{String message;public ErrorResponse(String message){this.message=message;}public String getMessage(){return message;}public void setMessage(String message){this.message=message;}}
}
