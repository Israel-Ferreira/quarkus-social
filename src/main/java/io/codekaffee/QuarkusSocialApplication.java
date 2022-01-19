package io.codekaffee;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;


@OpenAPIDefinition(
        info = @Info(
                title = "Quarkus Social API",
                version = "1.0",
                contact = @Contact(
                        name = "Quarkus social Api",
                        email = "israelfsouza@hotmail.com",
                        url = "https://github.com/Israel-Ferreira"
                )
        )
)
public class QuarkusSocialApplication extends Application {
}
