<?php
// Cambia la URL si tu API corre en otro host/puerto
define('API_BASE', 'http://127.0.0.1:8000/api');

if (session_status() === PHP_SESSION_NONE) {
    session_start();
    }

    function api_call(string $method, string $path, ?array $body = null, bool $withAuth = true): array {
    $url = rtrim(API_BASE, '/') . '/' . ltrim($path, '/');
    $ch = curl_init($url);

    $headers = ['Accept: application/json'];
    if ($withAuth && !empty($_SESSION['token'])) {
        $headers[] = 'Authorization: Bearer ' . $_SESSION['token'];
    }
    if ($body !== null) {
        $headers[] = 'Content-Type: application/json';
    }

    curl_setopt_array($ch, [
        CURLOPT_CUSTOMREQUEST    => strtoupper($method),
        CURLOPT_HTTPHEADER       => $headers,
        CURLOPT_RETURNTRANSFER   => true,
    ]);

    if ($body !== null) {
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($body));
    }

    $resp = curl_exec($ch);
    $code = curl_getinfo($ch, CURLINFO_RESPONSE_CODE);
    if ($resp === false) {
        $err = curl_error($ch);
        curl_close($ch);
        return [$code ?: 0, ['error' => $err]];
    }
    curl_close($ch);

    $json = json_decode($resp, true);
    // Si la respuesta no es JSON, devuélvela como texto
    if ($json === null && json_last_error() !== JSON_ERROR_NONE) {
        $json = ['raw' => $resp];
    }
    return [$code, $json];
    }
