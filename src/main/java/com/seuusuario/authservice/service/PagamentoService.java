package com.seuusuario.authservice.service;

import com.seuusuario.authservice.dto.CreatePagamentoRequest;
import com.seuusuario.authservice.dto.PagamentoResponse;
import com.seuusuario.authservice.entity.Pagamento;
import com.seuusuario.authservice.entity.User;
import com.seuusuario.authservice.repository.PagamentoRepository;
import com.seuusuario.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mercadopago.notification.url}")
    private String notificationUrl;

    public PagamentoResponse criarPagamento(CreatePagamentoRequest request, Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String accessToken = user.getCodigoMercadoPago();

            Map<String, Object> preference = new HashMap<>();
            preference.put("auto_return", "approved");
            preference.put("notification_url", notificationUrl);
            preference.put("external_reference", "TEMP");

            Map<String, String> backUrls = new HashMap<>();
            backUrls.put("success", "https://www.seusite.com/sucesso");
            backUrls.put("failure", "https://www.seusite.com/falha");
            backUrls.put("pending", "https://www.seusite.com/pendente");
            preference.put("back_urls", backUrls);

            Map<String, Object> item = new HashMap<>();
            item.put("title", request.getTitle());
            item.put("quantity", 1);
            item.put("currency_id", "BRL");
            item.put("unit_price", request.getUnitPrice());
            preference.put("items", List.of(item));

            OffsetDateTime expirationDateTime = null;
            if (request.getDateOfExpiration() != null && !request.getDateOfExpiration().isEmpty()) {
                if (request.getDateOfExpiration().contains("T")) {
                    LocalDateTime localDateTime = LocalDateTime.parse(request.getDateOfExpiration());
                    expirationDateTime = localDateTime.atOffset(ZoneOffset.of("-03:00"));
                } else {
                    LocalDate dateOnly = LocalDate.parse(request.getDateOfExpiration());
                    LocalDateTime localDateTime = LocalDateTime.of(dateOnly, LocalTime.of(23, 45, 59));
                    expirationDateTime = localDateTime.atOffset(ZoneOffset.of("-03:00"));
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                String dateFormatted = expirationDateTime.format(formatter);
                preference.put("date_of_expiration", dateFormatted);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(preference, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.mercadopago.com/checkout/preferences",
                    entity,
                    Map.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Erro ao criar pagamento no Mercado Pago");
            }

            Map<String, Object> body = response.getBody();

            Pagamento pagamento = new Pagamento();
            pagamento.setMercadoPagoId((String) body.get("id"));
            pagamento.setInitPoint((String) body.get("init_point"));
            pagamento.setTitle(request.getTitle());
            pagamento.setDescription(request.getDescription());
            pagamento.setUnitPrice(request.getUnitPrice());
            pagamento.setStatus("pending");
            pagamento.setUserId(userId);
            if (expirationDateTime != null) {
                pagamento.setDateOfExpiration(expirationDateTime.toLocalDateTime());
            }

            pagamento = pagamentoRepository.save(pagamento);

            atualizarExternalReference(pagamento.getMercadoPagoId(), pagamento.getId().toString(), accessToken);

            PagamentoResponse pagamentoResponse = new PagamentoResponse();
            pagamentoResponse.setId(pagamento.getId());
            pagamentoResponse.setMercadoPagoId(pagamento.getMercadoPagoId());
            pagamentoResponse.setInitPoint(pagamento.getInitPoint());
            pagamentoResponse.setTitle(pagamento.getTitle());
            pagamentoResponse.setDescription(pagamento.getDescription());
            pagamentoResponse.setUnitPrice(pagamento.getUnitPrice());
            pagamentoResponse.setStatus(pagamento.getStatus());
            pagamentoResponse.setDateOfExpiration(pagamento.getDateOfExpiration() != null ? pagamento.getDateOfExpiration().toString() : null);
            pagamentoResponse.setCreatedAt(pagamento.getCreatedAt() != null ? pagamento.getCreatedAt().toString() : null);

            return pagamentoResponse;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage(), e);
        }
    }

    private void atualizarExternalReference(String preferenceId, String externalReference, String accessToken) {
        String url = "https://api.mercadopago.com/checkout/preferences/" + preferenceId;

        Map<String, Object> body = new HashMap<>();
        body.put("external_reference", externalReference);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

    public Page<Pagamento> listarPagamentos(Long userId, Pageable pageable) {
        return pagamentoRepository.findAllByUserId(userId, pageable);
    }

    public void deletarPagamento(Long id, Long userId) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        if (!pagamento.getUserId().equals(userId)) {
            throw new RuntimeException("Usuário não autorizado a deletar este pagamento");
        }

        pagamentoRepository.deleteById(id);
    }

    public void atualizarStatusPagamento(Long id, String status) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setStatus(status);
        pagamentoRepository.save(pagamento);
    }

    public Optional<Pagamento> buscarPagamentoPorId(Long id, Long userId) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

        if (pagamento.isPresent() && pagamento.get().getUserId().equals(userId)) {
            return pagamento;
        }
        return Optional.empty();
    }
}
