package simon.diploma_moneytransfer.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Amount {
    private Integer value;
    private Currency currency;
    @Override
    public String toString() {
        return value + " " + currency;
    }
}

