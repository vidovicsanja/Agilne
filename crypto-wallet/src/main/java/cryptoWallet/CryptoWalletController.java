package cryptoWallet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cryptoWallet.dto.CryptoWalletDto;
import cryptoWallet.dto.CryptoWalletResponseDto;
import feign.FeignException;

@RestController
public class CryptoWalletController {
    
    @Autowired //dependency injection
    private CryptoWalletRepository repo;

    @Autowired
    private BankAccountProxy bankAccountProxy;
    
    @GetMapping("/crypto-wallet/wallets")
	public List<CryptoWallet> getAllWallets(){
		return repo.findAll();
	}

    @GetMapping("/crypto-wallet/{email}")
	public Boolean getByEmail(@PathVariable("email") String email) {
		return repo.existsByEmail(email);
	}

    @PostMapping("/crypto-wallet")
    public ResponseEntity<?> addCryptoWallet(@RequestBody CryptoWallet wallet) {
        try {
            // send request to users microservice
            ResponseEntity<Boolean> response = bankAccountProxy.existsByEmail(wallet.getEmail());

            // account with email exists
            if (response.getBody()) {
                if (repo.existsByEmail(wallet.getEmail())) {
                    return ResponseEntity.status(409).body("Crypto wallet connected with email " + wallet.getEmail() + " already exists");
                }
                repo.save(wallet);
                return ResponseEntity.status(201).body(wallet);
            } else {
                return ResponseEntity.status(404).body("Bank account with email " + wallet.getEmail() + " doesn't exist");
            }
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.getMessage());
        }
    }

    @PutMapping("/crypto-wallet")
    public ResponseEntity<?> editCryptoWallet(@RequestBody CryptoWallet wallet) {
        try {
            if (repo.existsById(wallet.getId())) {
                // send request to users microservice
                ResponseEntity<Boolean> response = bankAccountProxy.existsByEmail(wallet.getEmail());
        
                // account with email exists
                if (response.getBody()) {
                    if (repo.existsByEmail(wallet.getEmail()) && repo.findByEmail(wallet.getEmail()).getId() != repo.findById(wallet.getId()).get().getId()) {
                        return ResponseEntity.status(409).body("Crypto wallet connected with email " + wallet.getEmail() + " already exists");
                    }
                    repo.save(wallet);
                    return ResponseEntity.status(200).body(wallet);
                } else {
                    return ResponseEntity.status(404).body("Bank account with email " + wallet.getEmail() + " doesn't exist");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Crypto wallet with id " + wallet.getId() + "doesn't exist");
            }
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.getMessage());
        }
    }

    @DeleteMapping("/crypto-wallet/{email}")
    public ResponseEntity<?> deleteCryptoWallet(@PathVariable("email") String email) {
		if (repo.existsByEmail(email)) {
            CryptoWallet wallet = repo.findByEmail(email);
			repo.delete(wallet);
			return ResponseEntity.status(HttpStatus.OK).body("Crypto wallet successfully deleted");
		}
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Crypto wallet connected with email " + email + "doesn't exist");
    }

    @PostMapping("/crypto-wallet/conversion")
    public ResponseEntity<?> conversion(@RequestBody CryptoWalletDto wallet) {

        if (!repo.existsByEmail(wallet.getEmail()))
		    return ResponseEntity.status(404).body("Crypto wallet connected with email " + wallet.getEmail() + " doesn't exist");

        CryptoWallet walletDb = repo.findByEmail(wallet.getEmail());

        switch (wallet.getFrom().toLowerCase()) {
            case "btc": {
                if (repo.findByEmail(wallet.getEmail()).getBtc().compareTo(wallet.getFromValue()) == -1)
		            return ResponseEntity.status(400).body("You don't have enough " + wallet.getFrom() + " for the exchange");
                else {
                    walletDb.setBtc(repo.findByEmail(wallet.getEmail()).getBtc().subtract(wallet.getFromValue()));
                }
                break;
            }
            case "eth": {
                if (repo.findByEmail(wallet.getEmail()).getEth().compareTo(wallet.getFromValue()) == -1)
		            return ResponseEntity.status(400).body("You don't have enough " + wallet.getFrom() + " for the exchange");
                else {
                    walletDb.setEth(repo.findByEmail(wallet.getEmail()).getEth().subtract(wallet.getFromValue()));
                }
                break;
            }
            case "bnb": {
                if (repo.findByEmail(wallet.getEmail()).getBnb().compareTo(wallet.getFromValue()) == -1)
		            return ResponseEntity.status(400).body("You don't have enough " + wallet.getFrom() + " for the exchange");
                else {
                    walletDb.setBnb(repo.findByEmail(wallet.getEmail()).getBnb().subtract(wallet.getFromValue()));
                }
                break;
            }
            case "ada": {
                if (repo.findByEmail(wallet.getEmail()).getAda().compareTo(wallet.getFromValue()) == -1)
		            return ResponseEntity.status(400).body("You don't have enough " + wallet.getFrom() + " for the exchange");
                else {
                    walletDb.setAda(repo.findByEmail(wallet.getEmail()).getAda().subtract(wallet.getFromValue()));
                }
                break;
            }
        }

        switch (wallet.getTo().toLowerCase()) {
            case "btc": {
                walletDb.setBtc(repo.findByEmail(wallet.getEmail()).getBtc().add(wallet.getToValue()));
                break;
            }
            case "eth": {
                walletDb.setEth(repo.findByEmail(wallet.getEmail()).getEth().add(wallet.getToValue()));
                break;
            }
            case "bnb": {
                walletDb.setBnb(repo.findByEmail(wallet.getEmail()).getBnb().add(wallet.getToValue()));
                break;
            }
            case "ada": {
                walletDb.setAda(repo.findByEmail(wallet.getEmail()).getAda().add(wallet.getToValue()));
                break;
            }
        }

        repo.save(walletDb);

        CryptoWalletResponseDto responseDto = new CryptoWalletResponseDto(
            walletDb.getEmail(), 
            walletDb.getBtc(), walletDb.getEth(), walletDb.getBnb(), walletDb.getAda(),
            "Successfully exchanged " + wallet.getFromValue() + wallet.getFrom() + " to " + wallet.getTo());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
