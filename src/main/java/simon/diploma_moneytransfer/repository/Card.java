package simon.diploma_moneytransfer.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String number;
    private String validTill;
    private String cvv;
    private Map<Currency, Amount> amounts;
}
