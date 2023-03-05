package simon.diploma_moneytransfer.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TransferRepository {

    private final Map<Long, Transfer> transfers = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    public Map<String, String> credentials = new HashMap<>();
    public Map<String, String> authorities = new HashMap<>();

    public long addTransfer(Transfer transfer) {
        long id = this.id.incrementAndGet();
        transfers.put(id, transfer);
        return id;
    }

    public boolean confirmOperation(Confirmation confirmation) {
        return transfers.containsKey(Long.parseLong(confirmation.getOperationId()));
    }

    public Transfer getTransferById(String id) {
        return transfers.get(Long.parseLong(id));
    }
}
