package ntnu.idata2503.group9.stockappbackend.Services;

import ntnu.idata2503.group9.stockappbackend.Models.StockPurchase;
import ntnu.idata2503.group9.stockappbackend.Repository.StockPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represent the service class for StockPurchase
 * Handle the logic of StockPurchase repository.
 *
 * @author Gruppe..
 * @version 1.0
 */
@Service
public class StockPurchaseService {

    @Autowired
    StockPurchaseRepository stockPurchaseRepository;

    public Iterable<StockPurchase> getAll() {
        return this.stockPurchaseRepository.findAll();
    }

    public StockPurchase findById(long id) {
        return this.stockPurchaseRepository.findById(id).orElse(null);
    }

    private boolean canBeAdded(StockPurchase stockPurchase) {
        return stockPurchase != null && stockPurchase.isValid();
    }

    public boolean add(StockPurchase stockPurchase) {
        boolean added = false;
        if(canBeAdded(stockPurchase)) {
            this.stockPurchaseRepository.save(stockPurchase);
            added = true;
        }
        return added;
    }

    public boolean delete(long id) {
        boolean deleted = false;
        if(findById(id) != null) {
            this.stockPurchaseRepository.deleteById(id);
            deleted = true;
        }
        return deleted;
    }

    public void update(long id, StockPurchase stockPurchase) {
        StockPurchase existingStockPurchase = findById(id);
        String errorMessage = null;
        if (existingStockPurchase == null) {
            errorMessage = "No user exists with the id " + id;
        }
        if (stockPurchase == null || !stockPurchase.isValid()) {
            errorMessage = "Wrong data in request body";
        }
        else if(stockPurchase.getSpid() != id) {
            errorMessage = "The ID of the user in the URL does not match anny ID in the JSON data";
        }
        if (errorMessage == null) {
            this.stockPurchaseRepository.save(stockPurchase);
        }
    }
}