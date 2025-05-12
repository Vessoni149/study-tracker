package com.study_tracker.back.exceptions;

import com.study_tracker.back.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase implementa un manejador global de excepciones centralizado utilizando la anotación
 * @ControllerAdvice de Spring. Esto permite capturar y procesar todas las excepciones que ocurran en
 * cualquier servicio de la aplicación.
 * La clase contiene tres manejadores de excepciones para diferentes situaciones:
 * -EntityNotFoundException: Para cuando no se encuentra un recurso solicitado.
 * -MethodArgumentNotValidException: Para errores de validación de entrada.
 * -Exception: Captura cualquier otra excepción no manejada específicamente.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthenticated(UserNotAuthenticatedException ex) {
        ApiResponse<Object> body = ApiResponse.error(
                HttpStatus.UNAUTHORIZED,
                null,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
    @ExceptionHandler(EntityNotFoundException.class) // Registra este método para ese tipo de excepción, Al ocurrir un error
    // EntityNotFoundException en cualquier parte de mi codigo, se ejecutará éste método.
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiResponse<Void> response = ApiResponse.error(status, ex.getMessage());    //Éste objeto ApiResponse no va a contener
        // un xResponseDto, por eso el <Void>.
        return new ResponseEntity<>(response, status);
    }

    /**
     * Manejador para errores de validación (400 Bad Request).
     * @ExceptionHandler registra el método como un manejador específico para la excepción MethodArgumentNotValidException.
     * Cuando Spring detecta que esta excepción ha sido lanzada en cualquier parte de tu aplicación, redirige automáticamente
     * el control a este método.
     * Gracias a que en el controlador usamos la anotación @Valid y en el xRequestDto usamos anotaciones de
     * validacion como @NotNull por ej., al ocurrir un error Spring lanza una excepcion MethodArgumentNotValidException.
     *
     * @param ex este método que lanza spring será recibido como parámetro y contiene los errores de validación ocurridos.
     * @return Esto devuelve una respuesta HTTP que contiene mi objeto ApiResponse, que a su vez encapsula un
     * map de errores conteniendo como clave el nombre del campo y como valor el mensaje de error que establecimos en las
     * etiquetas de validación como @NotNull.
     *
     * Flujo de ejecución:
     * Del objeto MethodArgumentNotValidException recibido por parámetro (creado por srping al ocurrir un error de
     * validación en el controlador), se pueden obtener los detalles del/los errores con los métodos getBindingResult()
     * y .getAllErrors.
     * Se va a crear un Map llamado "errors" que contendrá como clave el nombre del campo en el que ocurrio el error de
     * validacion, y como valor, el mensaje de error que se estableció en la anotación de validacion en el xRequestDto, ej:
     *
     *     @NotNull(message = "el campo 'date' no puede ser 'null'")
     *     private LocalDate date;
     *
     * Del objeto recibido por parámetro "ex" vamos a extraer los campos donde sucedieron errores y los mensajes de errores
     * para añadirlos a nuestro map "errors".
     * Para cada error, obtenidos de los metodos ex.getBindingResult().getAllErrors:
     * Hace un cast a FieldError para poder acceder al nombre del campo que falló
     * Extrae el mensaje de error predeterminado
     * Agrega una entrada al mapa con el formato "nombreCampo": "mensajeError"
     *
     * Luego creamos una variable de tipo HttpStatus que será una BAD_REQUEST
     *
     * Finalmente, creamos el ApiResponse (usando el metodo builder 'error' de 3 parámetros) almacenado en "response" que contendrá:
     * - el status instanciado acá mismo,
     * - el map errors,
     * - y el mensaje.
     * Con ese objeto ApiResponse, más el status creamos el ResponseEntity que será devuelto
     *
     * El campo status es usado 2 veces, tanto para crear el objeteo ApiResponse como para crear el objeto ResponseEntity,
     * esto porque:
     * Cuando un cliente procesa una respuesta API, hay dos lugares donde puede obtener el estado HTTP:
     * - En los encabezados HTTP (status code)
     * - En el cuerpo de la respuesta serializado (tu objeto JSON)
     * Incluir el estado en el cuerpo asegura que incluso si alguien está analizando solo el JSON (sin acceso a los
     * encabezados HTTP), toda la información contextual esté disponible.
     * En diseño de API REST, hay dos escuelas de pensamiento:
     * Minimalistas: Argumentan que el cuerpo de la respuesta debería contener solo datos relevantes para el dominio,
     * dejando los detalles HTTP al protocolo. Para ellos, incluir códigos HTTP en el JSON es redundante.
     * Completos: Prefieren respuestas auto-contenidas donde el cuerpo incluye metadatos completos, incluyendo estado,
     * para que sea independiente del protocolo de transporte.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<Map<String, String>> response = ApiResponse.error(status, errors, "Validation failed");
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponse<Void> response = ApiResponse.error(status, "An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(response, status);
    }


    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<Void> response = ApiResponse.error(status, ex.getMessage());
        return new ResponseEntity<>(response, status);
    }



}
