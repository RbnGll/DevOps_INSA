package spoon.resource;

import io.swagger.annotations.Api;
import spoon.ast.api.SpoonAST;
import spoon.ast.api.TreeLevel;
import spoon.ast.builder.SpoonTreeCmdBase;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.Optional;

@Singleton
@Path("spoon")
@Api(value = "spoon")
public class SpoonResource {
    public SpoonResource() {
        super();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("ast")
    public SpoonAST createAST(final CodeDTO code) {
        final var okLevel = TreeLevel.from(code.level);
        if (okLevel.isEmpty()) {
            throw new WebApplicationException("Incorrect level");
        }

        final var cmd = new SpoonTreeCmdBase(true, code.code, okLevel.get());
        final Optional<SpoonAST> execute = cmd.execute();
        return execute.orElseThrow(() -> new WebApplicationException("Fail to parse"));
    }
}