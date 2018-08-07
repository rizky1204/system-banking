package system.vo;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TransferVO {

    private String remark;
    private BigInteger amount;
    private String noRekening;
}
