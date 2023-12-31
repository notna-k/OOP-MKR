package com.example.carsharing.Auth;

import com.example.carsharing.Auth.dto.SignUserDto;
import com.example.carsharing.Users.User;
import com.example.carsharing.Users.dto.CreateUserDto;
import com.example.carsharing.shared.dataWriter.UserData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@RestController
@RequestMapping("/auth")
public class AuthController {
    String secret = "Somesecretkeyherenothingweryinterestingthough";
    Algorithm algorithm;
    UserData userData;
    int jwtExpirationTime = 24*60*60*1000;  //24 hours

    public AuthController() throws Exception{
        this.userData = new UserData();
        algorithm = Algorithm.HMAC256(secret);
    }


    @PostMapping("/sign_up")
    public ResponseEntity register(@RequestBody() CreateUserDto user){
        try{
            User createdUser = this.userData.create(user);
            String accessToken = JWT.create()
                    .withClaim("id", createdUser.getId())
                    .withClaim("email", createdUser.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationTime))
                    .sign(algorithm);
            return ResponseEntity.ok().body(accessToken);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: Incorrect register credentials");
        }
    }

    @PostMapping("/sign_in")
    public ResponseEntity register(@RequestBody() SignUserDto user){
        try{
            User createdUser = this.userData.get(user.getEmail());
            String accessToken = JWT.create()
                    .withClaim("id", createdUser.getId())
                    .withClaim("email", createdUser.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationTime))
                    .sign(algorithm);
            return ResponseEntity.ok().body(accessToken);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: Incorrect register credentials");
        }
    }

}