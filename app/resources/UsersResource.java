package resources;

import models.User;
import play.libs.F;
import play.libs.F.Promise;

public interface UsersResource {

    Promise<User> getUser(String username);

}
