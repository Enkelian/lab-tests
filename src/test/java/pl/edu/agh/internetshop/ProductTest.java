package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;

import javax.print.attribute.standard.MediaSize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static pl.edu.agh.internetshop.util.CustomAssertions.assertBigDecimalCompareValue;

import static org.mockito.Mockito.mock;

import java.math.BigDecimal;


public class ProductTest {

	
    private static final String NAME = "Mr. Sparkle";
    private static final BigDecimal PRICE = BigDecimal.valueOf(1);
    private static final double DISCOUNT = 0.23;

    @Test
    public void testProductName() throws Exception{
        //given
    	
        // when
        Product product = new Product(NAME, PRICE);
        
        // then
        assertEquals(NAME, product.getName());
    }
    
    @Test
    public void testProductPrice() throws Exception{
        //given
    	
        // when
        Product product = new Product(NAME, PRICE);
        
        // then
        assertBigDecimalCompareValue(product.getPrice(), PRICE);
    }

    @Test
    public void testProductDiscount() {
        //given

        //when
        Product product = new Product(NAME,PRICE);
        product.setDiscountValue(DISCOUNT);

        //then
        assertBigDecimalCompareValue(product.getDiscountValue(),BigDecimal.valueOf(1-DISCOUNT));
    }

    @Test
    public void testProductWithTooBigDiscount() {
        //given
        double discount = 1.2;

        //when
        Product product = new Product(NAME,PRICE);

        //then
        assertThrows(IllegalArgumentException.class, ()->product.setDiscountValue(discount));
    }

    @Test
    public void testProductWithTooSmallDiscount() {
        //given
        double discount = -0.5;

        //when
        Product product = new Product(NAME,PRICE);

        //then
        assertThrows(IllegalArgumentException.class, ()->product.setDiscountValue(discount));
    }

    @Test
    public void testProductPriceWithDiscountWithRoundUp() {
        //given

        //when
        Product product = new Product(NAME,BigDecimal.valueOf(0.01));
        product.setDiscountValue(DISCOUNT);
        //then
        assertBigDecimalCompareValue(product.getPriceWithDiscount(),BigDecimal.valueOf(0.01));
    }

    @Test
    public void testProductPriceWithDiscountWithoutRoundDown() {
        //given

        //when
        Product product = new Product(NAME,BigDecimal.valueOf(2.0));
        product.setDiscountValue(DISCOUNT);
        //then
        assertBigDecimalCompareValue(product.getPriceWithDiscount(),BigDecimal.valueOf(1.54));
    }

    @Test
    public void testProductPriceWithDiscountWithRoundDown() {
        //given

        //when
        Product product = new Product(NAME,BigDecimal.valueOf(0.03));
        product.setDiscountValue(DISCOUNT);
        //then
        assertBigDecimalCompareValue(product.getPriceWithDiscount(),BigDecimal.valueOf(0.02));
    }
}