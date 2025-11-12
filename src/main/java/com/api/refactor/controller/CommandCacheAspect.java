package com.api.refactor.controller;

import ec.diners.com.ms.apiclient.utils.cache.annotation.CacheableCommand;
import ec.diners.com.ms.apiclient.utils.cache.service.CacheKeyGenerator;
import ec.diners.com.ms.apiclient.utils.cache.service.CacheService;
import ec.diners.com.ms.apiclient.utils.domain.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Aspect for handling caching logic for commands.
 * <p>
 * This aspect intercepts methods annotated with {@link CacheableCommand} and manages
 * caching behavior, including retrieving cached responses and storing new ones.
 * It implements intelligent caching by validating response content before storage,
 * preventing the caching of empty or meaningless data.
 * </p>
 * <p>
 * Key features:
 * <ul>
 *   <li>Automatic cache key generation based on request parameters and headers</li>
 *   <li>Cache key exclusion support for dynamic bypass logic</li>
 *   <li>Smart validation of response content before caching</li>
 *   <li>Support for multiple response types using generics</li>
 *   <li>Error handling with automatic cache eviction on corrupted data</li>
 *   <li>Reflection-based validation for custom objects with collections</li>
 * </ul>
 * </p>
 * <p>
 * The aspect is conditionally enabled based on the property
 * {@code cache.aspects.evict-enabled=true} and requires a {@link CacheService} bean
 * to be available in the application context.
 * </p>
 *
 * @see CacheableCommand
 * @see CacheService
 * @see CacheKeyGenerator
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cache.aspects.evict-enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean(CacheService.class)
public class CommandCacheAspect {

    /**
     * Service for managing cache operations.
     */
    private final CacheService cacheService;

    /**
     * Utility for generating cache keys based on annotations and request data.
     */
    private final CacheKeyGenerator keyGenerator;

    /**
     * Intercepts methods annotated with {@link CacheableCommand} to handle caching logic.
     * <p>
     * This method implements the complete cache lifecycle with the following flow:
     * </p>
     * <ol>
     *   <li><b>Validation</b>: Ensures the method has at least one argument (request object)</li>
     *   <li><b>Exclusion check</b>: Generates exclusion key and bypasses cache if criteria are met</li>
     *   <li><b>Cache key generation</b>: Creates a unique cache key based on request and headers</li>
     *   <li><b>Cache lookup</b>: Attempts to retrieve cached data for the generated key</li>
     *   <li><b>Cache hit</b>: Returns cached data wrapped in {@link Response} if found</li>
     *   <li><b>Cache miss</b>: Executes the original method if no cached data exists</li>
     *   <li><b>Validation</b>: Validates the result content using {@link #shouldCacheValue(Object)}</li>
     *   <li><b>Cache storage</b>: Stores the result if validation passes and TTL is configured</li>
     * </ol>
     * <p>
     * Error handling:
     * <ul>
     *   <li>Corrupted cache entries are automatically evicted</li>
     *   <li>Storage errors are logged without affecting the response</li>
     *   <li>The original method result is always returned even if cache operations fail</li>
     * </ul>
     * </p>
     *
     * @param joinPoint        the join point representing the intercepted method.
     * @param cacheableCommand the {@link CacheableCommand} annotation instance containing
     *                         cache configuration (TTL, response type, etc.).
     * @return the result of the intercepted method, either from the cache or the method execution.
     * @throws Throwable if the intercepted method throws an exception.
     */
    @Around("@annotation(cacheableCommand)")
    public Object cacheCommand(ProceedingJoinPoint joinPoint, CacheableCommand cacheableCommand) throws Throwable {

        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            return joinPoint.proceed();
        }

        Object request = args[0];
        Object headers = args.length > 1 ? args[1] : null;

        if (shouldExcludeFromCache(cacheableCommand, request)) {
            return joinPoint.proceed();
        }

        String cacheKey = keyGenerator.generateKey(cacheableCommand, request, headers);
        log.debug("UNIVERSAL CACHE: Clave generada: {}", cacheKey);

        Optional<Response<?>> cachedResponse = getCachedResponse(cacheKey, cacheableCommand.responseType());
        if (cachedResponse.isPresent()) {
            return cachedResponse.get();
        }

