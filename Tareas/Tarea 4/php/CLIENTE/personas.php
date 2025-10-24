<?php
require __DIR__ . '/config.php';
if (empty($_SESSION['token'])) { header('Location: login.php'); exit; }

// Crear/Actualizar
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $payload = [
        'nombres'    => trim($_POST['nombres']    ?? ''),
        'apellidos'  => trim($_POST['apellidos']  ?? ''),
        'ci'         => trim($_POST['ci']         ?? ''),
        'direccion'  => trim($_POST['direccion']  ?? ''),
        'telefono'   => trim($_POST['telefono']   ?? ''),
        'email'      => trim($_POST['email']      ?? ''),
    ];

    $id = isset($_POST['id']) && $_POST['id'] !== '' ? (int)$_POST['id'] : null;

    if ($id) {
        [$code, $data] = api_call('PUT', "personas/{$id}", $payload);
    } else {
        [$code, $data] = api_call('POST', 'personas', $payload);
    }

    if ($code < 200 || $code >= 300) {
        echo "<pre>ERROR {$code}\n" . htmlspecialchars(print_r($data, true)) . "</pre>";
        exit;
    }
    header('Location: personas.php'); exit;
    }


    // Eliminar
    if (isset($_GET['del'])) {
    $id = (int)$_GET['del'];
    [$code, $data] = api_call('DELETE', "personas/{$id}");
    header('Location: personas.php'); exit;
    }

    // Listado
    [$code, $personas] = api_call('GET', 'personas');
    // Para editar
    $edit = null;
    if (isset($_GET['edit'])) {
    $editId = (int)$_GET['edit'];
    if (is_array($personas)) {
        foreach ($personas as $p) {
        if ((int)$p['id'] === $editId) { $edit = $p; break; }
        }
    }
    }
    ?>
    <!doctype html>
    <html lang="es">
    <head>
    <meta charset="utf-8">
    <title>Personas – Cliente PHP</title>
    <style>
        body{font-family:system-ui,Arial;margin:2rem auto;max-width:1000000px}
        table{width:100%;border-collapse:collapse;margin:1rem 0}
        th,td{border-bottom:1px solid #eee;padding:8px;text-align:left}
        .row{display:flex;gap:8px;margin:.5rem 0}
        input,button{padding:8px}
        .topbar{display:flex;justify-content:space-between;align-items:center}
        .badge{background:#eee;padding:4px 8px;border-radius:10px}
        .btn{padding:8px 16px;border:1px solid #ddd;border-radius:6px;background:#fafafa;cursor:pointer}
        .btn-danger{border-color:#e99;background:#fee}
        .btn-primary{border-color:#9ae;background:#eef6ff}
        .box{border:1px solid #ddd;padding:12px;border-radius:8px}
        a{text-decoration:none}
    </style>
    </head>
    <body>
    <div class="topbar">
        <h1>Personas</h1>
        <div>
        <span class="badge">Autenticado</span>
        <a class="btn" href="logout.php">Salir</a>
        </div>
    </div>

    <div class="box">
        <table>
        <thead>
    <tr>
        <th>ID</th>
        <th>CI</th>            <!-- NUEVO -->
        <th>Nombres</th>
        <th>Apellidos</th>
        <th>Dirección</th>     <!-- NUEVO -->
        <th>Teléfono</th>
        <th>Email</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <?php if (is_array($personas)): foreach ($personas as $p): ?>
        <tr>
        <td><?= htmlspecialchars($p['id']) ?></td>
        <td><?= htmlspecialchars($p['ci'] ?? '') ?></td>                 <!-- NUEVO -->
        <td><?= htmlspecialchars($p['nombres'] ?? '') ?></td>
        <td><?= htmlspecialchars($p['apellidos'] ?? '') ?></td>
        <td><?= htmlspecialchars($p['direccion'] ?? '') ?></td>          <!-- NUEVO -->
        <td><?= htmlspecialchars($p['telefono'] ?? '') ?></td>
        <td><?= htmlspecialchars($p['email'] ?? '') ?></td>
        <td>
            <a class="btn btn-primary" href="?edit=<?= (int)$p['id'] ?>">Editar</a>
            <a class="btn btn-danger"  href="?del=<?= (int)$p['id'] ?>" onclick="return confirm('¿Eliminar?')">Borrar</a>
        </td>
        </tr>
        <?php endforeach; else: ?>
            <tr><td colspan="8">No hay datos o error (código <?= (int)$code ?>)</td></tr>
        <?php endif; ?>
        </tbody>

        </table>
        </div>

    <h2><?= $edit ? 'Editar #'.$edit['id'] : 'Crear nueva' ?></h2>
    <form method="post" class="box">
    <?php if ($edit): ?>
        <input type="hidden" name="id" value="<?= (int)$edit['id'] ?>">
    <?php endif; ?>

    <!-- Fila 1: Nombres y Apellidos -->
    <div class="row">
        <input name="nombres"   placeholder="Nombres"   value="<?= htmlspecialchars($edit['nombres']   ?? '') ?>" required>
        <input name="apellidos" placeholder="Apellidos" value="<?= htmlspecialchars($edit['apellidos'] ?? '') ?>" required>
    </div>

    <!-- Fila 2: CI y Dirección (NUEVO) -->
    <div class="row">
        <input name="ci"        placeholder="CI"         value="<?= htmlspecialchars($edit['ci']        ?? '') ?>">
        <input name="direccion" placeholder="Dirección"  value="<?= htmlspecialchars($edit['direccion'] ?? '') ?>">
    </div>

    <!-- Fila 3: Teléfono y Email -->
    <div class="row">
        <input name="telefono" placeholder="Teléfono" value="<?= htmlspecialchars($edit['telefono'] ?? '') ?>">
        <input type="email" name="email" placeholder="Email" value="<?= htmlspecialchars($edit['email'] ?? '') ?>">
    </div>

    <div class="row">
        <button class="btn btn-primary"><?= $edit ? 'Actualizar' : 'Crear' ?></button>
        <a class="btn" href="personas.php">Cancelar</a>
    </div>
    </form>

</body>
</html>
