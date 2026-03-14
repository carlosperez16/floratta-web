package com.floratta.service;
 
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.floratta.model.entity.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
 
@Service
public class PagoService {
 
    @Value("${mercadopago.access.token}")
    private String accessToken;
 
    @Value("${app.url:http://localhost:8080}")
    private String appUrl;
 

    public String crearPreferenciaPago(Pedido pedido) {
        try {

            MercadoPagoConfig.setAccessToken(accessToken);

            PreferenceClient client = new PreferenceClient();
 
            List<PreferenceItemRequest> items = new ArrayList<>();
            pedido.getDetalles().forEach(detalle -> {
                PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(String.valueOf(detalle.getProducto().getId()))
                    .title(detalle.getProducto().getNombre())
                    .quantity(detalle.getCantidad())
                    .unitPrice(detalle.getPrecioUnitario())
                    .currencyId("COP") // Pesos colombianos
                    .build();
                items.add(item);
            });
 
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(appUrl + "/pago/exito?pedidoId=" + pedido.getId())
                .failure(appUrl + "/pago/error?pedidoId=" + pedido.getId())
                .pending(appUrl + "/pago/pendiente?pedidoId=" + pedido.getId())
                .build();
 
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .externalReference(String.valueOf(pedido.getId()))
                .build();
 
            Preference preference = client.create(preferenceRequest);
 
            return preference.getSandboxInitPoint(); 
 
            } catch (MPException | com.mercadopago.exceptions.MPApiException e) {
             throw new RuntimeException("Error al crear preferencia de pago: " + e.getMessage());
}
    }
}
