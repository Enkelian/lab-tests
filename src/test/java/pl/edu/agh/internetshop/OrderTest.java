package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static pl.edu.agh.internetshop.util.CustomAssertions.assertBigDecimalCompareValue;

public class OrderTest {

    private static final String NAME = "Mr. Sparkle";
    private static final BigDecimal PRICE = BigDecimal.valueOf(1);

	private Order getOrderWithMockedProduct() {
		Product product = mock(Product.class);
		return new Order(Collections.singletonList(product));
	}

	@Test
	public void testEmptyListOfProducts() {
		// when
		// then
		assertThrows(IllegalArgumentException.class, () -> new Order(Collections.emptyList()));
	}

	@Test
	public void testNullListOfProducts() {
		//when
		//then
		assertThrows(NullPointerException.class, () -> new Order(null));
	}

	@Test
	public void productListWithNullElement() {
		//given
		Product product = mock(Product.class);
		List<Product> products = Arrays.asList(product,null);
		//when
		//then
		assertThrows(IllegalArgumentException.class, () -> new Order(products));
	}

	@Test
	public void getProductThroughOrder() {
		// given
		Product expectedProduct = mock(Product.class);
		Order order = new Order(Collections.singletonList(expectedProduct));

		// when
		List<Product> actualProduct = order.getProducts();

		// then
		assertSame(expectedProduct, actualProduct.get(0));
	}

	@Test
	public void getManyProductsThroughOrder() {
		//given
		Product expectedProduct1 = mock(Product.class);
		Product expectedProduct2 = mock(Product.class);
		Order order = new Order(Arrays.asList(expectedProduct1,expectedProduct2));

		//when
		List<Product> actualProducts = order.getProducts();

		//then
		assertSame(expectedProduct1,actualProducts.get(0));
		assertSame(expectedProduct2,actualProducts.get(1));
		assertEquals(actualProducts.size(),2);
	}

	@Test
	public void testSetShipment() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		Shipment expectedShipment = mock(Shipment.class);

		// when
		order.setShipment(expectedShipment);

