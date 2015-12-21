package resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import modules.ExamModule;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import javax.inject.Named;

public class WSUsersResource implements UsersResource {

    private final WSClient ws;
    private final String rootUrl;
    private final ObjectMapper mapper;

    @Inject
    private WSUsersResource(WSClient ws, @Named(ExamModule.BACKEND_URL) String rootUrl, ExamModule.CustomizedObjectMapper objectMapper) {
        this.ws = ws;
        this.rootUrl = rootUrl;
        this.mapper = objectMapper;
    }

    @Override
    public Promise<User> getUser(String username) {
        return ws.url(rootUrl + "/users/" + username).get()
            .map(WSResponse::asJson)
            .map(json -> json.get("user"))
            .map(x -> mapper.readValue(x.toString(), User.class));
    }
}
