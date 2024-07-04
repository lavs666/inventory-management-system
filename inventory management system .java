// Item.java
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private int quantity;

    // getters and setters
}

// ItemRepository.java
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}

// ItemService.java
@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }
    
    public void updateItemQuantity(Long itemId, int quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        item.setQuantity(quantity);
        itemRepository.save(item);
    }
    
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}

// ItemController.java
@RestController
@RequestMapping("/api/items")
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }
    
    @PostMapping
    public Item addItem(@RequestBody Item item) {
        return itemService.addItem(item);
    }
    
    @PutMapping("/{itemId}/quantity")
    public void updateItemQuantity(@PathVariable Long itemId, @RequestParam int quantity) {
        itemService.updateItemQuantity(itemId, quantity);
    }
    
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
    }
}

// Order.java
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;
    
    private LocalDateTime orderDate;
    private String customerName;
    private String status;

    // getters and setters
}

// OrderItem.java
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long itemId;
    private int quantity;
    private BigDecimal price;

    // getters and setters
}

// OrderRepository.java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

// OrderService.java
@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order createOrder(Order order) {
        order.setStatus("CREATED");
        order.setOrderDate(LocalDateTime.now());
        // logic to update stock levels based on order items
        
        // save order and return
        return orderRepository.save(order);
    }
    
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus("CANCELLED");
        // logic to adjust stock levels
        
        orderRepository.save(order);
    }
}

// OrderController.java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }
    
    @PutMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }
}

// Supplier.java
@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String contactInfo;

    // getters and setters
}

// SupplierRepository.java
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}

// SupplierService.java
@Service
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
    
    public void deleteSupplier(Long supplierId) {
        supplierRepository.deleteById(supplierId);
    }
}

// SupplierController.java
@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    
    @Autowired
    private SupplierService supplierService;
    
    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }
    
    @PostMapping
    public Supplier addSupplier(@RequestBody Supplier supplier) {
        return supplierService.addSupplier(supplier);
    }
    
    @DeleteMapping("/{supplierId}")
    public void deleteSupplier(@PathVariable Long supplierId) {
        supplierService.deleteSupplier(supplierId);
    }
}

// ReportingService.java
@Service
public class ReportingService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    // method to generate stock level report
    public List<Item> generateStockLevelReport() {
        return itemRepository.findAll();
    }
    
    // method to generate order status report
    public List<Order> generateOrderStatusReport() {
        return orderRepository.findAll();
    }
}

// ReportingController.java
@RestController
@RequestMapping("/api/reports")
public class ReportingController {
    
    @Autowired
    private ReportingService reportingService;
    
    @GetMapping("/stock-levels")
    public List<Item> generateStockLevelReport() {
        return reportingService.generateStockLevelReport();
    }
    
    @GetMapping("/order-status")
    public List<Order> generateOrderStatusReport() {
        return reportingService.generateOrderStatusReport();
    }
}

// Application.java
@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryManagementApplication.class, args);
    }
}
