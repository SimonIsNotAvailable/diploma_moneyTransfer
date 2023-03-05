package simon.diploma_moneytransfer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import simon.diploma_moneytransfer.exceptions.InvalidCardException;
import simon.diploma_moneytransfer.exceptions.InvalidConfirmationException;
import simon.diploma_moneytransfer.repository.*;

import java.util.Objects;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private static final Logger logger = LoggerFactory.getLogger("file-log");
    private final double transferFee;
    private final String verificationCode;
    private final CardRepository cardRepository;

    @Autowired
    public TransferService(TransferRepository transferRepository,
                           @Value("${transfer.commission:0}") double transferFee,
                           @Value("${verification.code:0000}") String verificationCode,
                           CardRepository cardRepository) {
        this.transferRepository = transferRepository;
        this.transferFee = transferFee;
        this.verificationCode = verificationCode;
        this.cardRepository = cardRepository;
    }

    public long transfer(Transfer transfer) throws InvalidCardException {
        String cardFromNumber = transfer.getCardFromNumber();
        Card validCard = cardRepository.getCardByNumber(cardFromNumber).orElseThrow(
                ()-> new InvalidCardException("There's no such card: " + cardFromNumber ));

        validateCard(transfer, validCard);

        return transferRepository.addTransfer(transfer);
    }

    public void validateCard(Transfer transfer, Card card ) throws InvalidCardException {

        boolean validTillIsCorrect = Objects.equals(card.getValidTill(), transfer.getCardFromValidTill());
        boolean cvvIsCorrect = Objects.equals(card.getCvv(), transfer.getCardFromCVV());
        if (!validTillIsCorrect || !cvvIsCorrect) {
            throw new InvalidCardException("Введены неверные данные карты (срок действия / CVV номер)");
        }

        Currency transferCurrency = transfer.getAmount().getCurrency();
        if (!card.getAmounts().containsKey(transferCurrency)) {
            throw new InvalidCardException("На выбранной карте отсутствует счет в валюте " + transferCurrency);
        }

        Integer cardAvailableAmount = card.getAmounts().get(transferCurrency).getValue();
        Integer transferAmountWithCommission = (int) (transfer.getAmount().getValue() * (1 + transferFee));
        if (cardAvailableAmount < transferAmountWithCommission) {
            throw new InvalidCardException("На выбранной карте недостаточно средств. На карте имеется " +
                    cardAvailableAmount + ", необходимо (с учетом комиссии) " + transferAmountWithCommission);
        }
    }

    public void executeTransfer(String operationId) {
        Transfer transfer = transferRepository.getTransferById(operationId);
        Card validCardFrom = cardRepository.getCardByNumber(transfer.getCardFromNumber()).get();

        Currency transferCurrency = transfer.getAmount().getCurrency();
        Integer transferAmountWithFee = (int) (transfer.getAmount().getValue() * (1 + transferFee));
        Integer balance = validCardFrom.getAmounts().get(transferCurrency).getValue() - transferAmountWithFee;

        validCardFrom.getAmounts().put(transferCurrency, new Amount(balance, transferCurrency));

        logger.info("С карты {} успешно переведена сумма в размере {} на карту {}. Размер комиссии составил {} {}. " +
                        "Остаток на карте: {} {}. ID операции: {}",
                transfer.getCardFromNumber(),
                transfer.getAmount(),
                transfer.getCardToNumber(),
                transfer.getAmount().getValue() * transferFee, transferCurrency,
                balance, transferCurrency,
                operationId);
    }

    public void confirm(Confirmation confirmation) throws InvalidConfirmationException {

        if (!confirmation.getCode().equals(verificationCode)) {
            throw new InvalidConfirmationException("Неверный код подтверждения (" + confirmation.getCode() + ")");
        }

        if (!transferRepository.confirmOperation(confirmation)) {
            throw new InvalidConfirmationException("Нет операции с id " + confirmation.getOperationId());
        }

        executeTransfer(confirmation.getOperationId());
    }
}
