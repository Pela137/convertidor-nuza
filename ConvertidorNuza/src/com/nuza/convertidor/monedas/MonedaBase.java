package com.nuza.convertidor.monedas;

import com.google.gson.annotations.SerializedName;
import com.nuza.convertidor.principal.Principal;


public class MonedaBase implements Comparable<MonedaBase>  {
    double montoBase = Principal.montoBase;
    @SerializedName("conversion_rate")
    double tasaDeCambio;
    @SerializedName("conversion_result")
    double resultado;
    @SerializedName("base_code")
    String monedaBase;
    @SerializedName("target_code")
    String monedaObjetivo;

    public MonedaBase(Exchange miExchange) {
        this.montoBase = getMontoBase();
        this.monedaBase = miExchange.base_code();
        this.monedaObjetivo = miExchange.target_code();
        this.tasaDeCambio = miExchange.conversion_rate();
        this.resultado = miExchange.conversion_result();
    }

    public double getMontoBase() {
        return montoBase;
    }

    public String getMonedaBase() {
        return monedaBase;
    }

    @Override
    public String toString() {
        return String.format("%.2f", montoBase) + " " + monedaBase + " = " + resultado + " " + monedaObjetivo + "   -Tasa de cambio: " + tasaDeCambio;
    }

    @Override
    public int compareTo(MonedaBase otraConversion) {
        return this.getMonedaBase().compareTo(otraConversion.getMonedaBase());
    }
}
