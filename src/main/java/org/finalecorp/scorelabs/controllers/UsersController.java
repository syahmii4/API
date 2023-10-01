package org.finalecorp.scorelabs.controllers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Builder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.requestObjects.ChangePasswordForm;
import org.finalecorp.scorelabs.requestObjects.EmailContent;
import org.finalecorp.scorelabs.requestObjects.RegisterForm;
import org.finalecorp.scorelabs.services.EmailService;
import org.finalecorp.scorelabs.services.PasswordChangeRequestService;
import org.finalecorp.scorelabs.services.RoleService;
import org.finalecorp.scorelabs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(maxAge = 3600, origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = {"Authorization"}, methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class UsersController {
    @Value("${space.endpoint}") // Replace with your Space endpoint
    private String spaceEndpoint;

    @Value("${space.bucket}") // Replace with your Space name
    private String spaceBucket;
    @Value("${space.url}") // Replace with your Space name
    private String spaceUrl;
    @Value("${spring.aws.accessKeyId}") // Access Key ID
    private String awsAccessKeyId;

    @Value("${spring.aws.secretKey}") // Secret Access Key
    private String awsSecretKey;
    public UserService userService;
    public RoleService roleService;
    public EmailService emailService;
    public PasswordChangeRequestService passwordChangeRequestService;
    @Autowired
    public void setUserService(UserService userService, RoleService roleService, PasswordChangeRequestService passwordChangeRequestService) {
        this.userService = userService;
        this.roleService = roleService;
        this.emailService = new EmailService();
        this.passwordChangeRequestService = passwordChangeRequestService;
    }
    @PostMapping("/rest/auth/createuser")
    public String register(@RequestBody RegisterForm registerForm){

        userService.createUser(registerForm);
        return "User registered";
    }

    @ResponseBody
    @PostMapping("/rest/auth/checkuser")
    public Boolean checkUser(@RequestBody String username){
        return userService.userExists(username);
    }

    @ResponseBody
    @PostMapping("rest/auth/checkemail")
    public Boolean checkEmail(@RequestBody String email) { return userService.getUserByEmail(email) != null; }

    @CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.OPTIONS, RequestMethod.GET}, exposedHeaders = {"Content-Type"}, allowedHeaders = {"Authorization"}, allowCredentials = "true")
    @ResponseBody
    @GetMapping("/api/v1/user/getuser")
    public ResponseEntity<Map<String, String>> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();
        String emailAddress = (String) authDetails.get("email");
        String fullName = (String) authDetails.get("fullName");
        String role = (String) authDetails.get("role");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Map<String, String> userResponse = new HashMap<>();

        userResponse.put("username", username);
        userResponse.put("email", emailAddress);
        userResponse.put("fullName", fullName);
        userResponse.put("role", roleService.getRoleName(Integer.parseInt(role)));
        userResponse.put("profilepic", userService.getUserByUsername(username).getProfilePicture());

        return new ResponseEntity<>(userResponse, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/api/v1/user/changepassword")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordForm form){
        try {
            Users targetUser = userService.getUserByUsername(form.username);
            if(userService.validatePassword(targetUser, form.oldPassword)){
                userService.changePassword(targetUser, form.newPassword);
                return new ResponseEntity<>("New Password Saved", HttpStatusCode.valueOf(200));
            }
            else {
                return new ResponseEntity<>("Incorrect Password", HttpStatusCode.valueOf(403));
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/rest/auth/requestpasschange")
    @ResponseBody
    public ResponseEntity<String> requestPassReset(@RequestBody String email){
        email = email.replace('\"', '\0');
        try {
            Users targetUser = userService.getUserByEmail(email);
            if(targetUser == null){
                return new ResponseEntity<>("User with this email was not found", HttpStatusCode.valueOf(404));
            }
            else {
                EmailContent emailContent = new EmailContent();

                String reqId = passwordChangeRequestService.generateRequestId(targetUser.getUserId());
                emailContent.setMsgBody("Hello " + targetUser.getFullName() + ",\nYou have requested to reset your password.\nPlease follow this link: http://localhost:3000/resetpassword/" + reqId);
                emailContent.setSubject("Reset Your Password");
                emailContent.setRecipient(email);
                emailService.sendSimpleMail(emailContent);
                return new ResponseEntity<>("Email sent", HttpStatusCode.valueOf(200));
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong", HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/rest/auth/validaterequestpasschange")
    @ResponseBody
    public Boolean validateResetPass(@RequestBody String reqId){
        return passwordChangeRequestService.validatePasscode(reqId);
    }

    @PostMapping("/rest/auth/resetpassword")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestBody String newPassword, @RequestParam String reqId){
        try {
            if(passwordChangeRequestService.validatePasscode(reqId)) {
                int userId = passwordChangeRequestService.getUserId(reqId);
                if (userId == -1) {
                    return new ResponseEntity<>("Not Found", HttpStatusCode.valueOf(404));
                }
                Users targetUser = userService.getUserByUserId(userId);
                userService.changePassword(targetUser, newPassword);
                passwordChangeRequestService.invalidatePasscode(reqId);
                return new ResponseEntity<>("New Password Saved", HttpStatusCode.valueOf(200));

            }
            else {
                return new ResponseEntity<>("Bad Request", HttpStatusCode.valueOf(400));
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/api/v1/user/setprofilepic")
    public ResponseEntity<String> setProfilePic(@RequestParam MultipartFile profilePictureFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, Object> authDetails = (Map<String, Object>) authentication.getDetails();

        Users user = userService.getUserByUsername(username);
        String currentPFP = user.getProfilePicture();

        if(profilePictureFile.isEmpty()){
            return new ResponseEntity<>("File not found", HttpStatusCode.valueOf(404));
        }

        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(spaceEndpoint, "sgp1"))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();

            InputStream stream = profilePictureFile.getInputStream();
            Date date = new Date();
            Timestamp timestamp =  new Timestamp(date.getTime());

            String key = "pfp/u" + user.getUserId() + "-" + timestamp + profilePictureFile.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(profilePictureFile.getContentType());

            s3.putObject(spaceBucket, key, stream, metadata);
            s3.setObjectAcl(spaceBucket, key, CannedAccessControlList.PublicRead);

            String fileUrl = spaceUrl + "/" + key;

            userService.setUserProfilePictureByUserId(user.getUserId(), fileUrl);

            if(currentPFP != null){
                s3.deleteObject(spaceBucket, currentPFP.replace(spaceUrl + "/", ""));
            }
            return new ResponseEntity<>("Profile picture changed", HttpStatusCode.valueOf(200));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
