package spoon.resource;

import com.github.hanleyt.JerseyExtension;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import spoon.ast.impl.SpoonASTImpl;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class TestSpoonResource {
    static final Logger log = Logger.getLogger(TestSpoonResource.class.getSimpleName());

    static {
        System.setProperty("jersey.config.test.container.port", "0");
    }

    @SuppressWarnings("unused")
    @RegisterExtension
    JerseyExtension jerseyExtension = new JerseyExtension(this::configureJersey);

    Application configureJersey() {
        return new ResourceConfig(SpoonResource.class)
                .register(MoxyJsonFeature.class);
    }

    <T> T logJSONAndUnmarshallValue(final Response res, final Class<T> classToRead) {
        res.bufferEntity();
        final String json = res.readEntity(String.class);
        log.log(Level.INFO, "JSON received: " + json);
        final T obj = res.readEntity(classToRead);
        res.close();
        return obj;
    }

    @Test
    void testGetNames(final Client client, final URI baseUri) {
        final Response res = client
                .target(baseUri)
                .path("spoon/ast")
                .request()
                .post(Entity.json(new CodeDTO("public class Foo {private String bar = \"barbar\";}", "a")));

        final SpoonASTImpl spoonAST = logJSONAndUnmarshallValue(res, SpoonASTImpl.class);

        assertThat(res.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(spoonAST).isNotNull();
    }

    @Test
    void testCreateASTThrowsWebApplicationException(final Client client, final URI baseUri) {
        final Response res = client.
                target(baseUri)
                .path("spoon/ast")
                .request()
                .post(Entity.json(new CodeDTO("","anything")));
        assertThat(res.getStatus()).isEqualTo(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }
}
