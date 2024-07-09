package com.nuza.convertidor.monedas;

public record Exchange(double montoBase, String base_code, double conversion_result, String target_code, double conversion_rate) {
}
