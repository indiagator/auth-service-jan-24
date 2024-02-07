package com.cbt.authservicejan24;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/ud")
public class AuthController
{
    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    UserdetailRepository userdetailRepository;
    @Autowired
    UsertypelinkRepository usertypelinkRepository;
    @Autowired
    UsernamewalletlinkRepository usernamewalletlinkRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    Producer producer;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody Credential credential) throws JsonProcessingException {

        credentialRepository.save(credential); // CRUD OPERATION ON DB
        try
        {
            producer.sendAuthEvent("SIGNUP", credential.getUsername()); // PUBLISHING TO THE BROKER
        }
        catch( RuntimeException e)
        {
            // IF THE MESSAGE WAS NOT PUBLISHED THEN REVERT THE ABOVE CRUD OPERATION ON DB
        }

        return ResponseEntity.ok("New Signup Successful");

    }


        @GetMapping("login") //AUTHENTICATION
        public ResponseEntity<String> login(@RequestBody Credential credential) throws JsonProcessingException {
        if(credentialRepository.findById(credential.getUsername()).isPresent())
        {
            if(credentialRepository.findById(credential.getUsername()).get().getPassword().equals(credential.getPassword()))
            {
                String AUTH_TOKEN = "OUHGIGYG324325235HJHJH"; // THIS IS JUST FOR DEMO
                producer.sendAuthEvent("LOGIN", credential.getUsername());
                return ResponseEntity.ok(AUTH_TOKEN);
            }
            else
            {
                return ResponseEntity.ok("AUTH FAILED - INCORRECT PASSWORD");
            }
        }
        else
            {
                return ResponseEntity.ok("AUTH FAILED - INCORRECT USERNAME");
            }
        }


        @GetMapping("logout/{username}")
        public ResponseEntity<String> logout(@PathVariable String username) throws JsonProcessingException {

            producer.sendAuthEvent("LOGOUT",username);
            return ResponseEntity.ok(username+" LOGGED OUT");

        }

    @PostMapping("update/user/details")
    public ResponseEntity<Userdetail> updateUserDetails(@RequestBody Userdetail userdetail) throws JsonProcessingException {
        if(userdetailRepository.findById(userdetail.getUsername()).isEmpty())
        {
            userdetailRepository.save(userdetail);
        }

        Wallet wallet = new Wallet();
        wallet.setWalletid(String.valueOf((int) (Math.random() * 10000)));
        wallet.setBalance(5000000);
        walletRepository.save(wallet);

        Usernamewalletlink usernamewalletlink = new Usernamewalletlink();
        usernamewalletlink.setWalletid(wallet.getWalletid());
        usernamewalletlink.setUsername(userdetail.getUsername());
        usernamewalletlinkRepository.save(usernamewalletlink);

        ObjectMapper objectMapper = new ObjectMapper();

        producer.sendAuthEvent("DETAILS", userdetail.getUsername());
        producer.sendAuthEvent("WALLET", userdetail.getUsername());

        return ResponseEntity.ok(userdetail);
    }

    @PostMapping("update/user/type")
    public ResponseEntity<String> updateUserType(@RequestBody Usertypelink usertypelink) throws JsonProcessingException {
        usertypelink.setLinkid(String.valueOf((int) (Math.random() * 10000)));
        usertypelinkRepository.save(usertypelink);
        producer.sendAuthEvent("TYPE", usertypelink.getUsername());
        return ResponseEntity.ok("User Registered as a: "+usertypelink.getType());
    }

    @GetMapping("get/users/all")
    public ResponseEntity<List<String>> getAllUsers()
    {
        List<String> users = credentialRepository.findAll().stream().map(credential -> credential.getUsername()).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

}
