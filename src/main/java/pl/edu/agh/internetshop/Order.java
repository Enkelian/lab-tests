package pl.edu.agh.internetshop;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;


public class Order {
    private static final BigDecimal TAX_VALUE = BigDecimal.valueOf(1.23);
	private final UUID id;
    private final List<Product> products;
    private boolean paid;
    private Shipment shipment;
    private ShipmentMethod shipmentMethod;
    private PaymentMethod paymentMethod;
    private BigDecimal discountValue = BigDecimal.valueOf(1);

    public Order(List<Product> products) {
        if(products.isEmpty()) throw new IllegalArgumentException("List of products cannot be empty");
        if(products.contains(null)) throw new IllegalArgumentException("List of products cannot contain null");
        this.products = products;
        id = UUID.randomUUID();
        paid = false;
    }

    public void setDiscount(double discount) {
        if(discount>1 || discount<0) throw new IllegalArgumentException("Value of discount out of range (0,1)");
        this.discountValue = BigDecimal.valueOf(1-discount);
    }

    public BigDecimal getDiscountValue() { return discountValue;
    }

    public UUID getId() {
        return id;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSent() {
        return shipment != null && shipment.isShipped();
    }

    public boolean isPaid() { return paid; }

    public Shipment getShipment() {
        return shipment;
    }

    public BigDecimal getPrice() {
        BigDecimal price = BigDecimal.ZERO;
        for (Product prod: products){
            price = price.add(prod.getPrice());
        }
        return price;
    }

    public BigDecimal getPriceWithTaxes() {
        return getPrice().multiply(TAX_VALUE).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public BigDecimal getPriceWithDiscounts() {
        BigDecimal price = BigDecimal.ZERO;
        for (Product prod: products){
            price = price.add(prod.getPriceWithDiscount());
        }
        return price.multiply(this.discountValue).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public List<Product> getProducts() {
        return products;
    }

    public ShipmentMethod getShipmentMethod() {
        return shipmentMethod;
    }

    public void setShipmentMethod(ShipmentMethod shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }

    public void send() {
        boolean sentSuccesful = getShipmentMethod().send(shipment, shipment.getSenderAddress(), shipment.getRecipientAddress());
        shipment.setShipped(sentSuccesful);
    }

    public void pay(MoneyTransfer moneyTransfer) {
        moneyTransfer.setCommitted(getPaymentMethod().commit(moneyTransfer));
        paid = moneyTransfer.isCommitted();
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
