package system.vo;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class TransactionListVO {

    private String username;
    private String fullName;
    private String noRekening;
    private String email;
    private List<TransactionVO> transactionVOList;
}
