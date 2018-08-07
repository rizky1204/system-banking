package system.vo;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class DepositAndWithdrawVO {

    private Long transactionDate;
    private String status;
    private BigInteger saldo;
    private String accountSender;
}
