/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package personas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author ghost
 */
public class PersonasFrame extends javax.swing.JFrame {
   private JTable tabla;
    private DefaultTableModel modelo;

    // Inputs para agregar persona
    private JTextField txtNombres, txtApellidos, txtCI, txtDireccion, txtTelefono, txtEmail;
    private JButton btnAgregar;

    public PersonasFrame() {
        setTitle("Gestión de Personas");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Tabla
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{"ID", "Nombres", "Apellidos", "CI", "Dirección", "Teléfono", "Email"});
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para inputs
        JPanel inputPanel = new JPanel(new GridLayout(2, 7, 5, 5));
        txtNombres = new JTextField();
        txtApellidos = new JTextField();
        txtCI = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        btnAgregar = new JButton("Agregar Persona");

        inputPanel.add(new JLabel("Nombres"));
        inputPanel.add(new JLabel("Apellidos"));
        inputPanel.add(new JLabel("CI"));
        inputPanel.add(new JLabel("Dirección"));
        inputPanel.add(new JLabel("Teléfono"));
        inputPanel.add(new JLabel("Email"));
        inputPanel.add(new JLabel("")); // espacio para el botón

        inputPanel.add(txtNombres);
        inputPanel.add(txtApellidos);
        inputPanel.add(txtCI);
        inputPanel.add(txtDireccion);
        inputPanel.add(txtTelefono);
        inputPanel.add(txtEmail);
        inputPanel.add(btnAgregar);

        add(inputPanel, BorderLayout.SOUTH);

        cargarPersonas();

        // Acción del botón
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarPersona();
            }
        });
    }

    private void cargarPersonas() {
        try {
            String response = ApiClient.getPersonas();
            JSONArray personas = new JSONArray(response);
            modelo.setRowCount(0); // limpia tabla antes de recargar

            for (int i = 0; i < personas.length(); i++) {
                JSONObject p = personas.getJSONObject(i);
                modelo.addRow(new Object[]{
                    p.getInt("id"),
                    p.getString("nombres"),
                    p.getString("apellidos"),
                    p.getString("ci"),
                    p.getString("direccion"),
                    p.getString("telefono"),
                    p.getString("email")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar personas: " + e.getMessage());
        }
    }

    private void agregarPersona() {
        try {
            JSONObject data = new JSONObject();
            data.put("nombres", txtNombres.getText());
            data.put("apellidos", txtApellidos.getText());
            data.put("ci", txtCI.getText());
            data.put("direccion", txtDireccion.getText());
            data.put("telefono", txtTelefono.getText());
            data.put("email", txtEmail.getText());

            URL url = new URL("http://127.0.0.1:8000/api/personas");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + ApiClient.getToken());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(data.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 201 || responseCode == 200) {
                JOptionPane.showMessageDialog(this, "Persona agregada correctamente");
                cargarPersonas(); // recarga tabla
                limpiarInputs();
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar persona: " + responseCode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void limpiarInputs() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCI.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
    }
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
