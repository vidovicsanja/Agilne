package cryptoExchange;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoExchangeController {
    
    @Autowired //dependency injection
    private CryptoExchangeRepository repo;
    
    @GetMapping("/crypto-exchange/from/{from}/to/{to}")
    public ResponseEntity<?> getExchange(@PathVariable String from, @PathVariable String to) {

        CryptoExchange kurs = repo.findByFromAndToIgnoreCase(from, to); //find this in database, based on from and to values

        if (kurs != null) {
            return ResponseEntity.ok(kurs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested crypto exchange could not be found");
        }
        
    }

}