		// then
		assertSame(expectedShipment, order.getShipment());
	}

	@Test
	public void testShipmentWithoutSetting() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertNull(order.getShipment());
	}

	@Test
	public void testGetPrice() throws Exception {
		// given
		BigDecimal expectedProductPrice = BigDecimal.valueOf(1000);
		Product product = mock(Product.class);
		given(product.getPrice()).willReturn(expectedProductPrice);
		Order order = new Order(Collections.singletonList(product));

		// when
		BigDecimal actualProductPrice = order.getPrice();

		// then
		assertBigDecimalCompareValue(expectedProductPrice, actualProductPrice);
	}

	private Order getOrderWithCertainProductPrice(double productPriceValue) {
		BigDecimal productPrice = BigDecimal.valueOf(productPriceValue);
		Product product = mock(Product.class);
		given(product.getPrice()).willReturn(productPrice);
		return new Order(Collections.singletonList(product));
	}

	private Order getOrderWithManyCertainProductPrices(List<Double> prices) {
		List<Product> products = new ArrayList<>();
		for (Double price: prices) {
			BigDecimal decimalPrice = BigDecimal.valueOf(price);
			Product product  = new Product(NAME,decimalPrice);
			products.add(product);
		}
		return new Order(products);
	}

	@Test
	public void getPriceOfOrderWithManyProducts() {
		//given
		List<Double> prices = new ArrayList<>(Arrays.asList(900.0,100.0,2000.0));
		Order order = getOrderWithManyCertainProductPrices(prices);
		BigDecimal expectedPrice = BigDecimal.ZERO;
		for (Double price: prices) {
			expectedPrice = expectedPrice.add(BigDecimal.valueOf(price));
		}

		//when
		BigDecimal actualPrice = order.getPrice();

		//then
		assertEquals(actualPrice,expectedPrice);
	}

    private Order getOrderWithProductsDiscounts(List<Double> prices,List<Double> discounts) {
        Order order = getOrderWithManyCertainProductPrices(prices);
        for (int i=0;i<order.getProducts().size();i++){
            order.getProducts().get(i).setDiscountValue(discounts.get(i));
        }
        return order;
    }

	@Test
    public void testOrderGeneralDiscount() {
	    //given
        double discount = 0.2;
        Order order = getOrderWithMockedProduct();
        order.setDiscount(discount);

        //when
        //then
        assertEquals(order.getDiscountValue(),BigDecimal.valueOf(1-discount));
    }

	@Test
    public void testOrderWithTooBigDiscount() {
	    //given
        double discount = 1.2;

        //when
        Order order = getOrderWithMockedProduct();

        //then
        assertThrows(IllegalArgumentException.class, () -> order.setDiscount(discount));
    }

    @Test
    public void testOrderWithTooSmallDiscount() {
        //given
        double discount = -0.1;

        //when
        Order order = getOrderWithMockedProduct();

        //then
        assertThrows(IllegalArgumentException.class, () -> order.setDiscount(discount));
    }

	@Test
	public void testPriceWithOnlyGeneralDiscount() {
		//given
        List<Double> prices = new ArrayList<>(Arrays.asList(100.0,200.0,90.0));
        Order order = getOrderWithManyCertainProductPrices(prices);
        double discount = 0.2;
        order.setDiscount(discount);
        BigDecimal expectedPrice = BigDecimal.ZERO;
        for (Double price: prices){
            expectedPrice = expectedPrice.add(BigDecimal.valueOf(price));
        }
        expectedPrice = expectedPrice.multiply(BigDecimal.valueOf(1-discount));

        //when
        BigDecimal actualPrice = order.getPriceWithDiscounts();

        //then
        assertBigDecimalCompareValue(actualPrice,expectedPrice);
	}

	@Test
    public void testPriceWithOnlyProductsDiscount() {
	    //given
        List<Double> prices = new ArrayList<>(Arrays.asList(100.0,200.0,90.0));
        List<Double> discounts = new ArrayList<>(Arrays.asList(0.2,0.1,0.3));
        Order order = getOrderWithProductsDiscounts(prices,discounts);
        BigDecimal expectedPrice = BigDecimal.ZERO;
        for (int i=0;i<prices.size();i++) {
            expectedPrice = expectedPrice.add(BigDecimal.valueOf(prices.get(i)).multiply(BigDecimal.valueOf(1-discounts.get(i))));
        }

        //when
        BigDecimal actualPrice = order.getPriceWithDiscounts();

        //then
        assertBigDecimalCompareValue(expectedPrice,actualPrice);
    }

    @Test
    public void testPriceWithBothKindOfDiscounts() {
	    //given
        List<Double> prices = new ArrayList<>(Arrays.asList(100.0,200.0,90.0));
        List<Double> discounts = new ArrayList<>(Arrays.asList(0.2,0.1,0.3));
        Order order = getOrderWithProductsDiscounts(prices,discounts);
        double discount = 0.2;
        order.setDiscount(discount);
        BigDecimal expectedPrice = BigDecimal.ZERO;
        for (int i=0;i<prices.size();i++) {
            expectedPrice = expectedPrice.add(BigDecimal.valueOf(prices.get(i)).multiply(BigDecimal.valueOf(1-discounts.get(i))));
        }
        expectedPrice = expectedPrice.multiply(BigDecimal.valueOf(1-discount));

        //when
        BigDecimal actualPrice = order.getPriceWithDiscounts();

        //then
        assertBigDecimalCompareValue(actualPrice,expectedPrice);
    }

	@Test
	public void testPriceWithTaxesWithoutRoundUp() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(2); // 2 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(2.46)); // 2.46 PLN
	}

	@Test
	public void testPriceWithTaxesWithRoundDown() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(0.01); // 0.01 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(0.01)); // 0.01 PLN
																							
	}

	@Test
	public void testPriceWithTaxesWithRoundUp() {
		// given

		// when
		Order order = getOrderWithCertainProductPrice(0.03); // 0.03 PLN

		// then
		assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(0.04)); // 0.04 PLN
																							
	}

	@Test
	public void testSetShipmentMethod() {
		// given
		Order order = getOrderWithMockedProduct();
		ShipmentMethod surface = mock(SurfaceMailBus.class);

		// when
		order.setShipmentMethod(surface);

		// then
		assertSame(surface, order.getShipmentMethod());
	}

	@Test
	public void testSending() {
		// given
		Order order = getOrderWithMockedProduct();
		SurfaceMailBus surface = mock(SurfaceMailBus.class);
		Shipment shipment = mock(Shipment.class);
		given(shipment.isShipped()).willReturn(true);

		// when
		order.setShipmentMethod(surface);
		order.setShipment(shipment);
		order.send();

		// then
		assertTrue(order.isSent());
	}

	@Test
	public void testIsSentWithoutSending() {
		// given
		Order order = getOrderWithMockedProduct();
		Shipment shipment = mock(Shipment.class);
		given(shipment.isShipped()).willReturn(true);

		// when

		// then
		assertFalse(order.isSent());
	}

	@Test
	public void testWhetherIdExists() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertNotNull(order.getId());
	}

	@Test
	public void testSetPaymentMethod() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		PaymentMethod paymentMethod = mock(MoneyTransferPaymentTransaction.class);

		// when
		order.setPaymentMethod(paymentMethod);

		// then
		assertSame(paymentMethod, order.getPaymentMethod());
	}

	@Test
	public void testPaying() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();
		PaymentMethod paymentMethod = mock(MoneyTransferPaymentTransaction.class);
		given(paymentMethod.commit(any(MoneyTransfer.class))).willReturn(true);
		MoneyTransfer moneyTransfer = mock(MoneyTransfer.class);
		given(moneyTransfer.isCommitted()).willReturn(true);

		// when
		order.setPaymentMethod(paymentMethod);
		order.pay(moneyTransfer);

		// then
		assertTrue(order.isPaid());
	}

	@Test
	public void testIsPaidWithoutPaying() throws Exception {
		// given
		Order order = getOrderWithMockedProduct();

		// when

		// then
		assertFalse(order.isPaid());
	}
}
