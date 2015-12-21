import com.google.common.collect.ImmutableList;
import com.jayway.restassured.RestAssured;
import expections.UserNotFoundException;
import models.Product;
import models.Purchase;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.F;
import play.libs.F.Promise;
import resources.ProductsResource;
import resources.PurchasesResource;
import resources.UsersResource;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static play.inject.Bindings.bind;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;


public class IntegrationTest {

    private int port;
    private ProductsResource products;
    private PurchasesResource purchases;
    private Application application;
    private UsersResource users;

    @Before
    public void setup() {
        port = 3333;
        RestAssured.port = port;
        products = mock(ProductsResource.class);
        purchases = mock(PurchasesResource.class);
        users = mock(UsersResource.class);
        application = new GuiceApplicationBuilder()
            .overrides(
                bind(ProductsResource.class).toInstance(products),
                bind(PurchasesResource.class).toInstance(purchases),
                bind(UsersResource.class).toInstance(users)
            )
            .build();
    }

    @Test
    public void unknowUserShouldReturnNotFound() {

        when(users.getUser("bobby")).thenReturn(
            Promise.throwing(new UserNotFoundException()));

        running(testServer(port, application), () -> {
            given()
                .when()
                .get("/api/recent_purchases/bobby")
                .then()
                .statusCode(404)
                .content(equalTo("User with username of 'bobby' was not found"));
        });
    }

    @Test
    public void unexpectedErrorShouldReturnInternalServerError() {

        when(users.getUser("bobby")).thenReturn(
            Promise.throwing(new RuntimeException()));

        running(testServer(port, application), () -> {
            given()
                .when()
                .get("/api/recent_purchases/bobby")
                .then()
                .statusCode(500);
        });
    }

    @Test
    public void globalTest() {
        String username = "Kyla.Larkin40";

        when(users.getUser(username)).thenReturn(
            Promise.pure(User.builder().email("kyla@nowhere.com").username(username).build()));

        when(purchases.listRecentPurchasesForUser(username))
            .thenReturn(Promise.pure(
                ImmutableList.of(
                    Purchase.builder().productId(Product.id(1)).username(username).build(),
                    Purchase.builder().productId(Product.id(2)).username(username).build())));
        when(products.getProduct(Product.id(1)))
            .thenReturn(Promise.pure(Product.builder().face("(:").id(Product.id(1)).price(12).build()));
        when(products.getProduct(Product.id(2)))
            .thenReturn(Promise.pure(Product.builder().face("):").id(Product.id(2)).price(1200).build()));

        when(purchases.listSimilarPurchasesFor(Product.id(1)))
            .thenReturn(Promise.pure(
                ImmutableList.of(
                    Purchase.builder().productId(Product.id(1)).username("Bob").build(),
                    Purchase.builder().productId(Product.id(1)).username("Alice").build(),
                    Purchase.builder().productId(Product.id(1)).username("Bob").build(),
                    Purchase.builder().productId(Product.id(1)).username(username).build()
                )
            ));

        when(purchases.listSimilarPurchasesFor(Product.id(2)))
            .thenReturn(Promise.pure(
                ImmutableList.of(
                    Purchase.builder().productId(Product.id(2)).username("Robert").build(),
                    Purchase.builder().productId(Product.id(2)).username(username).build()
                )
            ));


        running(testServer(port, application), () -> {
            given()
                .when()
                .get("/api/recent_purchases/" + username)
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(1))
                .body("[0].price", equalTo(12))
                .body("[0].face", equalTo("(:"))
                .body("[0].recent", containsInAnyOrder("Bob", "Alice", username))
                .body("[1].id", equalTo(2))
                .body("[1].price", equalTo(1200))
                .body("[1].face", equalTo("):"))
                .body("[1].recent", containsInAnyOrder("Robert", username));

        });
    }

}
