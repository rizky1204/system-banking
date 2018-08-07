package system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.requesthandle.RequestHandler;
import system.service.TransService;
import system.vo.DepositAndWithdrawVO;
import system.vo.TransactionVO;
import system.vo.ResponseVO;
import system.vo.TransferVO;

@RestController
@RequestMapping(path="/api/transaction")
public class TransactionController {

    @Autowired
    TransService transService;

    @RequestMapping(value = "/deposit",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResponseVO> deposit(@RequestBody TransactionVO transactionVO,
                                              @RequestParam(value = "keyAccess", required = true)
                                                      String keyAccess) {
        RequestHandler handler = new RequestHandler() {
            @Override
            public Object processRequest() {
                return  transService.deposit(transactionVO , keyAccess);
            }
        };
        return handler.getResult();
    }

    @RequestMapping(value = "/withdraw",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResponseVO> withdraw(@RequestBody TransactionVO transactionVO,
                                              @RequestParam(value = "keyAccess", required = true) String keyAccess) {
        RequestHandler handler = new RequestHandler() {
            @Override
            public Object processRequest() {
                return  transService.withdraw(transactionVO , keyAccess);
            }
        };
        return handler.getResult();
    }

    @RequestMapping(value = "/transfer",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResponseVO> withdraw(@RequestBody TransferVO transferVO,
                                               @RequestParam(value = "keyAccess", required = true) String keyAccess) {
        RequestHandler handler = new RequestHandler() {
            @Override
            public Object processRequest() {
                return  transService.transfer(transferVO , keyAccess);
            }
        };
        return handler.getResult();
    }

    @RequestMapping(value = "/detail-list",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResponseVO> getAll(@RequestParam(value = "keyAccess", required = true) String keyAccess){
        RequestHandler handler = new RequestHandler() {
            @Override
            public Object processRequest() {
                return transService.listTransaction(keyAccess);
            }
        };
        return handler.getResult();
    }
}
