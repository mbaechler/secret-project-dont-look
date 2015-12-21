package resources;


import models.User;
import play.cache.CacheApi;
import play.cache.NamedCache;
import play.libs.F.Promise;

import javax.inject.Inject;

public class CachingUsersResource implements UsersResource {

    private final WSUsersResource wsUsersResource;
    private final CacheApi cacheApi;

    @Inject
    private CachingUsersResource(WSUsersResource wsUsersResource, @NamedCache("users-cache") CacheApi cacheApi) {
        this.wsUsersResource = wsUsersResource;
        this.cacheApi = cacheApi;
    }

    @Override
    public Promise<User> getUser(String username) {
        return cacheApi.getOrElse("getUser" + username, () -> wsUsersResource.getUser(username));
    }
}
