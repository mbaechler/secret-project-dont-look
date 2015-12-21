package modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import play.Configuration;
import play.libs.Json;
import resources.CachingProductsResource;
import resources.CachingPurchasesResource;
import resources.CachingUsersResource;
import resources.ProductsResource;
import resources.PurchasesResource;
import resources.UsersResource;

import javax.inject.Named;
import javax.inject.Singleton;

public class ExamModule extends AbstractModule {

    public static final String BACKEND_URL = "BackendUrl";

    @Override
    protected void configure() {
        bind(CustomizedObjectMapper.class).asEagerSingleton();
        bind(PurchasesResource.class).to(CachingPurchasesResource.class);
        bind(ProductsResource.class).to(CachingProductsResource.class);
        bind(UsersResource.class).to(CachingUsersResource.class);
    }

    @Provides
    @Named(BACKEND_URL)
    @Singleton
    public String rootUrl(Configuration configuration) {
        return configuration.getString("backend.url");
    }

    public static class CustomizedObjectMapper extends ObjectMapper {
        public CustomizedObjectMapper() {
            this.registerModules(new Jdk8Module(), new JSR310Module());
            Json.setObjectMapper(this);
        }
    };
}
