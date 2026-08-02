package cryptoExchange;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoExchangeRepository extends JpaRepository<CryptoExchange, Long> {
 
    CryptoExchange findByFromAndToIgnoreCase(String from, String to);
}
