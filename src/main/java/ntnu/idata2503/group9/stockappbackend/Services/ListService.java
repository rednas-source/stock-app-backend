package ntnu.idata2503.group9.stockappbackend.Services;

import ntnu.idata2503.group9.stockappbackend.Models.List;
import ntnu.idata2503.group9.stockappbackend.Models.Stock;
import ntnu.idata2503.group9.stockappbackend.Models.User;
import ntnu.idata2503.group9.stockappbackend.Repository.ListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represent the service class for list.
 * Handle the logic of the list repository.
 *
 * @author Gruppe 4
 * @version 1.0
 */
@Service
public class ListService {

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private StockService stockService;

    /**
     * Return all lists
     * @return all lists
     */
    public Iterable<List> getAll() {
        return this.listRepository.findAll();
    }

    /**
     * Return a list by id.
     * @param id of the list you want to find.
     * @return list.
     */
    public List findById(long id) {
        return this.listRepository.findById(id).orElse(null);
    }

    /**
     * Return list by user
     * @param user the user that you want the list from
     * @return lists
     */
    public Iterable<List> findAllByUser(User user) {
        return this.listRepository.findAllByUser(user);
    }

    /**
     * Checks if the list can be added to the repository.
     * @param list the list you want to checek if it can be added.
     * @return boolean statement. True if ti can be added, false if not.
     */
    private boolean canBeAdded(List list) {
        return list != null & list.isValid();
    }

    /**
     * Added a list to the list repository.
     * @param list the list you want to add.
     * @return Boolean statement. True if added, false if not.
     */
    public boolean add(List list) {
        boolean added = false;
        if(!canBeAdded(list) || list.getName().equals("Favorites") || list.getName().equals("favorites")) {
            return added;
        }
        this.listRepository.save(list);
        added = true;
        return added;
    }

    /**
     * Removes a list from the repository
     * @param id the id of the list you want to remove.
     * @return boolean statement. True if removed, false if not.
     */
    public boolean delete(long id) {
        boolean deleted = false;
        if(findById(id) != null) {
            this.listRepository.deleteById(id);
            deleted = true;
        }
        return deleted;
    }

    /**
     * Updates a list in the repository
     * @param id the id of the list you want to update.
     * @param list the new list you want the old list to be updated to.
     */
    public void update(long id, List list) {
        List existingList = findById(id);
        String errorMessage = null;
        if(existingList == null) {
            errorMessage = "No list exists with the id: " + id;
        }
        if(list == null || !list.isValid()) {
            errorMessage = "Wrong data in request body";
        }
        else if (list.getLid() != id) {
            errorMessage = "The ID of the user in the URL does not match anny ID on the JSON data";
        }
        if(errorMessage == null) {
            this.listRepository.save(list);
        }
    }

    /**
     * Add a stock to the list repository
     * @param lid id of the list that you want to add the stock to
     * @param stock the stock you want to add
     */
    public void addStockToList( long lid,Stock stock) {
        List list = findById(lid);
        list.addStockToList(stock);
        this.listRepository.save(list);
    }

    /**
     * Remove stock from the list in list repository.
     * @param lid the id of the list you want a stock to be removed.
     * @param stock the stock you want to remove.
     */
     public void removeStockFromList(long lid, Stock stock) {
        List list = findById(lid);
        list.removeStockFromList(stock);
        this.listRepository.save(list);
     }

    /**
     * Set a new name for a list
     * @param lid the id of the list you want to set a new name.
     * @param name the name you want to update the list with.
     */
     public void updateName(long lid, String name) {
         List list = findById(lid);
         list.setName(name);
         this.listRepository.save(list);
     }
}
