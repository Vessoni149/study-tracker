package com.study_tracker.back;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder  // Implementa el patrón Builder para crear instancias de forma fluida. Los mátodos que implementan este patrón
// no son constructores, sino que se los llama métodos de fábrica. Sirven para crear instancias pero de otra manera,
// facilitando dicha creación al hacerla más versátil, ya que no se necesitan varios constructores con distintos parámetros.
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private LocalDateTime timestamp;  // Momento exacto en que se generó la respuesta
    private int statusCode;           // Código HTTP numérico (200, 404, etc.)
    private HttpStatus status;        // Enum de Spring que representa el estado HTTP
    private String message;           // Mensaje descriptivo de la respuesta
    private T data;                   // Contenido genérico de la respuesta (puede ser cualquier tipo), pensado para mis Dtos.

    /**
     * * Éste método será usado directamente en el Service para construir las respuestas satisfactorias.
     * @param data Recibirá un tipo genérico,que servirá para recibir un xResponseDto que devolverá como respuesta.
     * @param  message Recibirá además un mensaje para asignarle a ApiResponse.
     * @return Devuelve una respuesta ApiResponse con los datos de la entidad ApiResponse y un objeto genérico que será el
     * xResponseDto.
     * En este método el httpStatus se asigna por defecto como OK, ya que construye una respuesta exitosa.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Este método será usado en el manejador de excepciones globales para construir una respuesta de error simple.
     * @param status estado del error http provisto por el manejador de excepciones globales.
     * @param message mensaje del error.
     * @return devuelve una instancia de ApiResponse con los datos de la entidad ApiResponse, pero sin el atributo "data" que
     * vendría a representar xResponseDto.
     */
    public static ApiResponse error(HttpStatus status, String message) {
        return ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(status.value())
                .status(status)
                .message(message)
                .build();
    }

    /**
     * * Este método será usado en el manejador de excepciones globales para construir una respuesta de error compleja.
     * @param status estado del error http provisto por el manejador de excepciones globales.
     * @param data recibira un objeto de respuesta xResponseDto.
     * @param message mensaje del error.
     * @return Devolverá un objeto ApiResponse con todos los datos de la entidad incluso "data", conteniendo el xResponseDto.
     */
    public static <T> ApiResponse<T> error(HttpStatus status, T data, String message) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .statusCode(status.value())
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}