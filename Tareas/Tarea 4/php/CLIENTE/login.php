<?php
require __DIR__ . '/config.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  $email = trim($_POST['email'] ?? '');
  $password = (string)($_POST['password'] ?? '');

  [$code, $data] = api_call('POST', 'login', [
    'email' => $email,
    'password' => $password
  ], false);

  if ($code === 200 && isset($data['token'])) {
    $_SESSION['token'] = $data['token'];
    header('Location: personas.php');
    exit;
  } else {
    $error = 'Credenciales inválidas o error de API.';
    $debug = $data;
  }
}
?>
<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <title>Login – Cliente PHP</title>
  <style>
    body{font-family:system-ui,Arial;margin:2rem auto;max-width:520px}
    form{display:flex;gap:8px;flex-direction:column}
    input,button{padding:10px;font-size:14px}
    .error{color:#b00; margin-top:10px}
    .box{border:1px solid #ddd; padding:16px; border-radius:8px}
  </style>
</head>
<body>
  <h1>Login</h1>
  <div class="box">
    <form method="post">
      <input type="email" name="email" placeholder="email" value="demo@demo.com" required>
      <input type="password" name="password" placeholder="password" value="demo123" required>
      <button>Ingresar</button>
    </form>
    <?php if (!empty($error)): ?>
      <div class="error"><?= htmlspecialchars($error) ?></div>
      <pre style="white-space:pre-wrap;background:#f7f7f7;padding:8px;border-radius:6px"><?= htmlspecialchars(print_r($debug,true)) ?></pre>
    <?php endif; ?>
  </div>
</body>
</html>
