package system.vo;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class TransactionVO  extends DepositAndWithdrawVO{

    private String remark;
    private BigInteger amount;

}
