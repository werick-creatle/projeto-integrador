package br.com.gamestore.loja_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/*
 * @RestControllerAdvice
 * Diz ao Spring que esta classe vai "ouvir" globalmente
 * por exceções em todos os @RestControllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * @ExceptionHandler(MethodArgumentNotValidException.class)
     * Diz ao Spring: "Quando um @Valid falhar (e lançar MethodArgumentNotValidException),
     * execute este método aqui."
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        //Isso cria um mapa para guardar os erros
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName =((FieldError) error).getField();

            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções ResponseStatusException (ex: 400, 404, 403)
     * que são lançadas manualmente pelos Services.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        // Retorna o Status (ex: 400) e a Mensagem (ex: "Estoque insuficiente...")
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }

}
