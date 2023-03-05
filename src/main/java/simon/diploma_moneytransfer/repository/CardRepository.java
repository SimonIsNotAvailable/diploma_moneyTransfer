package simon.diploma_moneytransfer.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CardRepository {
    private final Map<String, Card> cards = new ConcurrentHashMap<>();
    {
        cards.put("1111222233334444", new Card("1111222233334444", "01/30", "111",
                new ConcurrentHashMap<>(Map.of(Currency.RUBLES, new Amount(50000, Currency.RUBLES)))));

        cards.put("9999888877776666", new Card("9999888877776666", "10/25", "222",
                new ConcurrentHashMap<>(Map.of(Currency.EURO, new Amount(100000, Currency.EURO)))));

        cards.put("1234567890123456", new Card("1234567890123456", "05/27", "333",
                new ConcurrentHashMap<>(Map.of(Currency.USD, new Amount(10000, Currency.USD)))));

        cards.put("1122334455667788", new Card("1122334455667788", "03/31", "444",
                new ConcurrentHashMap<>(Map.of(
                        Currency.RUBLES, new Amount(150000, Currency.RUBLES),
                        Currency.USD, new Amount(20000, Currency.USD)
                ))));

        cards.put("9876543210987654", new Card("9876543210987654", "08/24", "555",
                new ConcurrentHashMap<>(Map.of(
                        Currency.RUBLES, new Amount(250000, Currency.RUBLES),
                        Currency.USD, new Amount(5000, Currency.USD),
                        Currency.EURO, new Amount(1000, Currency.EURO)
                ))));
    }

    public Optional<Card> getCardByNumber(String cardNumber) {
        return Optional.ofNullable(cards.get(cardNumber));
    }
}
