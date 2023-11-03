package ru.itis.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            int port = 8888;
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Сервер запущен. Ожидание подключения игрока...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Игрок подключился: " + clientSocket);

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Получено сообщение от клиента: " + inputLine);

                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode jsonObject = objectMapper.readValue(inputLine, ObjectNode.class);

                    String command = jsonObject.get("command").asText();

                    if (command.equals("start")) {
                        String clientName = jsonObject.get("clientName").asText();
                        ObjectNode startResponse = objectMapper.createObjectNode();
                        startResponse.put("status", "start");
                        startResponse.put("message", "Привет, " + clientName);
                        startResponse.putArray("startPoint").add(1).add(4);
                        startResponse.putArray("raiting");
                        out.println(startResponse.toString() + "\n");
                    } else if (command.equals("direction")) {
                        String direction = jsonObject.get("direction").asText();
                        // Логика обработки направления и отправка соответствующего ответа клиенту
                    } else if (command.equals("stop")) {
                        // Логика остановки игры и формирования ответа сервера при остановке
                        ObjectNode stopResponse = objectMapper.createObjectNode();
                        stopResponse.put("status", "stop");
                        stopResponse.putArray("raiting");
                        out.println(stopResponse.toString() + "\n");
                        break;

                    }
                }
                System.out.println("Соединение разорвано с клиентом.");
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
