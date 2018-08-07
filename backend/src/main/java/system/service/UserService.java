package system.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import system.domain.Users;
import system.exception.BankException;
import system.repository.UserRepository;
import system.requesthandle.StatusCode;
import system.vo.LoginResponseVO;
import system.vo.LoginVO;
import system.vo.RegistrationUserVO;
import system.vo.ResponseVO;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class UserService  {


    @Autowired
    UserRepository userRepository;

    @Autowired
    KeyAccessService keyAccessService;

    public Boolean userRegistration(RegistrationUserVO vo){
        Users username = userRepository.findByUserName(vo.userName);
        if(username != null){
            if(username.getUserName().equals(vo.getUserName())){
                throw new BankException("username sudah terdaftar");
            }
        }

        String generatePassword = password(vo.getPassword());
        Users user = new Users();
        user.setCreatedBy("SYSTEM");
        user.setLastModifiedDate(new Date());
        user.setLastModifiedBy("SYSTEM");
        user.setFullName(vo.getFullName());
        user.setUserName(vo.getUserName());
        user.setEmail(vo.getEmail());
        user.setPassword(generatePassword);
        user.setNik(vo.getNIK());
        user.setNoRekening(randomKey() + vo.getNIK().substring(3 ,9));
        user.setKeyAccess(randomKey());
        userRepository.save(user);

        return  Boolean.TRUE;

    }

    public ResponseVO userLogin(LoginVO loginVO){
        ResponseVO vo = new ResponseVO();
        Users user = userRepository.findByUserName(loginVO.getUsername());
        if(user == null){
            throw new BankException("username tidak terdaftar");
        }

        String matchingPasword =  password(loginVO.getPassword());
        if(user.getPassword().equalsIgnoreCase(matchingPasword)){
                LoginResponseVO loginResponseVO = new LoginResponseVO();
                loginResponseVO.setUserName(loginVO.getUsername());
                loginResponseVO.setKeyAccess(user.getKeyAccess());
                vo.setResponse(StatusCode.OK.name());
                vo.setResult(loginResponseVO);
        }else{
            vo.setResponse(StatusCode.ERROR.name());
            vo.setResult("Password salah");
        }
        return vo;
    }

    public Boolean userLogout(String keyAccess){
        Users user =  keyAccessService.checkKeyAccess(keyAccess);
        user.setKeyAccess(randomKey());
        userRepository.save(user);
        return Boolean.TRUE;
    }

    private String password(String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        StringBuffer passwords = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            passwords.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return String.valueOf(passwords);
    }

    private String randomKey(){
        Random rand = new Random();
        int  n = rand.nextInt(10000) + 1;
        return String.valueOf(n);
    }

}
