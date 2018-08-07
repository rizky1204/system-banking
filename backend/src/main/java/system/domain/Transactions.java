package system.domain;


import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "TRANSACTIONS")
public class Transactions extends Base {

   @Column(name = "SALDO")
   private BigInteger saldo;

   @Column(name = "NOTE")
   private String remark;

   @Column(name = "STATUS")
   private String status;

   @Column (name = "AMOUNT")
   private BigInteger amount;

   @Column(name = "ACCOUNT_SENDER")
   private String accountSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS")
    private Users users;


}
