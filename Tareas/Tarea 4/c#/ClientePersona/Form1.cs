using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ClientePersona
{
    public partial class Form1 : Form
    {
        private readonly HttpClient http = new HttpClient();
        private readonly string BASE = ConfigurationManager.AppSettings["BaseApi"] ?? "http://127.0.0.1:8000/api";
        private string token = "";

        private static readonly JsonSerializerSettings JsonOpts = new JsonSerializerSettings
        {
            ContractResolver = new CamelCasePropertyNamesContractResolver(),
            NullValueHandling = NullValueHandling.Ignore,
            Formatting = Formatting.Indented
        };

        public Form1()
        {
            InitializeComponent();
            this.btnLogin.Click += btnLogin_Click;
            this.btnListar.Click += btnListar_Click;
            this.btnCrear.Click += btnCrear_Click;
            this.btnActualizar.Click += btnActualizar_Click;
            this.btnEliminar.Click += btnEliminar_Click;
            // headers comunes
            http.DefaultRequestHeaders.Accept.Clear();
            http.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        }

        // ===== DTOs =====
        public class LoginRequest { public string Email; public string Password; public LoginRequest(string e, string p) { Email = e; Password = p; } }
        public class UsuarioDto { public int Id; public string Name; public string Email; }
        public class LoginResponse
        {
            public string Mensaje;
            public string Token;
            public string Type;
            public long Expires;
            public UsuarioDto Usuario;
        }

        public class PersonaCreate
        {
            public string Nombres;
            public string Apellidos;
            public string Ci;
            public string Email;
            public string Direccion;
            public string Telefono;
        }

        public class PersonaUpdate
        {
            public string Nombres;
            public string Apellidos;
            public string Ci;
            public string Email;
            public string Direccion;
            public string Telefono;
        }

        public class PersonaDto
        {
            public int Id;
            public string Nombres;
            public string Apellidos;
            public string Ci;
            public string Email;
            public string Direccion;
            public string Telefono;
            public string Created_At;
            public string Updated_At;
        }

        // ===== Helpers =====
        private void SetOutput(object obj) => txtOutput.Text = JsonConvert.SerializeObject(obj, JsonOpts);

        private void EnsureAuth()
        {
            if (string.IsNullOrWhiteSpace(token))
                throw new InvalidOperationException("Primero inicia sesión (token vacío).");

            http.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", token);
        }

        private StringContent JsonContent(object o)
        {
            var json = JsonConvert.SerializeObject(o, JsonOpts);
            return new StringContent(json, Encoding.UTF8, "application/json");
        }

        private async Task<T> GetAsync<T>(string url)
        {
            using (var res = await http.GetAsync(url))
            {
                var body = await res.Content.ReadAsStringAsync();
                if (!res.IsSuccessStatusCode) throw new Exception($"GET {url} -> {(int)res.StatusCode}: {body}");
                return JsonConvert.DeserializeObject<T>(body);
            }
        }

        private async Task<TRes> PostAsync<TReq, TRes>(string url, TReq data)
        {
            using (var res = await http.PostAsync(url, JsonContent(data)))
            {
                var body = await res.Content.ReadAsStringAsync();
                if (!res.IsSuccessStatusCode) throw new Exception($"POST {url} -> {(int)res.StatusCode}: {body}");
                return JsonConvert.DeserializeObject<TRes>(body);
            }
        }

        private async Task<TRes> PutAsync<TReq, TRes>(string url, TReq data)
        {
            using (var res = await http.PutAsync(url, JsonContent(data)))
            {
                var body = await res.Content.ReadAsStringAsync();
                if (!res.IsSuccessStatusCode) throw new Exception($"PUT {url} -> {(int)res.StatusCode}: {body}");
                return JsonConvert.DeserializeObject<TRes>(body);
            }
        }

        private async Task DeleteAsync(string url)
        {
            using (var res = await http.DeleteAsync(url))
            {
                if (!res.IsSuccessStatusCode)
                {
                    var body = await res.Content.ReadAsStringAsync();
                    throw new Exception($"DELETE {url} -> {(int)res.StatusCode}: {body}");
                }
            }
        }

        // ===== Eventos UI =====

        private async void btnLogin_Click(object sender, EventArgs e)
        {
            try
            {
                var req = new LoginRequest(txtEmail.Text.Trim(), txtPassword.Text);
                var res = await PostAsync<LoginRequest, LoginResponse>($"{BASE}/login", req);
                token = res.Token;
                lblToken.Text = $"Token: {(token?.Length > 25 ? token.Substring(0, 25) + "..." : token)}";
                SetOutput(res);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Login error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private async void btnListar_Click(object sender, EventArgs e)
        {
            try
            {
                EnsureAuth();
                var list = await GetAsync<List<PersonaDto>>($"{BASE}/personas");
                dgvPersonas.DataSource = list;
                SetOutput(list);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Listar error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }


        private async void btnCrear_Click(object sender, EventArgs e)
        {
            try
            {
                EnsureAuth();
                var nueva = new PersonaCreate
                {
                    Nombres = txtNombres.Text.Trim(),
                    Apellidos = txtApellidos.Text.Trim(),
                    Ci = txtCi.Text.Trim(),
                    Email = txtEmailPersona.Text.Trim(),
                    Direccion = string.IsNullOrWhiteSpace(txtDireccion.Text) ? null : txtDireccion.Text.Trim(),
                    Telefono = string.IsNullOrWhiteSpace(txtTelefono.Text) ? null : txtTelefono.Text.Trim(),
                };

                var creada = await PostAsync<PersonaCreate, PersonaDto>($"{BASE}/personas", nueva);
                SetOutput(creada);
                await RefrescarTabla();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Crear error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private async void btnActualizar_Click(object sender, EventArgs e)
        {
            try
            {
                EnsureAuth();
                if (!int.TryParse(txtUpdId.Text, out var id)) throw new Exception("ID inválido.");

                var upd = new PersonaUpdate
                {
                    Nombres = string.IsNullOrWhiteSpace(txtUpdNombres.Text) ? null : txtUpdNombres.Text.Trim(),
                    Apellidos = string.IsNullOrWhiteSpace(txtUpdApellidos.Text) ? null : txtUpdApellidos.Text.Trim()
                };

                var actualizada = await PutAsync<PersonaUpdate, PersonaDto>($"{BASE}/personas/{id}", upd);
                SetOutput(actualizada);
                await RefrescarTabla();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Actualizar error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private async void btnEliminar_Click(object sender, EventArgs e)
        {
            try
            {
                EnsureAuth();
                if (!int.TryParse(txtDelId.Text, out var id)) throw new Exception("ID inválido.");
                await DeleteAsync($"{BASE}/personas/{id}");
                SetOutput(new { message = "Eliminado", id });
                await RefrescarTabla();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Eliminar error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private async Task RefrescarTabla()
        {
            try
            {
                var list = await GetAsync<List<PersonaDto>>($"{BASE}/personas");
                dgvPersonas.DataSource = list;
            }
            catch { /* silencioso */ }
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            // (vacío) — solo para que el diseñador abra
        }

        private void button1_Click(object sender, EventArgs e)
        {
            // Este método estaba enganchado en el diseñador pero no existe.
            // Puedes borrarlo luego desde las Propiedades del botón.
        }

        private void labelPassword_Click(object sender, EventArgs e)
        {

        }

        private void label4_Click(object sender, EventArgs e)
        {

        }

        private void label12_Click(object sender, EventArgs e)
        {

        }
    }
}
