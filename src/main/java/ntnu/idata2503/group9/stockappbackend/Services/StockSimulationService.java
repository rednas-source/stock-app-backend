package ntnu.idata2503.group9.stockappbackend.Services;

import ntnu.idata2503.group9.stockappbackend.Models.Stock;
import ntnu.idata2503.group9.stockappbackend.Repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class StockSimulationService {

    private final StockRepository stockRepository;
    private final ScheduledExecutorService scheduler;
    private final Random random = new Random();

    @Autowired
    public StockSimulationService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void startSimulation() {
        // Add 30 different stocks (you can customize this as per your requirements)
        addInitialStocks();

        // Schedule price updates every 10 seconds
        ScheduledFuture<?> priceUpdateHandle = scheduler.scheduleAtFixedRate(this::updateStockPrices, 0, 10, TimeUnit.SECONDS);
    }

    private void addInitialStocks() {
        // You can create and save 30 different stocks with initial prices here.
        // For the sake of brevity, let's assume you have predefined stock data.
        // Example: Stock(stockSymbol, stockName, initialPrice)
        Stock stock1 = new Stock("EQNR", "Equinor", 369.05, -1.47);
        Stock stock2 = new Stock("NAS", "Norwegian Air Shuttle", 9.052, +11.43);
        Stock stock3 = new Stock("DNB", "DNB Bank ASA", 197.4, -0.93);
        Stock stock4 = new Stock("FRO", "Frontline PLC", 252.85, +0.67);
        Stock stock5 = new Stock("TEL", "Telenor ASA", 167.1, -0.24);
        Stock stock6 = new Stock("ORK", "Orkla ASA", 86.9, +0.12);
        Stock stock7 = new Stock("STL", "Statoil ASA", 164.1, -0.24);
        Stock stock8 = new Stock("TGS", "TGS-NOPEC Geophysical Company ASA", 141.1, +0.14);
        Stock stock9 = new Stock("AKER", "Aker ASA", 294.1, +0.17);
        Stock stock10 = new Stock("NHY", "Norsk Hydro ASA", 44.1, -0.24);
        Stock stock11 = new Stock("SUBC", "Subsea 7 SA", 104.1, +0.24);
        Stock stock12 = new Stock("MHG", "Marine Harvest ASA", 164.1, -0.24);
        Stock stock13 = new Stock("NAS", "Norwegian Air Shuttle ASA", 164.1, +0.24);
        Stock stock14 = new Stock("AKERBP", "Aker BP ASA", 164.1, -0.24);
        Stock stock15 = new Stock("YAR", "Yara International ASA", 164.1, +0.24);
        Stock stock16 = new Stock("SCHA", "Schibsted ASA", 164.1, -0.24);
        Stock stock17 = new Stock("PGS", "PGS ASA", 164.1, +0.24);
        Stock stock18 = new Stock("OTL", "Odfjell Technology LTD", 164.1, -0.24);
        Stock stock19 = new Stock("SALM", "SalMar ASA", 164.1, +0.24);
        Stock stock20 = new Stock("GJF", "Gjensidige Forsikring ASA", 164.1, -0.24);
        // ... add more stocks
        stockRepository.saveAll(List.of(stock1, stock2, stock3, stock4, stock5, stock6, stock7, stock8, stock9, stock10,
                stock11, stock12, stock13, stock14, stock15, stock16, stock17, stock18, stock19, stock20));
    }

    private void updateStockPrices() {
        List<Stock> stocks = (List<Stock>) stockRepository.findAll();

        for (Stock stock : stocks) {
            double currentPrice = stock.getPrice();
            double percentageChange = (random.nextDouble() * 0.004) + 0.001; // 0.1% to 0.4% change
            double priceChange = currentPrice * percentageChange;
            double newPrice = currentPrice + priceChange;
            stock.setPrice(newPrice);
            stockRepository.save(stock);
        }
    }
}