        Object result = joinPoint.proceed();
        storeCacheIfValid(cacheKey, result, cacheableCommand);

        return result;
    }

    /**
     * Determines if the cache should be excluded based on exclusion key.
     *
     * @param cacheableCommand the cache annotation.
     * @param request the request object.
     * @return true if cache should be excluded, false otherwise.
     */
    private boolean shouldExcludeFromCache(CacheableCommand cacheableCommand, Object request) {
        Optional<String> keyExcluir = keyGenerator.generateKeyExcluir(cacheableCommand, request);
        return keyExcluir.isPresent() && !keyExcluir.get().isEmpty();
    }

    /**
     * Retrieves cached response if available.
     *
     * @param cacheKey the cache key.
     * @param responseType the expected response type.
     * @return Optional containing the cached Response, or empty if not found.
     */
    private Optional<Response<?>> getCachedResponse(String cacheKey, Class<?> responseType) {
        try {
            Optional<?> cachedData = cacheService.get(cacheKey, responseType);

            if (cachedData.isPresent()) {
                log.info("UNIVERSAL CACHE: Cache HIT para key: {}", cacheKey);
                return Optional.of(new Response<>(cachedData.get()));
            }
        } catch (Exception e) {
            log.warn("UNIVERSAL CACHE: Error al obtener del caché: {}", e.getMessage());
            evictCorruptedCache(cacheKey);
        }
        return Optional.empty();
    }

    /**
     * Evicts corrupted cache entry.
     *
     * @param cacheKey the cache key to evict.
     */
    private void evictCorruptedCache(String cacheKey) {
        try {
            cacheService.evict(cacheKey);
        } catch (Exception evictError) {
            log.debug("No se pudo limpiar caché corrupta", evictError);
        }
    }

    /**
     * Stores result in cache if it's valid for caching.
     *
     * @param cacheKey the cache key.
     * @param result the result to cache.
     * @param cacheableCommand the cache annotation containing TTL.
     */
    private void storeCacheIfValid(String cacheKey, Object result, CacheableCommand cacheableCommand) {
        if (result == null) {
            return;
        }

        try {
            log.info("UNIVERSAL CACHE: Guardando en caché key: {}", cacheKey);
            if (result instanceof Response<?> response &&
                response.getValue() != null &&
                shouldCacheValue(response.getValue())) {
                log.info("Guardando en caché key: {}", cacheKey);
                this.cacheService.put(cacheKey, response.getValue(), cacheableCommand.ttlSeconds());
            }
            log.info("UNIVERSAL CACHE: Guardado exitoso en caché");
        } catch (Exception e) {
            log.warn("UNIVERSAL CACHE: Error al guardar en caché: {}", e.getMessage());
        }
    }

    /**
     * Determines whether a value should be cached based on its content.
     * <p>
     * This method evaluates different types of objects to decide if they contain meaningful data
     * worth caching. Empty or null values are not cached to avoid storing useless data.
     * </p>
     * <p>
     * The validation logic includes:
     * <ul>
     *   <li>{@link Collection} - Cached only if not empty</li>
     *   <li>{@link Map} - Cached only if not empty</li>
     *   <li>{@link String} - Cached only if not blank (after trimming)</li>
     *   <li>Arrays - Cached only if length &gt; 0</li>
     *   <li>{@link Optional} - Cached only if present</li>
     *   <li>Objects with isEmpty() method - Uses reflection to invoke the method</li>
     *   <li>Objects with Collection/Map fields - Validates using {@link #hasEmptyCollectionFields(Object)}</li>
     *   <li>Other objects - Cached by default</li>
     * </ul>
     * </p>
     *
     * @param value the object to evaluate for caching eligibility.
     * @return {@code true} if the value should be cached, {@code false} otherwise.
     */
    private boolean shouldCacheValue(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Collection<?> collection) {
            return !collection.isEmpty();
        }

        if (value instanceof Map<?, ?> map) {
            return !map.isEmpty();
        }

        if (value instanceof String str) {
            return !str.trim().isEmpty();
        }

        if (value.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            return length > 0;
        }

        if (value instanceof Optional<?> optional) {
            return optional.isPresent();
        }

        if (hasIsEmptyMethod(value)) {
            try {
                Method isEmptyMethod = value.getClass().getMethod("isEmpty");
                Object result = isEmptyMethod.invoke(value);
                if (result instanceof Boolean isEmpty) {
                    log.debug("UNIVERSAL CACHE: Objeto con isEmpty() = {} - cacheable: {}",
                            isEmpty, !isEmpty);
                    return !isEmpty;
                }
            } catch (Exception e) {
                log.debug("UNIVERSAL CACHE: Error al ejecutar isEmpty(): {}", e.getMessage());
            }
        }

        return !hasEmptyCollectionFields(value);
    }

    /**
     * Checks if an object has a public {@code isEmpty()} method that returns a boolean.
     * <p>
     * This method uses reflection to determine if the given object's class defines
     * an {@code isEmpty()} method with a boolean return type (either {@code boolean}
     * primitive or {@link Boolean} wrapper).
     * </p>
     * <p>
     * This is useful for dynamically validating custom objects that follow the
     * {@code isEmpty()} convention without requiring explicit type knowledge.
     * </p>
     *
     * @param value the object to inspect for the presence of an isEmpty() method.
     * @return {@code true} if the object has a public isEmpty() method returning boolean,
     *         {@code false} otherwise.
     */
    private boolean hasIsEmptyMethod(Object value) {
        try {
            Method isEmptyMethod = value.getClass().getMethod("isEmpty");
            Class<?> returnType = isEmptyMethod.getReturnType();
            return returnType == Boolean.class || returnType == boolean.class;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Checks if an object has {@link Collection} or {@link Map} fields and whether all of them are empty.
     * <p>
     * This method uses reflection to inspect all declared fields of the given object,
     * identifying those that are of type {@link Collection} or {@link Map}. It then
     * verifies if all such fields are empty.
     * </p>
     * <p>
     * This validation is particularly useful for custom response objects that contain
     * lists or maps as fields (e.g., {@code BeneficiariesCommandResponse} with a
     * {@code listBeneficiaries} field). It prevents caching objects that have no
     * meaningful data despite being non-null.
     * </p>
     * <p>
     * Behavior:
     * <ul>
     *   <li>Returns {@code true} only if the object has at least one Collection/Map field
     *       AND all such fields are empty or null</li>
     *   <li>Returns {@code false} if at least one Collection/Map field contains data</li>
     *   <li>Returns {@code false} if the object has no Collection/Map fields</li>
     *   <li>Returns {@code false} if an error occurs during reflection</li>
     * </ul>
     * </p>
     *
     * @param value the object to inspect for empty Collection/Map fields.
     * @return {@code true} if the object has Collection/Map fields and all are empty,
     *         {@code false} otherwise.
     */
    private boolean hasEmptyCollectionFields(Object value) {
        try {
            Field[] fields = value.getClass().getDeclaredFields();
            boolean hasCollectionFields = false;

            for (Field field : fields) {
                if (isCollectionOrMapField(field)) {
                    hasCollectionFields = true;
                    if (isFieldNonEmpty(field, value)) {
                        return false;
                    }
                }
            }
            return hasCollectionFields;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a field is a Collection or Map type.
     *
     * @param field the field to check.
     * @return true if the field is a Collection or Map, false otherwise.
     */
    private boolean isCollectionOrMapField(Field field) {
        return Collection.class.isAssignableFrom(field.getType()) ||
                Map.class.isAssignableFrom(field.getType());
    }

    /**
     * Checks if a field contains non-empty data.
     *
     * @param field the field to check.
     * @param value the object containing the field.
     * @return true if the field contains non-empty data, false otherwise.
     */
    private boolean isFieldNonEmpty(Field field, Object value) {
        try {
            if (!field.canAccess(value) && !field.trySetAccessible()) {
                log.debug("UNIVERSAL CACHE: No se pudo acceder al campo '{}'", field.getName());
                return false;
            }

            Object fieldValue = field.get(value);

            if (fieldValue == null) {
                return false;
            }

            if (fieldValue instanceof Collection<?> collection) {
                return !collection.isEmpty();
            }

            if (fieldValue instanceof Map<?, ?> map) {
                return !map.isEmpty();
            }

            return false;
        } catch (IllegalAccessException e) {
            log.debug("UNIVERSAL CACHE: No se pudo acceder al campo '{}': {}",
                    field.getName(), e.getMessage());
            return false;
        }
    }
}
