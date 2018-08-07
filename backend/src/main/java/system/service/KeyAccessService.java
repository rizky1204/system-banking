package system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import system.domain.Users;
import system.exception.BankException;
import system.repository.UserRepository;

@Service
public class KeyAccessService {

    @Autowired
    UserRepository userRepository;

    public Users checkKeyAccess(String keyAccess){
        Users checkKey = userRepository.findBykeyAccess(keyAccess);
        if(checkKey == null){
            throw new BankException("Key Access invalid");
        }

        return checkKey;
    }
}
