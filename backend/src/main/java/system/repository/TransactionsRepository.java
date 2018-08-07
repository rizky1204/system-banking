package system.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import system.domain.Transactions;
import system.domain.Users;

import java.util.List;

@Repository
public interface TransactionsRepository extends CrudRepository<Transactions, Long> {

    Transactions findTop1ByUsersOrderByCreationDateDesc(Users users);
    List<Transactions> findByUsers(Users users);


}
