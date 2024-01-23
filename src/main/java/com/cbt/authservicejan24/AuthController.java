package com.cbt.authservicejan24;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody Credential credential)
    {
        credentialRepository.save(credential);
        return ResponseEntity.ok("New Signup Successful");
    }

    @PostMapping("update/user/details")
    public ResponseEntity<Userdetail> updateUserDetails(@RequestBody Userdetail userdetail)
    {
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

        return ResponseEntity.ok(userdetail);
    }

    @PostMapping("update/user/type")
    public ResponseEntity<String> updateUserType(@RequestBody Usertypelink usertypelink)
    {
        usertypelink.setLinkid(String.valueOf((int) (Math.random() * 10000)));
        usertypelinkRepository.save(usertypelink);
        return ResponseEntity.ok("User Registered as a: "+usertypelink.getType());
    }




}
