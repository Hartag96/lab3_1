package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookKeeperTest {

    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private ProductData productData;
    private TaxPolicy taxPolicy;

    @Before
    public void initialize() {

        ClientData clientData = new ClientData(Id.generate(), "foo");
        InvoiceFactory invoiceFactory = new InvoiceFactory();

        bookKeeper = new BookKeeper(invoiceFactory);
        invoiceRequest = new InvoiceRequest(clientData);

        taxPolicy = mock(TaxPolicy.class);
        when(taxPolicy.calculateTax(ProductType.DRUG, new Money(2, Money.DEFAULT_CURRENCY))).thenReturn(
                new Tax(new Money(2, Money.DEFAULT_CURRENCY), "tax-drugs"));

        productData = mock(ProductData.class);
        when(productData.getType()).thenReturn(ProductType.DRUG);

    }

    @Test public void issuanceMethodReturnsInvoiceWithOneItem() {
        RequestItem requestItem1 = new RequestItem(productData, 1, new Money(2, Money.DEFAULT_CURRENCY));
        invoiceRequest.add(requestItem1);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Assert.assertThat(invoice.getItems().size(), Matchers.equalTo(1));
    }
}
