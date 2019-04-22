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

import static org.mockito.Mockito.*;

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

    @Test public void issuanceMethodInvokesCalculateTaxTwoTimes() {
        Money money = new Money(2, Money.DEFAULT_CURRENCY);

        RequestItem requestItem1 = new RequestItem(productData, 1, money);
        RequestItem requestItem2 = new RequestItem(productData, 1, money);

        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, times(2)).calculateTax(ProductType.DRUG, money);
    }

    @Test public void issuanceMethodReturnsInvoiceWithValidProductType(){
        RequestItem requestItem1 = new RequestItem(productData, 1, new Money(2, Money.DEFAULT_CURRENCY));
        invoiceRequest.add(requestItem1);
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Assert.assertThat(invoice.getItems().get(0).getProduct().getType(), Matchers.equalTo(ProductType.DRUG));
    }

    @Test public void issuanceMethodCallProductGetTypeTwice(){
        Money money = new Money(2, Money.DEFAULT_CURRENCY);

        RequestItem requestItem1 = new RequestItem(productData, 1, money);
        RequestItem requestItem2 = new RequestItem(productData, 1, money);

        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(productData, times(2)).getType();
    }

    @Test public void issuanceMethodReturnsInvoiceWithoutItems() {
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Assert.assertThat(invoice.getItems().size(), Matchers.equalTo(0));
    }
}
