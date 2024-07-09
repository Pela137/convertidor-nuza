package com.nuza.convertidor.principal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nuza.convertidor.monedas.Exchange;
import com.nuza.convertidor.monedas.MonedaBase;
import com.nuza.convertidor.monedas.OtrasMonedas;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    public static double montoBase;

    public static void main(String[] args) throws IOException, InterruptedException {

        List<MonedaBase> conversiones = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        System.out.println("*** TE DAMOS LA BIENVENIDA ***");
        String menuMonedas = """
                    \n     Dólar estadounidense   -> USD
                         Euro                   -> EUR
                         Peso uruguayo          -> UYU
                         Peso argentino         -> ARS
                         Real brasilero         -> BRL
                                                         """;
        String menuMasMonedas = "Digita 'MAS' si quieres ver otras monedas";
        String monedaBase;
        String monedaObjetivo;
        String masMonedas = "https://v6.exchangerate-api.com/v6/ace9151621743ddd254b4513/codes";

        while (true) {
            Scanner lectura = new Scanner(System.in);
            do {
                System.out.println("\nEscribe el código de la moneda a convertir (ejemplo: USD):");
                System.out.println(menuMonedas);
                System.out.println(menuMasMonedas);
                monedaBase = lectura.nextLine();
            } while (monedaBase.length() != 3);
            if (monedaBase.equalsIgnoreCase("SAL")) {
                break;
            } else if (monedaBase.equalsIgnoreCase("MAS")) {
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(masMonedas))
                            .build();
                    HttpResponse<String> response = client
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    String json = response.body();
                    OtrasMonedas listaMonedas = gson.fromJson(json, OtrasMonedas.class);
                    System.out.println("Más monedas:\n" + listaMonedas.supported_codes() + "\n");

                } catch (NumberFormatException e) {
                    System.out.println("Ocurrió un error");
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error en la URI. Verifique dirección");
                    System.out.println(e.getMessage());
                } catch (IllegalStateException e) {
                    System.out.println("Error");
                    System.out.println(e.getMessage());
                }
                do {
                    System.out.println("Escribe el código de la moneda a convertir (ejemplo: USD):");
                    System.out.println(menuMonedas);
                    monedaBase = lectura.nextLine();
                } while (monedaBase.length() != 3);
            }

            do {
                System.out.println("¿A qué moneda quieres hacer la conversión? (ejemplo: USD)");
                System.out.println(menuMonedas);
                System.out.println(menuMasMonedas);
                monedaObjetivo = lectura.nextLine();
            } while (monedaObjetivo.length() != 3);
            if (monedaObjetivo.equalsIgnoreCase("MAS")) {
                try {

                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(masMonedas))
                            .build();
                    HttpResponse<String> response = client
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    String json = response.body();
                    OtrasMonedas listaMonedas = gson.fromJson(json, OtrasMonedas.class);

                    System.out.println("Más monedas:\n" + listaMonedas.supported_codes() + "\n");

                } catch (NumberFormatException e) {
                    System.out.println("Ocurrió un error");
                    System.out.println(e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error en la URI. Verifique dirección");
                    System.out.println(e.getMessage());
                } catch (IllegalStateException e) {
                    System.out.println("Error");
                    System.out.println(e.getMessage());
                }

                System.out.println("\n¿A qué moneda quieres hacer la conversión? (ejemplo: USD)");
                System.out.println(menuMonedas);
                monedaObjetivo = lectura.nextLine();
            }

            System.out.println("¿Cuál es el monto?");
            montoBase = lectura.nextDouble();

            String direccion = "https://v6.exchangerate-api.com/v6/ace9151621743ddd254b4513/pair/"
                    + monedaBase +
                    "/" + monedaObjetivo + "/" + montoBase;

            try {

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(direccion))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                Exchange miExchange = gson.fromJson(json, Exchange.class);
                MonedaBase miMonedaBase = new MonedaBase(miExchange);
                System.out.println("Conversión realizada:\n" + miMonedaBase);
                System.out.println("\nDigita 'SAL' para concluir y ver la lista de conversiones\n\nó");

                conversiones.add(miMonedaBase);

            } catch (NumberFormatException e) {
                System.out.println("Ocurrió un error");
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error en la URI. Verifique dirección");
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Lista de conversiones:");
        for (MonedaBase conversione : conversiones) {
            System.out.println(conversione);
        }

        FileWriter escritura = new FileWriter("conversiones.json");
        escritura.write(gson.toJson(conversiones));
        escritura.close();
        System.out.println("\nGracias por usar ConvertidorNuza");
    }
}