package ntnu.idata2503.group9.stockappbackend.Repository;

import ntnu.idata2503.group9.stockappbackend.Models.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface StockRepository extends CrudRepository<Stock, Long> {
}
