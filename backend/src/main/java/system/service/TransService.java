package system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import system.Constans;
import system.domain.Transactions;
import system.domain.Users;
import system.exception.BankException;
import system.repository.TransactionsRepository;
import system.repository.UserRepository;
import system.vo.TransactionListVO;
import system.vo.TransactionVO;
import system.vo.TransferVO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TransService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    KeyAccessService keyAccessService;

    @Autowired
    TransactionsRepository  transactionsRepository;

    public Boolean deposit(TransactionVO vo , String keyAccess){

        Users user = keyAccessService.checkKeyAccess(keyAccess);
        if(user != null){
            Transactions transactions = new Transactions();
            transactions.setCreatedBy(user.getUserName());
            transactions.setLastModifiedBy(user.getUserName());
            transactions.setAmount(vo.getAmount());
            transactions.setRemark(vo.getRemark());
            transactions.setStatus(Constans.STATUS.DEBET);
            Transactions checkSaldo = transactionsRepository.findTop1ByUsersOrderByCreationDateDesc(user);
            if(checkSaldo == null){
                transactions.setSaldo(vo.getAmount());
                log.info("New Transaction for this user");
            }else{
                BigInteger currentSaldo =  checkSaldo.getSaldo();
                transactions.setSaldo(currentSaldo.add(vo.getAmount()));
            }
            transactions.setUsers(user);
            transactionsRepository.save(transactions);
        }
        return Boolean.TRUE;
    }

    public Boolean withdraw(TransactionVO vo , String keyAccess){

        Users user = keyAccessService.checkKeyAccess(keyAccess);
        if(user != null){
            Transactions transactions = new Transactions();
            transactions.setCreatedBy(user.getUserName());
            transactions.setLastModifiedBy(user.getUserName());
            transactions.setAmount(vo.getAmount());
            transactions.setRemark(vo.getRemark());
            transactions.setStatus(Constans.STATUS.WITHDRAW);
            Transactions checkSaldo = transactionsRepository.findTop1ByUsersOrderByCreationDateDesc(user);
            log.info(String.valueOf(checkSaldo.getAmount()));
            if(checkSaldo.getSaldo().compareTo(BigInteger.ZERO) <= 0){
                log.info("No saldo for this user");
                throw new BankException("Tidak Ada Saldo");
            }else if(checkSaldo.getSaldo().compareTo(vo.getAmount()) < 0){
                throw  new BankException("Saldo anda tidak cukup");
            }else{
                BigInteger currentSaldo =  checkSaldo.getSaldo();
                transactions.setSaldo(currentSaldo.subtract(vo.getAmount()));
            }
            transactions.setUsers(user);
            transactionsRepository.save(transactions);
        }
        return Boolean.TRUE;
    }

    public TransactionListVO listTransaction(String keyAccess){
        Users users =  keyAccessService.checkKeyAccess(keyAccess);
        TransactionListVO vo = new TransactionListVO();
        vo.setUsername(users.getUserName());
        vo.setFullName(users.getFullName());
        vo.setNoRekening(users.getNoRekening());
        vo.setEmail(users.getEmail());
        List<Transactions> transactionsList = transactionsRepository.findByUsers(users);
        List<TransactionVO> listTransaction = new ArrayList<>();
        for(Transactions transactions : transactionsList){
            TransactionVO vos =  new TransactionVO();
            vos.setAmount(transactions.getAmount());
            vos.setRemark(transactions.getRemark());
            vos.setStatus(transactions.getStatus());
            vos.setSaldo(transactions.getSaldo());
            vos.setAccountSender(transactions.getAccountSender());
            vos.setTransactionDate(transactions.getCreationDate().getTime());
            listTransaction.add(vos);

        }
        vo.setTransactionVOList(listTransaction);
        return vo;
    }

    public Boolean transfer(TransferVO vo , String keyAccess){
       Users user = keyAccessService.checkKeyAccess(keyAccess);
       Transactions checkSaldo = transactionsRepository.findTop1ByUsersOrderByCreationDateDesc(user);
       if(checkSaldo == null) throw new BankException("Silahkan deposit terlebih dahulu");
        if(checkSaldo.getSaldo().compareTo(BigInteger.ZERO) <= 0){
            log.info("No Transaction for this user");
            throw new BankException("Tidak Ada Saldo");
        }else if(checkSaldo.getSaldo().compareTo(vo.getAmount()) < 0){
            throw  new BankException("Saldo anda tidak cukup");
        }else{
            BigInteger currentSaldo =  checkSaldo.getSaldo();
            Transactions newTranscation = new Transactions();
            newTranscation.setSaldo(currentSaldo.subtract(vo.getAmount()));
            newTranscation.setStatus(Constans.STATUS.TRANSFER);
            newTranscation.setRemark(vo.getRemark());
            newTranscation.setUsers(user);
            newTranscation.setCreatedBy(user.getUserName());
            newTranscation.setLastModifiedBy(user.getUserName());
            newTranscation.setAmount(vo.getAmount());
            transactionsRepository.save(newTranscation);
        }

        Users transferDestination =  userRepository.findByNoRekening(vo.getNoRekening());
        Transactions currentSaldoAccountDestination =
                transactionsRepository.findTop1ByUsersOrderByCreationDateDesc(transferDestination);
        Transactions newTransaction = new Transactions();
        newTransaction.setStatus(Constans.STATUS.INCOME);
        if(currentSaldoAccountDestination!= null){
            newTransaction.setSaldo(vo.getAmount().add(currentSaldoAccountDestination.getSaldo()));
        }else{
            newTransaction.setSaldo(vo.getAmount());
        }

        newTransaction.setUsers(transferDestination);
        newTransaction.setRemark(vo.getRemark());
        newTransaction.setAmount(vo.getAmount());
        newTransaction.setAccountSender(user.getNoRekening());
        newTransaction.setCreatedBy(transferDestination.getUserName());
        newTransaction.setLastModifiedBy(transferDestination.getUserName());
        log.info("save");
        transactionsRepository.save(newTransaction);

       return Boolean.TRUE;


    }
}
