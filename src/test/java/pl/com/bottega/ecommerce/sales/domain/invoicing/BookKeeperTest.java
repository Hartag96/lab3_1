package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;
import static pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType.DRUG;

public class BookKeeperTest {
    Id id = new Id("1");
    ClientData cd = new ClientData(id, "foo");
    InvoiceFactory invoiceFactory = new InvoiceFactory();
    BookKeeper bk = new BookKeeper(invoiceFactory);

    InvoiceRequest ir = new InvoiceRequest(cd);
    TaxPolicy taxPolicy;

    Id productId1 = new Id("11");
    BigDecimal priceProduct1 = BigDecimal.valueOf(100);
    Money moneyProduct1 = new Money(priceProduct1);
    String product1Name = "product1";
    Product productData1 = new Product(productId1, moneyProduct1, product1Name, DRUG);
    RequestItem requestItem1 = new RequestItem(productData1.generateSnapshot(), 1, moneyProduct1);




    @Test public void issuance() {
        ir.add(requestItem1);
        System.out.println(taxPolicy);
        System.out.println(ir.getClientData());
        bk.issuance(ir, taxPolicy);
    }
}
