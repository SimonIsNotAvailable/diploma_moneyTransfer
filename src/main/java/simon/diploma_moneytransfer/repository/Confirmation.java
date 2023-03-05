package simon.diploma_moneytransfer.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Confirmation {

    private String code;
    private String operationId;
}
