package system.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import system.domain.Users;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {

    Users findByUserName(String username);
    Users findBykeyAccess(String keyAccess);
    Users findByNoRekening(String noRekening);

}
