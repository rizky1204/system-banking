package system.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Entity // This tells Hibernate to make a table out of this class
@Table(name = "USERS")
@DynamicUpdate
@Data
public class Users extends Base {

    @Column(name = "FULL_NAME")
	private String fullName;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NO_REKENING")
    private String noRekening;

    @Column(name = "NIK")
    private String nik;

    @Column(name = "KEY_ACCESS")
    private String keyAccess;

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "TRANSACTIONS")
//    private Transactions transactions;

//    public Users(){
//        this.fullName = fullName;
//        this.email = email;
//        this.noRekening = noRekening;
//        this.nik = nik;
//
//    }
    
    
}

