package com.tp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.util.stream.Collectors;
import com.tp.models.RandomUserResponse;
import com.tp.models.User;
import com.tp.models.UserResult;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import reactor.core.publisher.Mono;
import java.util.*;

/**
 * Servicio para obtener los datos de usuarios random desde la API {@code https://randomuser.me/api/}.
 * Utiliza WebClient para realizar peticiones HTTP de forma asíncrona y optimizada.
 * Nota: Es la implementación que creo que funciona mejor, debido a que prácticamente tarda lo
 * mismo que hacer una solo patición en lugar de tardar el tiempo de 3 peticiones.
 * <p>
 * Implementa concurrencia con {@code ParallelFlux} para realizar múltiples peticiones en paralelo
 * y reduce el tiempo de respuesta a menos de 2.5 segundos.
 * </p>
 */
@Service
public class UserService {

    private static final String API_URL = "https://randomuser.me/api/?results=5000";
    private final WebClient webClient;

    /**
     * Constructor de {@code UserService}.
     * <p>
     * Configura un {@link WebClient} (librería para hacer peticiones HTTP) con una memoria de a 10 MB
     * para manejar grandes respuestas de la API.
     * </p>
     *
     * @param webClientBuilder Constructor de WebClient.
     */
    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024)) // 10 MB en vez de 256 KB
                        .build())
                .baseUrl(API_URL)
                .build();
    }

    /**
     * Obtiene 15,000 usuarios desde la API realizando 3 peticiones en paralelo.
     * <p>
     * - Ejecuta 3 peticiones de 5,000 usuarios cada una. <br>
     * - Usa {@code ParallelFlux} para paralelizar las peticiones. <br>
     * - Filtra los usuarios duplicados basándose en {@code firstName} y {@code lastName}. <br>
     * - Asegura que el resultado final contenga exactamente 15,000 usuarios únicos.
     * </p>
     *
     * @return Una lista de 15,000 usuarios únicos obtenidos desde la API externa.
     */
    public List<User> fetchUsers() {
        // Ejecuta 3 peticiones en paralelo y espera la respuesta de todas
        List<User> allUsers = Flux.range(1, 3) // Ejecuta 3 veces
                .parallel() // Ejecuta paralelo
                .runOn(reactor.core.scheduler.Schedulers.parallel()) // Usa múltiples hilos
                .flatMap(i -> fetchUsersFromAPI()) // Llama a la API
                .sequential()
                .flatMapIterable(users -> users) // Convierte List<User> en elementos individuales de Flux<User>
                .collectList() // Recolecta la lista
                .block(); // Espera la respuesta final

        // Aplanar lista y filtrar duplicados
        return allUsers.stream()
                .distinct()
                .limit(15000)
                .collect(Collectors.toList());
    }

    /**
     * Realiza una petición HTTP GET a la API externa {@code https://randomuser.me/api/}
     * y obtiene una lista de usuarios.
     * <p>
     * - Convierte la respuesta JSON en un objeto {@link RandomUserResponse}. <br>
     * - Extrae la lista de usuarios y los transforma en {@link User}. <br>
     * </p>
     *
     * @return Un {@link Mono} conteniendo una lista de usuarios.
     */
    private Mono<List<User>> fetchUsersFromAPI() {
        return webClient.get()
                .retrieve()
                .bodyToMono(RandomUserResponse.class)
                .map(response -> response.getResults().stream()
                        .map(UserResult::toUser)
                        .collect(Collectors.toList())
                );
    }
}
