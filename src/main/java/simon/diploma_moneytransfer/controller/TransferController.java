package simon.diploma_moneytransfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simon.diploma_moneytransfer.exceptions.InvalidCardException;
import simon.diploma_moneytransfer.exceptions.InvalidConfirmationException;
import simon.diploma_moneytransfer.repository.*;
import simon.diploma_moneytransfer.service.TransferService;

@RestController
@CrossOrigin
public class TransferController {

    private TransferService transferService;

    @Autowired
    public TransferController() {
    }

    @PostMapping("/transfer")
    public ResponseEntity<SuccessTransfer> transfer(@RequestParam Transfer transfer)
            throws InvalidCardException {
        Long id = transferService.transfer(transfer);
        return ResponseEntity.ok(new SuccessTransfer(String.valueOf(id)));
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<SuccessConfirmation> confirmOperation(@RequestBody Confirmation confirmation)
            throws InvalidConfirmationException {
        transferService.confirm(confirmation);
        return ResponseEntity.ok(new SuccessConfirmation(confirmation.getOperationId()));
    }
}
