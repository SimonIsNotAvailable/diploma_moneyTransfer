package simon.diploma_moneytransfer.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transfer {
    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;
}
