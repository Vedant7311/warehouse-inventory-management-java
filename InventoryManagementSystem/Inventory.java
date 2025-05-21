//Inventory.java
import java.util.*;
public class Inventory {
    private Map<String, Item> items = new HashMap<>();

    public boolean addItem(Item item) {
        if (items.containsKey(item.getSku())) {
            return false;
        }
        items.put(item.getSku(), item);
        return true;
    }

    public boolean deleteItem(String sku) {
        return items.remove(sku) != null;
    }

    public boolean updateQuantity(String sku, int quantity) {
        Item item = items.get(sku);
        if (item != null) {
            item.setQuantity(quantity);
            return true;
        }
        return false;
    }

    public Item searchItem(String query) {
        for (Item item : items.values()) {
            if (item.getSku().equalsIgnoreCase(query) || item.getName().equalsIgnoreCase(query)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }
}