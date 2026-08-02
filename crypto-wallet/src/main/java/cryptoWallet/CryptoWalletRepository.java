package cryptoWallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long> {

    boolean existsByEmail(String email);
    CryptoWallet findByEmail(String email);
}
