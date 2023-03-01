package com.distribuida.rest;

import com.distribuida.db.Book;
import com.distribuida.services.BookService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;

@ApplicationScoped
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

@OpenAPIDefinition(
        info = @Info(
                title = "BookRest",
                version = "1.0.0",
                contact = @Contact(
                        name = "Espinosa - Herrera - Quishpe - Sandoval"
                )
        )
)

public class BookRest {

    @Inject
    private BookService bookService;

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar libro por el ID",
            description = "Se necesita el ID para buscarlo - GET")
    @APIResponse(responseCode = "200", description = "Se encontro el libro",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "500", description = "Error del servidor, algo salio mal al procesar la solicitud")
    @APIResponse(responseCode = "400", description = "Solicitud Incorrecta")
    @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Book.class),
                    examples = @ExampleObject(
                            name = "Buscar libros por id",
                            summary = "ID"
                    )))
    public Book findById(@PathParam("id") String id) {
        return bookService.getBookById(id);
    }


    @GET
    @Operation(summary = "Listar todos los libros",
            description = "No se necesita algún parametro para listar - GET")
    @APIResponse(responseCode = "200", description = "Se encontro los libros",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "500", description = "Error del servidor, algo salio mal al procesar la solicitud")
    @APIResponse(responseCode = "400", description = "Solicitud Incorrecta")
    @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Book.class),
                    examples = @ExampleObject(
                            name = "Buscar todos los libros"

                    )))
       public List<Book> findAll() {
        return bookService.getBooks();
    }


    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar un libro",
            description = "Elimina un libro por buscando por su id - DELETE")
    @APIResponse(responseCode = "200", description = "Se elimino el libro",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "500", description = "Error del servidor, algo salio mal al procesar la solicitud")
    @APIResponse(responseCode = "400", description = "Solicitud Incorrecta")
    @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Book.class),
                    examples = @ExampleObject(
                            name = "Eliminar Books por ID"
                    )))

    public Response delete(@PathParam("id") String id) {
        bookService.delete(id);
        return Response.status((Response.Status.NO_CONTENT)).build();
    }



    @POST
    @Operation(summary = "Crear un libro")
    @APIResponse(description = "Crea un libro y lo manda en una peticion - POST")
    @APIResponse(responseCode = "200", description = "Se inserto el libro",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "500", description = "Error del servidor, algo salio mal al procesar la solicitud")
    @APIResponse(responseCode = "400", description = "Solicitud Incorrecta")
    @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Book.class),
                    examples = @ExampleObject(
                            name = "Insertar Books"
                    )))
     public Response create(Book b) {
        bookService.createBook(b);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar un libro")
    @APIResponse(description = "Actualiza un libro por medio de su ID - PUT")
    @APIResponse(responseCode = "200", description = "Se actualizo el libro",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = Book.class)))
    @APIResponse(responseCode = "500", description = "Error del servidor, algo salio mal al procesar la solicitud")
    @APIResponse(responseCode = "400", description = "Solicitud Incorrecta")
    @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Book.class),
                    examples = @ExampleObject(
                            name = "Actualizar books",
                            summary = "ID"
                    )))
        public Response update(Book b, @PathParam("id") String id) {
        bookService.updateBook(id, b);
        return Response.status((Response.Status.NO_CONTENT)).build();
    }

}
